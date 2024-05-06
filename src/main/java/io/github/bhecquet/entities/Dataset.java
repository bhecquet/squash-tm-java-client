package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

public class Dataset extends Entity {

    private static final String DATASET_URL = "datasets/%s";


    private TestCase testCase;

    public Dataset(String url, String type, int id, String name) {
        this(url, type, id, name, null);
    }

    public Dataset(String url, String type, int id, String name, TestCase testCase) {
        super(url, type, id, name);
        this.testCase = testCase;
    }

    @Override
    public void completeDetails() {
        throw new NotImplementedException();
    }

    public static Dataset fromJson(JSONObject json) {
        try {
            Dataset dataset = new Dataset(json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.optString(FIELD_NAME));

            // add information about the related test case if we have it
            if (json.optJSONObject("test_case") != null) {
                dataset.testCase = TestCase.fromJson(json.getJSONObject("test_case"));
            }

            return dataset;
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create Dataset from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public static Dataset get(int id) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format(DATASET_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Dataset %d does not exist", id));
        }
    }

    public TestCase getTestCase() {
        return testCase;
    }


}
