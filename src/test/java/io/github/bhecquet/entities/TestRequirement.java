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

public class TestRequirement extends SquashTMTest {

    public static final String REQUIREMENT_REPLY_DATA = "{\n" +
            "  \"_type\" : \"requirement\",\n" +
            "  \"id\" : 624,\n" +
            "  \"name\" : \"sample requirement 62-4\",\n" +
            "  \"project\" : {\n" +
            "    \"_type\" : \"project\",\n" +
            "    \"id\" : 44,\n" +
            "    \"name\" : \"sample project\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/projects/44\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"path\" : \"/sample project/domain 1/sample requirement 62-4\",\n" +
            "  \"parent\" : {\n" +
            "    \"_type\" : \"requirement-folder\",\n" +
            "    \"id\" : 6,\n" +
            "    \"name\" : \"domain 1\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-folders/6\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"mode\" : \"NATIVE\",\n" +
            "  \"remote_req_id\" : \"null\",\n" +
            "  \"remote_req_url\" : \"null\",\n" +
            "  \"remote_req_perimeter_status\" : \"null\",\n" +
            "  \"current_version\" : {\n" +
            "    \"_type\" : \"requirement-version\",\n" +
            "    \"id\" : 98,\n" +
            "    \"name\" : \"sample requirement 62-4\",\n" +
            "    \"reference\" : \"REQ01\",\n" +
            "    \"version_number\" : 3,\n" +
            "    \"created_by\" : \"User-1\",\n" +
            "    \"created_on\" : \"2017-07-17T10:00:00.000+00:00\",\n" +
            "    \"last_modified_by\" : \"User-1\",\n" +
            "    \"last_modified_on\" : \"2017-07-17T10:00:00.000+00:00\",\n" +
            "    \"criticality\" : \"MAJOR\",\n" +
            "    \"category\" : {\n" +
            "      \"code\" : \"CAT_FUNCTIONAL\"\n" +
            "    },\n" +
            "    \"status\" : \"WORK_IN_PROGRESS\",\n" +
            "    \"description\" : \"<p>Description of the sample requirement.</p>\",\n" +
            "    \"custom_fields\" : [ {\n" +
            "      \"code\" : \"CF_TXT\",\n" +
            "      \"label\" : \"cuf text\",\n" +
            "      \"value\" : \"text value\"\n" +
            "    }, {\n" +
            "      \"code\" : \"CF_TAG\",\n" +
            "      \"label\" : \"cuf tag\",\n" +
            "      \"value\" : [ \"tag_1\", \"tag_2\" ]\n" +
            "    } ],\n" +
            "    \"verifying_test_cases\" : [ {\n" +
            "      \"_type\" : \"test-case\",\n" +
            "      \"id\" : 100,\n" +
            "      \"name\" : \"sample test case 1\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"https://localhost:4321/test-cases/100\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"_type\" : \"scripted-test-case\",\n" +
            "      \"id\" : 102,\n" +
            "      \"name\" : \"sample scripted test case 2\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"https://localhost:4321/test-cases/102\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"_type\" : \"keyword-test-case\",\n" +
            "      \"id\" : 103,\n" +
            "      \"name\" : \"sample keyword test case 3\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"https://localhost:4321/test-cases/103\"\n" +
            "        }\n" +
            "      }\n" +
            "    } ],\n" +
            "    \"attachments\" : [ ],\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/98\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"versions\" : [ {\n" +
            "    \"_type\" : \"requirement-version\",\n" +
            "    \"id\" : 78,\n" +
            "    \"name\" : \"sample requirement 98-1\",\n" +
            "    \"version_number\" : 1,\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/78\"\n" +
            "      }\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"_type\" : \"requirement-version\",\n" +
            "    \"id\" : 88,\n" +
            "    \"name\" : \"sample requirement 98-2\",\n" +
            "    \"version_number\" : 2,\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/88\"\n" +
            "      }\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"_type\" : \"requirement-version\",\n" +
            "    \"id\" : 98,\n" +
            "    \"name\" : \"sample requirement 62-4\",\n" +
            "    \"version_number\" : 3,\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/98\"\n" +
            "      }\n" +
            "    }\n" +
            "  } ],\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirements/624\"\n" +
            "    },\n" +
            "    \"project\" : {\n" +
            "      \"href\" : \"https://localhost:4321/projects/44\"\n" +
            "    },\n" +
            "    \"current_version\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-versions/98\"\n" +
            "    },\n" +
            "    \"issues\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirements/624/issues\"\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public static final String REQUIREMENT_REPLY_DATA2 = "{\n" +
            "  \"_type\" : \"requirement\",\n" +
            "  \"id\" : 666,\n" +
            "  \"name\" : \"sample requirement 66-6\",\n" +
            "  \"project\" : {\n" +
            "    \"_type\" : \"project\",\n" +
            "    \"id\" : 44,\n" +
            "    \"name\" : \"sample project\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/projects/44\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"path\" : \"/sample project/domain 1/sample requirement 66-6\",\n" +
            "  \"parent\" : {\n" +
            "    \"_type\" : \"requirement-folder\",\n" +
            "    \"id\" : 6,\n" +
            "    \"name\" : \"domain 1\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-folders/6\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"mode\" : \"NATIVE\",\n" +
            "  \"remote_req_id\" : \"null\",\n" +
            "  \"remote_req_url\" : \"null\",\n" +
            "  \"remote_req_perimeter_status\" : \"null\",\n" +
            "  \"current_version\" : {\n" +
            "    \"_type\" : \"requirement-version\",\n" +
            "    \"id\" : 98,\n" +
            "    \"name\" : \"sample requirement 62-4\",\n" +
            "    \"reference\" : \"REQ01\",\n" +
            "    \"version_number\" : 3,\n" +
            "    \"created_by\" : \"User-1\",\n" +
            "    \"created_on\" : \"2017-07-17T10:00:00.000+00:00\",\n" +
            "    \"last_modified_by\" : \"User-1\",\n" +
            "    \"last_modified_on\" : \"2017-07-17T10:00:00.000+00:00\",\n" +
            "    \"criticality\" : \"MAJOR\",\n" +
            "    \"category\" : {\n" +
            "      \"code\" : \"CAT_FUNCTIONAL\"\n" +
            "    },\n" +
            "    \"status\" : \"WORK_IN_PROGRESS\",\n" +
            "    \"description\" : \"<p>Description of the sample requirement.</p>\",\n" +
            "    \"custom_fields\" : [ {\n" +
            "      \"code\" : \"CF_TXT\",\n" +
            "      \"label\" : \"cuf text\",\n" +
            "      \"value\" : \"text value\"\n" +
            "    }, {\n" +
            "      \"code\" : \"CF_TAG\",\n" +
            "      \"label\" : \"cuf tag\",\n" +
            "      \"value\" : [ \"tag_1\", \"tag_2\" ]\n" +
            "    } ],\n" +
            "    \"verifying_test_cases\" : [ {\n" +
            "      \"_type\" : \"test-case\",\n" +
            "      \"id\" : 100,\n" +
            "      \"name\" : \"sample test case 1\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"https://localhost:4321/test-cases/100\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"_type\" : \"scripted-test-case\",\n" +
            "      \"id\" : 102,\n" +
            "      \"name\" : \"sample scripted test case 2\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"https://localhost:4321/test-cases/102\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"_type\" : \"keyword-test-case\",\n" +
            "      \"id\" : 103,\n" +
            "      \"name\" : \"sample keyword test case 3\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"https://localhost:4321/test-cases/103\"\n" +
            "        }\n" +
            "      }\n" +
            "    } ],\n" +
            "    \"attachments\" : [ ],\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/98\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"versions\" : [ {\n" +
            "    \"_type\" : \"requirement-version\",\n" +
            "    \"id\" : 78,\n" +
            "    \"name\" : \"sample requirement 98-1\",\n" +
            "    \"version_number\" : 1,\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/78\"\n" +
            "      }\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"_type\" : \"requirement-version\",\n" +
            "    \"id\" : 88,\n" +
            "    \"name\" : \"sample requirement 98-2\",\n" +
            "    \"version_number\" : 2,\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/88\"\n" +
            "      }\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"_type\" : \"requirement-version\",\n" +
            "    \"id\" : 98,\n" +
            "    \"name\" : \"sample requirement 62-4\",\n" +
            "    \"version_number\" : 3,\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/98\"\n" +
            "      }\n" +
            "    }\n" +
            "  } ],\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirements/624\"\n" +
            "    },\n" +
            "    \"project\" : {\n" +
            "      \"href\" : \"https://localhost:4321/projects/44\"\n" +
            "    },\n" +
            "    \"current_version\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-versions/98\"\n" +
            "    },\n" +
            "    \"issues\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirements/624/issues\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @BeforeMethod
    public void init() {
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testExists() {
        createServerMock("GET", "/requirements/624", 200, REQUIREMENT_REPLY_DATA);
        Assert.assertEquals(Requirement.get(624).getId(), 624);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testNotExist() {
        createServerMock("GET", "/requirements/625", 404, "{" +
                "    \"exception\": \"javax.persistence.EntityNotFoundException\"," +
                "    \"message\": \"Unable to find org.squashtest.tm.domain.testcase.TestCase with id 23\"" +
                "}");
        Requirement.get(625);
    }

    /**
     * Error retrieving requirement
     */
    @Test(expectedExceptions = SquashTmException.class)
    public void testExistsInError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/requirements/624", 200, "{}", "requestBodyEntity");
        when(getRequest.asJson()).thenThrow(UnirestException.class);

        Requirement.get(624);
    }

    @Test
    public void testFromJson() {

        JSONObject json = new JSONObject();
        json.put("_type", "test-case");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/test-case/1\"" +
                "        }}"));

        TestCase testCase = TestCase.fromJson(json);
        Assert.assertEquals(testCase.getId(), 1);
        Assert.assertEquals(testCase.getUrl(), "https://localhost:4321/test-case/1");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {

        JSONObject json = new JSONObject();
        json.put("_type", "test-case");
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/test-case/1\"" +
                "        }}"));

        Requirement.fromJson(json);
    }

    @Test
    public void testCompleteDetails() {
        createServerMock("GET", "/requirements/624", 200, REQUIREMENT_REPLY_DATA);
        createServerMock("GET", "/requirements/666", 200, REQUIREMENT_REPLY_DATA2);
        createServerMock("GET", "/requirements/667", 200, REQUIREMENT_REPLY_DATA2);

        Requirement req = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        req.completeDetails();

        Assert.assertEquals(req.getId(), 624);
        Assert.assertEquals(req.getPath(), "/sample project/domain 1/sample requirement 62-4");
        Assert.assertEquals(req.getDescription(), "<p>Description of the sample requirement.</p>");
        Assert.assertEquals(req.getProjectId(), 44);
        Assert.assertEquals(req.getProjectName(), "sample project");
        Assert.assertEquals(req.getAuthor(), "User-1");
        Assert.assertEquals(req.getReference(), "REQ01");
        Assert.assertEquals(req.getStatus(), "WORK_IN_PROGRESS");
        Assert.assertEquals(req.getCriticality(), "MAJOR");
        Assert.assertEquals(req.getCustomFields().size(), 2);
        Assert.assertEquals(req.getCustomFields().get(0).getCode(), "CF_TXT");
        Assert.assertEquals(req.getName(), "sample requirement 62-4");
        Assert.assertEquals(req.getCategory(), "CAT_FUNCTIONAL");
    }

    @Test
    public void testUpdateCustomField() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirements/624", 200, REQUIREMENT_REPLY_DATA);

        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        requirement.updateCustomField("foo", "bar");

        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement\",\"custom_fields\":[{\"value\":\"bar\",\"code\":\"foo\"}]}"));
    }

    @Test
    public void testUpdateCustomFields() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirements/624", 200, REQUIREMENT_REPLY_DATA);

        TestCase testCase = new TestCase("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        testCase.updateCustomFields(Map.of("foo", "bar", "foo2", "bar2"));
        ArgumentCaptor<JSONObject> jsonArgument = ArgumentCaptor.forClass(JSONObject.class);
        verify(patchRequest).body(jsonArgument.capture());

        // check all custom fields have been sent
        Assert.assertEquals(jsonArgument.getValue().getJSONArray("custom_fields").length(), 2);
    }

    @Test
    public void testUpdateCustomFieldsWithTags() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirements/624", 200, REQUIREMENT_REPLY_DATA);

        TestCase testCase = new TestCase("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        testCase.updateCustomFields(Map.of("tags", List.of("tag1", "tag2")));

        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement\",\"custom_fields\":[{\"code\":\"tags\",\"value\":[\"tag1\",\"tag2\"]}]}"));
    }

}
