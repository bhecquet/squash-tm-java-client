package fr.bhecquet.entities;

import fr.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class TestStep extends Step {

    protected static final String FIELD_EXPECTED_RESULT = "expected_result";
    protected static final String FIELD_ACTION = "action";
    private static final String FIELD_INDEX = "index";
    private static final String FIELD_CALLED_TEST_CASE = "called_test_case";


    public TestStep(int id, String type, String url, int order, String expectedResult, String action) {
        super(url, type, id, null);
        this.order = order;
        this.expectedResult = expectedResult;
        this.action = action;
    }

    public static List<TestStep> fromJson(JSONObject json) {
        try {
            String stepType = json.getString(FIELD_TYPE);
            if ("action-step".equals(stepType)) {
                TestStep testStep = new TestStep(
                        json.getInt(FIELD_ID),
                        json.getString(FIELD_TYPE),
                        json.getJSONObject("_links").getJSONObject("self").getString("href"),
                        json.getInt(FIELD_INDEX),
                        json.optString(FIELD_EXPECTED_RESULT, ""),
                        json.optString(FIELD_ACTION, "")
                );
                testStep.readCustomFields(json.getJSONArray(FIELD_CUSTOM_FIELDS));
                return Arrays.asList(testStep);
            } else if ("call-step".equals(stepType)) {
                TestCase calledTestCase = TestCase.get(json.getJSONObject(FIELD_CALLED_TEST_CASE).getInt(FIELD_ID));
                calledTestCase.completeDetails();
                return calledTestCase.getTestSteps();
            } else {
                throw new SquashTmException(String.format("Type %s inconnu pour un TestStep", stepType));
            }

        } catch (JSONException e) {
            throw new SquashTmException(String.format("Impossible de créer l'exécution ste depuis le JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }


}
