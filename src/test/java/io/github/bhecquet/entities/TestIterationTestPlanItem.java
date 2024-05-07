package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.SquashTmException;
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

    public static final String EXECUTION_REPLY_DATA = "{\n" +
            "  \"_type\" : \"execution\",\n" +
            "  \"id\" : 25,\n" +
            "  \"name\" : \"Christmas turkey test flight\",\n" +
            "  \"execution_order\" : 0,\n" +
            "  \"execution_status\" : \"READY\",\n" +
            "  \"last_executed_by\" : null,\n" +
            "  \"last_executed_on\" : null,\n" +
            "  \"execution_mode\" : \"MANUAL\",\n" +
            "  \"reference\" : \"CHR-T024\",\n" +
            "  \"dataset_label\" : \"\",\n" +
            "  \"execution_steps\" : [ {\n" +
            "    \"_type\" : \"execution-step\",\n" +
            "    \"id\" : 50,\n" +
            "    \"execution_status\" : \"READY\",\n" +
            "    \"action\" : \"<p>arm the slingshot</p>\",\n" +
            "    \"expected_result\" : \"<p>slingshot is armed</p>\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/execution-steps/50\"\n" +
            "      }\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"_type\" : \"execution-step\",\n" +
            "    \"id\" : 51,\n" +
            "    \"execution_status\" : \"READY\",\n" +
            "    \"action\" : \"<p>install the turkey</p>\",\n" +
            "    \"expected_result\" : \"<p>the turkey groans and is in place</p>\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/execution-steps/51\"\n" +
            "      }\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"_type\" : \"execution-step\",\n" +
            "    \"id\" : 52,\n" +
            "    \"execution_status\" : \"READY\",\n" +
            "    \"action\" : \"<p>release the slingshot</p>\",\n" +
            "    \"expected_result\" : \"<p>the turkey groans, at a distance though</p>\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/execution-steps/52\"\n" +
            "      }\n" +
            "    }\n" +
            "  } ],\n" +
            "  \"comment\" : null,\n" +
            "  \"prerequisite\" : \"\",\n" +
            "  \"description\" : \"<p>Will test the aerodynamic profile of a sample turkey</p>\",\n" +
            "  \"importance\" : \"LOW\",\n" +
            "  \"nature\" : {\n" +
            "    \"code\" : \"NAT_PERFORMANCE_TESTING\"\n" +
            "  },\n" +
            "  \"type\" : {\n" +
            "    \"code\" : \"TYP_COMPLIANCE_TESTING\"\n" +
            "  },\n" +
            "  \"test_case_status\" : \"WORK_IN_PROGRESS\",\n" +
            "  \"custom_fields\" : [ ],\n" +
            "  \"test_case_custom_fields\" : [ ],\n" +
            "  \"attachments\" : [ ],\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"https://localhost:4321/executions/25\"\n" +
            "    },\n" +
            "    \"project\" : {\n" +
            "      \"href\" : \"https://localhost:4321/projects/15\"\n" +
            "    },\n" +
            "    \"test_plan_item\" : {\n" +
            "      \"href\" : \"https://localhost:4321/iteration-test-plan-items/1\"\n" +
            "    },\n" +
            "    \"execution-steps\" : {\n" +
            "      \"href\" : \"https://localhost:4321/executions/25/execution-steps\"\n" +
            "    },\n" +
            "    \"attachments\" : {\n" +
            "      \"href\" : \"https://localhost:4321/executions/25/attachments\"\n" +
            "    }\n" +
            "  }\n" +
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
//
//    @Test
//    public void testCreateExecutionWithResult() {
//        createServerMock("POST", "/iteration-test-plan-items/4/executions", 200, EXECUTION_REPLY_DATA, "request");
//        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/executions/25?fields=execution_status", 200, "{" +
//                "  \"_type\" : \"execution\"," +
//                "  \"id\" : 25," +
//                "  \"execution_status\" : \"SUCCESS\"," +
//                "  \"comment\" : \"<p>the comment was modified...</p>\"," +
//                "  \"prerequisite\" : \"<p>... but the prerequisite was not</p>\"," +
//                "  \"custom_fields\" : [ {" +
//                "    \"code\" : \"TXT_STATUS\"," +
//                "    \"label\" : \"text\"," +
//                "    \"value\" : \"allright\"" +
//                "  }, {" +
//                "    \"code\" : \"TAGS_RELATED\"," +
//                "    \"label\" : \"see also\"," +
//                "    \"value\" : [ \"see this\", \"also that\" ]" +
//                "  } ]," +
//                "  \"test_case_custom_fields\" : [ {" +
//                "    \"code\" : \"TC_TEXT\"," +
//                "    \"label\" : \"test case cuf\"," +
//                "    \"value\" : \"I'm from the test case\"" +
//                "  }, {" +
//                "    \"code\" : \"TC_LABELS\"," +
//                "    \"label\" : \"labels\"," +
//                "    \"value\" : [ \"was\", \"not\", \"updated\" ]" +
//                "  } ]," +
//                "  \"_links\" : {" +
//                "    \"self\" : {" +
//                "      \"href\" : \"https://localhost:4321/executions/83\"" +
//                "    }," +
//                "    \"project\" : {" +
//                "      \"href\" : \"https://localhost:4321/projects/14\"" +
//                "    }," +
//                "    \"test_plan_item\" : {" +
//                "      \"href\" : \"https://localhost:4321/iteration-test-plan-items/1\"" +
//                "    }," +
//                "    \"execution-steps\" : {" +
//                "      \"href\" : \"https://localhost:4321/executions/83/execution-steps\"" +
//                "    }," +
//                "    \"attachments\" : {" +
//                "      \"href\" : \"https://localhost:4321/executions/83/attachments\"" +
//                "    }" +
//                "  }" +
//                "}", "request");
//
//        TestCase testCase = new TestCase("https://localhost:4321/test-cases/25", "test-case", 25, "test_case");
//        IterationTestPlanItem iterationTestPlanItem = new IterationTestPlanItem("https://localhost:4321/iteration-test-plan-items/4/", "iteration-test-plan-items", 4, testCase, null);
//        iterationTestPlanItem.createExecutionWithResult(TestPlanItemExecution.ExecutionStatus.SUCCESS, "ok");
//    }
}
