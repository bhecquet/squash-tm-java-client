package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestStep extends Step {

    private static final String TEST_STEP_URL = "test-steps/%s";
    private static final String TEST_CASE_URL = "test-cases/%s";
    protected static final String FIELD_EXPECTED_RESULT = "expected_result";
    protected static final String FIELD_ACTION = "action";
    private static final String FIELD_INDEX = "index";
    private static final String FIELD_CALLED_TEST_CASE = "called_test_case";


    public TestStep(String url, String type, int id, int order, String expectedResult, String action) {
        super(url, type, id, action);
        this.order = order;
        this.expectedResult = expectedResult;
        this.action = action;
    }


    @Override
    public void completeDetails() {
        throw new NotImplementedException();
    }

    public static TestStep get(int id) {
        try {
            return fromJsonOne(getJSonResponse(buildGetRequest(apiRootUrl + String.format(TEST_STEP_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Test Case %d does not exist", id));
        }
    }

    public static TestStep fromJsonOne(JSONObject json) {
        try {
            return new TestStep(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getInt(FIELD_INDEX),
                    json.optString(FIELD_EXPECTED_RESULT, ""),
                    json.optString(FIELD_ACTION, "")
            );
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create test step from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public static List<TestStep> fromJson(JSONObject json) {
        try {
            String stepType = json.getString(FIELD_TYPE);
            if ("action-step".equals(stepType)) {
                TestStep testStep = fromJsonOne(json);
                testStep.readCustomFields(json.getJSONArray(FIELD_CUSTOM_FIELDS));
                return List.of(testStep);
            } else if ("call-step".equals(stepType)) {
                if ("unauthorized-resource".equals(json.getJSONObject(FIELD_CALLED_TEST_CASE).getString(FIELD_TYPE))) {
                    return new ArrayList<>();
                } else {
                    TestCase calledTestCase = TestCase.get(json.getJSONObject(FIELD_CALLED_TEST_CASE).getInt(FIELD_ID));
                    calledTestCase.completeDetails();
                    return calledTestCase.getTestSteps();
                }
            } else {
                throw new SquashTmException(String.format("Type %s inconnu pour un TestStep", stepType));
            }

        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create test step from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public void setDescription(String description) {
        this.action = description;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public static JSONObject buildAPIBody(Map<String, Object> datas) {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put(FIELD_TYPE, "action-step");
        for (var entry : datas.entrySet()) {
            bodyJson.put(entry.getKey(), entry.getValue());
        }
        bodyJson.put(CustomField.FIELD_CUSTOM_FIELDS, new JSONArray());

        return bodyJson;
    }

    /**
     * Create a test step
     *
     * @param testCaseId id of the test case to update
     * @param datas      datas to create the step with (description,expected result...)
     * @return updated test case
     */
    public static TestStep create(int testCaseId, Map<String, Object> datas) {

        try {
            JSONObject json = getJSonResponse(buildPostRequest(apiRootUrl + String.format(TEST_CASE_URL, testCaseId) + "/steps").body(buildAPIBody(datas)));
            return fromJsonOne(json);

        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot create a step for testcase %s", testCaseId), e);
        }
    }

    /**
     * Update a test step
     *
     * @param testStepId     id of the test step to update
     * @param action         new action value format is html, empty if not to be modified
     * @param expectedResult new expected result value format is html, empty if not to be modified
     * @return updated test case
     */
    public TestStep update(int testStepId, String action, String expectedResult) {

        try {
            Map<String, Object> datas = new HashMap<>();
            datas.put(FIELD_ACTION, action);
            datas.put(FIELD_EXPECTED_RESULT, expectedResult);
            JSONObject json = getJSonResponse(buildPatchRequest(apiRootUrl + String.format(TEST_STEP_URL, testStepId)).body(buildAPIBody(datas)));

            return fromJsonOne(json);

        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot update teststep %s", testStepId), e);
        }
    }

    public static void delete(String ids) {
        try {
            getJSonResponse(buildDeleteRequest(apiRootUrl + String.format(TEST_STEP_URL, ids)));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot delete test step %s", ids), e);
        }
    }

    public void uploadAttachment(File f, int id) {
        try {
            getJSonResponse(buildPostRequestAuthOnly(String.format(apiRootUrl + "test-steps/%d/attachments", id)).field("files", f));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot upload attachment for test step %s", id), e);
        }
    }
}
