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

import static org.mockito.Mockito.*;

public class TestExecutionStep extends SquashTMTest {


    public static final String SET_STATUS_REPLY_DATA = "{"
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
            + "}";
    public static final String EXECUTION_STEP_REPLY_DATA = "{"
            + "  \"_type\" : \"execution-step\","
            + "  \"id\" : 6,"
            + "  \"execution_status\" : \"SUCCESS\","
            + "  \"action\" : \"Click the button\","
            + "  \"expected_result\" : \"<p>The page shows up</p>\","
            + "  \"comment\" : \"hello\","
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
            + "}";

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
    public void testFromJsonFull() {

        JSONObject json = new JSONObject(" {"
                + "    \"_type\" : \"execution-step\","
                + "    \"id\" : 2403247,"
                + "    \"execution_status\" : \"SUCCESS\","
                + "    \"action\" : \"some action\","
                + "    \"expected_result\" : \"\","
                + "    \"comment\" : \"some comment\","
                + "    \"custom_fields\" : [],"
                + "    \"last_executed_by\" : \"some executor\","
                + "    \"last_executed_on\" : \"2024-12-11T16:56:49.733+00:00\","
                + "    \"referenced_test_step\" : 666,"
                + "    \"_links\" : {"
                + "      \"self\" : {"
                + "        \"href\" : \"https://localhost:4321/squash/execution-steps/2403247\""
                + "      }"
                + "    }"
                + "  }");

        ExecutionStep campaign = ExecutionStep.fromJsonFull(json);
        Assert.assertEquals(campaign.getId(), 2403247);
        Assert.assertEquals(campaign.getName(), "some action");
        Assert.assertEquals(campaign.getUrl(), "https://localhost:4321/squash/execution-steps/2403247");
        Assert.assertEquals(campaign.getStatus(), "SUCCESS");
        Assert.assertEquals(campaign.getComment(), "some comment");
        Assert.assertEquals(campaign.getLastExecutedBy(), "some executor");
        Assert.assertEquals(campaign.getLastExecutedOn(), "2024-12-11T16:56:49.733+00:00");
        Assert.assertEquals(campaign.getTestStepCustomFields(), null);
        Assert.assertEquals(campaign.getReferencedStepId(), 666);
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = ".*Cannot create execution step from JSON.*")
    public void testFromJsonInError() {

        JSONObject json = new JSONObject(" {"
                + "    \"_type\" : \"execution-step\","
                + "    \"id\" : 2403247,"
                + "    \"execution_status\" : \"SUCCESS\","
                + "    \"expected_result\" : \"\","
                + "    \"_links\" : {"
                + "      \"self\" : {"
                + "        \"href\" : \"https://localhost:4321/squash/execution-steps/2403247\""
                + "      }"
                + "    }"
                + "  }");

        ExecutionStep campaign = ExecutionStep.fromJson(json);
    }

    @Test
    public void testSetStatus() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/execution-steps/6/execution-status/SUCCESS", 200, SET_STATUS_REPLY_DATA);

        ExecutionStep executionStatus = new ExecutionStep("https://localhost:4321/execution-steps/6", 6, "Click the button");
        executionStatus.setStatus(TestPlanItemExecution.ExecutionStatus.SUCCESS);

        verify(patchRequest).asJson();
    }

    /**
     * With null, nothing done
     */
    @Test
    public void testSetStatusNull() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/execution-steps/6/execution-status/SUCCESS", 200, SET_STATUS_REPLY_DATA);

        ExecutionStep executionStatus = new ExecutionStep("https://localhost:4321/execution-steps/6", 6, "Click the button");
        executionStatus.setStatus(null);
        verify(patchRequest, never()).asJson();
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Cannot set result for execution step 6")
    public void testSetStatusInError() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/execution-steps/6/execution-status/SUCCESS", 200, SET_STATUS_REPLY_DATA);
        when(patchRequest.asJson()).thenThrow(new UnirestException("error"));

        ExecutionStep executionStatus = new ExecutionStep("https://localhost:4321/execution-steps/6", 6, "Click the button");
        executionStatus.setStatus(TestPlanItemExecution.ExecutionStatus.SUCCESS);
    }


    @Test
    public void testSetComment() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/execution-steps/6", 200, EXECUTION_STEP_REPLY_DATA);

        ExecutionStep executionStatus = new ExecutionStep("https://localhost:4321/execution-steps/6", 6, "Click the button");
        executionStatus.setComment("hello");

        verify(patchRequest).body(new JSONObject("{\"_type\":\"execution-step\",\"comment\":\"hello\"}"));
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testSetCommentWithError() {
        RequestBodyEntity patchRequest = (RequestBodyEntity) createServerMock("PATCH", "/execution-steps/6", 200, "{}", "requestBodyEntity");
        when(patchRequest.asJson()).thenThrow(UnirestException.class);

        ExecutionStep executionStep = new ExecutionStep("https://localhost:4321/execution-steps/6", 6, "Click the button");
        executionStep.setComment("hello");

    }

    @Test
    public void testCompleteDetails() {
        createServerMock("GET", "/execution-steps/6", 200, EXECUTION_STEP_REPLY_DATA);
        ExecutionStep executionStep = new ExecutionStep("https://localhost:4321/execution-steps/6", 6, "Click the button");
        executionStep.completeDetails();

        Assert.assertEquals(executionStep.getStatus(), "SUCCESS");
        Assert.assertEquals(executionStep.getOrder(), 1);
        Assert.assertEquals(executionStep.getExpectedResult(), "<p>The page shows up</p>");
        Assert.assertEquals(executionStep.getAction(), "Click the button");
        Assert.assertEquals(executionStep.getComment(), "hello");
        Assert.assertEquals(executionStep.getLastExecutedBy(), "User-J9");
        Assert.assertEquals(executionStep.getLastExecutedOn(), "2015-04-26T10:00:00.000+00:00");
        Assert.assertEquals(executionStep.getReferencedStepId(), 2);
        Assert.assertEquals(executionStep.getCustomFields().size(), 1);
        Assert.assertEquals(executionStep.getTestStepCustomFields().size(), 1);
    }

}
