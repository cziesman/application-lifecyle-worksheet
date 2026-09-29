package com.example.lifecycle.model;

public class StoryExportOptions {
    private String issueType;
    private String priority;
    private String components;
    private String labels;
    private String featureLink;
    private String fixVersions;
    private String reporter;

    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getComponents() { return components; }
    public void setComponents(String components) { this.components = components; }
    public String getLabels() { return labels; }
    public void setLabels(String labels) { this.labels = labels; }
    public String getFeatureLink() { return featureLink; }
    public void setFeatureLink(String featureLink) { this.featureLink = featureLink; }
    public String getFixVersions() { return fixVersions; }
    public void setFixVersions(String fixVersions) { this.fixVersions = fixVersions; }
    public String getReporter() { return reporter; }
    public void setReporter(String reporter) { this.reporter = reporter; }

    public StoryExportOptions copy() {
        StoryExportOptions copy = new StoryExportOptions();
        copy.issueType = issueType;
        copy.priority = priority;
        copy.components = components;
        copy.labels = labels;
        copy.featureLink = featureLink;
        copy.fixVersions = fixVersions;
        copy.reporter = reporter;
        return copy;
    }
}
