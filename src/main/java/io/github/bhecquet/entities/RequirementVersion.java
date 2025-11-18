package io.github.bhecquet.entities;


import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a requirement in Squash TM
 */
public class RequirementVersion extends Entity {

    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_REQUIREMENT = "requirement";
    private static final String FIELD_REFERENCE = "reference";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CRITICALITY = "criticality";
    private static final String FIELD_TESTCASES = "verifying_test_cases";
    private static final String FIELD_CATEGORY = "category";

    private static final String REQUIREMENT_VERSION_URL = "requirement-versions/%s";
    private String description;
    private String reference;
    private int requirementId = -1;
    private String requirementName = "";
    private String status = "";
    private String criticality = "";
    private String category = "";

    public RequirementVersion(String url, String type, int id, String name) {
        super(url, type, id, name);
    }

    public static RequirementVersion fromJson(JSONObject json) {
        try {
            return new RequirementVersion(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.optString(FIELD_NAME, "")
            );
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create Requirement Version from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public static RequirementVersion get(int id) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format(REQUIREMENT_VERSION_URL, id))));
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

        name = json.getString(FIELD_NAME);
        description = json.getString(FIELD_DESCRIPTION);
        requirementId = json.getJSONObject(FIELD_REQUIREMENT).getInt(FIELD_ID);
        requirementName = json.getJSONObject(FIELD_REQUIREMENT).getString(FIELD_NAME);
        reference = json.getString(FIELD_REFERENCE);
        status = json.getString(FIELD_STATUS);
        criticality = json.getString(FIELD_CRITICALITY);
        category = json.getJSONObject(FIELD_CATEGORY).getString("code");

        readCustomFields(json.getJSONArray(FIELD_CUSTOM_FIELDS));

    }

    public String getName() {
        return name;
    }

    public String getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirementName() {
        return requirementName;
    }

    public int getRequirementId() {
        return requirementId;
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

    public List<TestCase> getCoveringTestCases() {
        List<TestCase> coveringTestCases = new ArrayList<>();
        JSONObject json = getJSonResponse(buildGetRequest(url));
        for (JSONObject testcase : (List<JSONObject>) json.getJSONArray(FIELD_TESTCASES).toList()) {
            TestCase coveringTestCase = TestCase.fromJson(testcase);
            coveringTestCases.add(coveringTestCase);
        }
        return coveringTestCases;
    }

}
