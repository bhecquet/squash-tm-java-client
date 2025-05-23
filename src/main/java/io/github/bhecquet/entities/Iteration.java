package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.ConfigurationException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Iteration extends Entity {

    public static final String ITERATIONS_URL = "campaigns/%s/iterations";
    private static final String ITERATION_URL = "iterations/%d";
    private static final String TEST_PLAN_URL = "test-plan";
    private static final String FIELD_SCHEDULE_START_DATE = "scheduled_start_date";
    private static final String FIELD_SCHEDULE_END_DATE = "scheduled_end_date";
    private static final String FIELD_ACTUAL_START_DATE = "actual_start_date";
    private static final String FIELD_ACTUAL_END_DATE = "actual_end_date";
    private static final String FIELD_REFERENCE = "reference";
    private static final String FIELD_TEST_SUITE = "test_suites";
    private static final String FIELD_TEST_PLAN = "test-plan";

    private int index;
    private String scheduleStartDate;
    private String scheduleEndDate;
    private String actualStartDate;
    private String actualEndDate;
    private String reference;
    private Project project; // may be null

    private List<TestSuite> testSuites;
    private List<IterationTestPlanItem> iterationTestPlanItems;

    public Iteration(String url, String type, int id, String name) {
        super(url, type, id, name);
    }

    /**
     * Add a test case/dataset in this iteration, given their id
     * If test case is already in the iteration, do not add it a second time
     *
     * @param testCaseId
     * @param datasetId
     * @return
     */
    public IterationTestPlanItem addTestCase(int testCaseId, Integer datasetId) {

        // check if test case is already present with the same dataset as requested
        for (IterationTestPlanItem testPlanItem : getAllTestCases()) {
            if (testPlanItem.getTestCase() != null && testCaseId == testPlanItem.getTestCase().getId()
                    && (testPlanItem.getDataset() == null || datasetId == null || testPlanItem.getDataset() != null && datasetId == testPlanItem.getDataset().getId())) {
                return testPlanItem;
            }
        }

        // check that TestCase is valid
        TestCase testCase;
        try {
            testCase = TestCase.get(testCaseId);
        } catch (SquashTmException e) {
            throw new SquashTmException(String.format("Test case with id %d does not exist in Squash", testCaseId));
        }

        // check that Dataset is valid
        Dataset dataset = null;
        if (datasetId != null) {
            testCase.completeDetails(); // add details to the test case
            Optional<Dataset> datasetOpt = testCase.getDatasets().stream().filter(dataset1 -> dataset1.getId() == datasetId).findFirst();
            if (datasetOpt.isPresent()) {
                dataset = datasetOpt.get();
            } else {
                throw new ConfigurationException(String.format("Dataset with id %d does not belong to Test case with id %d", datasetId, testCaseId));
            }
        }

        return addTestCase(testCase, dataset);
    }

    public IterationTestPlanItem addTestCase(TestCase testCase, Dataset dataset) {
        return IterationTestPlanItem.create(this, testCase, dataset);
    }

    public List<IterationTestPlanItem> getAllTestCases() {
        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(apiRootUrl + String.format(IterationTestPlanItem.TEST_PLAN_ITEM_URL + "?sort=id", id)));

            List<IterationTestPlanItem> testPlanItems = new ArrayList<>();
            if (json.has(FIELD_EMBEDDED)) {
                for (JSONObject tpiJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_TEST_PLAN).toList()) {
                    testPlanItems.add(IterationTestPlanItem.fromJson(tpiJson));
                }
            }
            return testPlanItems;
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot get all test cases", e);
        }
    }


    public static Iteration fromJson(JSONObject json) {
        try {
            return new Iteration(json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_NAME));
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create Iteration from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }


    /**
     * Creates an interation in a campaign if it does not exist
     *
     * @param campaign      the campaign where iteration will be created
     * @param iterationName name of the iteration to create
     * @return
     */
    public static Iteration create(Campaign campaign, String iterationName) {
        for (Iteration iteration : campaign.getIterations()) {
            if (iteration.getName().equals(iterationName)) {
                return iteration;
            }
        }

        try {

            JSONObject body = new JSONObject();
            body.put(FIELD_TYPE, TYPE_ITERATION);
            body.put(FIELD_NAME, iterationName);

            JSONObject parent = new JSONObject();
            parent.put(FIELD_ID, campaign.id);
            parent.put(FIELD_TYPE, TYPE_CAMPAIGN);
            body.put(FIELD_PARENT, parent);

            JSONObject json = getJSonResponse(buildPostRequest(String.format(apiRootUrl + ITERATIONS_URL, campaign.id)).body(body));

            return fromJson(json);

        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot create campaign %s", iterationName), e);
        }

    }

    public void completeDetails() {

        JSONObject json = getJSonResponse(Unirest.get(url));
        completeDetails(json);
    }

    public void completeDetails(JSONObject json) {

        scheduleStartDate = json.optString(FIELD_SCHEDULE_START_DATE, "");
        scheduleEndDate = json.optString(FIELD_SCHEDULE_END_DATE, "");
        actualStartDate = json.optString(FIELD_ACTUAL_START_DATE, "");
        actualEndDate = json.optString(FIELD_ACTUAL_END_DATE, "");
        reference = json.getString(FIELD_REFERENCE);
        testSuites = new ArrayList<>();

        for (JSONObject jsonTestSuite : (List<JSONObject>) json.getJSONArray(FIELD_TEST_SUITE).toList()) {
            testSuites.add(TestSuite.fromJson(jsonTestSuite));
        }

        // in case project is not present, it means we get Iteration JSON from campaign list
        // in this case, we won't look at test plan
        if (json.getJSONObject("_links").has("project")) {
            project = Project.getFromUrl(json.getJSONObject("_links").getJSONObject("project").getString("href"));
            iterationTestPlanItems = getAllTestCases();
        }


        readCustomFields(json.getJSONArray(FIELD_CUSTOM_FIELDS));
    }

    public static Iteration get(int id) {
        try {
            JSONObject json = getJSonResponse(buildGetRequest(apiRootUrl + String.format(ITERATION_URL, id)));
            Iteration iteration = fromJson(json);

            return iteration;
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Iteration %d does not exist", id));
        }
    }


    public List<TestSuite> getTestSuites() {
        return testSuites;
    }

    public int getIndex() {
        return index;
    }

    public String getReference() {
        return reference;
    }

    public String getScheduleStartDate() {
        return scheduleStartDate;
    }

    public String getScheduleEndDate() {
        return scheduleEndDate;
    }

    public String getActualStartDate() {
        return actualStartDate;
    }

    public String getActualEndDate() {
        return actualEndDate;
    }

    public List<IterationTestPlanItem> getIterationTestPlanItems() {
        return iterationTestPlanItems;
    }

    public Project getProject() {
        return project;
    }

    public List<IterationTestPlanItem> getTestPlan() {
        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(String.format("%s%s/%s?sort=id", apiRootUrl, String.format(ITERATION_URL, id), TEST_PLAN_URL)));
            List<IterationTestPlanItem> iterationTestPlanItems = new ArrayList<>();
            if (json.has(FIELD_EMBEDDED)) {
                for (JSONObject jsonIterationTestPlanItem : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_TEST_PLAN).toList()) {
                    iterationTestPlanItems.add(IterationTestPlanItem.fromJson(jsonIterationTestPlanItem));
                }
            }
            return iterationTestPlanItems;
        } catch (UnirestException e) {
            throw new SquashTmException("Impossible de récupérer la liste des campagnes", e);
        }
    }

}
