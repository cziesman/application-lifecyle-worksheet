# Application Lifecycle Onboarding

Spring Boot application for the GEN application onboarding/migration worksheet.

## Application catalog

The configured onboarded applications are in `src/main/resources/config/applications.json` by default:

```json
[
  { "name": "Generic App 1", "abbreviation": "APP1" },
  { "name": "Generic App 2", "abbreviation": "APP2" }
]
```

The catalog can be supplied externally with `APPLICATION_CATALOG`, for example:

```text
APPLICATION_CATALOG=file:/opt/app/config/applications.json
```

The first configured application is selected automatically for a new HTTP session.

## Local answer files

The server does **not** persist questionnaire answers. This is intentional so multiple users can work on the same application and lifecycle phase simultaneously without affecting one another.

Each page has its own JSON file. The selected application's abbreviation and lifecycle phase are included in the filename, for example:

```text
APP1-onboarding-answers.json
APP1-deployment-answers.json
APP1-ha-recovery-answers.json
APP1-promotion-answers.json
```

The equivalent APP2 files are created for Generic App 2.

**Save this page** downloads the JSON file to the user's local machine. **Load saved answers** reads a JSON file from the user's local machine. **Clear answers** only clears the current browser form; it does not delete or modify an existing local file.

No PVC or server-side answer-data volume is required.

## Story IDs

The worksheet remains the source of story definitions and field prompts. When an application is selected, its abbreviation replaces `GEN` in displayed and generated story IDs. For example, GEN-01 becomes APP1-01 for Generic App 1 and APP2-01 for Generic App 2.

## CSV story generation

At the bottom of each lifecycle page, **Generate application stories** opens a dialog before any stories are generated. The dialog supplies values that are applied to every generated story in that phase:

- Issue Type
- Priority
- Components
- Labels
- Feature Link
- Fix Version/s
- Reporter

The following Jira CSV fields are generated automatically:

- **Summary** — application story ID plus the worksheet story name.
- **Description** — application, lifecycle phase, worksheet applicability, and the answers captured for the story's worksheet fields.
- **Acceptance Criteria** — one criterion for each worksheet field, using the captured response when available.
- **Story Points** — an estimate derived from the number of fields in the worksheet story definition using Fibonacci sizing.

The Fibonacci estimate uses these worksheet-field thresholds:

| Worksheet fields | Story points |
|---:|---:|
| 1–3 | 1 |
| 4–6 | 2 |
| 7–10 | 3 |
| 11–15 | 5 |
| 16–25 | 8 |
| 26–40 | 13 |
| 41+ | 21 |

The CSV header is exactly:

```text
Issue Type,Summary,Description,Acceptance Criteria,Priority,Components,Labels,Feature Link,Fix Version/s,Reporter,Story Points
```

## Story export defaults

Default values for the generation dialog are in `src/main/resources/config/story-export.properties`:

```properties
story.export.issue-type=Story
story.export.priority=Medium
story.export.components=Application Platform
story.export.labels=application-lifecycle
story.export.feature-link=Application Lifecycle Migration
story.export.fix-versions=Next Release
story.export.reporter=application-team
```

The application can load an external properties file with `STORY_EXPORT_CONFIG`, for example:

```text
STORY_EXPORT_CONFIG=file:/etc/gen-application-onboarding/applications/story-export.properties
```

The OpenShift deployment mounts that file from the `gen-application-onboarding-config` ConfigMap. Change the ConfigMap and restart the Deployment to make changed defaults available to users.

The same properties file also controls worksheet dropdowns:

```properties
worksheet.data-security-classification-options=PERSONAL - NONWORK,PUBLIC - OFFICIAL RELEASE,NONCONFIDENTIAL
worksheet.target-environment-options=DEV,TEST,QA,PROD
worksheet.scaling-approach-options=HORIZONTAL,VERTICAL,HYBRID,NONE
```

`Target Environment(s)` is a multi-select; multiple selections are stored as a comma-separated answer value. `Scaling Approach` is a single-select dropdown.

## OpenShift deployment

The deployment manifests are in `deploy/`. The ConfigMap contains both `applications.json` and `story-export.properties`.

Apply the manifests with:

```bash
oc apply -k deploy/
```

After changing the ConfigMap:

```bash
oc rollout restart deployment/gen-application-onboarding
```

Change the image in `deploy/deployment.yaml` from the `CHANGE-ME` placeholder to the image in your OpenShift project.
