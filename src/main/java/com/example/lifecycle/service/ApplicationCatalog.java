package com.example.lifecycle.service;

import com.example.lifecycle.model.OnboardedApplication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ApplicationCatalog {
    private final List<OnboardedApplication> applications;

    public ApplicationCatalog(ObjectMapper objectMapper,
                              @Value("${application.catalog:classpath:/config/applications.json}") Resource resource) {
        try (InputStream input = resource.getInputStream()) {
            List<OnboardedApplication> loaded = objectMapper.readValue(input,
                new TypeReference<List<OnboardedApplication>>() {});
            if (loaded == null || loaded.isEmpty()) {
                throw new IllegalStateException("Application catalog must contain at least one application");
            }
            for (OnboardedApplication app : loaded) {
                if (app.name() == null || app.name().isBlank() || app.abbreviation() == null || app.abbreviation().isBlank()) {
                    throw new IllegalStateException("Each onboarded application must have a name and abbreviation");
                }
                if (!app.abbreviation().matches("[A-Za-z][A-Za-z0-9_-]*")) {
                    throw new IllegalStateException("Application abbreviation must start with a letter and contain only letters, digits, '_' or '-': " + app.abbreviation());
                }
            }
            this.applications = List.copyOf(loaded);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load onboarded application catalog", e);
        }
    }

    public List<OnboardedApplication> all() {
        return applications;
    }

    public OnboardedApplication first() {
        return applications.get(0);
    }

    public OnboardedApplication find(String abbreviation) {
        return applications.stream()
            .filter(a -> a.abbreviation().equals(abbreviation))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown onboarded application: " + abbreviation));
    }
}
