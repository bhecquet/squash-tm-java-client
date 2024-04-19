package fr.bhecquet.entities;


import fr.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a test case in Squash TM
 */
public class TestCase extends Entity {

    private static final String FIELD_AUTOMATED_TEST_REFERENCE = "automated_test_reference";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_PATH = "path";
    private static final String FIELD_PROJECT = "project";
    private static final String FIELD_AUTHOR = "created_by";
    private static final String FIELD_NATURE = "nature";
    private static final String FIELD_REFERENCE = "reference";
    private static final String FIELD_TCTYPE = "type";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_STEPS = "steps";
    private static final String FIELD_IMPORTANCE = "importance";
    private static final String FIELD_PREREQUISITE = "prerequisite";
    private static final String FIELD_REQUIREMENTS = "verified_requirements";

    private static final String TEST_CASE_URL = "test-cases/%s";
    private String automatedTestReference;
    private String path;
    private String description;
    private String reference;
    private String marque = "";
    private String produit = "";
    private String application = "";
    private String projectName = "";
    private int projectId = -1;
    private String author = "";
    private String nature = "";
    private String tctype = "";
    private String status = "";
    private String importance = "";
    private String prerequisite = "";
    private int requirementNumber = -1;
    private List<TestStep> testSteps;

    public TestCase(int id) {
        super("", id, null);
    }

    public TestCase(int id, String url) {
        super(url, id, null);
    }


    public static TestCase fromJson(JSONObject json) {
        try {
            return new TestCase(
                    json.getInt(FIELD_ID),
                    json.getJSONObject("_links").getJSONObject("self").getString("href")
            );
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create TestCase from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public static TestCase get(int id) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format(TEST_CASE_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Test Case %d does not exist", id));
        }
    }

    public void completeDetails() {
        testSteps = new ArrayList<>();

        JSONObject json = getJSonResponse(Unirest.get(url));
        automatedTestReference = json.optString(FIELD_AUTOMATED_TEST_REFERENCE, "");
        path = json.getString(FIELD_PATH);
        description = json.getString(FIELD_DESCRIPTION);
        projectId = json.getJSONObject("project").getInt(FIELD_ID);
        projectName = json.getJSONObject("project").getString(FIELD_NAME);
        author = json.getString(FIELD_AUTHOR);
        nature = json.getJSONObject(FIELD_NATURE).getString("code");
        tctype = json.getJSONObject(FIELD_TCTYPE).getString("code");
        reference = json.getString(FIELD_REFERENCE);
        status = json.getString(FIELD_STATUS);
        importance = json.getString(FIELD_IMPORTANCE);
        prerequisite = json.getString(FIELD_PREREQUISITE);
        requirementNumber = json.getJSONArray(FIELD_REQUIREMENTS).length();

        for (JSONObject customField : (List<JSONObject>) json.getJSONArray(FIELD_CUSTOM_FIELDS).toList()) {
            if ("MarqueTNR".equalsIgnoreCase(customField.getString("code"))) {
                marque = customField.optString("value", "");
            } else if ("ApplicationTNR".equalsIgnoreCase(customField.getString("code"))) {
                application = customField.optString("value", "");
            } else if ("produit".equalsIgnoreCase(customField.getString("code"))) {
                produit = customField.optString("value", "");
            }
        }

        for (JSONObject step : (List<JSONObject>) json.getJSONArray(FIELD_STEPS).toList()) {
            testSteps.addAll(TestStep.fromJson(step));
        }

        readCustomFields(json.getJSONArray(FIELD_CUSTOM_FIELDS));

    }


    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    public String getReference() {
        return reference;
    }

    public String getAutomatedTestReference() {
        return automatedTestReference;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public String getProduit() {
        return produit;
    }

    public String getApplication() {
        return application;
    }

    public String getMarque() {
        return marque;
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

    public String getNature() {
        return nature;
    }

    public String getTctype() {
        return tctype;
    }

    public String getStatus() {
        return status;
    }

    public String getImportance() {
        return importance;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public int getRequirementNumber() {
        return requirementNumber;
    }
}
