package com.example.lifecycle.service;

import java.util.List;
import java.util.Map;
import com.example.lifecycle.model.WorksheetField;
import com.example.lifecycle.model.WorksheetStory;

public final class WorksheetCatalog {
    private WorksheetCatalog() {}

    public static final List<WorksheetStory> STORIES = List.of(
        new WorksheetStory("GEN-01", "Discovery Session", "Always", 15, List.of(
            new WorksheetField("Session Date", "Date the discovery session was held", "[Date]", ""),
            new WorksheetField("Attendees", "GEN app team lead and other attendees present", "[Free text]", ""),
            new WorksheetField("Ownership Captured", "Confirm ownership information was captured in session", "[Y/N]", ""),
            new WorksheetField("Environment Captured", "Confirm environment requirements were captured in session", "[Y/N]", ""),
            new WorksheetField("Capacity Captured", "Confirm capacity requirements were captured in session", "[Y/N]", ""),
            new WorksheetField("Security Classification Captured", "Confirm security classification was captured in session", "[Y/N]", ""),
            new WorksheetField("Network Requirements Captured", "Confirm network requirements were captured in session", "[Y/N]", ""),
            new WorksheetField("Workload Type Captured", "Confirm workload type discussion occurred in session", "[Y/N]", ""),
            new WorksheetField("Replica/Scaling Design Captured", "Confirm replica/scaling design was captured in session", "[Y/N]", ""),
            new WorksheetField("Anti-affinity/Topology Captured", "Confirm anti-affinity/topology requirements were captured in session", "[Y/N]", ""),
            new WorksheetField("PDB/Probes Captured", "Confirm PDB/probe requirements were captured in session", "[Y/N]", ""),
            new WorksheetField("Storage Requirements Captured", "Confirm storage requirements were captured in session", "[Y/N]", ""),
            new WorksheetField("Findings Sign-off Obtained", "Confirm GEN app team lead validated and signed off on findings", "[Y/N — Name]", "Required before GEN-01 is considered complete"),
            new WorksheetField("Gaps/Exceptions Logged", "Any gap that can't be immediately remediated, with remediation date and approver", "[Free text]", "Must be time-bound and approved, not silently dropped"),
            new WorksheetField("Record Flagged as Exempt", "Confirm resulting record is NOT flagged as exempt from ongoing gates", "[Y/N — should be N]", "")
        )),
        new WorksheetStory("GEN-02", "Ownership, Classification & Compliance", "Always", 9, List.of(
            new WorksheetField("Business Owner Name", "Name of the accountable Business Owner", "[Free text]", ""),
            new WorksheetField("Business Owner Contact", "Contact info for Business Owner", "[Free text]", ""),
            new WorksheetField("Technical Owner Name", "Name of the accountable Technical Owner", "[Free text]", ""),
            new WorksheetField("Technical Owner Contact", "Contact info for Technical Owner", "[Free text]", ""),
            new WorksheetField("Data/Security Classification", "Classification per the application's actual data sensitivity", "[Data/Security Classification]", ""),
            new WorksheetField("Regulatory/Compliance Framework", "Applicable regulatory/compliance framework, if any", "[Free text / None]", ""),
            new WorksheetField("Security Reviewer Sign-off Required", "Is high-sensitivity classification triggering mandatory security review?", "[Y/N]", "If Y, following two fields are required"),
            new WorksheetField("Security Reviewer Name", "Name of security reviewer who signed off", "[Free text]", "Required if Sign-off Required = Y"),
            new WorksheetField("Sign-off Timestamp", "Date/time of security reviewer sign-off", "[Date/Time]", "Required if Sign-off Required = Y")
        )),
        new WorksheetStory("GEN-03", "Environment, Capacity & Network", "Always", 10, List.of(
            new WorksheetField("Target Environment(s)", "Target environment(s), e.g., Dev/Test/Prod", "[Target Environment(s)]", ""),
            new WorksheetField("Region/Location Requirement", "Required region or location", "[Free text]", ""),
            new WorksheetField("Expected CPU Sizing", "Expected CPU sizing, structured (non-free-text) format", "[Value]", ""),
            new WorksheetField("Expected Memory Sizing", "Expected memory sizing, structured (non-free-text) format", "[Value]", ""),
            new WorksheetField("Expected Storage Sizing", "Expected storage sizing, structured (non-free-text) format", "[Value]", ""),
            new WorksheetField("Availability Tier", "Required availability tier", "[Free text]", ""),
            new WorksheetField("Inbound Connectivity Needs", "Required inbound connectivity", "[Free text]", ""),
            new WorksheetField("Outbound Connectivity Needs", "Required outbound connectivity", "[Free text]", ""),
            new WorksheetField("External Integration Needs", "External integration requirements", "[Free text]", ""),
            new WorksheetField("Non-standard Network Requirement", "Does GEN require non-standard network configuration?", "[Y/N]", "If Y, flag for Network team review")
        )),
        new WorksheetStory("GEN-04", "Platform Compatibility", "Always", 6, List.of(
            new WorksheetField("Required Storage Classes", "OpenShift storage classes required by GEN", "[Free text]", ""),
            new WorksheetField("Required Operators", "OpenShift operators required by GEN", "[Free text]", "Cross-check vs GEN-05"),
            new WorksheetField("Required Networking Features", "OpenShift networking features required by GEN", "[Free text]", ""),
            new WorksheetField("Target Cluster Baseline Version", "Target cluster's AutoShift-managed baseline version", "[Value — C5/AS3.x]", ""),
            new WorksheetField("Compatibility Result", "Result of cross-check against target cluster baseline", "[Pass/Fail]", ""),
            new WorksheetField("Unmet Requirements", "Specific unmet requirements, if any", "[Free text]", "Required if Compatibility Result = Fail")
        )),
        new WorksheetStory("GEN-05", "Operator / Platform-Service Dependencies", "Conditional — only if app has a non-baseline operator/platform-service dependency", 5, List.of(
            new WorksheetField("Applicable", "Does GEN have any operator/platform-service dependency beyond standard cluster baseline?", "[Y/N]", "If N, remainder of this story is N/A"),
            new WorksheetField("Dependency Name", "Name of dependency, e.g., caching, messaging, service mesh, cert-manager, DB operator", "[Free text]", "Repeat row per dependency"),
            new WorksheetField("Classification", "Classification of the dependency", "[Standard Baseline / Non-standard]", ""),
            new WorksheetField("Flagged for Platform/Network Review", "Was a non-standard dependency flagged for review?", "[Y/N]", "Required if Classification = Non-standard"),
            new WorksheetField("Feeds Downstream Records", "Confirm dependency list feeds GEN-16 and GEN-04", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-06", "Operator Provisioning/Validation", "Conditional — only if app has a non-baseline operator/platform-service dependency", 8, List.of(
            new WorksheetField("Applicable", "Confirm GEN-05 identified at least one dependency", "[Y/N]", "If N, remainder of this story is N/A"),
            new WorksheetField("Operator Name", "Name of operator being provisioned/validated", "[Free text]", "Repeat row per operator"),
            new WorksheetField("Standard Baseline: Installed/Subscribed/Healthy", "For standard baseline operators, confirm installed, subscribed to approved channel, and healthy", "[Y/N]", "Reuses Feature C5.7/AS3.9 baseline mechanism"),
            new WorksheetField("Non-standard: Change Request Raised", "For non-standard operators, confirm formal request raised through Cluster Change Management", "[Y/N — ticket #]", "Must include impact/risk assessment"),
            new WorksheetField("Impact/Risk Assessment Completed", "Confirm impact/risk assessment completed for non-standard request", "[Y/N]", "Required if Change Request Raised = Y"),
            new WorksheetField("Operator Version", "Installed/requested operator version", "[Value]", ""),
            new WorksheetField("Runtime Compatibility Confirmed", "Confirm operator version compatible with GEN's runtime requirements", "[Y/N]", ""),
            new WorksheetField("Pending Request Blocking Deployment", "Is a required non-standard operator request still pending?", "[Y/N]", "If Y, blocks GEN-23")
        )),
        new WorksheetStory("GEN-07", "Workload Type", "Always", 8, List.of(
            new WorksheetField("Runtime Type", "JVM / Node.js / Other", "[JVM / Node.js / Other]", "Determines the applicable runtime-specific stories and configuration branches"),
            new WorksheetField("Requires Persistent Per-Replica Storage", "Does GEN require persistent per-replica storage?", "[Y/N]", ""),
            new WorksheetField("Requires Stable Network Identity", "Does GEN require stable network identity?", "[Y/N]", ""),
            new WorksheetField("Requires Stable Hostnames", "Does GEN require stable hostnames?", "[Y/N]", ""),
            new WorksheetField("Recommended Workload Type", "Workload type determined via documented decision logic", "[Deployment / StatefulSet]", "Default: Deployment"),
            new WorksheetField("Justification (if StatefulSet)", "Justification documented if StatefulSet selected instead of default", "[Free text]", "Required if Recommended Workload Type = StatefulSet"),
            new WorksheetField("Mismatch Flagged", "Is there a mismatch between GEN's architecture and requested workload type?", "[Y/N]", "If Y, must be resolved before proceeding"),
            new WorksheetField("Reviewed with App Team", "Confirm recommended workload type reviewed with GEN app team", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-08", "Replica Count & Scaling Design", "Always", 9, List.of(
            new WorksheetField("Minimum Replica Count", "Baseline: 2, preferred 3", "[Value]", ""),
            new WorksheetField("Maximum Replica Count", "If HPA/KEDA is used", "[Value]", ""),
            new WorksheetField("Below Baseline?", "Is replica count below baseline?", "[Y/N]", "If Y, requires Exception Justification + Sign-off below"),
            new WorksheetField("Exception Justification", "Business/technical justification if below baseline", "[Free text]", ""),
            new WorksheetField("Platform Owner Sign-off (if exception)", "Name, timestamp", "[Free text]", ""),
            new WorksheetField("Scaling Approach", "Horizontal (preferred) / Vertical / Hybrid", "[Scaling Approach]", ""),
            new WorksheetField("Justification (if Vertical)", "Required if vertical scaling chosen", "[Free text]", ""),
            new WorksheetField("Flagged for Architecture Review?", "Required if vertical scaling exceeds documented thresholds", "[Y/N]", ""),
            new WorksheetField("Design Feeds Deployment HA Gate", "Confirm recorded design available to gate", "[Y/N]", "Feeds GEN-21")
        )),
        new WorksheetStory("GEN-09", "Node.js CPU Sizing", "Conditional — only if Node.js-based", 6, List.of(
            new WorksheetField("Applicable?", "Is GEN Node.js-based?", "[Y/N]", "If N, mark remaining fields N/A"),
            new WorksheetField("Concurrency Model", "Uses cluster module / worker_threads, or single-threaded per pod (default)", "[Value]", ""),
            new WorksheetField("CPU Request/Limit (if single-threaded)", "Sized to ~1 vCPU per pod", "[Value]", "Scaling lever is GEN-08 replica count, not vertical CPU"),
            new WorksheetField("CPU Limit (if cluster/worker_threads used)", "Sized to match configured worker count", "[Value]", ""),
            new WorksheetField("Justification (if cluster/worker_threads used)", "Rationale for intentional use", "[Free text]", ""),
            new WorksheetField("Mismatch Flagged?", "CPU allocation vs actual concurrency model", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-10", "Anti-Affinity & Topology", "Always", 6, List.of(
            new WorksheetField("Anti-affinity Type", "Preferred or Required, appropriate to criticality tier", "[Value]", ""),
            new WorksheetField("Topology Key", "e.g., kubernetes.io/hostname", "[Value]", ""),
            new WorksheetField("Multi-AZ Cluster?", "Is target cluster multi-AZ?", "[Y/N]", ""),
            new WorksheetField("Topology Spread/Zone Anti-affinity Configured?", "Confirm configured", "[Y/N]", "Required if multi-AZ"),
            new WorksheetField("Sufficient Node Capacity for Hard (Required) Anti-affinity?", "Confirm sufficient capacity", "[Y/N]", "Required anti-affinity approved only if capacity sufficient"),
            new WorksheetField("Exception Documented (if missing)?", "Exception and approver", "[Y/N]", "Missing config blocks onboarding completion absent exception")
        )),
        new WorksheetStory("GEN-11", "PodDisruptionBudget and Health Probe Configuration", "Always", 6, List.of(
            new WorksheetField("PDB minAvailable/maxUnavailable", "Value consistent with replica count (GEN-08)", "[Value]", ""),
            new WorksheetField("Readiness Probe Config", "Endpoint, method, interval, threshold", "[Free text]", "Must target app-specific health endpoint"),
            new WorksheetField("Liveness Probe Config", "Endpoint, method, interval, threshold", "[Free text]", "Must target app-specific health endpoint"),
            new WorksheetField("Startup Probe Required?", "Based on slow initialization", "[Y/N]", ""),
            new WorksheetField("Startup Probe Config (if required)", "Endpoint, delay, threshold", "[Free text]", ""),
            new WorksheetField("Exception (if PDB/probes missing)", "Justification + approver", "[Free text]", "Missing PDB/probes blocks onboarding completion absent exception")
        )),
        new WorksheetStory("GEN-12", "Node.js Event-Loop-Aware Probes", "Conditional — only if Node.js-based", 5, List.of(
            new WorksheetField("Applicable?", "Is GEN Node.js-based?", "[Y/N]", "If N, mark remaining fields N/A"),
            new WorksheetField("Probe Type Confirmed", "HTTP (event-loop dependent) vs. bare TCP socket check", "[Value]", "Cross-check vs GEN-11"),
            new WorksheetField("Rationale Documented?", "Blocked event loop can still accept TCP connections", "[Y/N]", ""),
            new WorksheetField("Blocked Event-Loop Test Performed?", "Confirm test performed and result", "[Y/N + result]", "Probe must fail/timeout correctly when event loop is blocked"),
            new WorksheetField("Probe Timeout/Threshold Tuning", "Value + justification", "[Value / Free text]", "Avoid false positives from brief normal delays")
        )),
        new WorksheetStory("GEN-13", "Node.js Graceful Shutdown (SIGTERM) Handling", "Conditional — only if Node.js-based", 6, List.of(
            new WorksheetField("Applicable?", "Is GEN Node.js-based?", "[Y/N]", "If N, mark remaining fields N/A"),
            new WorksheetField("SIGTERM Handler Implemented?", "Confirm implemented", "[Y/N]", "Node.js does not drain in-flight requests by default"),
            new WorksheetField("Behavior on SIGTERM", "Stops new connections, completes in-flight requests, exits cleanly", "[Value / Free text]", ""),
            new WorksheetField("terminationGracePeriodSeconds Value", "Value", "[Value]", "Must exceed typical request duration"),
            new WorksheetField("Coordinated with PDB?", "Cross-check vs GEN-11", "[Y/N]", ""),
            new WorksheetField("Validated via HA Tests?", "Validated in GEN-27 and GEN-29", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-14", "Persistent Storage Requirements", "Conditional — stateful/persistent storage", 8, List.of(
            new WorksheetField("Applicable?", "Does GEN own persistent storage, or is it stateless in front of an external DB?", "[Y/N]", ""),
            new WorksheetField("Storage Class Required", "Name", "[Value]", ""),
            new WorksheetField("Access Mode", "RWO / RWX", "[Value]", ""),
            new WorksheetField("Backup Strategy", "Description", "[Free text]", ""),
            new WorksheetField("Recovery Procedure", "Description", "[Free text]", ""),
            new WorksheetField("Replication Strategy", "Description", "[Free text]", ""),
            new WorksheetField("Validated Against Cluster's Available Storage Classes?", "Confirm validation", "[Y/N]", ""),
            new WorksheetField("Business-Critical Data Stored Directly?", "Confirm", "[Y/N]", "If Y, backup/recovery strategy required before completion")
        )),
        new WorksheetStory("GEN-15", "Runtime Memory Sizing Guidance (JVM / Node.js)", "Conditional — JVM-based / Conditional — Node.js-based", 7, List.of(
            new WorksheetField("Workload Profile", "REST API / event-driven consumer / other", "[Value]", ""),
            new WorksheetField("(JVM) MaxRAMPercentage or -Xmx Setting", "Value", "[Value]", "Applies only if Runtime Type = JVM"),
            new WorksheetField("(JVM) Target Heap % of Container Limit", "Target 50–70%", "[Value]", "Applies only if Runtime Type = JVM"),
            new WorksheetField("(Node.js) --max-old-space-size / NODE_OPTIONS Set?", "Value", "[Value]", "Applies only if Runtime Type = Node.js; V8 heap ceiling is not container-aware by default"),
            new WorksheetField("(Node.js) Heap Ceiling % of Container Limit", "Target ~75–80%", "[Value]", "Applies only if Runtime Type = Node.js"),
            new WorksheetField("Container Memory Request/Limit", "Value recorded together with heap setting above", "[Value]", ""),
            new WorksheetField("Load-Testing/Tuning Validated?", "Confirm validation", "[Y/N]", "If N, flagged for follow-up review")
        )),
        new WorksheetStory("GEN-16", "Target Cluster Selection and Namespace Provisioning", "Always", 10, List.of(
            new WorksheetField("Eligible Cluster(s) Evaluated", "List, evaluated against capacity/classification/region (GEN-03)", "[Free text]", ""),
            new WorksheetField("Target Cluster Selected", "Name", "[Value]", ""),
            new WorksheetField("Cluster State Confirmed", "READY / IN SERVICE", "[Value]", ""),
            new WorksheetField("Namespace Provisioned via AutoShift", "Confirm", "[Y/N]", ""),
            new WorksheetField("Namespace Metadata: App ID", "Value", "[Value]", ""),
            new WorksheetField("Namespace Metadata: Owner", "Value", "[Value]", ""),
            new WorksheetField("Namespace Metadata: Environment", "Value", "[Value]", ""),
            new WorksheetField("Namespace Metadata: Classification", "Value", "[Value]", ""),
            new WorksheetField("Standard Quotas/Network Policies Applied?", "Confirm", "[Y/N]", ""),
            new WorksheetField("Pending Non-Standard Operator Requests?", "Confirm", "[Y/N]", "Blocks provisioning if Y; cross-check vs GEN-06")
        )),
        new WorksheetStory("GEN-17", "Least-Privilege Access and Secrets Integration", "Always", 6, List.of(
            new WorksheetField("RBAC Roles Applied", "Developer/Operator/Viewer templates confirmed", "[Value]", "No cluster-admin or overly broad roles"),
            new WorksheetField("Cluster-Admin/Broad Roles Requested?", "Confirm", "[Y/N]", "Expected: No"),
            new WorksheetField("Access Grant Log", "Requester, approver, timestamp", "[Free text]", ""),
            new WorksheetField("Secrets Management Integration", "Vault / External Secrets / other", "[Value]", ""),
            new WorksheetField("Plaintext Secrets Present?", "Confirm", "[Y/N]", "Expected: No"),
            new WorksheetField("Resource Quotas Configured", "Values per GEN-03", "[Value]", "")
        )),
        new WorksheetStory("GEN-18", "Onboarding Completion Validation", "Always", 11, List.of(
            new WorksheetField("Namespace Complete?", "Confirm", "[Y/N]", ""),
            new WorksheetField("Access Complete?", "Confirm", "[Y/N]", ""),
            new WorksheetField("Quotas Complete?", "Confirm", "[Y/N]", ""),
            new WorksheetField("Monitoring Hooks Complete?", "Confirm", "[Y/N]", ""),
            new WorksheetField("Security Sign-off Complete?", "Confirm", "[Y/N]", "Cross-check vs GEN-02"),
            new WorksheetField("HA Design Items Confirmed (as applicable)", "Confirm", "[Y/N]", "Cross-check vs GEN-07–GEN-15"),
            new WorksheetField("Operator Dependencies Confirmed (as applicable)", "Confirm", "[Y/N]", "Cross-check vs GEN-05/GEN-06"),
            new WorksheetField("Incomplete Items Description", "Any incomplete item", "[Free text]", "Any incomplete item blocks transition"),
            new WorksheetField("Validation Evidence Recorded?", "Confirm", "[Y/N]", ""),
            new WorksheetField("State Transitioned to READY FOR DEPLOYMENT?", "Confirm", "[Y/N]", ""),
            new WorksheetField("Owners Notified?", "Business and Technical Owners", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-19", "Deployment Manifests in Git (AutoShift/ArgoCD)", "Always", 6, List.of(
            new WorksheetField("Manifests Authored", "Deployment/StatefulSet, Service, Route, PDB, probes, resource requests/limits authored", "[Value]", ""),
            new WorksheetField("Git Repository Location", "URL of repository containing manifests", "[URL]", ""),
            new WorksheetField("Repository Linked to App Inventory Record", "Confirm repo is linked", "[Y/N]", ""),
            new WorksheetField("Branch Protection Enforced", "Confirm branch protection enabled on production-tracked branch", "[Y/N]", ""),
            new WorksheetField("PR Review Required on Production Branch", "Confirm PR review enforced before merge", "[Y/N]", ""),
            new WorksheetField("Manual Edit Drift Detection Enabled", "Confirm direct in-cluster edits are flagged as drift, not a valid change path", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-20", "Release Artifact, Policy Conformance, and Vulnerability Scan Validation", "Always", 7, List.of(
            new WorksheetField("Image Signature/Provenance Validated", "Confirm container image signature/provenance validated before deployment", "[Y/N]", ""),
            new WorksheetField("Manifest Policy Check Result", "Resource limits present, no privileged containers, approved labels present", "[Pass/Fail]", ""),
            new WorksheetField("Vulnerability Scan Integrated in CI/CD", "Confirm build-time scan is part of pipeline", "[Y/N]", ""),
            new WorksheetField("Severity Threshold Defined", "Threshold that blocks promotion if exceeded", "[Value]", ""),
            new WorksheetField("Documented Exception", "Justification + approver, if threshold exceeded", "[Free text]", ""),
            new WorksheetField("Validation Results Logged/Retrievable", "Confirm pass/fail, findings, policy violations are logged", "[Y/N]", ""),
            new WorksheetField("Failure Reason Specificity Confirmed", "Confirm failures return specific, actionable reason vs. generic failure", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-21", "HA Configuration Gate (Deploy-Time)", "Always (conditional clauses apply if Node.js-based)", 12, List.of(
            new WorksheetField("Minimum Replica Count in Manifest", "Value found in manifest at deploy time", "[Value]", "Cross-check vs GEN-08"),
            new WorksheetField("Anti-affinity/Topology Spread Present", "Confirm present in manifest", "[Y/N]", "Cross-check vs GEN-10"),
            new WorksheetField("PodDisruptionBudget Present", "Confirm present in manifest", "[Y/N]", "Cross-check vs GEN-11"),
            new WorksheetField("Readiness Probe Present", "Confirm present in manifest", "[Y/N]", "Cross-check vs GEN-11"),
            new WorksheetField("Liveness Probe Present", "Confirm present in manifest", "[Y/N]", "Cross-check vs GEN-11"),
            new WorksheetField("(Node.js) CPU Request/Limit Matches Concurrency Model", "Confirm consistent with documented model", "[Y/N]", "Cross-check vs GEN-09; N/A if not Node.js"),
            new WorksheetField("(Node.js) SIGTERM Handling Implemented", "Confirm implemented in manifest/app", "[Y/N]", "Cross-check vs GEN-13; N/A if not Node.js"),
            new WorksheetField("(Node.js) terminationGracePeriodSeconds Set", "Value set, consistent with GEN-13", "[Value]", "N/A if not Node.js"),
            new WorksheetField("Discrepancies vs Onboarding HA Design", "List discrepancies found", "[Free text]", "Blocks deployment pending correction unless exception approved"),
            new WorksheetField("Approved Exceptions on File", "Reference number, if any", "[Value]", ""),
            new WorksheetField("Gate Result", "Pass/Fail", "[Pass/Fail]", ""),
            new WorksheetField("Gate Result Linked to Onboarding HA Design Record", "Confirm linkage recorded", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-22", "Memory Configuration Consistency Check (Deploy-Time)", "Conditional — JVM branch / Conditional — Node.js branch", 11, List.of(
            new WorksheetField("Runtime Type", "JVM / Node.js", "[Value]", "Determines applicable branch below"),
            new WorksheetField("[JVM] Container-Aware Memory Flag Present", "e.g., MaxRAMPercentage", "[Y/N]", "JVM branch only"),
            new WorksheetField("[JVM] -Xmx Value (if fixed)", "Value, if used instead of container-aware flag", "[Value]", "JVM branch only"),
            new WorksheetField("[JVM] -Xmx Consistent with Container Limit", "Confirm proportionally consistent per GEN-15 guidance", "[Y/N]", "JVM branch only"),
            new WorksheetField("[JVM] Blocked Due to -Xmx Container Limit", "Confirm block triggered if applicable", "[Y/N]", "JVM branch only"),
            new WorksheetField("[JVM] Exception Justification", "Documented justification", "[Free text]", "JVM branch only"),
            new WorksheetField("[Node.js] --max-old-space-size / NODE_OPTIONS Set", "Confirm explicitly set", "[Y/N]", "Node.js branch only"),
            new WorksheetField("[Node.js] Heap Ceiling Consistent with Container Limit", "Confirm consistent per GEN-15 guidance", "[Y/N]", "Node.js branch only"),
            new WorksheetField("[Node.js] Blocked Due to Missing/Excessive Ceiling", "Confirm block triggered if applicable", "[Y/N]", "Node.js branch only"),
            new WorksheetField("[Node.js] Exception Justification", "Documented justification", "[Free text]", "Node.js branch only"),
            new WorksheetField("Check Result Recorded on Deployment Record", "Confirm recorded", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-23", "Deploy via AutoShift/ArgoCD GitOps Pipeline", "Always", 9, List.of(
            new WorksheetField("Target Environment/Cluster Confirmed vs Onboarding Assignment", "Confirm match before sync", "[Y/N]", "Cross-check vs GEN-16"),
            new WorksheetField("Deployment Executed via ArgoCD Sync", "Confirm no manual oc apply steps required", "[Y/N]", ""),
            new WorksheetField("Manual oc apply Steps Used", "Confirm none used", "[Y/N]", "Expected: No"),
            new WorksheetField("App Team Holds Cluster Admin Credentials", "Confirm app team does not require direct credentials", "[Y/N]", "Expected: No"),
            new WorksheetField("Initiator (Automation Identity)", "Identity that triggered deployment", "[Value]", ""),
            new WorksheetField("Timestamp of Deployment", "Date/time of execution", "[Date/Time]", ""),
            new WorksheetField("Source Commit/Revision", "Git commit SHA deployed", "[Value]", ""),
            new WorksheetField("Manual Override Used", "Confirm whether break-glass override was used", "[Y/N]", "If Yes, document justification"),
            new WorksheetField("Override Audited Separately", "Confirm separate audit trail exists for override", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-24", "Deployment Success and Zero-Downtime Validation", "Always", 9, List.of(
            new WorksheetField("Readiness Probes Pass Post-Deployment", "Confirm pass", "[Y/N]", ""),
            new WorksheetField("Replica Count Healthy", "Confirm healthy replica count", "[Y/N]", ""),
            new WorksheetField("Smoke Test Result", "Pass/Fail", "[Pass/Fail]", ""),
            new WorksheetField("Rollout Strategy Configured", "maxUnavailable/maxSurge values", "[Value]", ""),
            new WorksheetField("Rollout Strategy Validated as Correct for Replica Count", "Confirm correctly configured", "[Y/N]", ""),
            new WorksheetField("Service Endpoint Availability Monitored During Rollout", "Confirm monitored throughout", "[Y/N]", ""),
            new WorksheetField("Availability Gap Observed During Rollout", "Duration/description, if any", "[Free text]", ""),
            new WorksheetField("Rollout Flagged as Degraded Despite Healthy Final State", "Confirm flag raised if gap exceeded tolerance", "[Y/N]", ""),
            new WorksheetField("Application State Transition to RUNNING", "Confirm transition occurred only on successful validation", "[Y/N + Timestamp]", "")
        )),
        new WorksheetStory("GEN-25", "Deployment Evidence and Rollback Confirmation", "Always", 12, List.of(
            new WorksheetField("Git Commit SHA", "Value", "[Value]", ""),
            new WorksheetField("Target Environment", "Value", "[Value]", ""),
            new WorksheetField("Initiator", "Value", "[Value]", ""),
            new WorksheetField("Timestamp", "Value", "[Date/Time]", ""),
            new WorksheetField("Validation Results", "Pass/Fail summary", "[Free text]", ""),
            new WorksheetField("Final Status", "Value", "[Value]", ""),
            new WorksheetField("Automated Failure Detection Tested", "Confirm simulated/test failed deployment correctly flagged", "[Y/N]", ""),
            new WorksheetField("Alert Routed to Responsible Team", "Confirm alert delivery on simulated failure", "[Y/N]", ""),
            new WorksheetField("Rollback Mechanism Confirmed Available", "ArgoCD rollback to prior sync revision", "[Y/N]", ""),
            new WorksheetField("Rollback Single-Action Initiable", "Confirm single-action capability", "[Y/N]", ""),
            new WorksheetField("HA Configuration Snapshot Captured", "Replica count, anti-affinity mode, PDB settings, probe configuration", "[Value / Link]", ""),
            new WorksheetField("Lifecycle State Auto-Updates on Outcome", "Confirm automatic update based on deployment outcome", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-26", "HA Test Plan Definition", "Always", 5, List.of(
            new WorksheetField("Applicable Failure Scenarios Identified", "Pod failure, rolling upgrade, node failure, readiness/dependency failure, zone failure, load-during-failure", "[Free text]", "Based on architecture/topology"),
            new WorksheetField("Pass/Fail Criteria per Scenario", "e.g., zero failed requests, recovery within N seconds, no manual intervention", "[Free text]", ""),
            new WorksheetField("Test Plan Version-Controlled", "Confirm stored alongside deployment configuration", "[Y/N]", "Location"),
            new WorksheetField("Test Plan Reviewed as Part of Onboarding HA Design Review", "Confirm review occurred", "[Y/N]", "Cross-check vs GEN-07–GEN-15"),
            new WorksheetField("Applicability of Each Scenario Explicitly Recorded", "Confirm recorded per application's Applicability designations, not re-derived ad hoc", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-27", "Pod Failure Test", "Always (graceful-shutdown check conditional — Node.js)", 10, List.of(
            new WorksheetField("Pod Deleted (Test Execution)", "Confirm test executed", "[Y/N + Timestamp]", ""),
            new WorksheetField("Automatic Replacement Confirmed", "Confirm transparent, automatic replacement", "[Y/N]", ""),
            new WorksheetField("Replacement Pod Reached Ready Without Manual Action", "Confirm", "[Y/N]", ""),
            new WorksheetField("Continuous Availability During Test", "Confirm via concurrent synthetic request stream", "[Y/N]", ""),
            new WorksheetField("Failed request count", "Count", "[Value]", ""),
            new WorksheetField("(Node.js) In-Flight Requests Completed Cleanly", "Confirm per SIGTERM handling", "[Y/N]", "Cross-check vs GEN-13; N/A if not Node.js"),
            new WorksheetField("(Node.js) Dropped Requests Attributable to Abrupt Termination", "Confirm none", "[Y/N]", "N/A if not Node.js"),
            new WorksheetField("Observed Recovery Time", "Value", "[Value]", ""),
            new WorksheetField("Test Result", "Pass/Fail", "[Pass/Fail]", "Failed test blocks production acceptance until remediated/retested"),
            new WorksheetField("Evidence Retained", "Location/link", "[URL / Free text]", "")
        )),
        new WorksheetStory("GEN-28", "Rolling Upgrade Test", "Always", 5, List.of(
            new WorksheetField("Rolling Restart Triggered", "Confirm oc rollout restart executed", "[Y/N + Timestamp]", ""),
            new WorksheetField("Zero Downtime Confirmed During Rollout", "Confirm via synthetic traffic / readiness-gated load balancing", "[Y/N]", ""),
            new WorksheetField("Rollout Completed with All Replicas on New Revision", "Confirm", "[Y/N]", ""),
            new WorksheetField("Result Linked to GEN-24", "Confirm linkage recorded", "[Y/N]", ""),
            new WorksheetField("Repeated Failures Observed", "Confirm whether repeated failures occurred", "[Y/N]", "If Yes, triggers review of readiness probe config and maxUnavailable/maxSurge settings")
        )),
        new WorksheetStory("GEN-29", "Node Failure/Drain Test", "Always (hard anti-affinity capacity check; graceful-shutdown check conditional — Node.js)", 7, List.of(
            new WorksheetField("Worker Node Drained", "Node hosting GEN pod(s) drained in test/non-prod env, coordinated with Platform team", "[Y/N]", ""),
            new WorksheetField("Pods Rescheduled to Healthy Nodes", "Confirm displaced pods rescheduled successfully", "[Y/N]", ""),
            new WorksheetField("Pods Reached Ready State", "Confirm rescheduled pods reach Ready without manual action", "[Y/N]", ""),
            new WorksheetField("Service Availability Maintained Throughout", "Confirm availability consistent with anti-affinity/topology spread", "[Y/N]", "Cross-check vs GEN-10"),
            new WorksheetField("Hard Anti-Affinity Pending Check", "If required (hard) anti-affinity used, confirm pod did not become permanently Pending due to insufficient capacity", "[Y/N / N/A]", "N/A if soft anti-affinity"),
            new WorksheetField("Node.js Clean SIGTERM on Drain", "Confirm drain triggered clean SIGTERM handling within terminationGracePeriodSeconds, not abrupt kill", "[Y/N]", "Node.js only; cross-check vs GEN-13"),
            new WorksheetField("Test Result", "Pass/Fail", "[Pass/Fail]", "Evidence retained per GEN-34")
        )),
        new WorksheetStory("GEN-30", "Readiness/Dependency Failure Test", "Always", 6, List.of(
            new WorksheetField("Dependency Outage Simulated", "Describe simulated outage (e.g., blocked network access to database)", "[Free text]", ""),
            new WorksheetField("Readiness Probe Failure Confirmed", "Confirm probe failure detected and pod removed from service endpoints within expected detection window", "[Y/N]", "Cross-check vs GEN-11"),
            new WorksheetField("Traffic Routed Only to Healthy Replicas", "Confirm during outage, traffic served only by healthy pods (if any remain)", "[Y/N]", ""),
            new WorksheetField("Pod Rejoined Service After Recovery", "Confirm pod returns to Ready and rejoins service automatically once dependency restored", "[Y/N]", "No manual intervention"),
            new WorksheetField("Detection Time Documented", "Observed time to detect failure", "[Value]", ""),
            new WorksheetField("Recovery Time Documented", "Observed time to recover once dependency restored", "[Value]", "")
        )),
        new WorksheetStory("GEN-31", "Zone Failure Test", "Conditional — only if target cluster is multi-zone", 7, List.of(
            new WorksheetField("Multi-Zone Applicability Confirmed", "Is target cluster multi-zone?", "[Y/N]", "If No, mark test N/A — do not skip silently"),
            new WorksheetField("Simulation Method", "Platform-team-supported method used to simulate/execute zone unavailability", "[Free text]", ""),
            new WorksheetField("Zones Affected", "Which zone(s) taken down for the test", "[Free text]", ""),
            new WorksheetField("Remaining Replica Behavior", "Confirm replicas in unaffected zones continue serving traffic without manual failover", "[Y/N]", "Cross-check vs GEN-10"),
            new WorksheetField("Recovery/Rebalancing Confirmed", "Confirm rebalancing once zone is restored", "[Y/N]", ""),
            new WorksheetField("Limitation Documented", "If test not feasible on target cluster, document limitation explicitly", "[Free text]", ""),
            new WorksheetField("Test Result", "Pass/Fail/N/A", "[Pass/Fail/N/A]", "Feeds GEN-34, GEN-35")
        )),
        new WorksheetStory("GEN-32", "Load Test During Simulated Failure", "Always", 9, List.of(
            new WorksheetField("Load Tool Used", "Approved tool (JMeter/k6/Gatling/etc.)", "[Value]", ""),
            new WorksheetField("Target Load Volume", "Volume representative of expected GEN production traffic", "[Value]", ""),
            new WorksheetField("Concurrent Failure Scenario", "Failure executed concurrently with load (pod failure, rolling upgrade, or node failure)", "[Value]", "Cross-check vs GEN-27/28/29"),
            new WorksheetField("Error Rate Observed", "Observed error rate during test", "[Value]", ""),
            new WorksheetField("Latency Impact Observed", "Observed latency impact during test", "[Value]", ""),
            new WorksheetField("Recovery Time Observed", "Observed recovery time under load", "[Value]", ""),
            new WorksheetField("SLO Definition", "GEN's defined SLO used for comparison", "[Free text]", ""),
            new WorksheetField("SLO Met Under Failure", "Confirm results meet SLO under failure conditions, not just normal operation", "[Y/N]", ""),
            new WorksheetField("Evidence Retained", "Location/link", "[URL / Free text]", "Feeds GEN-34")
        )),
        new WorksheetStory("GEN-33", "DB/Stateful Dependency Failover Validation", "Conditional — only if app has a stateful/DB dependency", 8, List.of(
            new WorksheetField("Dependency Applicability Confirmed", "Does GEN have a stateful/DB dependency?", "[Y/N]", "If No, mark N/A"),
            new WorksheetField("Dependency HA Mechanism", "Description of dependency's own failover mechanism", "[Free text]", ""),
            new WorksheetField("Failover Trigger Method", "How failover was triggered/simulated", "[Free text]", ""),
            new WorksheetField("Reconnect/Retry Behavior Confirmed", "Confirm GEN detects failover and reconnects/retries automatically, without pod restart or manual intervention", "[Y/N]", ""),
            new WorksheetField("In-Flight Transaction Handling", "Consistency/retry semantics observed; confirm no silent data loss where avoidable", "[Free text]", ""),
            new WorksheetField("Ownership Note", "Document explicitly as application/dependency-owned HA, not platform-provided", "[Free text]", ""),
            new WorksheetField("HA Gap Flagged", "If GEN lacks documented failover behavior, flag as HA gap before production acceptance", "[Y/N]", ""),
            new WorksheetField("Test Result", "Pass/Fail/N/A", "[Pass/Fail/N/A]", "Feeds GEN-34")
        )),
        new WorksheetStory("GEN-34", "HA Test Evidence Retention", "Always", 6, List.of(
            new WorksheetField("Evidence Package Contents", "Test type, environment, execution timestamp, executor, observed behavior, pass/fail result, anomalies noted", "[Free text / Link]", "Applies to GEN-27–GEN-33 results"),
            new WorksheetField("Linkage", "Evidence linked to GEN application record and specific promotion decision it supports", "[URL / Free text]", ""),
            new WorksheetField("Retention Policy Applied", "Organizational retention policy confirmed", "[Y/N]", ""),
            new WorksheetField("Retrieval SLA Defined", "SLA for retrieval when requested", "[Value]", ""),
            new WorksheetField("Failed Test History Retained", "Failed test evidence retained alongside remediation actions and re-test results", "[Y/N]", ""),
            new WorksheetField("N/A Tests Explicitly Noted", "Confirm GEN-31/GEN-33 marked explicitly not applicable if skipped, not silently omitted", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-35", "Production Promotion HA Gate", "Always", 6, List.of(
            new WorksheetField("Mandatory Test Set Results", "Recorded passing results for pod failure, rolling upgrade, readiness failure, and zone failure (if multi-zone)", "[Free text / Links]", "Cross-check vs GEN-27, GEN-28, GEN-30, GEN-31"),
            new WorksheetField("Gate Status", "Pass/Block", "[Pass/Block]", ""),
            new WorksheetField("Missing/Failed Results", "List any missing or failed mandatory results", "[Free text]", "Blocks promotion if present"),
            new WorksheetField("Exception Justification", "Documented justification if exception requested", "[Free text]", ""),
            new WorksheetField("Exception Approval", "Time-bound approval from GEN Application Owner and Platform Owner", "[Name / Date / Time]", ""),
            new WorksheetField("Evidence Linkage", "Gate status and linked evidence visible on promotion request record", "[URL / Free text]", "")
        )),
        new WorksheetStory("GEN-36", "Joint UAT Validation", "Always", 7, List.of(
            new WorksheetField("UAT Test Plan", "Business-critical functional workflows, jointly defined by Application Team and GEN Application Owner", "[Free text / Link]", ""),
            new WorksheetField("UAT Environment Confirmed", "Confirm UAT executed against GEN running on target cluster", "[Y/N]", ""),
            new WorksheetField("Prerequisite Validation", "Confirm GEN-24 and GEN-35 passed before UAT execution", "[Y/N]", "Blocked By: GEN-35"),
            new WorksheetField("UAT Execution Results", "Pass/fail per workflow", "[Free text]", ""),
            new WorksheetField("Defects Found", "List defects and resolution status", "[Free text]", ""),
            new WorksheetField("UAT Sign-off", "Name, role, timestamp of GEN Application Owner sign-off", "[Free text]", ""),
            new WorksheetField("Evidence Retained", "Sign-off, test plan, results retained as part of migration evidence package", "[Y/N / Link]", "")
        )),
        new WorksheetStory("GEN-37", "Cutover Schedule, Maintenance Window, Blackout Constraints", "Always", 7, List.of(
            new WorksheetField("Target Cutover Date/Time", "Defined cutover date/time", "[Date/Time]", ""),
            new WorksheetField("Maintenance Window Duration", "Defined duration", "[Value]", ""),
            new WorksheetField("Blackout Period Check", "Schedule checked against declared organizational blackout periods", "[Y/N]", ""),
            new WorksheetField("Conflict Identified", "Confirm whether conflict exists", "[Y/N]", ""),
            new WorksheetField("Resolution if Conflict", "Reschedule or emergency-change-style exception with elevated approval", "[Free text]", ""),
            new WorksheetField("Stakeholder Communication", "Confirm maintenance window communicated to GEN Owner, dependent app owners, Service Management", "[Y/N]", ""),
            new WorksheetField("Sign-off", "GEN Application Owner and Platform Owner sign-off before execution", "[Name / Date / Time]", "Blocked By: GEN-36")
        )),
        new WorksheetStory("GEN-38", "Rollback Triggers and Decision Criteria", "Always", 4, List.of(
            new WorksheetField("Rollback Trigger Conditions", "Specific, objective conditions that trigger invoking rollback (e.g., error rate threshold, data integrity failure, HA regression, broken UAT-critical workflow)", "[Free text]", "Cross-check vs GEN-25"),
            new WorksheetField("Decision Authority", "Named individual(s) who can invoke rollback and applicable decision time window", "[Free text]", ""),
            new WorksheetField("Review/Agreement", "Confirm reviewed and agreed as part of same sign-off as cutover schedule", "[Y/N]", "Cross-check vs GEN-37"),
            new WorksheetField("Reference in Hypercare", "Confirm trigger criteria will be explicitly referenced during hypercare daily stability checks", "[Y/N]", "Cross-check vs GEN-43")
        )),
        new WorksheetStory("GEN-39", "Multi-Party Cutover Sign-off", "Always", 7, List.of(
            new WorksheetField("Platform Lead Sign-off", "Name and timestamp of Platform Lead approval", "[Free text]", ""),
            new WorksheetField("Security Lead Sign-off", "Name and timestamp of Security Lead approval", "[Free text]", ""),
            new WorksheetField("GEN Application Owner Sign-off", "Name and timestamp of Application Owner approval", "[Free text]", ""),
            new WorksheetField("Completeness Check", "Confirmation all three sign-offs obtained before execution", "[Y/N]", "Partial sign-off blocks execution"),
            new WorksheetField("Rollback/Recovery Strategy Link", "Reference to rollback mechanism", "[URL / Free text]", "Link to GEN-25"),
            new WorksheetField("Blackout Constraint Link", "Reference to blackout-period constraints applicable to cutover window", "[URL / Free text]", "Link to GEN-37"),
            new WorksheetField("Evidence Retained", "Sign-off completeness and identity retained as evidence", "[Y/N]", "")
        )),
        new WorksheetStory("GEN-40", "Production Promotion/Cutover Execution", "Always", 7, List.of(
            new WorksheetField("Promotion Approval Record", "Approver identity, decision, and timestamp", "[Free text]", ""),
            new WorksheetField("Gate Satisfaction Confirmation", "Confirm standard production promotion gate is satisfied", "[Y/N]", "Satisfied by GEN-39 sign-off"),
            new WorksheetField("Deployment Trigger Confirmation", "Confirm desired-state update triggers automated deployment via AutoShift/ArgoCD", "[Y/N]", "Reuses mechanism validated in GEN-23"),
            new WorksheetField("DNS/Ingress Cutover Confirmation", "DNS/ingress cutover executed and validated", "[Y/N]", ""),
            new WorksheetField("Unapproved Attempt Blocking", "Confirmation unapproved promotion attempts are blocked", "[Y/N]", ""),
            new WorksheetField("Execution Timestamp", "Date/time of promotion execution", "[Date/Time]", ""),
            new WorksheetField("Source Commit/Revision", "Git commit/revision promoted", "[Value]", "")
        )),
        new WorksheetStory("GEN-41", "Operational Ownership & Escalation Path", "Always", 8, List.of(
            new WorksheetField("Operational Owner", "Named individual/team accountable for GEN in production", "[Free text]", ""),
            new WorksheetField("Owner Contact Info", "Phone/email/chat channel for operational owner", "[Free text]", ""),
            new WorksheetField("On-Call Contact", "Primary on-call/escalation contact for GEN", "[Free text]", ""),
            new WorksheetField("Escalation Path — Secondary", "Contact escalated to if primary doesn't acknowledge", "[Free text]", ""),
            new WorksheetField("Escalation Path — Tertiary", "Contact escalated to if secondary doesn't acknowledge", "[Free text]", ""),
            new WorksheetField("Escalation Time Thresholds", "Time allowed before auto-escalating to next level", "[Value]", ""),
            new WorksheetField("Ownership Attestation Frequency", "Cadence for periodic review/attestation of ownership info", "[Value]", ""),
            new WorksheetField("Ownership Status", "Confirmation ownership/escalation info is complete", "[Y/N]", "Missing ownership blocks continued IN SERVICE status")
        )),
        new WorksheetStory("GEN-42", "Continuous Monitoring Enablement", "Always", 7, List.of(
            new WorksheetField("Health Endpoint Monitoring Enabled", "Confirmation health endpoint monitoring is active", "[Y/N]", ""),
            new WorksheetField("Resource Utilization Monitoring Enabled", "Confirmation CPU/memory/storage monitoring is active", "[Y/N]", ""),
            new WorksheetField("Error Rate Monitoring Enabled", "Confirmation error rate monitoring is active", "[Y/N]", ""),
            new WorksheetField("Monitoring Coverage Gap Check", "Confirmation no gap exists between cutover and active monitoring", "[Y/N]", ""),
            new WorksheetField("SLO Definition", "SLO for GEN (availability/performance)", "[Free text]", ""),
            new WorksheetField("SLO Measurement Mechanism", "Tool/dashboard measuring performance against SLO", "[Free text]", ""),
            new WorksheetField("Monitoring Data Retention Policy", "Retention period applied per organizational policy", "[Value]", "")
        )),
        new WorksheetStory("GEN-43", "Hypercare Monitoring Period", "Always", 8, List.of(
            new WorksheetField("Hypercare Duration", "Length of hypercare window, based on criticality tier", "[Value]", "Extends standard post-change observation window rather than replacing it"),
            new WorksheetField("Hypercare Start/End Dates", "Start and end dates documented on change/cutover record", "[Date/Time]", ""),
            new WorksheetField("Tightened Alert Thresholds", "Specific tightened thresholds during hypercare (error rate, latency, etc.)", "[Free text]", ""),
            new WorksheetField("Threshold Revert Trigger", "Confirmation thresholds auto-revert to standard at window close", "[Y/N]", ""),
            new WorksheetField("Daily Stability Check Log", "Log of daily checks performed during hypercare", "[Free text / Link]", ""),
            new WorksheetField("Rollback Trigger Reference", "Confirmation daily checks reference rollback trigger criteria", "[Y/N]", "Cross-check vs GEN-38"),
            new WorksheetField("Hypercare Exit Sign-off", "Formal stability sign-off from GEN Application Owner", "[Name / Date]", ""),
            new WorksheetField("Migration-Related Defect Tag", "Tagging mechanism for issues found during hypercare, tracked distinctly", "[Value]", "")
        )),
        new WorksheetStory("GEN-44", "Final Migration Closure Evidence Package", "Always", 10, List.of(
            new WorksheetField("Onboarding Record Reference", "Link to onboarding evidence GEN-01–GEN-18", "[URL / Free text]", ""),
            new WorksheetField("Deployment/HA Gate Results Reference", "Link to deployment/HA gate evidence GEN-19–GEN-25", "[URL / Free text]", ""),
            new WorksheetField("HA Test Evidence Reference", "Link to HA test evidence GEN-26–GEN-34", "[URL / Free text]", ""),
            new WorksheetField("Promotion Gate & UAT Sign-off Reference", "Link to promotion gate and UAT sign-off GEN-35–GEN-36", "[URL / Free text]", ""),
            new WorksheetField("Cutover Sign-off Reference", "Link to multi-party cutover sign-off GEN-39", "[URL / Free text]", ""),
            new WorksheetField("Promotion Execution Reference", "Link to promotion execution record GEN-40", "[URL / Free text]", ""),
            new WorksheetField("Hypercare Sign-off Reference", "Link to hypercare exit sign-off GEN-43", "[URL / Free text]", ""),
            new WorksheetField("Accountable Owner Sign-off", "Platform Owner name and date", "[Name / Date]", ""),
            new WorksheetField("Closure Record Retention Location", "Where/how closure record is stored per audit/retention policy", "[Free text / URL]", ""),
            new WorksheetField("Stakeholder Notification", "Confirmation requester/stakeholders notified of completed migration", "[Y/N]", "")
        ))
    );

    public static WorksheetStory story(String id) {
        return STORIES.stream().filter(s -> s.id().equals(id)).findFirst().orElseThrow();
    }

}