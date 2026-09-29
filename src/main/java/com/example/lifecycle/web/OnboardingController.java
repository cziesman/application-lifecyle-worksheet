package com.example.lifecycle.web;

import com.example.lifecycle.model.OnboardedApplication;
import com.example.lifecycle.model.OnboardingForm;
import com.example.lifecycle.model.Story;
import com.example.lifecycle.model.StoryExportOptions;
import com.example.lifecycle.model.StoryExportRow;
import com.example.lifecycle.model.WorksheetStory;
import com.example.lifecycle.service.ApplicationCatalog;
import com.example.lifecycle.config.StoryExportProperties;
import com.example.lifecycle.service.StorySelectionService;
import com.example.lifecycle.service.WorksheetCatalog;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OnboardingController {
    private static final String APPLICATION_SESSION_ATTRIBUTE = OnboardingController.class.getName() + ".application";

    private final StorySelectionService selector;
    private final ApplicationCatalog applicationCatalog;
    private final StoryExportProperties storyExportProperties;

    private static final List<PageSpec> PAGES = List.of(
        new PageSpec("onboarding", "Onboarding", "Application onboarding", "GEN-01", "GEN-18"),
        new PageSpec("deployment", "Deployment", "Deployment", "GEN-19", "GEN-25"),
        new PageSpec("ha-recovery", "HA & Recovery", "HA & recovery validation", "GEN-26", "GEN-34"),
        new PageSpec("promotion", "Promotion & Operations", "Promotion, cutover & operations", "GEN-35", "GEN-44")
    );

    public OnboardingController(StorySelectionService selector,
                                ApplicationCatalog applicationCatalog,
                                StoryExportProperties storyExportProperties) {
        this.selector = selector;
        this.applicationCatalog = applicationCatalog;
        this.storyExportProperties = storyExportProperties;
    }

    @GetMapping("/")
    public String root(HttpSession session) {
        application(session);
        return "redirect:/onboarding";
    }

    @GetMapping("/onboarding")
    public String onboarding(Model model, HttpSession session) {
        return renderPage(model, "onboarding", session, false);
    }

    @GetMapping("/deployment")
    public String deployment(Model model, HttpSession session) {
        return renderPage(model, "deployment", session, false);
    }

    @GetMapping("/ha-recovery")
    public String haRecovery(Model model, HttpSession session) {
        return renderPage(model, "ha-recovery", session, false);
    }

    @GetMapping("/promotion")
    public String promotion(Model model, HttpSession session) {
        return renderPage(model, "promotion", session, false);
    }

    @PostMapping("/application")
    public String selectApplication(@RequestParam String abbreviation,
                                    @RequestParam(defaultValue = "onboarding") String page,
                                    HttpSession session) {
        applicationCatalog.find(abbreviation);
        page(page);
        session.setAttribute(APPLICATION_SESSION_ATTRIBUTE, abbreviation);
        return "redirect:/" + page;
    }

    @PostMapping("/generate")
    public String generate(@ModelAttribute("form") OnboardingForm form,
                           @ModelAttribute("exportOptions") StoryExportOptions exportOptions,
                           @RequestParam String page,
                           Model model,
                           HttpSession session) {
        PageSpec pageSpec = page(page);
        OnboardedApplication app = application(session);
        List<WorksheetStory> pageStories = pageStories(pageSpec);
        List<Story> stories = selector.select(form, pageStories, app.abbreviation());
        List<StoryExportRow> exportRows = selector.exportRows(stories, pageStories, form, app.abbreviation(), exportOptions, app.name(), pageSpec.label());
        model.addAttribute("stories", stories);
        model.addAttribute("exportRows", exportRows);
        model.addAttribute("exportOptions", exportOptions);
        model.addAttribute("allStories", displayStories(pageStories, app.abbreviation()));
        model.addAttribute("form", form);
        populateCommon(model, pageSpec, app);
        return "results";
    }

    @PostMapping("/download")
    public void download(@ModelAttribute OnboardingForm form,
                          @ModelAttribute StoryExportOptions exportOptions,
                          @RequestParam String page,
                          @RequestParam(defaultValue = "stories.csv") String filename,
                          HttpServletResponse response,
                          HttpSession session) throws IOException {
        PageSpec pageSpec = page(page);
        OnboardedApplication app = application(session);
        List<WorksheetStory> pageStories = pageStories(pageSpec);
        List<Story> stories = selector.select(form, pageStories, app.abbreviation());
        List<StoryExportRow> rows = selector.exportRows(stories, pageStories, form, app.abbreviation(), exportOptions, app.name(), pageSpec.label());
        String csv = selector.csv(rows);
        String safe = filename.replaceAll("[^A-Za-z0-9._-]", "_");
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + safe + "\"");
        response.getOutputStream().write(csv.getBytes(StandardCharsets.UTF_8));
    }

    private String renderPage(Model model, String key, HttpSession session, boolean loaded) {
        PageSpec page = page(key);
        OnboardedApplication app = application(session);
        OnboardingForm form = new OnboardingForm();
        model.addAttribute("form", form);
        model.addAttribute("worksheetStories", displayStories(pageStories(page), app.abbreviation()));
        populateCommon(model, page, app);
        return "onboarding";
    }

    private void populateCommon(Model model, PageSpec page, OnboardedApplication app) {
        model.addAttribute("page", page);
        model.addAttribute("navigation", PAGES);
        model.addAttribute("applications", applicationCatalog.all());
        model.addAttribute("selectedApplication", app);
        model.addAttribute("exportDefaults", storyExportProperties.defaults());
        model.addAttribute("exportIssueTypes", storyExportProperties.issueTypes());
        model.addAttribute("exportPriorities", storyExportProperties.priorities());
        model.addAttribute("exportComponents", storyExportProperties.components());
        model.addAttribute("exportFeatureLinks", storyExportProperties.featureLinks());
        model.addAttribute("exportFixVersions", storyExportProperties.fixVersions());
        model.addAttribute("dataSecurityClassifications", storyExportProperties.dataSecurityClassifications());
        model.addAttribute("targetEnvironments", storyExportProperties.targetEnvironments());
        model.addAttribute("scalingApproaches", storyExportProperties.scalingApproaches());
    }

    private List<WorksheetStory> pageStories(PageSpec page) {
        return WorksheetCatalog.STORIES.stream()
            .filter(s -> between(s.id(), page.firstStory(), page.lastStory()))
            .toList();
    }

    private List<WorksheetStory> displayStories(List<WorksheetStory> stories, String abbreviation) {
        return stories.stream()
            .map(s -> new WorksheetStory(
                selector.displayId(s.id(), abbreviation),
                replaceGen(s.name(), abbreviation),
                replaceGen(s.applicability(), abbreviation),
                s.fieldCount(),
                s.fields().stream()
                    .map(f -> new com.example.lifecycle.model.WorksheetField(
                        replaceGen(f.name(), abbreviation),
                        replaceGen(f.prompt(), abbreviation),
                        f.responseType(),
                        replaceGen(f.notes(), abbreviation)))
                    .toList()))
            .toList();
    }

    private String replaceGen(String value, String abbreviation) {
        return value == null ? null : value.replace("GEN", abbreviation);
    }

    private boolean between(String id, String first, String last) {
        int value = Integer.parseInt(id.substring(id.indexOf('-') + 1));
        int low = Integer.parseInt(first.substring(first.indexOf('-') + 1));
        int high = Integer.parseInt(last.substring(last.indexOf('-') + 1));
        return value >= low && value <= high;
    }

    private PageSpec page(String key) {
        return PAGES.stream()
            .filter(p -> p.key().equals(key))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown worksheet page: " + key));
    }

    private OnboardedApplication application(HttpSession session) {
        Object value = session.getAttribute(APPLICATION_SESSION_ATTRIBUTE);
        if (value instanceof String abbreviation) {
            try {
                return applicationCatalog.find(abbreviation);
            } catch (IllegalArgumentException ignored) {
                // Fall through to the first configured application if the configured list changed.
            }
        }
        OnboardedApplication first = applicationCatalog.first();
        session.setAttribute(APPLICATION_SESSION_ATTRIBUTE, first.abbreviation());
        return first;
    }

    public record PageSpec(String key, String label, String title, String firstStory, String lastStory) {}
}
