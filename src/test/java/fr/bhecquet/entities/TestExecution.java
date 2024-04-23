package fr.bhecquet.entities;

import fr.bhecquet.SquashTMTest;
import fr.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;

public class TestExecution extends SquashTMTest {

    public static final String EXECUTION_REPLY_DATA = "{" +
            "  \"_type\" : \"execution\"," +
            "  \"id\" : 56," +
            "  \"name\" : \"sample test case 56\"," +
            "  \"execution_order\" : 4," +
            "  \"execution_status\" : \"BLOCKED\"," +
            "  \"last_executed_by\" : \"User-5\"," +
            "  \"last_executed_on\" : \"2017-07-24T10:00:00.000+00:00\"," +
            "  \"execution_mode\" : \"AUTOMATED\"," +
            "  \"reference\" : \"SAMP_EXEC_56\"," +
            "  \"dataset_label\" : \"sample dataset\"," +
            "  \"execution_steps\" : [ {" +
            "    \"_type\" : \"execution-step\"," +
            "    \"id\" : 22," +
            "    \"execution_status\" : \"SUCCESS\"," +
            "    \"action\" : \"<p>First action</p>\"," +
            "    \"expected_result\" : \"<p>First result</p>\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"http://localhost:8080/api/rest/latest/execution-steps/22\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"execution-step\"," +
            "    \"id\" : 23," +
            "    \"execution_status\" : \"BLOCKED\"," +
            "    \"action\" : \"<p>Second action</p>\"," +
            "    \"expected_result\" : \"<p>Second result</p>\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"http://localhost:8080/api/rest/latest/execution-steps/23\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"execution-step\"," +
            "    \"id\" : 27," +
            "    \"execution_status\" : \"SUCCESS\"," +
            "    \"action\" : \"<p>The Action</p>\"," +
            "    \"expected_result\" : \"<p>The Result</p>\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"http://localhost:8080/api/rest/latest/execution-steps/27\"" +
            "      }" +
            "    }" +
            "  } ]," +
            "  \"comment\" : \"<p>I have no comment</p>\"," +
            "  \"prerequisite\" : \"<p>Being alive.</p>\"," +
            "  \"description\" : \"<p>This is nice.</p>\"," +
            "  \"importance\" : \"LOW\"," +
            "  \"nature\" : {" +
            "    \"code\" : \"NAT_SECURITY_TESTING\"" +
            "  }," +
            "  \"type\" : {" +
            "    \"code\" : \"TYP_EVOLUTION_TESTING\"" +
            "  }," +
            "  \"test_case_status\" : \"APPROVED\"," +
            "  \"test_plan_item\" : {" +
            "    \"_type\" : \"iteration-test-plan-item\"," +
            "    \"id\" : 15," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"http://localhost:8080/api/rest/latest/iteration-test-plan-items/15\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"automated_execution_extender\" : {" +
            "    \"_type\" : \"automated-execution-extender\"," +
            "    \"id\" : 778," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"http://localhost:8080/api/rest/latest/automated-execution-extenders/778\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"custom_fields\" : [ {" +
            "    \"code\" : \"CUF_TXT\"," +
            "    \"label\" : \"cuf text\"," +
            "    \"value\" : \"cuf text value\"" +
            "  }, {" +
            "    \"code\" : \"CUF_TXT_2\"," +
            "    \"label\" : \"cuf text 2\"," +
            "    \"value\" : \"cuf text value 2\"" +
            "  } ]," +
            "  \"environment_variables\" : [ {" +
            "    \"name\" : \"environment name\"," +
            "    \"value\" : \"environment value\"," +
            "    \"type\" : \"PLAIN_TEXT\"" +
            "  } ]," +
            "  \"test_case_custom_fields\" : [ {" +
            "    \"code\" : \"TC_CUF_TXT\"," +
            "    \"label\" : \"tc cuf text\"," +
            "    \"value\" : \"tc cuf text value\"" +
            "  }, {" +
            "    \"code\" : \"TC_CUF_TXT_2\"," +
            "    \"label\" : \"tc cuf text 2\"," +
            "    \"value\" : \"tc cuf text value 2\"" +
            "  } ]," +
            "  \"attachments\" : [ ]," +
            "  \"_links\" : {" +
            "    \"self\" : {" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/executions/56\"" +
            "    }," +
            "    \"project\" : {" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/projects/87\"" +
            "    }," +
            "    \"test_plan_item\" : {" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/iteration-test-plan-items/15\"" +
            "    }," +
            "    \"execution-steps\" : {" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/executions/56/execution-steps\"" +
            "    }," +
            "    \"attachments\" : {" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/executions/56/attachments\"" +
            "    }," +
            "    \"issues\" : {" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/executions/56/issues\"" +
            "    }" +
            "  }" +
            "}";


    @BeforeMethod
    public void init() {
        Execution.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testFromJson() {
        JSONObject json = new JSONObject("{" +
                "    \"_type\" : \"execution\"," +
                "    \"id\" : 10," +
                "    \"execution_status\" : \"SUCCESS\"," +
                "    \"last_executed_by\" : \"User 6\"," +
                "    \"last_executed_on\" : \"2017-02-04T11:00:00.000+00:00\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"http://localhost:8080/api/rest/latest/executions/10\"" +
                "      }" +
                "    }" +
                "}");

        Execution execution = Execution.fromJson(json);
        Assert.assertEquals(execution.getId(), 10);
        Assert.assertEquals(execution.getUrl(), "https://localhost:8080/api/rest/latest/executions/10");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {
        JSONObject json = new JSONObject("{}");
        Execution.fromJson(json);
    }

    @Test
    public void testCompleteDetails() {
        createServerMock("GET", "/executions/10", 200, EXECUTION_REPLY_DATA);
        Execution execution = new Execution("http://localhost:4321/executions/10", "execution", 10);
        execution.completeDetails();

        Assert.assertEquals(execution.getExecutionSteps().size(), 3);
        Assert.assertEquals(execution.getStatus(), "BLOCKED");
        Assert.assertEquals(execution.getOrder(), 4);
        Assert.assertEquals(execution.getLastExecutedBy(), "User-5");
        Assert.assertEquals(execution.getLastExecutedOn(), "2017-07-24T10:00:00.000+00:00");
        Assert.assertEquals(execution.getCustomFields().size(), 2);
    }

    /**
     * When test has not been executed, executedBy and executedOn are not present
     */
    @Test
    public void testCompleteDetailsNotExecuted() {
        createServerMock("GET", "/executions/10", 200, EXECUTION_REPLY_DATA
                .replace("  \"last_executed_by\" : \"User-5\",", "")
                .replace("  \"last_executed_on\" : \"2017-07-24T10:00:00.000+00:00\",", ""));
        Execution execution = new Execution("http://localhost:4321/executions/10", "execution", 10);
        execution.completeDetails();

        Assert.assertEquals(execution.getExecutionSteps().size(), 3);
        Assert.assertEquals(execution.getStatus(), "BLOCKED");
        Assert.assertEquals(execution.getOrder(), 4);
        Assert.assertEquals(execution.getLastExecutedBy(), "");
        Assert.assertEquals(execution.getLastExecutedOn(), "");
        Assert.assertEquals(execution.getCustomFields().size(), 2);
    }

    @Test
    public void testGet() {
        createServerMock("GET", "/executions/10", 200, EXECUTION_REPLY_DATA);
        Execution execution = Execution.get(10);

        Assert.assertEquals(execution.getExecutionSteps().size(), 3);
        Assert.assertEquals(execution.getStatus(), "BLOCKED");
        Assert.assertEquals(execution.getOrder(), 4);
        Assert.assertEquals(execution.getLastExecutedBy(), "User-5");
        Assert.assertEquals(execution.getLastExecutedOn(), "2017-07-24T10:00:00.000+00:00");
        Assert.assertEquals(execution.getCustomFields().size(), 2);
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Execution 10 does not exist")
    public void testGetInError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/executions/10", 404, "Error");
        when(getRequest.asJson()).thenThrow(new UnirestException("Cannot get"));
        Execution execution = Execution.get(10);

    }
}
