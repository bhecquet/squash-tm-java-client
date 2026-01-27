package io.github.bhecquet.entities;


import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Object representing a requirement in Squash TM
 */
public class Requirement extends Entity {

    public static final String FIELD_CURRENT_VERSION = "current_version";

    public enum Criticality {MINOR, MAJOR, CRITICAL, UNDEFINED}

    public enum Status {WORK_IN_PROGRESS, UNDER_REVIEW, APPROVED, OBSOLETE}

    private static final Map<Project, EntityCache<Requirement>> requirementCaches = new ConcurrentHashMap<>();
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_PATH = "path";
    private static final String FIELD_PARENT = "parent";
    private static final String FIELD_PROJECT = "project";
    private static final String FIELD_AUTHOR = "created_by";
    private static final String FIELD_REFERENCE = "reference";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CRITICALITY = "criticality";
    private static final String FIELD_TESTCASES = "verifying_test_cases";
    private static final String FIELD_CATEGORY = "category";

    public static final String REQUIREMENT_URL = "requirements/%s";
    public static final String REQUIREMENTS_URL = "requirements";
    public static final String REQUIREMENTS_FOR_PROJECT_URL = "projects/%d/requirements";
    private String path;
    private String description;
    private String reference;
    private String projectName = "";
    private int projectId = -1;
    private String author = "";
    private Status status = Status.WORK_IN_PROGRESS;
    private Criticality criticality = Criticality.UNDEFINED;
    private String category = "";
    private Entity parent = null;

    public Requirement(String url, String type, int id, String name) {
        super(url, type, id, name);
    }

    public Requirement(String url, String type, int id, String name, String path) {
        super(url, type, id, name);
        this.path = path;
    }

    public static Requirement fromJson(JSONObject json) {
        try {
            Requirement requirement = new Requirement(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.optString(FIELD_NAME, "")
            );
            requirement.completeDetails(json);

            return requirement;
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create Requirement from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public static Requirement get(int id) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format(REQUIREMENT_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Requirement %d does not exist", id));
        }
    }


    /**
     * Creates a requirement if it does not exist
     * Check will be done only on requirement name
     *
     * @param project      the project where requirement will be created
     * @param isHighLevel  true if it's a high level requirement
     * @param name         requirement name
     * @param description  requirement description
     * @param customFields custom fields to set
     * @param folderPath   where requirement will be created: "foo/bar"
     * @param criticality  criticality of requirement
     */
    public static Requirement create(Project project, boolean isHighLevel, String name, String description, Map<String, Object> customFields, String folderPath, Criticality criticality) {
        return create(project, isHighLevel, name, description, customFields, folderPath == null ? new ArrayList<>() : Arrays.asList(folderPath.split("/")), criticality);
    }

    public static Requirement create(Project project, boolean isHighLevel, String name, String description, Map<String, Object> customFields, List<String> folderPath, Criticality criticality) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }

        RequirementFolder parentFolder = RequirementFolder.createRequirementFolderTree(project, folderPath);

        if (folderPath == null) {
            folderPath = new ArrayList<>();
        }

        // creates the cache
        requirementCaches.putIfAbsent(project, new EntityCache<>());
        for (Requirement requirement : requirementCaches.get(project).getAll(Requirement::getAll, project)) {
            if (requirement.getName().equals(name)
                    && requirement.getPath().equals(String.format("/%s/%s/%s", project.getName(), String.join("/", folderPath), name).replace("//", "/"))) {
                return requirement;
            }
        }

        ParentEntity parent;
        parent = new ParentEntity(Objects.requireNonNullElse(parentFolder, project));

        return create(project, isHighLevel, name, description, customFields, parent, criticality);
    }

    /**
     * Creates a requirement if it does not exist
     * Check will be done only on requirement name
     *
     * @param project      the project where requirement will be created
     * @param isHighLevel  true if it's a high level requirement
     * @param name         requirement name
     * @param description  requirement description
     * @param customFields custom fields to set
     * @param parentEntity where requirement will be created: "foo/bar"
     * @param criticality  criticality of requirement
     */
    public static Requirement create(Project project, boolean isHighLevel, String name, String description, Map<String, Object> customFields, ParentEntity parentEntity, Criticality criticality) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Requirement name cannot be null or empty");
        }
        if (parentEntity == null) {
            parentEntity = new ParentEntity(project);
        }

        try {
            JSONObject body = new JSONObject();
            body.put(FIELD_TYPE, isHighLevel ? TYPE_REQUIREMENT_HIGH_LEVEL : TYPE_REQUIREMENT);
            JSONObject parent = new JSONObject();

            parent.put(FIELD_ID, parentEntity.getId());
            parent.put(FIELD_TYPE, parentEntity.getType());

            body.put("parent", parent);

            JSONObject currentVersion = new JSONObject();
            currentVersion.put(FIELD_TYPE, "requirement-version");
            currentVersion.put(FIELD_NAME, name);
            currentVersion.put(FIELD_CRITICALITY, criticality);
            currentVersion.put(FIELD_DESCRIPTION, description == null ? "" : description);
            currentVersion.put(FIELD_STATUS, Status.WORK_IN_PROGRESS.name());
            body.put(FIELD_CURRENT_VERSION, currentVersion);

            if (customFields != null && !customFields.isEmpty()) {
                JSONArray cFields = new JSONArray();
                for (Map.Entry<String, Object> customField : customFields.entrySet()) {
                    cFields.put(Map.of("code", customField.getKey(), "value", customField.getValue()));
                }
                body.put(FIELD_CUSTOM_FIELDS, cFields);
            }

            JSONObject json = getJSonResponse(buildPostRequest(apiRootUrl + REQUIREMENTS_URL).body(body));

            Requirement requirement = fromJson(json);
            requirementCaches.putIfAbsent(project, new EntityCache<>());
            requirementCaches.get(project).add(requirement);

            return requirement;
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot create requirement: %s", name), e);
        }
    }

    /**
     * Update requirement
     * Each parameter may be specified or set to null
     *
     * @param name         (optional) new name
     * @param description  (optional) new description
     * @param customFields (optional) new custom fields
     * @param criticality  (optional) new criticality
     * @param status       (optional) new status
     * @return the updated requirement
     */
    public Requirement update(String name, String description, Map<String, Object> customFields, Criticality criticality, Status status) {

        try {
            JSONObject body = new JSONObject();
            body.put(FIELD_TYPE, "requirement");

            JSONObject currentVersion = new JSONObject();
            currentVersion.put(FIELD_TYPE, "requirement-version");

            if (name != null && !name.isEmpty()) {
                currentVersion.put(FIELD_NAME, name);
            }
            if (criticality != null) {
                currentVersion.put(FIELD_CRITICALITY, criticality);
            }
            if (description != null) {
                currentVersion.put(FIELD_DESCRIPTION, description);
            }
            if (status != null) {
                currentVersion.put(FIELD_STATUS, status.name());
            }
            body.put(FIELD_CURRENT_VERSION, currentVersion);

            if (customFields != null) {
                JSONArray cFields = new JSONArray();
                for (Map.Entry<String, Object> customField : customFields.entrySet()) {
                    cFields.put(Map.of("code", customField.getKey(), "value", customField.getValue()));
                }
                body.put(FIELD_CUSTOM_FIELDS, cFields);
            }

            JSONObject json = getJSonResponse(buildPatchRequest(String.format(apiRootUrl + REQUIREMENT_URL, id)).body(body));

            completeDetails(json);
            return this;
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot update requirement", e);
        }
    }

    @Override
    public void completeDetails() {
        JSONObject json = getJSonResponse(buildGetRequest(url));
        completeDetails(json);
    }

    private void completeDetails(JSONObject json) {

        try {
            name = json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_NAME);
        } catch (JSONException e) {/* ignore */}
        try {
            description = json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_DESCRIPTION);
        } catch (JSONException e) {/* ignore */}
        try {
            projectId = json.getJSONObject(FIELD_PROJECT).getInt(FIELD_ID);
        } catch (JSONException e) {/* ignore */}
        try {
            projectName = json.getJSONObject(FIELD_PROJECT).getString(FIELD_NAME);
        } catch (JSONException e) {/* ignore */}
        try {
            path = json.optString(FIELD_PATH, "");
        } catch (JSONException e) {/* ignore */}
        try {
            author = json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_AUTHOR);
        } catch (JSONException e) {/* ignore */}
        try {
            reference = json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_REFERENCE);
        } catch (JSONException e) {/* ignore */}
        try {
            status = Status.valueOf(json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_STATUS));
        } catch (JSONException e) {/* ignore */}
        try {
            criticality = Criticality.valueOf(json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_CRITICALITY));
        } catch (JSONException e) {/* ignore */}
        try {
            category = json.getJSONObject(FIELD_CURRENT_VERSION).getJSONObject(FIELD_CATEGORY).getString("code");
        } catch (JSONException e) {/* ignore */}
        try {
            readCustomFields(json.getJSONObject(FIELD_CURRENT_VERSION).getJSONArray(FIELD_CUSTOM_FIELDS));
        } catch (JSONException e) {/* ignore */}
        try {
            parent = Entity.entityFromJson(json.getJSONObject(FIELD_PARENT));
        } catch (JSONException e) {/* ignore */}

    }


    public static List<Requirement> getAll(Project project) {
        return getAll(project, "path,name,reference");
    }

    public static List<Requirement> getAll(Project project, String fields) {
        JSONObject json;
        try {
            json = getPagedJSonResponse(buildGetRequest(apiRootUrl + String.format(REQUIREMENTS_FOR_PROJECT_URL, project.getId()) + "?sort=id&fields=" + URLEncoder.encode(fields, StandardCharsets.UTF_8)));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot get list of requirements for project %s: %s", project.getName(), e.getMessage()));
        }

        List<Requirement> requirements = new ArrayList<>();
        if (json.has(FIELD_EMBEDDED)) {
            for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_REQUIREMENTS).toList()) {
                requirements.add(Requirement.fromJson(folderJson));
            }

            if (json.getJSONObject(FIELD_EMBEDDED).has(FIELD_HIGH_LEVEL_REQUIREMENTS)) {
                for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_HIGH_LEVEL_REQUIREMENTS).toList()) {
                    requirements.add(Requirement.fromJson(folderJson));
                }
            }
        }
        return requirements;

    }


    public String getReference() {
        return reference;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status.toString();
    }

    public String getCriticality() {
        return criticality.toString();
    }

    public String getCategory() {
        return category;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Entity getParent() {
        return parent;
    }

    public List<TestCase> getCoveringTestCases() {
        List<TestCase> coveringTestCases = new ArrayList<>();
        JSONObject json = getJSonResponse(buildGetRequest(url));
        for (JSONObject testcase : (List<JSONObject>) json.getJSONObject(FIELD_CURRENT_VERSION).getJSONArray(FIELD_TESTCASES).toList()) {
            TestCase coveringTestCase = TestCase.fromJson(testcase);
            coveringTestCases.add(coveringTestCase);
        }
        return coveringTestCases;
    }

    public static Map<Project, EntityCache<Requirement>> getRequirementCache() {
        return requirementCaches;
    }
}
