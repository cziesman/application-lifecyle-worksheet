package com.example.lifecycle.service;

import com.example.lifecycle.model.OnboardingForm;
import com.example.lifecycle.model.Story;
import com.example.lifecycle.model.StoryExportOptions;
import com.example.lifecycle.model.StoryExportRow;
import com.example.lifecycle.model.WorksheetField;
import com.example.lifecycle.model.WorksheetStory;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service("storySelectionService")
public class StorySelectionService {
    public List<Story> select(OnboardingForm form) {
        return select(form, WorksheetCatalog.STORIES, "GEN");
    }

    public List<Story> select(OnboardingForm form, List<WorksheetStory> candidates, String abbreviation) {
        return candidates.stream()
            .filter(s -> applicable(displayId(s.id(), abbreviation), form, abbreviation))
            .map(s -> toStory(s, form, abbreviation))
            .toList();
    }

    public boolean applicable(String id, OnboardingForm f, String abbreviation) {
        String story = canonicalId(id, abbreviation);
        return switch (story) {
            case "GEN-05", "GEN-06" -> hasOperatorDependency(f, abbreviation);
            case "GEN-09", "GEN-12", "GEN-13" -> runtimeIsNode(f, storyId("07", abbreviation), "Runtime Type");
            case "GEN-14" -> yesAny(f, storyId("14", abbreviation), "Applicable?");
            case "GEN-15" -> runtimeIsJvmOrNode(f, storyId("07", abbreviation), "Runtime Type");
            case "GEN-22" -> runtimeIsJvmOrNode(f, storyId("22", abbreviation), "Runtime Type");
            case "GEN-31" -> yesAny(f, storyId("31", abbreviation), "Multi-Zone Applicability Confirmed");
            case "GEN-33" -> yesAny(f, storyId("33", abbreviation), "Dependency Applicability Confirmed");
            default -> true;
        };
    }

    private boolean hasOperatorDependency(OnboardingForm f, String abbreviation) {
        return yesAny(f, storyId("05", abbreviation), "Applicable")
            || hasText(f.answer(storyId("05", abbreviation), "Dependency Name"))
            || hasText(f.answer(storyId("04", abbreviation), "Required Operators"));
    }

    private boolean runtimeIsJvmOrNode(OnboardingForm f, String story, String field) {
        String runtime = f.answer(story, field);
        return hasText(runtime) && (runtime.equalsIgnoreCase("JVM") || runtime.equalsIgnoreCase("Node.js"));
    }

    private boolean runtimeIsNode(OnboardingForm f, String story, String field) {
        String runtime = f.answer(story, field);
        return hasText(runtime) && runtime.equalsIgnoreCase("Node.js");
    }

    private boolean yesAny(OnboardingForm f, String story, String field) {
        return f.yes(story, field);
    }

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private Story toStory(WorksheetStory s, OnboardingForm f, String abbreviation) {
        String id = displayId(s.id(), abbreviation);
        String reason = reason(id, f, abbreviation);
        String names = s.fields().stream()
            .map(WorksheetField::name)
            .map(name -> replaceGen(name, abbreviation))
            .collect(Collectors.joining(" | "));
        return new Story(id, replaceGen(s.name(), abbreviation), replaceGen(s.applicability(), abbreviation), reason, names, estimateStoryPoints(s));
    }

    public String reason(String id, OnboardingForm f, String abbreviation) {
        return switch (canonicalId(id, abbreviation)) {
            case "GEN-05", "GEN-06" -> "Selected because a non-baseline operator/platform-service dependency is indicated.";
            case "GEN-09", "GEN-12", "GEN-13" -> "Selected because " + storyId("07", abbreviation) + " identifies the application as Node.js-based.";
            case "GEN-14" -> "Selected because " + storyId("14", abbreviation) + " indicates persistent/stateful storage applicability.";
            case "GEN-15" -> "Selected because " + storyId("07", abbreviation) + " identifies a JVM or Node.js runtime.";
            case "GEN-22" -> "Selected because " + storyId("22", abbreviation) + " identifies a JVM or Node.js runtime branch.";
            case "GEN-31" -> "Selected because the target cluster is multi-zone.";
            case "GEN-33" -> "Selected because the application has a stateful/database dependency.";
            default -> "Always applicable per the " + abbreviation + " worksheet Story Index.";
        };
    }

    public List<StoryExportRow> exportRows(List<Story> stories, List<WorksheetStory> candidates,
                                            OnboardingForm form, String abbreviation,
                                            StoryExportOptions options, String applicationName,
                                            String phaseLabel) {
        return stories.stream()
            .map(story -> {
                WorksheetStory definition = candidates.stream()
                    .filter(candidate -> displayId(candidate.id(), abbreviation).equals(story.key()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No worksheet definition for " + story.key()));
                int points = estimateStoryPoints(definition);
                return new StoryExportRow(
                    clean(options.getIssueType()),
                    clean(applicationName) + " - " + story.summary(),
                    description(story, definition, form, abbreviation, applicationName, phaseLabel),
                    acceptanceCriteria(definition, form, abbreviation),
                    clean(options.getPriority()),
                    clean(options.getComponents()),
                    cleanLabels(options.getLabels()),
                    clean(options.getFeatureLink()),
                    clean(options.getFixVersions()),
                    clean(options.getReporter()),
                    points);
            })
            .toList();
    }

    public int estimateStoryPoints(WorksheetStory story) {
        int fields = story.fields().size();
        if (fields <= 3) return 1;
        if (fields <= 6) return 2;
        if (fields <= 10) return 3;
        if (fields <= 15) return 5;
        if (fields <= 25) return 8;
        if (fields <= 40) return 13;
        return 21;
    }

    public String csv(List<StoryExportRow> rows) {
        StringBuilder b = new StringBuilder();
        b.append("Issue Type,Summary,Description,Acceptance Criteria,Priority,Components,Labels,Feature Link,Fix Version/s,Reporter,Story Points\n");
        for (StoryExportRow row : rows) {
            b.append(csv(row.issueType())).append(',')
             .append(csv(row.summary())).append(',')
             .append(csv(row.description())).append(',')
             .append(csv(row.acceptanceCriteria())).append(',')
             .append(csv(row.priority())).append(',')
             .append(csv(row.components())).append(',')
             .append(csv(row.labels())).append(',')
             .append(csv(row.featureLink())).append(',')
             .append(csv(row.fixVersions())).append(',')
             .append(csv(row.reporter())).append(',')
             .append(row.storyPoints()).append('\n');
        }
        return b.toString();
    }

    private String description(Story story, WorksheetStory definition, OnboardingForm form,
                               String abbreviation, String applicationName, String phaseLabel) {
        StringBuilder b = new StringBuilder();
        b.append("Application: ").append(applicationName).append('\n');
        b.append("Lifecycle phase: ").append(phaseLabel).append('\n');
        b.append("Story: ").append(story.key()).append(" - ").append(replaceGen(story.summary(), abbreviation)).append('\n');
        b.append("Worksheet applicability: ").append(replaceGen(definition.applicability(), abbreviation)).append('\n');
        b.append("\nImplementation context:\n");
        b.append("Complete the worksheet-defined activities and validation for this story. The following captured values provide the current implementation context:\n");
        for (WorksheetField field : definition.fields()) {
            String displayFieldName = replaceGen(field.name(), abbreviation);
            String value = form.answer(story.key(), displayFieldName);
            b.append("- ").append(displayFieldName).append(": ")
             .append(hasText(value) ? replaceGen(value, abbreviation) : "Not provided").append('\n');
        }
        return b.toString().trim();
    }

    private String acceptanceCriteria(WorksheetStory definition, OnboardingForm form, String abbreviation) {
        StringBuilder b = new StringBuilder();
        for (WorksheetField field : definition.fields()) {
            String displayFieldName = replaceGen(field.name(), abbreviation);
            String value = form.answer(displayId(definition.id(), abbreviation), displayFieldName);
            b.append("- ").append(displayFieldName).append(": ")
             .append(hasText(value) ? "captured as " + replaceGen(value, abbreviation) : "captured and validated")
             .append('\n');
        }
        return b.toString().trim();
    }

    public String displayId(String worksheetId, String abbreviation) {
        return abbreviation + worksheetId.substring(worksheetId.indexOf('-'));
    }

    private String storyId(String number, String abbreviation) {
        return abbreviation + "-" + number;
    }

    private String canonicalId(String id, String abbreviation) {
        return "GEN" + id.substring(abbreviation.length());
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

    private String replaceGen(String value, String abbreviation) {
        return value == null ? null : value.replace("GEN", abbreviation);
    }

    private String cleanLabels(String value) {
        if (!hasText(value)) return "";
        return java.util.Arrays.stream(value.split("[;,]+"))
            .map(String::trim)
            .filter(this::hasText)
            .collect(Collectors.joining("; "));
    }

    private String csv(String value) {
        return "\"" + (value == null ? "" : value.replace("\"", "\"\"")) + "\"";
    }
}
