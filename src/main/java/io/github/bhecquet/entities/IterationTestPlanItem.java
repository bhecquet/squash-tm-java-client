package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IterationTestPlanItem extends Entity {

    private static final String ITERATION_TEST_PLAN_ITEM_URL = "iteration-test-plan-items/%d";
    private static final String FIELD_EXECUTION_STATUS = "execution_status";
    private static final String FIELD_REFERENCED_TEST_CASE = "referenced_test_case";
    private static final String FIELD_LAST_EXECUTED_BY = "last_executed_by";
    private static final String FIELD_LAST_EXECUTED_ON = "last_executed_on";
    private static final String FIELD_EXECUTIONS = "executions";
    public static final String TEST_PLAN_ITEM_URL = "iterations/%d/test-plan";
    public static final String TEST_PLAN_ITEM_EXECUTION_URL = "iteration-test-plan-items/%d/executions";
    public static final String FIELD_REFERENCED_DATASET = "referenced_dataset";

    private TestCase testCase;
    private String status;
    private TestSuite testSuite;
    private String lastExecutedBy;
    private String lastExecutedOn;
    private List<Execution> executions;

    private Dataset dataset;

    public IterationTestPlanItem(String url, String type, int id, TestCase testCase, Dataset dataset) {
        super(url, type, id, null);
        this.testCase = testCase;
        this.dataset = dataset;
    }

    public JSONObject asJson() {
        JSONObject json = new JSONObject();
        json.put(FIELD_TYPE, "iteration-test-plan-item");
        json.put(FIELD_ID, id);
        return json;
    }

    /**
     * Create an execution for this item
     *
     * @return
     */
    public TestPlanItemExecution createExecution() {
        try {
            return TestPlanItemExecution.fromJson(getJSonResponse(buildPostRequest(apiRootUrl + String.format(TEST_PLAN_ITEM_EXECUTION_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot create execution", e);
        }
    }

    /**
     * Create an execution for this item, providing a result
     *
     * @return
     */
    public TestPlanItemExecution createExecutionWithResult(TestPlanItemExecution.ExecutionStatus result, String comment) {

        TestPlanItemExecution execution = createExecution();
        execution.setResult(result, comment);
        return execution;
    }

    public static IterationTestPlanItem fromJson(JSONObject json) {

        try {
            JSONObject referencedTestCase = json.optJSONObject(FIELD_REFERENCED_TEST_CASE);
            JSONObject referencedDataset = json.optJSONObject(FIELD_REFERENCED_DATASET);

            return new IterationTestPlanItem(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    referencedTestCase == null ? null : TestCase.fromJson(referencedTestCase),
                    referencedDataset == null ? null : Dataset.fromJson(referencedDataset)
            );
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create IterationTestPlanItem from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    @Override
    public void completeDetails() {
        completeDetails(getJSonResponse(Unirest.get(url)));
    }

    private void completeDetails(JSONObject json) {
        executions = new ArrayList<>();

        lastExecutedBy = json.optString(FIELD_LAST_EXECUTED_BY, "");
        lastExecutedOn = json.optString(FIELD_LAST_EXECUTED_ON, "");

        for (JSONObject jsonExecution : (List<JSONObject>) json.getJSONArray(FIELD_EXECUTIONS).toList()) {
            executions.add(Execution.fromJson(jsonExecution));
        }
    }

    public static IterationTestPlanItem get(int id) {
        try {
            JSONObject json = getJSonResponse(buildGetRequest(apiRootUrl + String.format(ITERATION_TEST_PLAN_ITEM_URL, id)));
            IterationTestPlanItem iterationTestPlanItem = fromJson(json);
            iterationTestPlanItem.completeDetails(json);
            return iterationTestPlanItem;
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("L'iteration %d n'existe pas", id));
        }
    }

    public static IterationTestPlanItem create(Iteration iteration, TestCase testCase, Dataset dataset) {
        try {


            JSONObject body = new JSONObject();
            body.put(FIELD_TYPE, "iteration-test-plan-item");
            JSONObject testCaseJson = new JSONObject();
            testCaseJson.put(FIELD_ID, testCase.id);
            testCaseJson.put(FIELD_TYPE, "test-case");
            body.put("test_case", testCaseJson);

            if (dataset != null) {
                JSONObject datasetJson = new JSONObject();
                datasetJson.put(FIELD_ID, dataset.id);
                datasetJson.put(FIELD_TYPE, "dataset");
                body.put("dataset", datasetJson);
            }

            JSONObject json = getJSonResponse(buildPostRequest(apiRootUrl + String.format(TEST_PLAN_ITEM_URL, iteration.id)).body(body));


            return IterationTestPlanItem.fromJson(json);

        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot create Iteration test plan for iteration %s and test case %d", iteration.name, testCase.id), e);
        }

    }

    public TestCase getTestCase() {
        return testCase;
    }

    public Dataset getDataset() {
        return dataset;
    }


    public TestSuite getTestSuite() {
        return testSuite;
    }

    public void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    public String getStatus() {
        return status;
    }

    public String getLastExecutedBy() {
        return lastExecutedBy;
    }

    public String getLastExecutedOn() {
        return lastExecutedOn;
    }

    public List<Execution> getExecutions() {
        return executions;
    }
}
