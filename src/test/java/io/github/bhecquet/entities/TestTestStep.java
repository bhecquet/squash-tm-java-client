package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class TestTestStep extends SquashTMTest {

    @BeforeMethod
    public void init() {
        TestStep.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testFromJsonActionStep() {
        List<TestStep> steps = TestStep.fromJson(new JSONObject("{\"_type\" : \"action-step\"," +
                "    \"id\" : 165," +
                "    \"action\" : \"<p>move ${first_foot} forward</p>\\n\"," +
                "    \"expected_result\" : \"<p>I just advanced by one step</p>\\n\"," +
                "    \"index\" : 0," +
                "    \"custom_fields\" : [ {" +
                "      \"code\" : \"CF_TXT\"," +
                "      \"label\" : \"test level\"," +
                "      \"value\" : \"mandatory\"" +
                "    }, {" +
                "      \"code\" : \"CF_TAGS\"," +
                "      \"label\" : \"see also\"," +
                "      \"value\" : [ \"basic\", \"walking\" ]" +
                "    } ]," +
                "    \"attachments\" : [ ]," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/test-steps/165\"" +
                "      }" +
                "    }" +
                "  }"));

        Assert.assertEquals(steps.size(), 1);
        Assert.assertEquals(steps.get(0).getAction(), "<p>move ${first_foot} forward</p>\n");
        Assert.assertEquals(steps.get(0).getExpectedResult(), "<p>I just advanced by one step</p>\n");
        Assert.assertEquals(steps.get(0).getId(), 165);
        Assert.assertEquals(steps.get(0).getOrder(), 0);
        Assert.assertEquals(steps.get(0).getCustomFields().size(), 2);
        Assert.assertEquals(steps.get(0).getCustomFields().get(0).getCode(), "CF_TXT");
    }


    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonUnknownStep() {
        List<TestStep> steps = TestStep.fromJson(new JSONObject("{\"_type\" : \"unknown-step\"," +
                "    \"id\" : 165," +
                "    \"action\" : \"<p>move ${first_foot} forward</p>\\n\"," +
                "    \"expected_result\" : \"<p>I just advanced by one step</p>\\n\"," +
                "    \"index\" : 0," +
                "    \"custom_fields\" : [ {" +
                "      \"code\" : \"CF_TXT\"," +
                "      \"label\" : \"test level\"," +
                "      \"value\" : \"mandatory\"" +
                "    }, {" +
                "      \"code\" : \"CF_TAGS\"," +
                "      \"label\" : \"see also\"," +
                "      \"value\" : [ \"basic\", \"walking\" ]" +
                "    } ]," +
                "    \"attachments\" : [ ]," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/test-steps/165\"" +
                "      }" +
                "    }" +
                "  }"));

        Assert.assertEquals(steps.size(), 1);
        Assert.assertEquals(steps.get(0).getAction(), "<p>move ${first_foot} forward</p>\n");
        Assert.assertEquals(steps.get(0).getExpectedResult(), "<p>I just advanced by one step</p>\n");
        Assert.assertEquals(steps.get(0).getId(), 165);
        Assert.assertEquals(steps.get(0).getOrder(), 0);
        Assert.assertEquals(steps.get(0).getCustomFields().size(), 2);
        Assert.assertEquals(steps.get(0).getCustomFields().get(0).getCode(), "CF_TXT");
    }

    @Test
    public void testFromJsonCalledStep() {

        createServerMock("GET", "/test-cases/239", 200, TestTestCase.TEST_CASE_REPLY_DATA2);

        List<TestStep> steps = TestStep.fromJson(new JSONObject("{\n" +
                "    \"_type\" : \"call-step\",\n" +
                "    \"id\" : 167,\n" +
                "    \"delegate_parameter_values\" : false,\n" +
                "    \"called_test_case\" : {\n" +
                "      \"_type\" : \"test-case\",\n" +
                "      \"id\" : 239,\n" +
                "      \"name\" : \"victory dance\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/test-cases/239\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"called_dataset\" : null,\n" +
                "    \"index\" : 2,\n" +
                "    \"_links\" : {\n" +
                "      \"self\" : {\n" +
                "        \"href\" : \"https://localhost:4321/test-steps/167\"\n" +
                "      }\n" +
                "    }\n" +
                "  }"));

        Assert.assertEquals(steps.size(), 1);
        Assert.assertEquals(steps.get(0).getAction(), "<p>move ${first_foot} forward</p>\n");
        Assert.assertEquals(steps.get(0).getExpectedResult(), "<p>I just advanced by one step</p>\n");
        Assert.assertEquals(steps.get(0).getId(), 165);
        Assert.assertEquals(steps.get(0).getOrder(), 0);
        Assert.assertEquals(steps.get(0).getCustomFields().size(), 2);
        Assert.assertEquals(steps.get(0).getCustomFields().get(0).getCode(), "CF_TXT");
    }

    /**
     * In case of unauthorized resource, do not return any test step
     */
    @Test
    public void testFromJsonUnauthorizedResource() {

        createServerMock("GET", "/test-cases/239", 200, TestTestCase.TEST_CASE_REPLY_DATA2);

        List<TestStep> steps = TestStep.fromJson(new JSONObject("{\n" +
                "    \"_type\" : \"call-step\",\n" +
                "    \"id\" : 168,\n" +
                "    \"delegate_parameter_values\" : false,\n" +
                "    \"called_test_case\" : {\n" +
                "      \"_type\" : \"unauthorized-resource\",\n" +
                "      \"resource_type\" : \"test-case\",\n" +
                "      \"resource_id\" : 240,\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/test-cases/240\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"called_dataset\" : null,\n" +
                "    \"index\" : 3,\n" +
                "    \"_links\" : {\n" +
                "      \"self\" : {\n" +
                "        \"href\" : \"https://localhost:4321/test-steps/168\"\n" +
                "      }\n" +
                "    }\n" +
                "  }"));

        Assert.assertEquals(steps.size(), 0);
    }

    @Test(expectedExceptions = NotImplementedException.class)
    public void testCompleteDetails() {
        TestStep ts = new TestStep("osef", "ouais", 1, 1, "yolo", "cliquer");
        ts.completeDetails();
    }
}
