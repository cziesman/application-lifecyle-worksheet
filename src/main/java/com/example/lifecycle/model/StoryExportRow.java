package com.example.lifecycle.model;

public record StoryExportRow(
    String issueType,
    String summary,
    String description,
    String acceptanceCriteria,
    String priority,
    String components,
    String labels,
    String featureLink,
    String fixVersions,
    String reporter,
    int storyPoints
) {}
