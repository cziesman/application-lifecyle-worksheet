package com.example.lifecycle.service;

import static org.junit.jupiter.api.Assertions.*;
import com.example.lifecycle.model.OnboardingForm;
import com.example.lifecycle.model.StoryExportOptions;
import org.junit.jupiter.api.Test;

class StorySelectionServiceTest {
    private final StorySelectionService service = new StorySelectionService();

    @Test void alwaysStoriesAreSelected(){
        var ids = service.select(new OnboardingForm()).stream().map(x->x.key()).toList();
        assertEquals(34, ids.size());
        assertFalse(ids.contains("GEN-05"));
        assertFalse(ids.contains("GEN-09"));
        assertFalse(ids.contains("GEN-14"));
        assertFalse(ids.contains("GEN-15"));
        assertFalse(ids.contains("GEN-31"));
        assertFalse(ids.contains("GEN-33"));
        assertFalse(ids.contains("GEN-22"));
    }

    @Test void nodeBranchIsSelected(){
        var f = new OnboardingForm();
        f.getAnswers().put("GEN-07::Runtime Type", "Node.js");
        var ids = service.select(f).stream().map(x->x.key()).toList();
        assertTrue(ids.contains("GEN-09"));
        assertTrue(ids.contains("GEN-12"));
        assertTrue(ids.contains("GEN-13"));
        assertTrue(ids.contains("GEN-15"));
        f.getAnswers().put("GEN-22::Runtime Type", "Node.js");
        var deploymentIds = service.select(f).stream().map(x->x.key()).toList();
        assertTrue(deploymentIds.contains("GEN-22"));
    }

    @Test void runtimeBranchUsesGen07AndDoesNotUseGen15RuntimeType(){
        var f = new OnboardingForm();
        f.getAnswers().put("GEN-07::Runtime Type", "JVM");
        f.getAnswers().put("GEN-15::Runtime Type", "Node.js");
        var jvmIds = service.select(f).stream().map(x -> x.key()).toList();
        assertTrue(jvmIds.contains("GEN-15"));
        assertFalse(jvmIds.contains("GEN-09"));
        assertFalse(jvmIds.contains("GEN-12"));
        assertFalse(jvmIds.contains("GEN-13"));

        f.getAnswers().put("GEN-07::Runtime Type", "Other");
        var otherIds = service.select(f).stream().map(x -> x.key()).toList();
        assertFalse(otherIds.contains("GEN-09"));
        assertFalse(otherIds.contains("GEN-12"));
        assertFalse(otherIds.contains("GEN-13"));
        assertFalse(otherIds.contains("GEN-15"));
    }

    @Test void operatorBranchIsSelected(){
        var f = new OnboardingForm();
        f.getAnswers().put("GEN-05::Applicable", "Y");
        var ids = service.select(f).stream().map(x->x.key()).toList();
        assertTrue(ids.contains("GEN-05"));
        assertTrue(ids.contains("GEN-06"));
    }

    @Test void storageZoneAndDependencyBranchesAreSelected(){
        var f = new OnboardingForm();
        f.getAnswers().put("GEN-14::Applicable?", "Y");
        f.getAnswers().put("GEN-31::Multi-Zone Applicability Confirmed", "Y");
        f.getAnswers().put("GEN-33::Dependency Applicability Confirmed", "Y");
        var ids = service.select(f).stream().map(x->x.key()).toList();
        assertTrue(ids.contains("GEN-14"));
        assertTrue(ids.contains("GEN-31"));
        assertTrue(ids.contains("GEN-33"));
    }

    @Test void storyPointsUseFibonacciSizing(){
        var small = new com.example.lifecycle.model.WorksheetStory("X", "Small", "Always", 3,
            WorksheetCatalog.STORIES.get(0).fields().subList(0, 3));
        assertEquals(1, service.estimateStoryPoints(small));
        assertEquals(5, service.estimateStoryPoints(WorksheetCatalog.STORIES.get(0)));
        assertEquals(3, service.estimateStoryPoints(WorksheetCatalog.STORIES.get(1)));
    }

    @Test void csvHasRequiredJiraColumns(){
        var f = new OnboardingForm();
        var options = new StoryExportOptions();
        options.setIssueType("Story");
        options.setPriority("Medium");
        options.setComponents("Application Platform");
        options.setLabels("application-lifecycle");
        options.setFeatureLink("Application Lifecycle Migration");
        options.setFixVersions("Next Release");
        options.setReporter("application-team");
        var stories = service.select(f, WorksheetCatalog.STORIES.subList(0, 1), "APP1");
        var rows = service.exportRows(stories, WorksheetCatalog.STORIES.subList(0, 1), f, "APP1", options, "Generic App 1", "Onboarding");
        var csv = service.csv(rows);
        assertTrue(csv.startsWith("Issue Type,Summary,Description,Acceptance Criteria,Priority,Components,Labels,Feature Link,Fix Version/s,Reporter,Story Points\n"));
        assertTrue(csv.contains("\"Story\""));
        assertTrue(csv.contains("\"Generic App 1 - Discovery Session\""));
        assertFalse(csv.contains("\"APP1-01 - Discovery Session\""));
    }
}
