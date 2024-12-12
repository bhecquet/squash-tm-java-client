package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.ConfigurationException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.*;
import kong.unirest.core.json.JSONObject;
import org.mockito.MockedStatic;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestIteration extends SquashTMTest {

    public static final String TEST_PLAN_ITEMS_REPLY_DATA = "{" +
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
            "      \"referenced_dataset\" : null," +
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
            "}";

    private static String ITERATION_REPLY_DATA = "{" +
            "  \"_type\" : \"iteration\"," +
            "  \"id\" : 22," +
            "  \"name\" : \"sample iteration\"," +
            "  \"reference\" : \"SAMP_IT\"," +
            "  \"description\" : \"<p>A sample iteration</p>\"," +
            "  \"uuid\" : \"2f7194ca-eb2e-4379-f82d-ddc207c866bd\"," +
            "  \"parent\" : {" +
            "    \"_type\" : \"campaign\"," +
            "    \"id\" : 2," +
            "    \"name\" : \"sample campaign\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/campaigns/2\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"created_by\" : \"User-A\"," +
            "  \"created_on\" : \"2017-04-07T10:00:00.000+00:00\"," +
            "  \"last_modified_by\" : \"User-B\"," +
            "  \"last_modified_on\" : \"2017-04-15T10:00:00.000+00:00\"," +
            "  \"scheduled_start_date\" : \"2017-04-09T10:00:00.000+00:00\"," +
            "  \"scheduled_end_date\" : \"2017-04-14T10:00:00.000+00:00\"," +
            "  \"actual_start_date\" : \"2017-04-10T10:00:00.000+00:00\"," +
            "  \"actual_end_date\" : \"2017-04-15T10:00:00.000+00:00\"," +
            "  \"actual_start_auto\" : false," +
            "  \"actual_end_auto\" : true," +
            "  \"custom_fields\" : [ {" +
            "    \"code\" : \"CUF\"," +
            "    \"label\" : \"cuf\"," +
            "    \"value\" : \"value\"" +
            "  } ]," +
            "  \"test_suites\" : [ {" +
            "    \"_type\" : \"test-suite\"," +
            "    \"id\" : 1," +
            "    \"name\" : \"Suite_1\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/test-suites/1\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"test-suite\"," +
            "    \"id\" : 2," +
            "    \"name\" : \"Suite_2\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/test-suites/2\"" +
            "      }" +
            "    }" +
            "  } ]," +
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
            "    }," +
            "    \"issues\" : {" +
            "      \"href\" : \"https://localhost:4321/iterations/22/issues\"" +
            "    }" +
            "  }" +
            "}";

    private static String PROJECT_REPLY_DATA = "{" +
            "  \"_type\" : \"project\"," +
            "  \"id\" : 4," +
            "  \"description\" : \"<p>This project is the main sample project</p>\"," +
            "  \"label\" : \"Main Sample Project\"," +
            "  \"name\" : \"sample project\"," +
            "  \"active\" : true," +
            "  \"attachments\" : [ ]," +
            "  \"_links\" : {" +
            "    \"self\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/4\"" +
            "    }," +
            "    \"requirements\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/4/requirements-library/content\"" +
            "    }," +
            "    \"test-cases\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/4/test-cases-library/content\"" +
            "    }," +
            "    \"campaigns\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/4/campaigns-library/content\"" +
            "    }," +
            "    \"permissions\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/4/permissions\"" +
            "    }," +
            "    \"attachments\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/4/attachments\"" +
            "    }" +
            "  }" +
            "}";

    @BeforeMethod
    public void init() {
        new Project("https://localhost:4321/projects/1", "project", 1, "project");
        Iteration.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testGetAllTestCases() {
        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
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

    @Test
    public void testAddTestCase() {

        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");

        try (MockedStatic mockedTestCase = mockStatic(TestCase.class);
             MockedStatic mockedIterationTestPlanItem = mockStatic(IterationTestPlanItem.class)
        ) {
            mockedIterationTestPlanItem.when(() -> IterationTestPlanItem.fromJson(any(JSONObject.class))).thenCallRealMethod();

            TestCase mTestCase = new TestCase("", "test-case", 10, "TC1");
            mockedTestCase.when(() -> TestCase.get(10)).thenReturn(mTestCase);
            Dataset mDataset = spy(new Dataset("", "dataset", 11, "DS"));
            mTestCase.setDatasets(List.of(mDataset));

            iteration.addTestCase(10, 11);
            mockedIterationTestPlanItem.verify(() -> IterationTestPlanItem.create(iteration, mTestCase, mDataset));
        }
    }

    @Test
    public void testAddTestCaseNoDataset() {

        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");

        try (MockedStatic mockedTestCase = mockStatic(TestCase.class);
             MockedStatic mockedIterationTestPlanItem = mockStatic(IterationTestPlanItem.class)
        ) {
            mockedIterationTestPlanItem.when(() -> IterationTestPlanItem.fromJson(any(JSONObject.class))).thenCallRealMethod();

            TestCase mTestCase = new TestCase("", "test-case", 10, "TC1");
            mockedTestCase.when(() -> TestCase.get(10)).thenReturn(mTestCase);

            iteration.addTestCase(10, null);
            mockedIterationTestPlanItem.verify(() -> IterationTestPlanItem.create(iteration, mTestCase, null));
        }
    }

    /**
     * Test exception when dataset does not match the test case
     */
    @Test(expectedExceptions = ConfigurationException.class, expectedExceptionsMessageRegExp = "Dataset with id 11 does not belong to Test case with id 10")
    public void testAddTestCaseDatasetDoesNotMatch() {

        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");

        try (MockedStatic mockedTestCase = mockStatic(TestCase.class);
             MockedStatic mockedIterationTestPlanItem = mockStatic(IterationTestPlanItem.class)
        ) {
            mockedIterationTestPlanItem.when(() -> IterationTestPlanItem.fromJson(any(JSONObject.class))).thenCallRealMethod();

            TestCase mTestCase = new TestCase("", "test-case", 10, "TC1");
            mockedTestCase.when(() -> TestCase.get(10)).thenReturn(mTestCase);
            Dataset mDataset = spy(new Dataset("", "dataset", 12, "DS"));
            mTestCase.setDatasets(List.of(mDataset));

            iteration.addTestCase(10, 11);
        }
    }

    /**
     * Test case does not exist in Squash
     */
    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Test case with id 10 does not exist in Squash")
    public void testAddTestCaseDoesotExist() {

        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");

        try (MockedStatic mockedTestCase = mockStatic(TestCase.class);
             MockedStatic mockedIterationTestPlanItem = mockStatic(IterationTestPlanItem.class)
        ) {
            mockedIterationTestPlanItem.when(() -> IterationTestPlanItem.fromJson(any(JSONObject.class))).thenCallRealMethod();

            mockedTestCase.when(() -> TestCase.get(10)).thenThrow(new SquashTmException("Test case does not exist"));

            iteration.addTestCase(10, 11);
        }
    }


    @Test
    public void testAddTestCaseAlreadyPresent() {

        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");

        try (MockedStatic mockedIterationTestPlanItem = mockStatic(IterationTestPlanItem.class)
        ) {
            mockedIterationTestPlanItem.when(() -> IterationTestPlanItem.fromJson(any(JSONObject.class))).thenCallRealMethod();

            iteration.addTestCase(8, 90);
            mockedIterationTestPlanItem.verify(() -> IterationTestPlanItem.create(eq(iteration), any(), any()), never());
        }
    }

    /**
     * If no dataset is provided, only check with test case
     */
    @Test
    public void testAddTestCaseAlreadyPresentNoDataset() {

        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");

        try (MockedStatic mockedIterationTestPlanItem = mockStatic(IterationTestPlanItem.class)
        ) {
            mockedIterationTestPlanItem.when(() -> IterationTestPlanItem.fromJson(any(JSONObject.class))).thenCallRealMethod();

            iteration.addTestCase(8, null);
            mockedIterationTestPlanItem.verify(() -> IterationTestPlanItem.create(eq(iteration), any(), any()), never());
        }
    }

    @Test
    public void testAddTestCaseAlreadyPresentNoDataset2() {

        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");

        try (MockedStatic mockedIterationTestPlanItem = mockStatic(IterationTestPlanItem.class)
        ) {
            mockedIterationTestPlanItem.when(() -> IterationTestPlanItem.fromJson(any(JSONObject.class))).thenCallRealMethod();

            iteration.addTestCase(16, 3);
            mockedIterationTestPlanItem.verify(() -> IterationTestPlanItem.create(eq(iteration), any(), any()), never());
        }
    }

    @Test
    public void testAddTestCaseAlreadyPresentNoDataset3() {

        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");

        try (MockedStatic mockedIterationTestPlanItem = mockStatic(IterationTestPlanItem.class)
        ) {
            mockedIterationTestPlanItem.when(() -> IterationTestPlanItem.fromJson(any(JSONObject.class))).thenCallRealMethod();

            iteration.addTestCase(16, null);
            mockedIterationTestPlanItem.verify(() -> IterationTestPlanItem.create(eq(iteration), any(), any()), never());
        }
    }

    /**
     * Test record the same test case with a different dataset
     */
    @Test
    public void testAddTestCaseAlreadyPresentDatasetDifferent() {

        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");

        try (MockedStatic mockedTestCase = mockStatic(TestCase.class);
             MockedStatic mockedIterationTestPlanItem = mockStatic(IterationTestPlanItem.class)
        ) {
            mockedIterationTestPlanItem.when(() -> IterationTestPlanItem.fromJson(any(JSONObject.class))).thenCallRealMethod();
            mockedTestCase.when(() -> TestCase.fromJson(any(JSONObject.class))).thenCallRealMethod();

            TestCase mTestCase = new TestCase("", "test-case", 8, "TC1");
            mockedTestCase.when(() -> TestCase.get(8)).thenReturn(mTestCase);
            Dataset mDataset = spy(new Dataset("", "dataset", 11, "DS"));
            mTestCase.setDatasets(List.of(mDataset));

            iteration.addTestCase(8, 11);
            mockedIterationTestPlanItem.verify(() -> IterationTestPlanItem.create(iteration, mTestCase, mDataset));
        }
    }

    @Test
    public void testCompleteDetails() {
        createServerMock("GET", "/iterations/1", 200, ITERATION_REPLY_DATA);
        createServerMock("GET", "/projects/4", 200, PROJECT_REPLY_DATA);
        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");
        iteration.completeDetails();

        Assert.assertEquals(iteration.getScheduleStartDate(), "2017-04-09T10:00:00.000+00:00");
        Assert.assertEquals(iteration.getScheduleEndDate(), "2017-04-14T10:00:00.000+00:00");
        Assert.assertEquals(iteration.getActualStartDate(), "2017-04-10T10:00:00.000+00:00");
        Assert.assertEquals(iteration.getActualEndDate(), "2017-04-15T10:00:00.000+00:00");
        Assert.assertEquals(iteration.getReference(), "SAMP_IT");
        Assert.assertEquals(iteration.getTestSuites().size(), 2);
        Assert.assertEquals(iteration.getIterationTestPlanItems().size(), 2);
        Assert.assertEquals(iteration.getCustomFields().size(), 1);
        Assert.assertEquals(iteration.getProject().getId(), 4);
    }

    @Test
    public void testCompleteDetailsNoDates() {
        createServerMock("GET", "/iterations/1", 200, ITERATION_REPLY_DATA
                .replace("\"scheduled_start_date\" : \"2017-04-09T10:00:00.000+00:00\",", "")
                .replace("\"scheduled_end_date\" : \"2017-04-14T10:00:00.000+00:00\",", "")
                .replace("\"actual_start_date\" : \"2017-04-10T10:00:00.000+00:00\",", "")
                .replace("\"actual_end_date\" : \"2017-04-15T10:00:00.000+00:00\",", ""));

        createServerMock("GET", "/projects/4", 200, PROJECT_REPLY_DATA);
        createServerMock("GET", "/iterations/1/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = new Iteration("https://localhost:4321/iterations/1", "iteration", 1, "my_iteration");
        iteration.completeDetails();

        Assert.assertEquals(iteration.getScheduleStartDate(), "");
        Assert.assertEquals(iteration.getScheduleEndDate(), "");
        Assert.assertEquals(iteration.getActualStartDate(), "");
        Assert.assertEquals(iteration.getActualEndDate(), "");
        Assert.assertEquals(iteration.getReference(), "SAMP_IT");
        Assert.assertEquals(iteration.getTestSuites().size(), 2);
        Assert.assertEquals(iteration.getIterationTestPlanItems().size(), 2);
        Assert.assertEquals(iteration.getCustomFields().size(), 1);
    }

    @Test
    public void testGet() {

        createServerMock("GET", "/projects/4", 200, PROJECT_REPLY_DATA);
        createServerMock("GET", "/iterations/22", 200, ITERATION_REPLY_DATA);
        Iteration iteration = Iteration.get(22);
        Assert.assertEquals(iteration.getName(), "sample iteration");
        Assert.assertEquals(iteration.getId(), 22);
        Assert.assertEquals(iteration.getUrl(), "https://localhost:4321/iterations/22");
        Assert.assertNull(iteration.getProject());
        Assert.assertEquals(iteration.getIndex(), 0);
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "request to https://localhost:4321 failed\\[404\\]: null")
    public void testGetInError() {
        createServerMock("GET", "/iterations/22", 404, ITERATION_REPLY_DATA);
        Iteration iteration = Iteration.get(22);
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Iteration 22 does not exist")
    public void testGetInError2() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/iterations/22", 404, ITERATION_REPLY_DATA);
        when(getRequest.asJson()).thenThrow(new UnirestException("error"));
        Iteration iteration = Iteration.get(22);
    }

    @Test
    public void testGetTestPlan() {
        createServerMock("GET", "/iterations/22", 200, ITERATION_REPLY_DATA);
        createServerMock("GET", "/iterations/22/test-plan?sort=id", 200, TEST_PLAN_ITEMS_REPLY_DATA);
        Iteration iteration = Iteration.get(22);
        List<IterationTestPlanItem> listITPI = iteration.getTestPlan();
        Assert.assertEquals(listITPI.size(), 2);
    }
}
