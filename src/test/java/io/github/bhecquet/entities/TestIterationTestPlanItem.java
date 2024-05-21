package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpRequestWithBody;
import kong.unirest.core.RequestBodyEntity;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestIterationTestPlanItem extends SquashTMTest {

    public static final String EXECUTION_REPLY_DATA = "{" +
            "  \"_type\" : \"execution\"," +
            "  \"id\" : 25," +
            "  \"name\" : \"Christmas turkey test flight\"," +
            "  \"execution_order\" : 0," +
            "  \"execution_status\" : \"READY\"," +
            "  \"last_executed_by\" : null," +
            "  \"last_executed_on\" : null," +
            "  \"execution_mode\" : \"MANUAL\"," +
            "  \"reference\" : \"CHR-T024\"," +
            "  \"dataset_label\" : \"\"," +
            "  \"execution_steps\" : [ {" +
            "    \"_type\" : \"execution-step\"," +
            "    \"id\" : 50," +
            "    \"execution_status\" : \"READY\"," +
            "    \"action\" : \"<p>arm the slingshot</p>\"," +
            "    \"expected_result\" : \"<p>slingshot is armed</p>\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/execution-steps/50\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"execution-step\"," +
            "    \"id\" : 51," +
            "    \"execution_status\" : \"READY\"," +
            "    \"action\" : \"<p>install the turkey</p>\"," +
            "    \"expected_result\" : \"<p>the turkey groans and is in place</p>\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/execution-steps/51\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"execution-step\"," +
            "    \"id\" : 52," +
            "    \"execution_status\" : \"READY\"," +
            "    \"action\" : \"<p>release the slingshot</p>\"," +
            "    \"expected_result\" : \"<p>the turkey groans, at a distance though</p>\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/execution-steps/52\"" +
            "      }" +
            "    }" +
            "  } ]," +
            "  \"comment\" : null," +
            "  \"prerequisite\" : \"\"," +
            "  \"description\" : \"<p>Will test the aerodynamic profile of a sample turkey</p>\"," +
            "  \"importance\" : \"LOW\"," +
            "  \"nature\" : {" +
            "    \"code\" : \"NAT_PERFORMANCE_TESTING\"" +
            "  }," +
            "  \"type\" : {" +
            "    \"code\" : \"TYP_COMPLIANCE_TESTING\"" +
            "  }," +
            "  \"test_case_status\" : \"WORK_IN_PROGRESS\"," +
            "  \"custom_fields\" : [ ]," +
            "  \"test_case_custom_fields\" : [ ]," +
            "  \"attachments\" : [ ]," +
            "  \"_links\" : {" +
            "    \"self\" : {" +
            "      \"href\" : \"https://localhost:4321/executions/25\"" +
            "    }," +
            "    \"project\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/15\"" +
            "    }," +
            "    \"test_plan_item\" : {" +
            "      \"href\" : \"https://localhost:4321/iteration-test-plan-items/1\"" +
            "    }," +
            "    \"execution-steps\" : {" +
            "      \"href\" : \"https://localhost:4321/executions/25/execution-steps\"" +
            "    }," +
            "    \"attachments\" : {" +
            "      \"href\" : \"https://localhost:4321/executions/25/attachments\"" +
            "    }" +
            "  }" +
            "}";

    private String ITERATION_TEST_PLAN_ITEM_REPLY_DATA = "{" +
            "  \"_type\" : \"iteration-test-plan-item\"," +
            "  \"id\" : 6," +
            "  \"execution_status\" : \"SUCCESS\"," +
            "  \"referenced_test_case\" : {" +
            "    \"_type\" : \"test-case\"," +
            "    \"id\" : 3," +
            "    \"name\" : \"Test Case 3\"," +
            "    \"reference\" : \"TC3\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/test-cases/3\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"referenced_dataset\" : {" +
            "    \"_type\" : \"dataset\"," +
            "    \"id\" : 2," +
            "    \"name\" : \"Dataset 2\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/datasets/2\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"last_executed_by\" : \"User 6\"," +
            "  \"last_executed_on\" : \"2017-02-04T11:00:00.000+00:00\"," +
            "  \"assigned_to\" : \"User 6\"," +
            "  \"executions\" : [ {" +
            "    \"_type\" : \"execution\"," +
            "    \"id\" : 10," +
            "    \"execution_status\" : \"SUCCESS\"," +
            "    \"last_executed_by\" : \"User 6\"," +
            "    \"last_executed_on\" : \"2017-02-04T11:00:00.000+00:00\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/executions/10\"" +
            "      }" +
            "    }" +
            "  } ]," +
            "  \"iteration\" : {" +
            "    \"_type\" : \"iteration\"," +
            "    \"id\" : 1," +
            "    \"name\" : \"Iteration 1\"," +
            "    \"reference\" : \"IT1\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/iterations/1\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"_links\" : {" +
            "    \"self\" : {" +
            "      \"href\" : \"https://localhost:4321/iteration-test-plan-items/6\"" +
            "    }," +
            "    \"project\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/1\"" +
            "    }," +
            "    \"test-case\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/3\"" +
            "    }," +
            "    \"dataset\" : {" +
            "      \"href\" : \"https://localhost:4321/datasets/2\"" +
            "    }," +
            "    \"iteration\" : {" +
            "      \"href\" : \"https://localhost:4321/iterations/1\"" +
            "    }," +
            "    \"executions\" : {" +
            "      \"href\" : \"https://localhost:4321/iteration-test-plan-items/6/executions\"" +
            "    }" +
            "  }" +
            "}";

    @BeforeMethod
    public void init() {
        new Project("https://localhost:4321/projects/1", "project", 1, "project");
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }


    @Test
    public void testAsJson() {
        TestCase testCase = new TestCase("https://localhost:4321/test-cases/3", "test-case", 3, "my_test");
        IterationTestPlanItem itpi = new IterationTestPlanItem("https://localhost:4321/iteration-test-plan-items/6", "iteration-test-plan-item", 6, testCase, null);

        JSONObject json = itpi.asJson();
        Assert.assertEquals(json.getInt("id"), 6);
        Assert.assertEquals(json.getString("_type"), "iteration-test-plan-item");
    }


    @Test
    public void testCreateExecution() {
        createServerMock("POST", "/iteration-test-plan-items/1/executions", 200, EXECUTION_REPLY_DATA, "request");

        TestCase testCase = new TestCase("https://localhost:4321/test-cases/3", "test-case", 3, "my_test");
        IterationTestPlanItem itpi = new IterationTestPlanItem("https://localhost:4321/iteration-test-plan-items/1", "test-plan-item", 1, testCase, null);

        TestPlanItemExecution execution = itpi.createExecution();
        Assert.assertEquals(execution.getId(), 25);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testCreateExecutionWithError() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/iteration-test-plan-items/1/executions", 200, "{}", "request");

        when(postRequest.asJson()).thenThrow(UnirestException.class);

        TestCase testCase = new TestCase("https://localhost:4321/test-cases/3", "test-case", 3, "my_test");
        IterationTestPlanItem itpi = new IterationTestPlanItem("https://localhost:4321/iteration-test-plan-items/1", "test-plan-item", 1, testCase, null);

        itpi.createExecution();
    }

    @Test
    public void testFromJson() {

        JSONObject json = new JSONObject();
        json.put("_type", "iteration-test-plan-item");
        json.put("id", 6);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iteration-test-plan-items/6\"" +
                "        }}"));
        json.put("referenced_test_case", new JSONObject("{" +
                "    \"_type\" : \"test-case\"," +
                "    \"id\" : 25," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/test-cases/25\"" +
                "      }" +
                "    }" +
                "  }"));

        IterationTestPlanItem iteration = IterationTestPlanItem.fromJson(json);
        Assert.assertEquals(iteration.getId(), 6);
        Assert.assertEquals(iteration.getTestCase().getId(), 25);
        Assert.assertNull(iteration.getDataset());
        Assert.assertEquals(iteration.getUrl(), "https://localhost:4321/iteration-test-plan-items/6");
    }

    @Test
    public void testFromJsonWithDataset() {

        JSONObject json = new JSONObject();
        json.put("_type", "iteration-test-plan-item");
        json.put("id", 6);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iteration-test-plan-items/6\"" +
                "        }}"));
        json.put("referenced_test_case", new JSONObject("{" +
                "    \"_type\" : \"test-case\"," +
                "    \"id\" : 25," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/test-cases/25\"" +
                "      }" +
                "    }" +
                "  }"));
        json.put("referenced_dataset", new JSONObject("{" +
                "        \"_type\" : \"dataset\"," +
                "        \"id\" : 12," +
                "        \"name\" : \"Dataset 1\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost/datasets/12\"" +
                "          }" +
                "        }" +
                "      }"));

        IterationTestPlanItem iteration = IterationTestPlanItem.fromJson(json);
        Assert.assertEquals(iteration.getId(), 6);
        Assert.assertEquals(iteration.getTestCase().getId(), 25);
        Assert.assertEquals(iteration.getDataset().getId(), 12);
        Assert.assertEquals(iteration.getUrl(), "https://localhost:4321/iteration-test-plan-items/6");
    }

    /**
     * Sometimes, referenced test-case is null, handle this case
     */
    @Test
    public void testFromJsonNullTestCase() {

        JSONObject json = new JSONObject();
        json.put("_type", "iteration-test-plan-item");
        json.put("id", 6);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iteration-test-plan-items/6\"" +
                "        }}"));
        json.put("referenced_test_case", (Object) null);

        IterationTestPlanItem iteration = IterationTestPlanItem.fromJson(json);
        Assert.assertEquals(iteration.getId(), 6);
        Assert.assertNull(iteration.getTestCase());
        Assert.assertEquals(iteration.getUrl(), "https://localhost:4321/iteration-test-plan-items/6");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {

        JSONObject json = new JSONObject();
        json.put("_type", "iteration-test-plan-item");
        json.put("id", 6);

        IterationTestPlanItem.fromJson(json);
    }

    @Test
    public void testCreateIterationTestPlanItem() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/iterations/4/test-plan", 200, "{" +
                "  \"_type\" : \"iteration-test-plan-item\"," +
                "  \"id\" : 38," +
                "  \"execution_status\" : \"READY\"," +
                "  \"referenced_test_case\" : {" +
                "    \"_type\" : \"test-case\"," +
                "    \"id\" : 25," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/test-cases/25\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"referenced_dataset\" : {" +
                "    \"_type\" : \"dataset\"," +
                "    \"id\" : 3," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/datasets/3\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"last_executed_by\" : null," +
                "  \"last_executed_on\" : null," +
                "  \"assigned_to\" : \"User-1\"," +
                "  \"executions\" : [ ]," +
                "  \"iteration\" : {" +
                "    \"_type\" : \"iteration\"," +
                "    \"id\" : 4," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/iterations/4\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/iteration-test-plan-items/38\"" +
                "    }," +
                "    \"project\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14\"" +
                "    }," +
                "    \"test-case\" : {" +
                "      \"href\" : \"https://localhost:4321/test-cases/25\"" +
                "    }," +
                "    \"dataset\" : {" +
                "      \"href\" : \"https://localhost:4321/datasets/3\"" +
                "    }," +
                "    \"iteration\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/4\"" +
                "    }," +
                "    \"executions\" : {" +
                "      \"href\" : \"https://localhost:4321/iteration-test-plan-items/38/executions\"" +
                "    }" +
                "  }" +
                "}", "request");

        Iteration iteration = new Iteration("https://localhost:4321/iterations/4", "iteration", 4, "my_iteration");
        IterationTestPlanItem itpi = IterationTestPlanItem.create(iteration, new TestCase("https://localhost:4321/test-cases/25", "test-case", 25, "TestCase"), null);

        verify(postRequest).body(new JSONObject("{\"_type\":\"iteration-test-plan-item\",\"test_case\":{\"id\":25,\"_type\":\"test-case\"}}"));
        Assert.assertEquals(itpi.getId(), 38);
    }

    @Test
    public void testCreateIterationTestPlanItemWithDataset() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/iterations/4/test-plan", 200, "{" +
                "  \"_type\" : \"iteration-test-plan-item\"," +
                "  \"id\" : 38," +
                "  \"execution_status\" : \"READY\"," +
                "  \"referenced_test_case\" : {" +
                "    \"_type\" : \"test-case\"," +
                "    \"id\" : 25," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/test-cases/25\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"referenced_dataset\" : {" +
                "    \"_type\" : \"dataset\"," +
                "    \"id\" : 24," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/datasets/24\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"last_executed_by\" : null," +
                "  \"last_executed_on\" : null," +
                "  \"assigned_to\" : \"User-1\"," +
                "  \"executions\" : [ ]," +
                "  \"iteration\" : {" +
                "    \"_type\" : \"iteration\"," +
                "    \"id\" : 4," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/iterations/4\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/iteration-test-plan-items/38\"" +
                "    }," +
                "    \"project\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14\"" +
                "    }," +
                "    \"test-case\" : {" +
                "      \"href\" : \"https://localhost:4321/test-cases/25\"" +
                "    }," +
                "    \"dataset\" : {" +
                "      \"href\" : \"https://localhost:4321/datasets/3\"" +
                "    }," +
                "    \"iteration\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/4\"" +
                "    }," +
                "    \"executions\" : {" +
                "      \"href\" : \"https://localhost:4321/iteration-test-plan-items/38/executions\"" +
                "    }" +
                "  }" +
                "}", "request");

        Iteration iteration = new Iteration("https://localhost:4321/iterations/4", "iteration", 4, "my_iteration");
        IterationTestPlanItem itpi = IterationTestPlanItem.create(iteration, new TestCase("https://localhost:4321/test-cases/25", "test-case", 25, "my_test"), new Dataset("https://localhost:4321/test-cases/24", "dataset", 24, "DS1"));

        verify(postRequest).body(new JSONObject("{\"_type\":\"iteration-test-plan-item\",\"test_case\":{\"id\":25,\"_type\":\"test-case\"},\"dataset\":{\"id\":24,\"_type\":\"dataset\"}}"));
        Assert.assertEquals(itpi.getId(), 38);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testCreateIterationTestPlanItemWithError() {
        RequestBodyEntity postRequest = (RequestBodyEntity) createServerMock("POST", "/iterations/4/test-plan", 200, "{}", "requestBodyEntity");
        when(postRequest.asJson()).thenThrow(UnirestException.class);

        Iteration iteration = new Iteration("https://localhost:4321/iterations/4", "iteration", 4, "my_iteration");
        IterationTestPlanItem.create(iteration, new TestCase("https://localhost:4321/test-cases/25", "test-case", 25, "test_case"), null);

    }

    /**
     * Check we can record execution status
     */
    @Test
    public void testCreateExecutionWithResult() {
        HttpRequestWithBody patchRequestSetStepStatus = (HttpRequestWithBody) createServerMock("PATCH", "/execution-steps/50/execution-status/SUCCESS", 200, TestExecutionStep.SET_STATUS_REPLY_DATA);
        HttpRequestWithBody patchRequestSetComment = (HttpRequestWithBody) createServerMock("PATCH", "/execution-steps/50", 200, TestExecutionStep.EXECUTION_STEP_REPLY_DATA);
        HttpRequestWithBody postRequestCreateExecution = (HttpRequestWithBody) createServerMock("POST", "/iteration-test-plan-items/4/executions", 200, EXECUTION_REPLY_DATA, "request");
        HttpRequestWithBody patchRequestSetStatus = (HttpRequestWithBody) createServerMock("PATCH", "/executions/25?fields=execution_status", 200, "{" +
                "  \"_type\" : \"execution\"," +
                "  \"id\" : 25," +
                "  \"execution_status\" : \"SUCCESS\"," +
                "  \"comment\" : \"<p>the comment was modified...</p>\"," +
                "  \"prerequisite\" : \"<p>... but the prerequisite was not</p>\"," +
                "  \"custom_fields\" : [ {" +
                "    \"code\" : \"TXT_STATUS\"," +
                "    \"label\" : \"text\"," +
                "    \"value\" : \"allright\"" +
                "  }, {" +
                "    \"code\" : \"TAGS_RELATED\"," +
                "    \"label\" : \"see also\"," +
                "    \"value\" : [ \"see this\", \"also that\" ]" +
                "  } ]," +
                "  \"test_case_custom_fields\" : [ {" +
                "    \"code\" : \"TC_TEXT\"," +
                "    \"label\" : \"test case cuf\"," +
                "    \"value\" : \"I'm from the test case\"" +
                "  }, {" +
                "    \"code\" : \"TC_LABELS\"," +
                "    \"label\" : \"labels\"," +
                "    \"value\" : [ \"was\", \"not\", \"updated\" ]" +
                "  } ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/executions/83\"" +
                "    }," +
                "    \"project\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14\"" +
                "    }," +
                "    \"test_plan_item\" : {" +
                "      \"href\" : \"https://localhost:4321/iteration-test-plan-items/1\"" +
                "    }," +
                "    \"execution-steps\" : {" +
                "      \"href\" : \"https://localhost:4321/executions/83/execution-steps\"" +
                "    }," +
                "    \"attachments\" : {" +
                "      \"href\" : \"https://localhost:4321/executions/83/attachments\"" +
                "    }" +
                "  }" +
                "}", "request");

        TestCase testCase = new TestCase("https://localhost:4321/test-cases/25", "test-case", 25, "test_case");
        IterationTestPlanItem iterationTestPlanItem = new IterationTestPlanItem("https://localhost:4321/iteration-test-plan-items/4/", "iteration-test-plan-items", 4, testCase, null);
        iterationTestPlanItem.createExecutionWithResult(TestPlanItemExecution.ExecutionStatus.SUCCESS, "ok");

        // check we create the execution, set its status and comment and set the step status
        verify(patchRequestSetStatus).body(new JSONObject("{\"_type\":\"execution\",\"execution_status\":\"SUCCESS\"}"));
        verify(postRequestCreateExecution).asJson();
        verify(patchRequestSetComment).body(new JSONObject("{\"_type\":\"execution-step\",\"comment\":\"ok\"}"));
        verify(patchRequestSetStepStatus).asJson();
    }

    @Test
    public void testCompleteDetails() {
        createServerMock("GET", "/iteration-test-plan-items/4", 200, ITERATION_TEST_PLAN_ITEM_REPLY_DATA, "request");

        TestCase testCase = new TestCase("https://localhost:4321/test-cases/25", "test-case", 25, "test_case");
        IterationTestPlanItem iterationTestPlanItem = new IterationTestPlanItem("https://localhost:4321/iteration-test-plan-items/4", "iteration-test-plan-items", 4, testCase, null);

        iterationTestPlanItem.completeDetails();

        Assert.assertEquals(iterationTestPlanItem.getLastExecutedBy(), "User 6");
        Assert.assertEquals(iterationTestPlanItem.getLastExecutedOn(), "2017-02-04T11:00:00.000+00:00");
        Assert.assertEquals(iterationTestPlanItem.getExecutions().size(), 1);
        Assert.assertEquals(iterationTestPlanItem.getExecutions().get(0).getId(), 10);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testCompleteDetailsInError() {
        createServerMock("GET", "/iteration-test-plan-items/4", 404, ITERATION_TEST_PLAN_ITEM_REPLY_DATA, "request");

        TestCase testCase = new TestCase("https://localhost:4321/test-cases/25", "test-case", 25, "test_case");
        IterationTestPlanItem iterationTestPlanItem = new IterationTestPlanItem("https://localhost:4321/iteration-test-plan-items/4", "iteration-test-plan-items", 4, testCase, null);

        iterationTestPlanItem.completeDetails();

    }

    @Test
    public void testGet() {
        createServerMock("GET", "/iteration-test-plan-items/4", 200, ITERATION_TEST_PLAN_ITEM_REPLY_DATA, "request");

        IterationTestPlanItem iterationTestPlanItem = IterationTestPlanItem.get(4);

        Assert.assertEquals(iterationTestPlanItem.getTestCase().getId(), 3);
        Assert.assertEquals(iterationTestPlanItem.getDataset().getId(), 2);
        Assert.assertEquals(iterationTestPlanItem.getLastExecutedBy(), "User 6");
        Assert.assertEquals(iterationTestPlanItem.getLastExecutedOn(), "2017-02-04T11:00:00.000+00:00");
        Assert.assertEquals(iterationTestPlanItem.getExecutions().size(), 1);
        Assert.assertEquals(iterationTestPlanItem.getExecutions().get(0).getId(), 10);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetInError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/iteration-test-plan-items/4", 200, ITERATION_TEST_PLAN_ITEM_REPLY_DATA, "request");
        when(getRequest.asJson()).thenThrow(UnirestException.class);

        IterationTestPlanItem.get(4);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetInError2() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/iteration-test-plan-items/4", 404, ITERATION_TEST_PLAN_ITEM_REPLY_DATA, "request");
        IterationTestPlanItem.get(4);
    }
}
