package fr.bhecquet.entities;

import fr.bhecquet.SquashTMTest;
import fr.bhecquet.exceptions.SquashTmException;
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
        createServerMock("POST", "/iteration-test-plan-items/1/executions", 200, "{" +
                "  \"_type\" : \"iteration\"," +
                "  \"id\" : 22," +
                "  \"name\" : \"new iteration\"," +
                "  \"reference\" : \"NEW_IT\"," +
                "  \"description\" : \"<p>A new iteration</p>\"," +
                "  \"parent\" : {" +
                "    \"_type\" : \"campaign\"," +
                "    \"id\" : 2," +
                "    \"name\" : \"parent campaign\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/campaigns/2\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"created_by\" : \"User-A\"," +
                "  \"created_on\" : \"2017-04-07T10:00:00.000+0000\"," +
                "  \"last_modified_by\" : \"User-B\"," +
                "  \"last_modified_on\" : \"2017-04-15T10:00:00.000+0000\"," +
                "  \"scheduled_start_date\" : \"2017-04-09T10:00:00.000+0000\"," +
                "  \"scheduled_end_date\" : \"2017-04-14T10:00:00.000+0000\"," +
                "  \"actual_start_date\" : \"2017-04-10T10:00:00.000+0000\"," +
                "  \"actual_end_date\" : \"2017-04-15T10:00:00.000+0000\"," +
                "  \"actual_start_auto\" : false," +
                "  \"actual_end_auto\" : true," +
                "  \"custom_fields\" : [ {" +
                "    \"code\" : \"CUF\"," +
                "    \"label\" : \"cuf\"," +
                "    \"value\" : \"value\"" +
                "  } ]," +
                "  \"test_suites\" : [ ]," +
                "  \"attachments\" : [ ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/22\"" +
                "    }," +
                "    \"project\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/4\"" +
                "    }," +
                "    \"campaign\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/2\"" +
                "    }," +
                "    \"test-suites\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/22/test-suites\"" +
                "    }," +
                "    \"test-plan\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/22/test-plan\"" +
                "    }," +
                "    \"attachments\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/22/attachments\"" +
                "    }" +
                "  }" +
                "}", "request");

        TestCase testCase = new TestCase("https://localhost:4321/test-cases/3", "test-case", 3, "my_test");
        IterationTestPlanItem itpi = new IterationTestPlanItem("https://localhost:4321/iteration-test-plan-items/1", "test-plan-item", 1, testCase, null);

        TestPlanItemExecution execution = itpi.createExecution();
        Assert.assertEquals(execution.getId(), 22);
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
}
