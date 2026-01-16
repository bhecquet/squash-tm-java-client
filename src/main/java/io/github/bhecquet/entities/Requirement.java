package io.github.bhecquet.entities;


import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Object representing a requirement in Squash TM
 */
public class Requirement extends Entity {

    public static final String FIELD_CURRENT_VERSION = "current_version";

    public enum Criticality {MINOR, MAJOR, CRITICAL, UNDEFINED}

    public enum Status {WORK_IN_PROGRESS, UNDER_REVIEW, APPROVED, OBSOLETE}

    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_PATH = "path";
    private static final String FIELD_PROJECT = "project";
    private static final String FIELD_AUTHOR = "created_by";
    private static final String FIELD_REFERENCE = "reference";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CRITICALITY = "criticality";
    private static final String FIELD_TESTCASES = "verifying_test_cases";
    private static final String FIELD_CATEGORY = "category";

    public static final String REQUIREMENT_URL = "requirements/%s";
    public static final String REQUIREMENTS_URL = "requirements";
    private String path;
    private String description;
    private String reference;
    private String projectName = "";
    private int projectId = -1;
    private String author = "";
    private Status status = Status.WORK_IN_PROGRESS;
    private Criticality criticality = Criticality.UNDEFINED;
    private String category = "";

    public Requirement(String url, String type, int id, String name) {
        super(url, type, id, name);
    }

    public Requirement(String url, String type, int id, String name, String path) {
        super(url, type, id, name);
        this.path = path;
    }

    public static Requirement fromJson(JSONObject json) {
        try {
            return new Requirement(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.optString(FIELD_NAME, "")
            );
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
     * @param name         requirement name
     * @param description  requirement description
     * @param customFields custom fields to set
     * @param folderPath   where requirement will be created: "foo/bar"
     * @param criticality  criticality of requirement
     */
    public static Requirement create(Project project, String name, String description, Map<String, Object> customFields, String folderPath, Criticality criticality) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }

        RequirementFolder parentFolder = RequirementFolder.createRequirementFolderTree(project, folderPath);

        if (folderPath == null) {
            folderPath = "";
        }

        for (Requirement requirement : project.getRequirements()) {
            if (requirement.getName().equals(name)
                    && requirement.getPath().equals(String.format("/%s/%s/%s", project.getName(), folderPath, name).replace("//", "/"))) {
                return requirement;
            }
        }
        return create(project, name, description, customFields, parentFolder, criticality);
    }

    /**
     * Creates a requirement if it does not exist
     * Check will be done only on requirement name
     *
     * @param project      the project where requirement will be created
     * @param name         requirement name
     * @param description  requirement description
     * @param customFields custom fields to set
     * @param parentFolder where requirement will be created: "foo/bar"
     * @param criticality  criticality of requirement
     */
    public static Requirement create(Project project, String name, String description, Map<String, Object> customFields, RequirementFolder parentFolder, Criticality criticality) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Requirement name cannot be null or empty");
        }

        try {
            JSONObject body = new JSONObject();
            body.put(FIELD_TYPE, "requirement");
            JSONObject parent = new JSONObject();
            if (parentFolder == null) {
                parent.put(FIELD_ID, project.id);
                parent.put(FIELD_TYPE, TYPE_PROJECT);
            } else {
                parent.put(FIELD_ID, parentFolder.id);
                parent.put(FIELD_TYPE, "requirement-folder");
            }
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
            requirement.completeDetails(json);
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

        name = json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_NAME);
        description = json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_DESCRIPTION);
        projectId = json.getJSONObject(FIELD_PROJECT).getInt(FIELD_ID);
        projectName = json.getJSONObject(FIELD_PROJECT).getString(FIELD_NAME);
        path = json.optString(FIELD_PATH, "");
        author = json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_AUTHOR);
        reference = json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_REFERENCE);
        status = Status.valueOf(json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_STATUS));
        criticality = Criticality.valueOf(json.getJSONObject(FIELD_CURRENT_VERSION).getString(FIELD_CRITICALITY));
        category = json.getJSONObject(FIELD_CURRENT_VERSION).getJSONObject(FIELD_CATEGORY).getString("code");

        readCustomFields(json.getJSONObject(FIELD_CURRENT_VERSION).getJSONArray(FIELD_CUSTOM_FIELDS));

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

    public List<TestCase> getCoveringTestCases() {
        List<TestCase> coveringTestCases = new ArrayList<>();
        JSONObject json = getJSonResponse(buildGetRequest(url));
        for (JSONObject testcase : (List<JSONObject>) json.getJSONObject(FIELD_CURRENT_VERSION).getJSONArray(FIELD_TESTCASES).toList()) {
            TestCase coveringTestCase = TestCase.fromJson(testcase);
            coveringTestCases.add(coveringTestCase);
        }
        return coveringTestCases;
    }
}
