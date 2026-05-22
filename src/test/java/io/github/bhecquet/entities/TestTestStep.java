package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpRequestWithBody;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestTestStep extends SquashTMTest {

    private static final String TEST_STEP_REPLY_DATA = "{"
            + "\"_type\":\"action-step\","
            + "\"id\":165,"
            + "\"index\":0,"
            + "\"action\":\"<p>click button</p>\","
            + "\"expected_result\":\"<p>button clicked</p>\","
            + "\"custom_fields\":[],"
            + "\"attachments\":[],"
            + "\"_links\":{\"self\":{\"href\":\"https://localhost:4321/test-steps/165\"}}"
            + "}";

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

    // ── get ──────────────────────────────────────────────────────────────────

    @Test
    public void testGet() {
        createServerMock("GET", "/test-steps/165", 200, TEST_STEP_REPLY_DATA);
        TestStep step = TestStep.get(165);
        Assert.assertEquals(step.getId(), 165);
        Assert.assertEquals(step.getOrder(), 0);
        Assert.assertEquals(step.getAction(), "<p>click button</p>");
        Assert.assertEquals(step.getExpectedResult(), "<p>button clicked</p>");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/test-steps/165", 200, TEST_STEP_REPLY_DATA);
        when(getRequest.asJson()).thenThrow(UnirestException.class);
        TestStep.get(165);
    }

    // ── fromJsonOne ──────────────────────────────────────────────────────────

    @Test
    public void testFromJsonOne() {
        TestStep step = TestStep.fromJsonOne(new JSONObject(TEST_STEP_REPLY_DATA));
        Assert.assertEquals(step.getId(), 165);
        Assert.assertEquals(step.getOrder(), 0);
        Assert.assertEquals(step.getAction(), "<p>click button</p>");
        Assert.assertEquals(step.getExpectedResult(), "<p>button clicked</p>");
        Assert.assertEquals(step.getUrl(), "https://localhost:4321/test-steps/165");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonOneMissingId() {
        // JSON sans le champ obligatoire "id"
        TestStep.fromJsonOne(new JSONObject("{"
                + "\"_type\":\"action-step\","
                + "\"index\":0,"
                + "\"_links\":{\"self\":{\"href\":\"https://localhost:4321/test-steps/165\"}}"
                + "}"));
    }

    // ── create ───────────────────────────────────────────────────────────────

    @Test
    public void testCreate() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/test-cases/10/steps", 200, TEST_STEP_REPLY_DATA, "request");

        TestStep step = TestStep.create(10, Map.of("action", "<p>click</p>", "expected_result", "<p>ok</p>"));

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.forClass(JSONObject.class);
        verify(postRequest).body(captor.capture());

        JSONObject body = captor.getValue();
        Assert.assertEquals(body.getString("_type"), "action-step");
        Assert.assertEquals(body.getString("action"), "<p>click</p>");
        Assert.assertEquals(body.getString("expected_result"), "<p>ok</p>");
        Assert.assertNotNull(step);
        Assert.assertEquals(step.getId(), 165);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testCreateWithError() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/test-cases/10/steps", 200, TEST_STEP_REPLY_DATA, "request");
        when(postRequest.body(any(JSONObject.class))).thenThrow(UnirestException.class);
        TestStep.create(10, Map.of("action", "<p>click</p>", "expectedResult", "<p>ok</p>"));
    }

    // ── updateTestStep ────────────────────────────────────────────────────────

    @Test
    public void testUpdateTestStep() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/test-steps/165", 200, TEST_STEP_REPLY_DATA, "request");

        TestStep ts = new TestStep("https://localhost:4321/test-steps/165", "action-step", 165, 0, "", "");
        TestStep updated = ts.update(165, "<p>new action</p>", "<p>new result</p>");

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.forClass(JSONObject.class);
        verify(patchRequest).body(captor.capture());

        JSONObject body = captor.getValue();
        Assert.assertEquals(body.getString("_type"), "action-step");
        Assert.assertEquals(body.getString("action"), "<p>new action</p>");
        Assert.assertEquals(body.getString("expected_result"), "<p>new result</p>");
        Assert.assertNotNull(updated);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testUpdateTestStepWithError() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/test-steps/165", 200, TEST_STEP_REPLY_DATA, "request");
        when(patchRequest.body(any(JSONObject.class))).thenThrow(UnirestException.class);

        TestStep ts = new TestStep("https://localhost:4321/test-steps/165", "action-step", 165, 0, "", "");
        ts.update(165, "<p>new action</p>", "<p>new result</p>");
    }

    // ── delete ────────────────────────────────────────────────────────────────

    @Test
    public void testDelete() {
        createServerMock("DELETE", "/test-steps/165", 200, "{}");
        TestStep.delete("165"); // should not throw
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testDeleteWithError() {
        HttpRequestWithBody deleteRequest = (HttpRequestWithBody) createServerMock("DELETE", "/test-steps/165", 200, "{}");
        when(deleteRequest.asJson()).thenThrow(UnirestException.class);
        TestStep.delete("165");
    }

    // ── setDescription / setExpectedResult ────────────────────────────────────

    @Test
    public void testSetDescription() {
        TestStep ts = new TestStep("https://localhost:4321/test-steps/1", "action-step", 1, 0, "result", "old action");
        ts.setDescription("new action");
        Assert.assertEquals(ts.getAction(), "new action");
    }

    @Test
    public void testSetExpectedResult() {
        TestStep ts = new TestStep("https://localhost:4321/test-steps/1", "action-step", 1, 0, "old result", "action");
        ts.setExpectedResult("new result");
        Assert.assertEquals(ts.getExpectedResult(), "new result");
    }

    // ── uploadAttachment ──────────────────────────────────────────────────────

    @Test
    public void testUploadAttachment() throws IOException {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/test-steps/165/attachments", 200, "{}", "request");

        File tempFile = Files.createTempFile("attachment", ".txt").toFile();
        tempFile.deleteOnExit();

        TestStep ts = new TestStep("https://localhost:4321/test-steps/165", "action-step", 165, 0, "", "");
        ts.uploadAttachment(tempFile, 165);

        verify(postRequest).field(eq("files"), eq(tempFile));
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testUploadAttachmentWithError() throws IOException {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/test-steps/165/attachments", 200, "{}", "request");
        when(postRequest.field(eq("files"), any(File.class))).thenThrow(UnirestException.class);

        File tempFile = Files.createTempFile("attachment", ".txt").toFile();
        tempFile.deleteOnExit();

        TestStep ts = new TestStep("https://localhost:4321/test-steps/165", "action-step", 165, 0, "", "");
        ts.uploadAttachment(tempFile, 165);
    }

    // ── constructeur ──────────────────────────────────────────────────────────

    @Test
    public void testConstructor() {
        TestStep ts = new TestStep("https://localhost:4321/test-steps/99", "action-step", 99, 3, "<p>expected</p>", "<p>do something</p>");
        Assert.assertEquals(ts.getId(), 99);
        Assert.assertEquals(ts.getOrder(), 3);
        Assert.assertEquals(ts.getAction(), "<p>do something</p>");
        Assert.assertEquals(ts.getExpectedResult(), "<p>expected</p>");
        Assert.assertEquals(ts.getUrl(), "https://localhost:4321/test-steps/99");
        Assert.assertEquals(ts.getType(), "action-step");
    }

    // ── fromJsonOne – champs optionnels absents ───────────────────────────────

    @Test
    public void testFromJsonOneWithoutOptionalFields() {
        TestStep step = TestStep.fromJsonOne(new JSONObject("{"
                + "\"_type\":\"action-step\","
                + "\"id\":200,"
                + "\"index\":1,"
                + "\"_links\":{\"self\":{\"href\":\"https://localhost:4321/test-steps/200\"}}"
                + "}"));
        Assert.assertEquals(step.getId(), 200);
        Assert.assertEquals(step.getOrder(), 1);
        Assert.assertEquals(step.getAction(), "");
        Assert.assertEquals(step.getExpectedResult(), "");
    }

    // ── fromJson – JSON invalide (branche JSONException) ─────────────────────

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonInvalidJson() {
        // JSON sans le champ obligatoire "_type" → JSONException
        TestStep.fromJson(new JSONObject("{\"id\":1}"));
    }
}
