package io.github.bhecquet.entities;


import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

/**
 * Object representing a requirement in Squash TM
 */
public class Requirement extends Entity {

    private static final String FILED_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_PATH = "path";
    private static final String FIELD_PROJECT = "project";
    private static final String FIELD_AUTHOR = "created_by";
    private static final String FIELD_REFERENCE = "reference";
    private static final String FIELD_TCTYPE = "type";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CRITICALITY = "criticality";
    private static final String FIELD_PREREQUISITE = "prerequisite";
    private static final String FIELD_TESTCASES = "verifying_test_cases";
    private static final String FIELD_CATEGORY = "category";

    private static final String REQUIREMENT_URL = "requirements/%s";
    private String name;
    private String path;
    private String description;
    private String reference;
    private String projectName = "";
    private int projectId = -1;
    private String author = "";
    private String status = "";
    private String criticality = "";
    private String category = "";

    public Requirement(String url, String type, int id, String name) {
        super(url, type, id, name);
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

    @Override
    public void completeDetails() {

        JSONObject json = getJSonResponse(Unirest.get(url));
        completeDetails(json);
    }

    private void completeDetails(JSONObject json) {

        name = json.getJSONObject("current_version").getString(FIELD_NAME);
        path = json.getString(FIELD_PATH);
        description = json.getJSONObject("current_version").getString(FIELD_DESCRIPTION);
        projectId = json.getJSONObject(FIELD_PROJECT).getInt(FIELD_ID);
        projectName = json.getJSONObject(FIELD_PROJECT).getString(FIELD_NAME);
        author = json.getJSONObject("current_version").getString(FIELD_AUTHOR);
        reference = json.getJSONObject("current_version").getString(FIELD_REFERENCE);
        status = json.getJSONObject("current_version").getString(FIELD_STATUS);
        criticality = json.getJSONObject("current_version").getString(FIELD_CRITICALITY);
        category = json.getJSONObject("current_version").getJSONObject(FIELD_CATEGORY).getString("code");

        readCustomFields(json.getJSONObject("current_version").getJSONArray(FIELD_CUSTOM_FIELDS));

    }

    public String getName() {
        return name;
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
        return status;
    }

    public String getCriticality() {
        return criticality;
    }

    public String getCategory() {
        return category;
    }
}
