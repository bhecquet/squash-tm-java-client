package fr.bhecquet.entities;

import fr.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestSuite extends Entity {

    private static final String ITERATION_URL = "test-suites/%d";
    private static final String FIELD_TEST_PLAN = "test_plan";

    private List<IterationTestPlanItem> iterationTestPlanItems;

    public TestSuite(String url, String type, int id, String name) {
        super(url, type, id, name);
    }

    public static TestSuite fromJson(JSONObject json) {
        try {
            return new TestSuite(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_NAME)
            );

        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create test suite from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    @Override
    public void completeDetails() {
        iterationTestPlanItems = new ArrayList<>();
        JSONObject json = getJSonResponse(Unirest.get(String.format("%s", url)));

        for (JSONObject jsonIterationTestPlanItem : (List<JSONObject>) json.getJSONArray(FIELD_TEST_PLAN).toList()) {
            IterationTestPlanItem item = IterationTestPlanItem.fromJson(jsonIterationTestPlanItem);
            item.setTestSuite(this);
            iterationTestPlanItems.add(item);
        }

    }

    public static TestSuite get(int id) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format(ITERATION_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Iteration %d does not exist", id));
        }
    }

    public List<IterationTestPlanItem> getIterationTestPlanItems() {
        return iterationTestPlanItems;
    }
}
