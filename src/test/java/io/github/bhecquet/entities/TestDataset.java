package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;

public class TestDataset extends SquashTMTest {

    @BeforeMethod
    public void init() {
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void datasetExists() {
        createServerMock("GET", "/datasets/7", 200, "{\n" +
                "  \"_type\" : \"dataset\",\n" +
                "  \"id\" : 7,\n" +
                "  \"name\" : \"sample dataset\",\n" +
                "  \"parameters\" : [ {\n" +
                "    \"_type\" : \"parameter\",\n" +
                "    \"id\" : 1,\n" +
                "    \"name\" : \"param_1\"\n" +
                "  }, {\n" +
                "    \"_type\" : \"parameter\",\n" +
                "    \"id\" : 2,\n" +
                "    \"name\" : \"param_2\"\n" +
                "  } ],\n" +
                "  \"parameter_values\" : [ {\n" +
                "    \"parameter_test_case_id\" : 9,\n" +
                "    \"parameter_value\" : \"login_1\",\n" +
                "    \"parameter_name\" : \"param_1\",\n" +
                "    \"parameter_id\" : 1\n" +
                "  }, {\n" +
                "    \"parameter_test_case_id\" : 9,\n" +
                "    \"parameter_value\" : \"password_1\",\n" +
                "    \"parameter_name\" : \"param_2\",\n" +
                "    \"parameter_id\" : 2\n" +
                "  } ],\n" +
                "  \"test_case\" : {\n" +
                "    \"_type\" : \"test-case\",\n" +
                "    \"id\" : 9,\n" +
                "    \"name\" : \"login test\",\n" +
                "    \"_links\" : {\n" +
                "      \"self\" : {\n" +
                "        \"href\" : \"https://localhost:4321/test-cases/9\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/datasets/7\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
        Assert.assertEquals(Dataset.get(7).getId(), 7);
    }

    /**
     * Test case does not exist
     */
    @Test(expectedExceptions = SquashTmException.class)
    public void datasetNotExist() {
        createServerMock("GET", "/datasets/12", 404, "{\r\n" +
                "    \"exception\": \"javax.persistence.EntityNotFoundException\",\r\n" +
                "    \"message\": \"The dataset with id : 12 do not exist\"\r\n" +
                "}");
        Dataset.get(12);
    }

    /**
     * Error retrieving test case
     */
    @Test(expectedExceptions = SquashTmException.class)
    public void datasetExistsInError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/datasets/12", 200, "{}", "requestBodyEntity");
        when(getRequest.asJson()).thenThrow(UnirestException.class);

        Dataset.get(12);
    }

    /**
     * Test when dataset information is returned from iteration test plan item. Only ID and NAME are returned
     */
    @Test
    public void datasetFromSimpleJson() {

        JSONObject json = new JSONObject();
        json.put("_type", "dataset");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/datasets/1\"" +
                "        }}"));

        Dataset dataset = Dataset.fromJson(json);
        Assert.assertEquals(dataset.getId(), 1);
        Assert.assertEquals(dataset.getUrl(), "https://localhost:4321/datasets/1");
    }

    @Test
    public void datasetFromCompleteJson() {

        JSONObject json = new JSONObject("{\n" +
                "  \"_type\" : \"dataset\",\n" +
                "  \"id\" : 7,\n" +
                "  \"name\" : \"sample dataset\",\n" +
                "  \"parameters\" : [ {\n" +
                "    \"_type\" : \"parameter\",\n" +
                "    \"id\" : 1,\n" +
                "    \"name\" : \"param_1\"\n" +
                "  }, {\n" +
                "    \"_type\" : \"parameter\",\n" +
                "    \"id\" : 2,\n" +
                "    \"name\" : \"param_2\"\n" +
                "  } ],\n" +
                "  \"parameter_values\" : [ {\n" +
                "    \"parameter_test_case_id\" : 9,\n" +
                "    \"parameter_value\" : \"login_1\",\n" +
                "    \"parameter_name\" : \"param_1\",\n" +
                "    \"parameter_id\" : 1\n" +
                "  }, {\n" +
                "    \"parameter_test_case_id\" : 9,\n" +
                "    \"parameter_value\" : \"password_1\",\n" +
                "    \"parameter_name\" : \"param_2\",\n" +
                "    \"parameter_id\" : 2\n" +
                "  } ],\n" +
                "  \"test_case\" : {\n" +
                "    \"_type\" : \"test-case\",\n" +
                "    \"id\" : 9,\n" +
                "    \"name\" : \"login test\",\n" +
                "    \"_links\" : {\n" +
                "      \"self\" : {\n" +
                "        \"href\" : \"https://localhost:4321/test-cases/9\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/datasets/7\"\n" +
                "    }\n" +
                "  }\n" +
                "}");


        Dataset dataset = Dataset.fromJson(json);
        Assert.assertEquals(dataset.getId(), 7);
        Assert.assertEquals(dataset.getName(), "sample dataset");
        Assert.assertEquals(dataset.getUrl(), "https://localhost:4321/datasets/7");
        Assert.assertNotNull(dataset.getTestCase());
        Assert.assertEquals(dataset.getTestCase().getId(), 9);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {

        JSONObject json = new JSONObject();
        json.put("_type", "dataset");
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/datasets/1\"" +
                "        }}"));

        Dataset.fromJson(json);
    }

    @Test(expectedExceptions = NotImplementedException.class)
    public void testCompleteDetails() {
        Dataset ds = new Dataset("osef", "ouais", 1, "yolo");
        ds.completeDetails();
    }

}
