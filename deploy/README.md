# OpenShift deployment

This directory deploys the application with:

- `Deployment`
- `Service`
- `Route`
- `ConfigMap` containing the application catalog and CSV story-export defaults

There is intentionally **no PVC**. Questionnaire answers remain in the user's browser and are saved/loaded as local JSON files.

Apply:

```bash
oc apply -k deploy/
```

The image in `deployment.yaml` is a placeholder:

```text
image-registry.openshift-image-registry.svc:5000/CHANGE-ME/gen-application-onboarding:latest
```

Change `CHANGE-ME` to the OpenShift project containing the image.

## ConfigMap

`configmap.yaml` contains:

- `applications.json` — the application name/abbreviation catalog.
- `story-export.properties` — defaults shown by the **Generate application stories** dialog and configurable worksheet dropdown values.

Worksheet dropdown properties include:

```properties
worksheet.data-security-classification-options=PERSONAL - NONWORK,PUBLIC - OFFICIAL RELEASE,NONCONFIDENTIAL
worksheet.target-environment-options=DEV,TEST,QA,PROD
worksheet.scaling-approach-options=HORIZONTAL,VERTICAL,HYBRID,NONE
```

`Target Environment(s)` supports multiple selections; `Scaling Approach` supports one selection.

After changing either value, restart the Deployment:

```bash
oc rollout restart deployment/gen-application-onboarding
```
