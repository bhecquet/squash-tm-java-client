package fr.bhecquet.entities;

import fr.bhecquet.SquashTMTest;
import fr.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.*;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestIteration extends SquashTMTest {

    @BeforeMethod
    public void init() {
        new Project("https://localhost:4321/projects/1", "project", 1, "project");
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testGetAllTestCases() {
        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"test-plan\" : [ {" +
                "      \"_type\" : \"iteration-test-plan-item\"," +
                "      \"id\" : 4," +
                "      \"execution_status\" : \"READY\"," +
                "      \"referenced_test_case\" : {" +
                "        \"_type\" : \"test-case\"," +
                "        \"id\" : 8," +
                "        \"name\" : \"sample test case 8\"," +
                "        \"reference\" : \"TC-8\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/test-cases/8\"" +
                "          }" +
                "        }" +
                "      }," +
                "      \"referenced_dataset\" : {" +
                "        \"_type\" : \"dataset\"," +
                "        \"id\" : 90," +
                "        \"name\" : \"sample dataset 90\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/datasets/90\"" +
                "          }" +
                "        }" +
                "      }," +
                "      \"last_executed_by\" : \"User-1\"," +
                "      \"last_executed_on\" : \"2017-06-25T10:00:00.000+0000\"," +
                "      \"assigned_to\" : \"User-1\"," +
                "      \"executions\" : [ {" +
                "        \"_type\" : \"execution\"," +
                "        \"id\" : 2," +
                "        \"execution_status\" : \"BLOCKED\"," +
                "        \"last_executed_by\" : \"User-1\"," +
                "        \"last_executed_on\" : \"2017-06-24T10:00:00.000+0000\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/executions/2\"" +
                "          }" +
                "        }" +
                "      }, {" +
                "        \"_type\" : \"execution\"," +
                "        \"id\" : 3," +
                "        \"execution_status\" : \"SUCCESS\"," +
                "        \"last_executed_by\" : \"User-1\"," +
                "        \"last_executed_on\" : \"2017-06-25T10:00:00.000+0000\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/executions/3\"" +
                "          }" +
                "        }" +
                "      } ]," +
                "      \"iteration\" : {" +
                "        \"_type\" : \"iteration\"," +
                "        \"id\" : 1," +
                "        \"name\" : \"sample iteration\"," +
                "        \"reference\" : \"IT1\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/iterations/1\"" +
                "          }" +
                "        }" +
                "      }," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iteration-test-plan-items/4\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"iteration-test-plan-item\"," +
                "      \"id\" : 12," +
                "      \"execution_status\" : \"READY\"," +
                "      \"referenced_test_case\" : {" +
                "        \"_type\" : \"test-case\"," +
                "        \"id\" : 16," +
                "        \"name\" : \"sample test case 16\"," +
                "        \"reference\" : \"TC-16\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/test-cases/16\"" +
                "          }" +
                "        }" +
                "      }," +
                "      \"referenced_dataset\" : {" +
                "        \"_type\" : \"dataset\"," +
                "        \"id\" : 12," +
                "        \"name\" : \"sample dataset 12\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/datasets/12\"" +
                "          }" +
                "        }" +
                "      }," +
                "      \"last_executed_by\" : \"User-1\"," +
                "      \"last_executed_on\" : \"2017-06-28T10:00:00.000+0000\"," +
                "      \"assigned_to\" : \"User-1\"," +
                "      \"executions\" : [ {" +
                "        \"_type\" : \"execution\"," +
                "        \"id\" : 9," +
                "        \"execution_status\" : \"FAILURE\"," +
                "        \"last_executed_by\" : \"User-1\"," +
                "        \"last_executed_on\" : \"2017-06-26T10:00:00.000+0000\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/executions/9\"" +
                "          }" +
                "        }" +
                "      }, {" +
                "        \"_type\" : \"execution\"," +
                "        \"id\" : 35," +
                "        \"execution_status\" : \"SUCCESS\"," +
                "        \"last_executed_by\" : \"User-1\"," +
                "        \"last_executed_on\" : \"2017-06-28T10:00:00.000+0000\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/executions/35\"" +
                "          }" +
                "        }" +
                "      } ]," +
                "      \"iteration\" : {" +
                "        \"_type\" : \"iteration\"," +
                "        \"id\" : 1," +
                "        \"name\" : \"sample iteration\"," +
                "        \"reference\" : \"IT1\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/iterations/1\"" +
                "          }" +
                "        }" +
                "      }," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iteration-test-plan-items/12\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/1/test-plan?page=0&size=2\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/1/test-plan?page=0&size=2\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/1/test-plan?page=1&size=2\"" +
                "    }," +
                "    \"next\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/1/test-plan?page=2&size=2\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/1/test-plan?page=2&size=2\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 2," +
                "    \"totalElements\" : 6," +
                "    \"totalPages\" : 3," +
                "    \"number\" : 1" +
                "  }" +
                "}");
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");
        List<IterationTestPlanItem> testCases = iteration.getAllTestCases();
        Assert.assertEquals(testCases.size(), 2);
        Assert.assertEquals(testCases.get(0).getTestCase().getId(), 8);
        Assert.assertEquals(testCases.get(0).getId(), 4);
        Assert.assertEquals(testCases.get(0).getUrl(), "https://localhost:4321/iteration-test-plan-items/4");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetAllTestCaseWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, "{}", "requestBodyEntity");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);

        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");
        iteration.getAllTestCases();
    }

    /**
     * Check case where no campaigns are available
     */

    @Test
    public void testGetAllNoTestCases() {
        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, "{" +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/1/test-plan?page=0&size=2\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/1/test-plan?page=0&size=2\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/1/test-plan?page=1&size=2\"" +
                "    }," +
                "    \"next\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/1/test-plan?page=2&size=2\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/iterations/1/test-plan?page=2&size=2\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 2," +
                "    \"totalElements\" : 6," +
                "    \"totalPages\" : 3," +
                "    \"number\" : 1" +
                "  }" +
                "}");

        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");
        List<IterationTestPlanItem> testCases = iteration.getAllTestCases();
        Assert.assertEquals(testCases.size(), 0);
    }

    @Test
    public void testFromJson() {

        JSONObject json = new JSONObject();
        json.put("_type", "iteration");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iterations/1\"" +
                "        }}"));

        Iteration iteration = Iteration.fromJson(json);
        Assert.assertEquals(iteration.getId(), 1);
        Assert.assertEquals(iteration.getName(), "foo");
        Assert.assertEquals(iteration.getUrl(), "https://localhost:4321/iterations/1");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {

        JSONObject json = new JSONObject();
        json.put("_type", "iteration");
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iterations/1\"" +
                "        }}"));

        Iteration.fromJson(json);
    }

    @Test
    public void testCreateIteration() {
        createServerMock("GET", "/campaigns/2/iterations", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"iterations\" : [ {" +
                "      \"_type\" : \"iteration\"," +
                "      \"id\" : 1," +
                "      \"name\" : \"Iteration\"," +
                "      \"reference\" : \"\"," +
                "      \"description\" : \"\"," +
                "      \"uuid\" : \"a60b3994-6d04-440a-8d0a-becc9ce75ac5\"," +
                "      \"parent\" : {" +
                "        \"_type\" : \"campaign\"," +
                "        \"id\" : 2," +
                "        \"name\" : \"my_campaign\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/campaigns/2\"" +
                "          }" +
                "        }" +
                "      }," +
                "      \"created_by\" : \"xxx\"," +
                "      \"created_on\" : \"2024-01-02T13:55:10.697+00:00\"," +
                "      \"last_modified_by\" : \"xxx\"," +
                "      \"last_modified_on\" : \"2024-01-02T13:55:10.716+00:00\"," +
                "      \"actual_start_date\" : null," +
                "      \"actual_end_date\" : null," +
                "      \"actual_start_auto\" : false," +
                "      \"actual_end_auto\" : false," +
                "      \"custom_fields\" : [ ]," +
                "      \"test_suites\" : [ ]," +
                "      \"attachments\" : [ ]," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iterations/12\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/2/iterations?page=0&size=20\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 20," +
                "    \"totalElements\" : 1," +
                "    \"totalPages\" : 1," +
                "    \"number\" : 0" +
                "  }" +
                "}");

        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaigns/2/iterations", 200, "{" +
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

        Campaign campaign = new Campaign("https://localhost:4321/campaigns/2", "campaign", 2, "campaign");
        Iteration.create(campaign, "new iteration");
        verify(postRequest).body(new JSONObject("{\"_type\":\"iteration\",\"name\":\"new iteration\",\"parent\":{\"id\":2,\"_type\":\"campaign\"}}"));
    }


    @Test
    public void testCreateIterationAlreadyExist() {
        createServerMock("GET", "/campaigns/2/iterations", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"iterations\" : [ {" +
                "      \"_type\" : \"iteration\"," +
                "      \"id\" : 1," +
                "      \"name\" : \"new iteration\"," +
                "      \"reference\" : \"\"," +
                "      \"description\" : \"\"," +
                "      \"uuid\" : \"a60b3994-6d04-440a-8d0a-becc9ce75ac5\"," +
                "      \"parent\" : {" +
                "        \"_type\" : \"campaign\"," +
                "        \"id\" : 2," +
                "        \"name\" : \"my_campaign\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/campaigns/2\"" +
                "          }" +
                "        }" +
                "      }," +
                "      \"created_by\" : \"xxx\"," +
                "      \"created_on\" : \"2024-01-02T13:55:10.697+00:00\"," +
                "      \"last_modified_by\" : \"xxx\"," +
                "      \"last_modified_on\" : \"2024-01-02T13:55:10.716+00:00\"," +
                "      \"actual_start_date\" : null," +
                "      \"actual_end_date\" : null," +
                "      \"actual_start_auto\" : false," +
                "      \"actual_end_auto\" : false," +
                "      \"custom_fields\" : [ ]," +
                "      \"test_suites\" : [ ]," +
                "      \"attachments\" : [ ]," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iterations/12\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/2/iterations?page=0&size=20\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 20," +
                "    \"totalElements\" : 1," +
                "    \"totalPages\" : 1," +
                "    \"number\" : 0" +
                "  }" +
                "}");

        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaigns/2/iterations", 200, "{" +
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

        Campaign campaign = new Campaign("https://localhost:4321/campaigns/2", "campaign", 2, "campaign");
        Iteration.create(campaign, "new iteration");
        verify(postRequest, never()).body(new JSONObject("{\"_type\":\"iteration\",\"name\":\"new iteration\",\"parent\":{\"id\":2,\"_type\":\"campaign\"}}"));
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testCreateIterationWithError() {
        createServerMock("GET", "/campaigns/2/iterations", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"iterations\" : [ {" +
                "      \"_type\" : \"iteration\"," +
                "      \"id\" : 1," +
                "      \"name\" : \"Iteration\"," +
                "      \"reference\" : \"\"," +
                "      \"description\" : \"\"," +
                "      \"uuid\" : \"a60b3994-6d04-440a-8d0a-becc9ce75ac5\"," +
                "      \"parent\" : {" +
                "        \"_type\" : \"campaign\"," +
                "        \"id\" : 2," +
                "        \"name\" : \"my_campaign\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/campaigns/2\"" +
                "          }" +
                "        }" +
                "      }," +
                "      \"created_by\" : \"xxx\"," +
                "      \"created_on\" : \"2024-01-02T13:55:10.697+00:00\"," +
                "      \"last_modified_by\" : \"xxx\"," +
                "      \"last_modified_on\" : \"2024-01-02T13:55:10.716+00:00\"," +
                "      \"actual_start_date\" : null," +
                "      \"actual_end_date\" : null," +
                "      \"actual_start_auto\" : false," +
                "      \"actual_end_auto\" : false," +
                "      \"custom_fields\" : [ ]," +
                "      \"test_suites\" : [ ]," +
                "      \"attachments\" : [ ]," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iterations/12\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/2/iterations?page=0&size=20\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 20," +
                "    \"totalElements\" : 1," +
                "    \"totalPages\" : 1," +
                "    \"number\" : 0" +
                "  }" +
                "}");


        RequestBodyEntity postRequest = (RequestBodyEntity) createServerMock("POST", "/campaigns/2/iterations", 200, "{}", "requestBodyEntity");
        when(postRequest.asJson()).thenThrow(UnirestException.class);

        Campaign campaign = new Campaign("https://localhost:4321/campaigns/2", "campaign", 2, "campaign");
        Iteration.create(campaign, "new iteration");
    }
}
