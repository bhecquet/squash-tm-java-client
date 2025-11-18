package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpRequestWithBody;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestRequirementVersion extends SquashTMTest {

    public static final String REQUIREMENT_VERSION_REPLY_DATA = "{\n" +
            "  \"_type\" : \"requirement-version\",\n" +
            "  \"id\" : 3,\n" +
            "  \"name\" : \"sample requirement version\",\n" +
            "  \"reference\" : \"SAMP_REQ_VER\",\n" +
            "  \"version_number\" : 2,\n" +
            "  \"requirement\" : {\n" +
            "    \"_type\" : \"requirement\",\n" +
            "    \"id\" : 64,\n" +
            "    \"name\" : \"sample requirement\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"http://localhost:8080/api/rest/latest/requirements/64\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"created_by\" : \"User-1\",\n" +
            "  \"created_on\" : \"2017-07-19T10:00:00.000+00:00\",\n" +
            "  \"last_modified_by\" : \"User-2\",\n" +
            "  \"last_modified_on\" : \"2017-07-20T10:00:00.000+00:00\",\n" +
            "  \"criticality\" : \"CRITICAL\",\n" +
            "  \"category\" : {\n" +
            "    \"code\" : \"CAT_PERFORMANCE\"\n" +
            "  },\n" +
            "  \"status\" : \"APPROVED\",\n" +
            "  \"description\" : \"<p>Approved performance requirement-version</p>\",\n" +
            "  \"custom_fields\" : [ {\n" +
            "    \"code\" : \"CUF1\",\n" +
            "    \"label\" : \"Cuf One\",\n" +
            "    \"value\" : \"value_1\"\n" +
            "  }, {\n" +
            "    \"code\" : \"CUF2\",\n" +
            "    \"label\" : \"Cuf Two\",\n" +
            "    \"value\" : \"value_2\"\n" +
            "  } ],\n" +
            "  \"verifying_test_cases\" : [ {\n" +
            "    \"_type\" : \"test-case\",\n" +
            "    \"id\" : 4,\n" +
            "    \"name\" : \"verifying test case 1\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"http://localhost:8080/api/rest/latest/test-cases/4\"\n" +
            "      }\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"_type\" : \"scripted-test-case\",\n" +
            "    \"id\" : 9,\n" +
            "    \"name\" : \"verifying scripted test case 2\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"http://localhost:8080/api/rest/latest/test-cases/9\"\n" +
            "      }\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"_type\" : \"keyword-test-case\",\n" +
            "    \"id\" : 14,\n" +
            "    \"name\" : \"verifying keyword test case 3\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"http://localhost:8080/api/rest/latest/test-cases/14\"\n" +
            "      }\n" +
            "    }\n" +
            "  } ],\n" +
            "  \"milestones\" : [ {\n" +
            "    \"id\" : 1,\n" +
            "    \"label\" : \"milestone 1\"\n" +
            "  } ],\n" +
            "  \"attachments\" : [ ],\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/requirement-versions/3\"\n" +
            "    },\n" +
            "    \"project\" : {\n" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/projects/85\"\n" +
            "    },\n" +
            "    \"requirement\" : {\n" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/requirements/64\"\n" +
            "    },\n" +
            "    \"attachments\" : {\n" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/requirement-versions/3/attachments\"\n" +
            "    },\n" +
            "    \"issues\" : {\n" +
            "      \"href\" : \"http://localhost:8080/api/rest/latest/requirement-versions/3/issues\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @BeforeMethod
    public void init() {
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testExists() {
        createServerMock("GET", "/requirement-versions/3", 200, REQUIREMENT_VERSION_REPLY_DATA);
        Assert.assertEquals(RequirementVersion.get(3).getId(), 3);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testNotExist() {
        createServerMock("GET", "/requirement-versions/3", 404, "{" +
                "    \"exception\": \"javax.persistence.EntityNotFoundException\"," +
                "    \"message\": \"Unable to find org.squashtest.tm.domain.testcase.TestCase with id 3\"" +
                "}");
        RequirementVersion.get(3);
    }

    /**
     * Error retrieving requirement
     */
    @Test(expectedExceptions = SquashTmException.class)
    public void testExistsInError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/requirement-versions/3", 200, "{}", "requestBodyEntity");
        when(getRequest.asJson()).thenThrow(UnirestException.class);

        RequirementVersion.get(3);
    }

    @Test
    public void testFromJson() {

        JSONObject json = new JSONObject();
        json.put("_type", "requirement-version");
        json.put("id", 6);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/requirement-versions/6\"" +
                "        }}"));

        RequirementVersion reqver = RequirementVersion.fromJson(json);
        Assert.assertEquals(reqver.getId(), 6);
        Assert.assertEquals(reqver.getUrl(), "https://localhost:4321/requirement-versions/6");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {

        JSONObject json = new JSONObject();
        json.put("_type", "requirement-version");
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/requirement-versions/1\"" +
                "        }}"));

        RequirementVersion.fromJson(json);
    }

    @Test
    public void testCompleteDetails() {
        createServerMock("GET", "/requirement-versions/3", 200, REQUIREMENT_VERSION_REPLY_DATA);

        RequirementVersion reqver = new RequirementVersion("https://localhost:4321/requirement-versions/3", "requirement-version", 3, "sample requirement");
        reqver.completeDetails();

        Assert.assertEquals(reqver.getId(), 3);
        Assert.assertEquals(reqver.getDescription(), "<p>Approved performance requirement-version</p>");
        Assert.assertEquals(reqver.getRequirementId(), 64);
        Assert.assertEquals(reqver.getRequirementName(), "sample requirement");
        Assert.assertEquals(reqver.getReference(), "SAMP_REQ_VER");
        Assert.assertEquals(reqver.getStatus(), "APPROVED");
        Assert.assertEquals(reqver.getCriticality(), "CRITICAL");
        Assert.assertEquals(reqver.getCustomFields().size(), 2);
        Assert.assertEquals(reqver.getCustomFields().get(0).getCode(), "CUF1");
        Assert.assertEquals(reqver.getName(), "sample requirement version");
        Assert.assertEquals(reqver.getCategory(), "CAT_PERFORMANCE");
    }

    @Test
    public void testUpdateCustomField() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirement-versions/6", 200, REQUIREMENT_VERSION_REPLY_DATA);

        Requirement requirement = new Requirement("https://localhost:4321/requirement-versions/6", "requirement-version", 6, "sample requirement version");
        requirement.updateCustomField("foo", "bar");

        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement-version\",\"custom_fields\":[{\"value\":\"bar\",\"code\":\"foo\"}]}"));
    }

    @Test
    public void testUpdateCustomFields() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirement-versions/6", 200, REQUIREMENT_VERSION_REPLY_DATA);

        RequirementVersion reqver = new RequirementVersion("https://localhost:4321/requirement-versions/6", "requirement-version", 6, "sample requirement version");
        reqver.updateCustomFields(Map.of("foo", "bar", "foo2", "bar2"));
        ArgumentCaptor<JSONObject> jsonArgument = ArgumentCaptor.forClass(JSONObject.class);
        verify(patchRequest).body(jsonArgument.capture());

        // check all custom fields have been sent
        Assert.assertEquals(jsonArgument.getValue().getJSONArray("custom_fields").length(), 2);
    }

    @Test
    public void testUpdateCustomFieldsWithTags() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirement-versions/6", 200, REQUIREMENT_VERSION_REPLY_DATA);

        RequirementVersion reqver = new RequirementVersion("https://localhost:4321/requirement-versions/6", "requirement-version", 6, "sample requirement version");
        reqver.updateCustomFields(Map.of("tags", List.of("tag1", "tag2")));

        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement-version\",\"custom_fields\":[{\"code\":\"tags\",\"value\":[\"tag1\",\"tag2\"]}]}"));
    }

    @Test
    public void testGetCoveringTestCases() {
        createServerMock("GET", "/requirement-versions/3", 200, REQUIREMENT_VERSION_REPLY_DATA);

        RequirementVersion reqver = new RequirementVersion("https://localhost:4321/requirement-versions/3", "requirement-version", 3, "sample requirement");
        List<TestCase> coveringTestCases = reqver.getCoveringTestCases();
        Assert.assertEquals(coveringTestCases.size(), 3);
        Assert.assertEquals(coveringTestCases.get(0).getId(), 4);
        Assert.assertEquals(coveringTestCases.get(1).getName(), "verifying scripted test case 2");
        Assert.assertEquals(coveringTestCases.get(2).getType(), "keyword-test-case");
    }

}
