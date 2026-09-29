package com.example.lifecycle.config;

import com.example.lifecycle.model.StoryExportOptions;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("${STORY_EXPORT_CONFIG:classpath:/config/story-export.properties}")
public class StoryExportProperties {
    private final StoryExportOptions defaults = new StoryExportOptions();
    private final List<String> issueTypes;
    private final List<String> priorities;
    private final List<String> components;
    private final List<String> featureLinks;
    private final List<String> fixVersions;
    private final List<String> dataSecurityClassifications;
    private final List<String> targetEnvironments;
    private final List<String> scalingApproaches;

    public StoryExportProperties(
            @Value("${story.export.issue-type:Story}") String issueType,
            @Value("${story.export.priority:Medium}") String priority,
            @Value("${story.export.components:Application Platform}") String components,
            @Value("${story.export.labels:application-lifecycle}") String labels,
            @Value("${story.export.feature-link:APP-LIFECYCLE-FEATURE}") String featureLink,
            @Value("${story.export.fix-versions:Next Release}") String fixVersions,
            @Value("${story.export.reporter:application-team}") String reporter,
            @Value("${story.export.issue-types:Story,Task,Bug}") String issueTypes,
            @Value("${story.export.priorities:Highest,High,Medium,Low,Lowest}") String priorities,
            @Value("${story.export.components-options:Application Platform,Application Runtime,Infrastructure}") String componentOptions,
            @Value("${story.export.feature-links:APP-LIFECYCLE-FEATURE,Application Migration Feature,Application Onboarding Feature}") String featureLinkOptions,
            @Value("${story.export.fix-version-options:Next Release,Current Release,Backlog}") String fixVersionOptions,
            @Value("${worksheet.data-security-classification-options:PERSONAL - NONWORK,PUBLIC - OFFICIAL RELEASE,NONCONFIDENTIAL}") String dataSecurityClassificationOptions,
            @Value("${worksheet.target-environment-options:DEV,TEST,QA,PROD}") String targetEnvironmentOptions,
            @Value("${worksheet.scaling-approach-options:HORIZONTAL,VERTICAL,HYBRID,NONE}") String scalingApproachOptions) {
        defaults.setIssueType(issueType);
        defaults.setPriority(priority);
        defaults.setComponents(components);
        defaults.setLabels(labels);
        defaults.setFeatureLink(featureLink);
        defaults.setFixVersions(fixVersions);
        defaults.setReporter(reporter);
        this.issueTypes = valuesWithDefault(issueTypes, issueType);
        this.priorities = valuesWithDefault(priorities, priority);
        this.components = valuesWithDefault(componentOptions, components);
        this.featureLinks = valuesWithDefault(featureLinkOptions, featureLink);
        this.fixVersions = valuesWithDefault(fixVersionOptions, fixVersions);
        this.dataSecurityClassifications = valuesWithDefault(dataSecurityClassificationOptions, "");
        this.targetEnvironments = valuesWithDefault(targetEnvironmentOptions, "");
        this.scalingApproaches = valuesWithDefault(scalingApproachOptions, "");
    }

    public StoryExportOptions defaults() {
        return defaults.copy();
    }

    public List<String> issueTypes() { return issueTypes; }
    public List<String> priorities() { return priorities; }
    public List<String> components() { return components; }
    public List<String> featureLinks() { return featureLinks; }
    public List<String> fixVersions() { return fixVersions; }
    public List<String> dataSecurityClassifications() { return dataSecurityClassifications; }
    public List<String> targetEnvironments() { return targetEnvironments; }
    public List<String> scalingApproaches() { return scalingApproaches; }

    private List<String> valuesWithDefault(String configured, String defaultValue) {
        java.util.ArrayList<String> values = new java.util.ArrayList<>(Arrays.stream(configured.split(","))
            .map(String::trim)
            .filter(v -> !v.isEmpty())
            .toList());
        if (defaultValue != null && !defaultValue.isBlank() && !values.contains(defaultValue.trim())) {
            values.add(0, defaultValue.trim());
        }
        return List.copyOf(values);
    }
}
