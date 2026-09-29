package com.example.lifecycle.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Stores worksheet answers in both the current HTTP session and a server-side JSON file.
 * The file is scoped by application, lifecycle phase, and HTTP session so simultaneous
 * users working on the same application/page never overwrite one another.
 */
@Service
public class UserWorksheetState {
    private static final String SESSION_ATTRIBUTE = UserWorksheetState.class.getName();
    private final ObjectMapper objectMapper;
    private final Path dataDirectory;

    public UserWorksheetState(ObjectMapper objectMapper,
                              @Value("${application.data-directory:./data}") String dataDirectory) {
        this.objectMapper = objectMapper;
        this.dataDirectory = Paths.get(dataDirectory);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, Map<String, String>>> state(HttpSession session) {
        Object existing = session.getAttribute(SESSION_ATTRIBUTE);
        if (existing instanceof Map<?, ?>) {
            return (Map<String, Map<String, Map<String, String>>>) existing;
        }
        Map<String, Map<String, Map<String, String>>> created = new LinkedHashMap<>();
        session.setAttribute(SESSION_ATTRIBUTE, created);
        return created;
    }

    public Map<String, String> get(HttpSession session, String application, String page) {
        Map<String, Map<String, String>> appState = state(session).get(application);
        if (appState != null && appState.containsKey(page)) {
            return new LinkedHashMap<>(appState.get(page));
        }
        return readFile(session, application, page);
    }

    public void save(HttpSession session, String application, String page, Map<String, String> answers) {
        Map<String, Map<String, Map<String, String>>> all = state(session);
        all.computeIfAbsent(application, ignored -> new LinkedHashMap<>())
            .put(page, new LinkedHashMap<>(answers == null ? Map.of() : answers));
        session.setAttribute(SESSION_ATTRIBUTE, all);
        writeFile(session, application, page, answers == null ? Map.of() : answers);
    }

    public void clear(HttpSession session, String application, String page) {
        Map<String, Map<String, Map<String, String>>> all = state(session);
        Map<String, Map<String, String>> appState = all.get(application);
        if (appState != null) {
            appState.remove(page);
        }
        session.setAttribute(SESSION_ATTRIBUTE, all);
        try {
            Files.deleteIfExists(file(session, application, page));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to clear saved worksheet answers", e);
        }
    }

    private Map<String, String> readFile(HttpSession session, String application, String page) {
        Path file = file(session, application, page);
        if (!Files.exists(file)) {
            return new LinkedHashMap<>();
        }
        try {
            Map<String, String> answers = objectMapper.readValue(file.toFile(), new TypeReference<Map<String, String>>() {});
            Map<String, Map<String, Map<String, String>>> all = state(session);
            all.computeIfAbsent(application, ignored -> new LinkedHashMap<>())
                .put(page, new LinkedHashMap<>(answers));
            session.setAttribute(SESSION_ATTRIBUTE, all);
            return new LinkedHashMap<>(answers);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load saved worksheet answers from " + file, e);
        }
    }

    private void writeFile(HttpSession session, String application, String page, Map<String, String> answers) {
        Path file = file(session, application, page);
        try {
            Files.createDirectories(file.getParent());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), answers);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save worksheet answers to " + file, e);
        }
    }

    private Path file(HttpSession session, String application, String page) {
        return dataDirectory.resolve(safe(application)).resolve(safe(page)).resolve(session.getId() + ".json");
    }

    private String safe(String value) {
        return value.replaceAll("[^A-Za-z0-9_-]", "_");
    }
}
