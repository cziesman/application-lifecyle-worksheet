package com.example.lifecycle.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class OnboardingForm {
    private Map<String, String> answers = new LinkedHashMap<>();
    public Map<String, String> getAnswers() { return answers; }
    public void setAnswers(Map<String, String> answers) { this.answers = answers == null ? new LinkedHashMap<>() : new LinkedHashMap<>(answers); }
    public String answer(String story, String field) { return answers.getOrDefault(story + "::" + field, ""); }
    public boolean yes(String story, String field) { return "Y".equalsIgnoreCase(answer(story, field)) || "Yes".equalsIgnoreCase(answer(story, field)) || "true".equalsIgnoreCase(answer(story, field)); }
}
