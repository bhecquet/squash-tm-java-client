package io.github.bhecquet.entities;


import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private static final String FIELD_DATASETS = "datasets";
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
    private List<TestStep> testSteps = new ArrayList<>();

    private List<Dataset> datasets = new ArrayList<>();

    public TestCase(String url, String type, int id, String name) {
        super(url, type, id, name);
    }


    public static TestCase fromJson(JSONObject json) {
        try {
            TestCase testCase = new TestCase(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.optString(FIELD_NAME, "")
            );
            testCase.completeDetails(json, false);
            return testCase;
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

    @Override
    public void completeDetails() {
        JSONObject json = getJSonResponse(buildGetRequest(url));
        completeDetails(json, true);
    }

    /**
     * @param json                     JSON to get
     * @param completeDependentObjects whether we call related objects found in json
     */
    private void completeDetails(JSONObject json, boolean completeDependentObjects) {

        testSteps = new ArrayList<>();

        automatedTestReference = json.optString(FIELD_AUTOMATED_TEST_REFERENCE, "");
        try {
            path = json.getString(FIELD_PATH);
        } catch (JSONException e) {/* ignore */}
        try {
            description = json.getString(FIELD_DESCRIPTION);
        } catch (JSONException e) {/* ignore */}
        try {
            projectId = json.getJSONObject(FIELD_PROJECT).getInt(FIELD_ID);
        } catch (JSONException e) {/* ignore */}
        try {
            projectName = json.getJSONObject(FIELD_PROJECT).getString(FIELD_NAME);
        } catch (JSONException e) {/* ignore */}
        try {
            author = json.getString(FIELD_AUTHOR);
        } catch (JSONException e) {/* ignore */}
        try {
            nature = json.getJSONObject(FIELD_NATURE).getString("code");
        } catch (JSONException e) {/* ignore */}
        try {
            tctype = json.getJSONObject(FIELD_TCTYPE).getString("code");
        } catch (JSONException e) {/* ignore */}
        try {
            reference = json.getString(FIELD_REFERENCE);
        } catch (JSONException e) {/* ignore */}
        try {
            status = json.getString(FIELD_STATUS);
        } catch (JSONException e) {/* ignore */}
        try {
            importance = json.getString(FIELD_IMPORTANCE);
        } catch (JSONException e) {/* ignore */}
        try {
            prerequisite = json.getString(FIELD_PREREQUISITE);
        } catch (JSONException e) {/* ignore */}
        try {
            requirementNumber = json.getJSONArray(FIELD_REQUIREMENTS).length();
        } catch (JSONException e) {/* ignore */}
        try {
            if (completeDependentObjects) {
                for (JSONObject step : (List<JSONObject>) json.getJSONArray(FIELD_STEPS).toList()) {
                    testSteps.addAll(TestStep.fromJson(step));
                }
            }
        } catch (JSONException e) {/* ignore */}
        try {
            for (JSONObject datasetJson : (List<JSONObject>) json.getJSONArray(FIELD_DATASETS).toList()) {
                Dataset dataset = Dataset.fromJson(datasetJson);
                dataset.setTestCase(this);
                datasets.add(dataset);
            }
        } catch (JSONException e) {/* ignore */}
        try {
            readCustomFields(json.getJSONArray(FIELD_CUSTOM_FIELDS));
        } catch (JSONException e) {/* ignore */}

    }

    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    public List<Dataset> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<Dataset> datasets) {
        this.datasets = datasets;
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

    public String getProduit() {
        return produit;
    }

    public String getApplication() {
        return application;
    }

    public String getMarque() {
        return marque;
    }

    public List<RequirementVersion> getCoveredRequirements() {
        List<RequirementVersion> coveredRequirements = new ArrayList<>();
        JSONObject json = getJSonResponse(buildGetRequest(url));
        for (JSONObject reqver : (List<JSONObject>) json.getJSONArray(FIELD_REQUIREMENTS).toList()) {
            if (Objects.equals(reqver.getString(FIELD_TYPE), "requirement-version")) {
                RequirementVersion coveredRequirement = RequirementVersion.fromJson(reqver);
                coveredRequirements.add(coveredRequirement);
            }
        }
        return coveredRequirements;
    }
}
