package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TestCustomField extends SquashTMTest {

    @BeforeMethod
    public void init() {
        CustomField.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testFromJson() {
        JSONObject json = new JSONObject();
        json.put("_links", new JSONObject().put("self", new JSONObject().put("href", "http://example.com")));
        json.put("_type", "type");
        json.put("id", 1);
        json.put("name", "name");
        json.put("label", "label");
        json.put("code", "code");

        CustomField cf = CustomField.fromJson(json);
        Assert.assertEquals("https://example.com", cf.getUrl());
        Assert.assertEquals("type", cf.getType());
        Assert.assertEquals(1, cf.getId());
        Assert.assertEquals("name", cf.getName());
        Assert.assertEquals("label", cf.getLabel());
        Assert.assertEquals("code", cf.getCode());
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonMissingData() {
        JSONObject json = new JSONObject();
        json.put("_links", new JSONObject().put("self", new JSONObject().put("href", "http://example.com")));
        json.put("_type", "type");
        json.put("name", "name");
        json.put("label", "label");
        json.put("code", "code");

        CustomField cf = CustomField.fromJson(json);
    }

    @Test
    public void testGetByCode() {
        createServerMock("GET", "/custom-fields", 200, "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"custom-fields\" : [ {\n" +
                "      \"_type\" : \"custom-field\",\n" +
                "      \"id\" : 1,\n" +
                "      \"name\" : \"checkbox1\",\n" +
                "      \"code\" : \"ck1\",\n" +
                "      \"label\" : \"ck1\",\n" +
                "      \"optional\" : false,\n" +
                "      \"options\" : [ ],\n" +
                "      \"input_type\" : \"CHECKBOX\",\n" +
                "      \"default_value\" : \"true\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/custom-fields/1\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"custom-field\",\n" +
                "      \"id\" : 2,\n" +
                "      \"name\" : \"checkbox2\",\n" +
                "      \"code\" : \"ck2\",\n" +
                "      \"label\" : \"ck2\",\n" +
                "      \"optional\" : false,\n" +
                "      \"options\" : [ ],\n" +
                "      \"input_type\" : \"CHECKBOX\",\n" +
                "      \"default_value\" : \"true\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/custom-fields/2\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/custom-fields?page=0&size=20\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"page\" : {\n" +
                "    \"size\" : 20,\n" +
                "    \"totalElements\" : 2,\n" +
                "    \"totalPages\" : 1,\n" +
                "    \"number\" : 0\n" +
                "  }\n" +
                "}");
        CustomField customField = CustomField.getByCode("ck2");
        Assert.assertEquals(customField.getId(), 2);
    }

    @Test
    public void testGetByCodeNotPresent() {
        createServerMock("GET", "/custom-fields", 200, "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"custom-fields\" : [ {\n" +
                "      \"_type\" : \"custom-field\",\n" +
                "      \"id\" : 1,\n" +
                "      \"name\" : \"checkbox1\",\n" +
                "      \"code\" : \"ck1\",\n" +
                "      \"label\" : \"ck1\",\n" +
                "      \"optional\" : false,\n" +
                "      \"options\" : [ ],\n" +
                "      \"input_type\" : \"CHECKBOX\",\n" +
                "      \"default_value\" : \"true\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/custom-fields/1\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/custom-fields?page=0&size=20\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"page\" : {\n" +
                "    \"size\" : 20,\n" +
                "    \"totalElements\" : 2,\n" +
                "    \"totalPages\" : 1,\n" +
                "    \"number\" : 0\n" +
                "  }\n" +
                "}");
        CustomField customField = CustomField.getByCode("ck2");
        Assert.assertNull(customField);
    }

    @Test
    public void testGetAll() {
        createServerMock("GET", "/custom-fields", 200, "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"custom-fields\" : [ {\n" +
                "      \"_type\" : \"custom-field\",\n" +
                "      \"id\" : 1,\n" +
                "      \"name\" : \"checkbox1\",\n" +
                "      \"code\" : \"ck1\",\n" +
                "      \"label\" : \"ck1\",\n" +
                "      \"optional\" : false,\n" +
                "      \"options\" : [ ],\n" +
                "      \"input_type\" : \"CHECKBOX\",\n" +
                "      \"default_value\" : \"true\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/custom-fields/1\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"custom-field\",\n" +
                "      \"id\" : 2,\n" +
                "      \"name\" : \"checkbox2\",\n" +
                "      \"code\" : \"ck2\",\n" +
                "      \"label\" : \"ck2\",\n" +
                "      \"optional\" : false,\n" +
                "      \"options\" : [ ],\n" +
                "      \"input_type\" : \"CHECKBOX\",\n" +
                "      \"default_value\" : \"true\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/custom-fields/2\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/custom-fields?page=0&size=20\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"page\" : {\n" +
                "    \"size\" : 20,\n" +
                "    \"totalElements\" : 2,\n" +
                "    \"totalPages\" : 1,\n" +
                "    \"number\" : 0\n" +
                "  }\n" +
                "}");
        List<CustomField> customFields = CustomField.getAll();
        Assert.assertEquals(customFields.size(), 2);
        Assert.assertEquals(customFields.get(0).getName(), "checkbox1");
        Assert.assertEquals(customFields.get(0).getCode(), "ck1");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetAllWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/custom-fields", 200, "{}", "requestBodyEntity");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);

        List<CustomField> customFields = CustomField.getAll();
    }
}

