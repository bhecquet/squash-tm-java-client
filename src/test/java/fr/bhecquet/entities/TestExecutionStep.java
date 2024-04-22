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

public class TestExecutionStep extends SquashTMTest {


    @BeforeMethod
    public void init() {
        new Project("https://localhost:4321/projects/1", "project", 1, "project");
        ExecutionStep.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testFromJson() {

        JSONObject json = new JSONObject(" {"
                + "    \"_type\" : \"execution-step\","
                + "    \"id\" : 2403247,"
                + "    \"execution_status\" : \"SUCCESS\","
                + "    \"action\" : \"some action\","
                + "    \"expected_result\" : \"\","
                + "    \"_links\" : {"
                + "      \"self\" : {"
                + "        \"href\" : \"https://localhost:4321/squash/execution-steps/2403247\""
                + "      }"
                + "    }"
                + "  }");

        ExecutionStep campaign = ExecutionStep.fromJson(json);
        Assert.assertEquals(campaign.getId(), 2403247);
        Assert.assertEquals(campaign.getName(), "some action");
        Assert.assertEquals(campaign.getUrl(), "https://localhost:4321/squash/execution-steps/2403247");
    }

    @Test
    public void testSetStatus() {
        createServerMock("PATCH", "/execution-steps/6/execution-status/SUCCESS", 200, "{"
                + "  \"_type\" : \"execution-step\","
                + "  \"id\" : 6,"
                + "  \"execution_status\" : \"SUCCESS\","
                + "  \"action\" : \"Click the button\","
                + "  \"expected_result\" : \"<p>The page shows up</p>\","
                + "  \"comment\" : \"<p>This is quite simple.</p>\","
                + "  \"last_executed_by\" : \"User-J9\","
                + "  \"last_executed_on\" : \"2015-04-26T10:00:00.000+00:00\","
                + "  \"execution_step_order\" : 1,"
                + "  \"referenced_test_step\" : {"
                + "    \"_type\" : \"action-step\","
                + "    \"id\" : 2,"
                + "    \"_links\" : {"
                + "      \"self\" : {"
                + "        \"href\" : \"https://localhost:4321/test-steps/2\""
                + "      }"
                + "    }"
                + "  },"
                + "  \"execution\" : {"
                + "    \"_type\" : \"execution\","
                + "    \"id\" : 3,"
                + "    \"execution_status\" : \"BLOCKED\","
                + "    \"_links\" : {"
                + "      \"self\" : {"
                + "        \"href\" : \"https://localhost:4321/executions/3\""
                + "      }"
                + "    }"
                + "  },"
                + "  \"custom_fields\" : [ {"
                + "    \"code\" : \"CUF_TAG\","
                + "    \"label\" : \"Tag Cuf\","
                + "    \"value\" : [ \"tag_1\", \"tag_2\", \"tag_3\" ]"
                + "  } ],"
                + "  \"test_step_custom_fields\" : [ {"
                + "    \"code\" : \"CUF_TXT\","
                + "    \"label\" : \"Basic Text Cuf\","
                + "    \"value\" : \"The Value\""
                + "  } ],"
                + "  \"attachments\" : [ ],"
                + "  \"_links\" : {"
                + "    \"self\" : {"
                + "      \"href\" : \"https://localhost:4321/execution-steps/6\""
                + "    },"
                + "    \"project\" : {"
                + "      \"href\" : \"https://localhost:4321/projects/10\""
                + "    },"
                + "    \"execution\" : {"
                + "      \"href\" : \"https://localhost:4321/executions/3\""
                + "    }"
                + "  }"
                + "}");

        ExecutionStep executionStatus = new ExecutionStep("https://localhost:4321/execution-steps/6", 6, "Click the button");
        executionStatus.setStatus(TestPlanItemExecution.ExecutionStatus.SUCCESS);
    }

    /**
     * With null, nothing done
     */
    @Test
    public void testSetStatusNull() {
        createServerMock("PATCH", "/execution-steps/6/execution-status/SUCCESS", 200, "{"
                + "  \"_type\" : \"execution-step\","
                + "  \"id\" : 6,"
                + "  \"execution_status\" : \"SUCCESS\","
                + "  \"action\" : \"Click the button\","
                + "  \"expected_result\" : \"<p>The page shows up</p>\","
                + "  \"comment\" : \"<p>This is quite simple.</p>\","
                + "  \"last_executed_by\" : \"User-J9\","
                + "  \"last_executed_on\" : \"2015-04-26T10:00:00.000+00:00\","
                + "  \"execution_step_order\" : 1,"
                + "  \"referenced_test_step\" : {"
                + "    \"_type\" : \"action-step\","
                + "    \"id\" : 2,"
                + "    \"_links\" : {"
                + "      \"self\" : {"
                + "        \"href\" : \"https://localhost:4321/test-steps/2\""
                + "      }"
                + "    }"
                + "  },"
                + "  \"execution\" : {"
                + "    \"_type\" : \"execution\","
                + "    \"id\" : 3,"
                + "    \"execution_status\" : \"BLOCKED\","
                + "    \"_links\" : {"
                + "      \"self\" : {"
                + "        \"href\" : \"https://localhost:4321/executions/3\""
                + "      }"
                + "    }"
                + "  },"
                + "  \"custom_fields\" : [ {"
                + "    \"code\" : \"CUF_TAG\","
                + "    \"label\" : \"Tag Cuf\","
                + "    \"value\" : [ \"tag_1\", \"tag_2\", \"tag_3\" ]"
                + "  } ],"
                + "  \"test_step_custom_fields\" : [ {"
                + "    \"code\" : \"CUF_TXT\","
                + "    \"label\" : \"Basic Text Cuf\","
                + "    \"value\" : \"The Value\""
                + "  } ],"
                + "  \"attachments\" : [ ],"
                + "  \"_links\" : {"
                + "    \"self\" : {"
                + "      \"href\" : \"https://localhost:4321/execution-steps/6\""
                + "    },"
                + "    \"project\" : {"
                + "      \"href\" : \"https://localhost:4321/projects/10\""
                + "    },"
                + "    \"execution\" : {"
                + "      \"href\" : \"https://localhost:4321/executions/3\""
                + "    }"
                + "  }"
                + "}");

        ExecutionStep executionStatus = new ExecutionStep("https://localhost:4321/execution-steps/6", 6, "Click the button");
        executionStatus.setStatus(null);
    }


    @Test
    public void testSetComment() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/execution-steps/6", 200, "{"
                + "  \"_type\" : \"execution-step\","
                + "  \"id\" : 6,"
                + "  \"execution_status\" : \"SUCCESS\","
                + "  \"action\" : \"Click the button\","
                + "  \"expected_result\" : \"<p>The page shows up</p>\","
                + "  \"comment\" : \"<p>This is quite simple.</p>\","
                + "  \"last_executed_by\" : \"User-J9\","
                + "  \"last_executed_on\" : \"2015-04-26T10:00:00.000+00:00\","
                + "  \"execution_step_order\" : 1,"
                + "  \"referenced_test_step\" : {"
                + "    \"_type\" : \"action-step\","
                + "    \"id\" : 2,"
                + "    \"_links\" : {"
                + "      \"self\" : {"
                + "        \"href\" : \"https://localhost:4321/test-steps/2\""
                + "      }"
                + "    }"
                + "  },"
                + "  \"execution\" : {"
                + "    \"_type\" : \"execution\","
                + "    \"id\" : 3,"
                + "    \"execution_status\" : \"BLOCKED\","
                + "    \"_links\" : {"
                + "      \"self\" : {"
                + "        \"href\" : \"https://localhost:4321/executions/3\""
                + "      }"
                + "    }"
                + "  },"
                + "  \"custom_fields\" : [ {"
                + "    \"code\" : \"CUF_TAG\","
                + "    \"label\" : \"Tag Cuf\","
                + "    \"value\" : [ \"tag_1\", \"tag_2\", \"tag_3\" ]"
                + "  } ],"
                + "  \"test_step_custom_fields\" : [ {"
                + "    \"code\" : \"CUF_TXT\","
                + "    \"label\" : \"Basic Text Cuf\","
                + "    \"value\" : \"The Value\""
                + "  } ],"
                + "  \"attachments\" : [ ],"
                + "  \"_links\" : {"
                + "    \"self\" : {"
                + "      \"href\" : \"https://localhost:4321/execution-steps/6\""
                + "    },"
                + "    \"project\" : {"
                + "      \"href\" : \"https://localhost:4321/projects/10\""
                + "    },"
                + "    \"execution\" : {"
                + "      \"href\" : \"https://localhost:4321/executions/3\""
                + "    }"
                + "  }"
                + "}");

        ExecutionStep executionStatus = new ExecutionStep("https://localhost:4321/execution-steps/6", 6, "Click the button");
        executionStatus.setComment("hello");

        verify(patchRequest).body(new JSONObject("{\"_type\":\"execution-step\",\"comment\":\"hello\"}"));
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testSetCommentWithError() {
        RequestBodyEntity patchRequest = (RequestBodyEntity) createServerMock("PATCH", "/execution-steps/6", 200, "{}", "requestBodyEntity");
        when(patchRequest.asJson()).thenThrow(UnirestException.class);

        ExecutionStep executionStatus = new ExecutionStep("https://localhost:4321/execution-steps/6", 6, "Click the button");
        executionStatus.setComment("hello");

    }
}
