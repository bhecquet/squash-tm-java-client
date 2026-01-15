package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpRequestWithBody;
import kong.unirest.core.RequestBodyEntity;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    // reply when creating a requirement
    public static final String REQUIREMENT_REPLY_DATA3 = "{\n" +
            "  \"_type\" : \"requirement\",\n" +
            "  \"id\" : 456,\n" +
            "  \"name\" : \"new age\",\n" +
            "  \"project\" : {\n" +
            "    \"_type\" : \"project\",\n" +
            "    \"id\" : 15,\n" +
            "    \"name\" : \"Winter will be gone\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"http://localhost:4321/api/rest/latest/projects/15\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"parent\" : {\n" +
            "    \"_type\" : \"requirement-folder\",\n" +
            "    \"id\" : 300,\n" +
            "    \"name\" : \"root-level folder\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"http://localhost:4321/api/rest/latest/requirement-folders/300\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"mode\" : \"NATIVE\",\n" +
            "  \"current_version\" : {\n" +
            "    \"_type\" : \"requirement-version\",\n" +
            "    \"id\" : 333,\n" +
            "    \"name\" : \"new age\",\n" +
            "    \"reference\" : \"SAMP_REQ_VER\",\n" +
            "    \"version_number\" : 1,\n" +
            "    \"created_by\" : \"admin\",\n" +
            "    \"created_on\" : \"2017-06-15T10:00:00.000+00:00\",\n" +
            "    \"last_modified_by\" : \"admin\",\n" +
            "    \"last_modified_on\" : \"2017-06-15T10:00:00.000+00:00\",\n" +
            "    \"criticality\" : \"MINOR\",\n" +
            "    \"category\" : {\n" +
            "      \"code\" : \"CAT_USER_STORY\"\n" +
            "    },\n" +
            "    \"status\" : \"UNDER_REVIEW\",\n" +
            "    \"description\" : \"<p>leave a comment please</p>\",\n" +
            "    \"custom_fields\" : [ {\n" +
            "      \"code\" : \"cuf_txt_note\",\n" +
            "      \"label\" : \"note\",\n" +
            "      \"value\" : \"Star Trek style welcomed but not mandatory\"\n" +
            "    }, {\n" +
            "      \"code\" : \"cuf_tags_see_also\",\n" +
            "      \"label\" : \"see also\",\n" +
            "      \"value\" : [ \"smart home\", \"sensors\", \"hand gesture\" ]\n" +
            "    } ],\n" +
            "    \"verifying_test_cases\" : [ ],\n" +
            "    \"milestones\" : [ ],\n" +
            "    \"attachments\" : [ ],\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"http://localhost:4321/api/rest/latest/requirement-versions/333\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"versions\" : [ {\n" +
            "    \"_type\" : \"requirement-version\",\n" +
            "    \"id\" : 333,\n" +
            "    \"name\" : \"new age\",\n" +
            "    \"version_number\" : 1,\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"http://localhost:4321/api/rest/latest/requirement-versions/333\"\n" +
            "      }\n" +
            "    }\n" +
            "  } ],\n" +
            "  \"remote_req_id\" : \"null\",\n" +
            "  \"remote_req_url\" : \"null\",\n" +
            "  \"remote_req_perimeter_status\" : \"null\",\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"http://localhost:4321/api/rest/latest/requirements/456\"\n" +
            "    },\n" +
            "    \"project\" : {\n" +
            "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/15\"\n" +
            "    },\n" +
            "    \"current_version\" : {\n" +
            "      \"href\" : \"http://localhost:4321/api/rest/latest/requirement-versions/333\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private Project project;

    @Mock
    private RequirementFolder requirementFolder;

    @BeforeMethod
    public void init() {
        project = spy(new Project("https://localhost:4321/projects/1", "project", 1, "project"));
        Requirement.configureEntity("user", "pwd", SERVER_URL + "/");
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
        json.put("_type", "requirement");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/requirement/1\"" +
                "        }}"));

        TestCase testCase = TestCase.fromJson(json);
        Assert.assertEquals(testCase.getId(), 1);
        Assert.assertEquals(testCase.getUrl(), "https://localhost:4321/requirement/1");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {

        JSONObject json = new JSONObject();
        json.put("_type", "requirement");
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/requirements/1\"" +
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

        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        requirement.updateCustomFields(Map.of("foo", "bar", "foo2", "bar2"));
        ArgumentCaptor<JSONObject> jsonArgument = ArgumentCaptor.forClass(JSONObject.class);
        verify(patchRequest).body(jsonArgument.capture());

        // check all custom fields have been sent
        Assert.assertEquals(jsonArgument.getValue().getJSONArray("custom_fields").length(), 2);
    }

    @Test
    public void testUpdateCustomFieldsWithTags() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirements/624", 200, REQUIREMENT_REPLY_DATA);

        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        requirement.updateCustomFields(Map.of("tags", List.of("tag1", "tag2")));

        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement\",\"custom_fields\":[{\"code\":\"tags\",\"value\":[\"tag1\",\"tag2\"]}]}"));
    }

    @Test
    public void testGetCoveringTestCases() {
        createServerMock("GET", "/requirements/624", 200, REQUIREMENT_REPLY_DATA);

        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        List<TestCase> coveringTestCases = requirement.getCoveringTestCases();
        Assert.assertEquals(coveringTestCases.size(), 3);
        Assert.assertEquals(coveringTestCases.get(0).getId(), 100);
        Assert.assertEquals(coveringTestCases.get(1).getName(), "sample scripted test case 2");
        Assert.assertEquals(coveringTestCases.get(2).getType(), "keyword-test-case");
    }

    @Test
    public void testCreateRequirementNoFolder() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirements", 200, REQUIREMENT_REPLY_DATA3, "request");
        Requirement.create(project, "myRequirement", "some description", new HashMap<>(), (RequirementFolder) null, Requirement.Criticality.UNDEFINED);
        verify(postRequest).body(new JSONObject("{\"_type\":\"requirement\",\"parent\":{\"id\":1,\"_type\":\"project\"},\"current_version\":{\"_type\":\"requirement-version\",\"name\":\"myRequirement\",\"criticality\":\"UNDEFINED\",\"description\":\"some description\",\"status\":\"WORK_IN_PROGRESS\"}}"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Requirement name cannot be null or empty")
    public void testCreateRequirementNoName() {
        createServerMock("POST", "/requirements", 200, REQUIREMENT_REPLY_DATA3, "request");
        Requirement.create(project, null, "some description", new HashMap<>(), (RequirementFolder) null, Requirement.Criticality.UNDEFINED);
    }

    @Test
    public void testCreateRequirementWithCustomFields() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirements", 200, REQUIREMENT_REPLY_DATA3, "request");
        Requirement.create(project, "myRequirement", "some description", Map.of("APP", List.of("comp1", "comp2")), (RequirementFolder) null, Requirement.Criticality.UNDEFINED);
        verify(postRequest).body(new JSONObject("{\"_type\":\"requirement\",\"parent\":{\"id\":1,\"_type\":\"project\"},\"current_version\":{\"_type\":\"requirement-version\",\"name\":\"myRequirement\",\"criticality\":\"UNDEFINED\",\"description\":\"some description\",\"status\":\"WORK_IN_PROGRESS\"},\"custom_fields\":[{\"code\":\"APP\",\"value\":[\"comp1\",\"comp2\"]}]}"));
    }

    @Test
    public void testCreateRequirementWithFolder() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirements", 200, REQUIREMENT_REPLY_DATA3, "request");

        RequirementFolder requirementFolder2 = new RequirementFolder(
                "https://localhost:4321/requirement-folders/7",
                "folder",
                7,
                "foo",
                project,
                null);
        Requirement.create(project, "myRequirement", "some description", Map.of("APP", List.of("comp1", "comp2")), requirementFolder2, Requirement.Criticality.UNDEFINED);

        verify(postRequest).body(new JSONObject("{\"_type\":\"requirement\",\"parent\":{\"id\":7,\"_type\":\"requirement-folder\"},\"current_version\":{\"_type\":\"requirement-version\",\"name\":\"myRequirement\",\"criticality\":\"UNDEFINED\",\"description\":\"some description\",\"status\":\"WORK_IN_PROGRESS\"},\"custom_fields\":[{\"code\":\"APP\",\"value\":[\"comp1\",\"comp2\"]}]}"));
    }

    @Test
    public void testCreateRequirementWithFolder2() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirements", 200, REQUIREMENT_REPLY_DATA3, "request");

        try (MockedStatic<RequirementFolder> mockedRequirementFolder = mockStatic(RequirementFolder.class);) {

            mockedRequirementFolder.when(() -> RequirementFolder.createRequirementFolderTree(project, "folder1/folder2")).thenReturn(requirementFolder);
            doReturn(new ArrayList<>()).when(project).getRequirements();

            Requirement.create(project, "myRequirement", "some description", new HashMap<>(), "folder1/folder2", Requirement.Criticality.UNDEFINED);

            verify(postRequest).body(new JSONObject("{\"_type\":\"requirement\",\"parent\":{\"id\":0,\"_type\":\"requirement-folder\"},\"current_version\":{\"_type\":\"requirement-version\",\"name\":\"myRequirement\",\"criticality\":\"UNDEFINED\",\"description\":\"some description\",\"status\":\"WORK_IN_PROGRESS\"}}"));
            mockedRequirementFolder.verify(() -> RequirementFolder.createRequirementFolderTree(eq(project), eq("folder1/folder2")));
        }
    }

    /**
     * If requirement exists do not recreate it
     */
    @Test
    public void testDoNotCreateRequirement() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirements", 200, "{}", "request");

        try (MockedStatic<RequirementFolder> mockedRequirementFolder = mockStatic(RequirementFolder.class);) {

            mockedRequirementFolder.when(() -> RequirementFolder.createRequirementFolderTree(project, "folder1/folder2")).thenReturn(requirementFolder);
            doReturn(List.of(new Requirement("https://localhost:4321/requirements/1", "requirement", 1, "myRequirement", "/project/myRequirement"),
                    new Requirement("https://localhost:4321/requirements/1", "requirement", 2, "myRequirement", "/project/folder1/folder2/myRequirement"))).when(project).getRequirements();

            Requirement requirement = Requirement.create(project, "myRequirement", "some description", new HashMap<>(), "folder1/folder2", Requirement.Criticality.UNDEFINED);
            Assert.assertEquals(requirement.getId(), 2); // check this is the requirement within the requested folder which is chosen
            verify(postRequest, never()).body(any(JSONObject.class));
            mockedRequirementFolder.verify(() -> RequirementFolder.createRequirementFolderTree(eq(project), eq("folder1/folder2")));
        }
    }

    /**
     * If requirement exists do not recreate it
     */
    @Test
    public void testDoNotCreateRequirement2() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirements", 200, "{}", "request");

        try (MockedStatic<RequirementFolder> mockedRequirementFolder = mockStatic(RequirementFolder.class);) {

            mockedRequirementFolder.when(() -> RequirementFolder.createRequirementFolderTree(project, "folder1/folder2")).thenReturn(requirementFolder);
            doReturn(List.of(new Requirement("https://localhost:4321/requirements/1", "requirement", 1, "myRequirement", "/project/myRequirement"),
                    new Requirement("https://localhost:4321/requirements/1", "requirement", 2, "myRequirement", "/project/folder1/folder2/myRequirement"))).when(project).getRequirements();

            Requirement requirement = Requirement.create(project, "myRequirement", "some description", new HashMap<>(), (String) null, Requirement.Criticality.UNDEFINED);

            Assert.assertEquals(requirement.getId(), 1); // check this is the requirement within the requested folder which is chosen
            verify(postRequest, never()).body(any(JSONObject.class));
            mockedRequirementFolder.verify(() -> RequirementFolder.createRequirementFolderTree(eq(project), isNull()));
        }
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Cannot create requirement: myRequirement")
    public void testCreateRequirementWithError() {
        RequestBodyEntity postRequest = (RequestBodyEntity) createServerMock("POST", "/requirements", 200, "{}", "requestBodyEntity");
        when(postRequest.asJson()).thenThrow(UnirestException.class);

        RequirementFolder requirementFolder2 = new RequirementFolder(
                "https://localhost:4321/requirement-folders/7",
                "folder",
                7,
                "foo",
                project,
                null);
        Requirement.create(project, "myRequirement", "some description", Map.of("APP", List.of("comp1", "comp2")), requirementFolder2, Requirement.Criticality.UNDEFINED);

    }


    @Test
    public void testUpdateRequirement() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirements/624", 200, REQUIREMENT_REPLY_DATA3, "request");
        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        requirement.update("myRequirement", "some description", new HashMap<>(), Requirement.Criticality.UNDEFINED, Requirement.Status.APPROVED);
        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement\",\"current_version\":{\"_type\":\"requirement-version\",\"name\":\"myRequirement\",\"criticality\":\"UNDEFINED\",\"description\":\"some description\",\"status\":\"APPROVED\"},\"custom_fields\":[]}"));
    }

    @Test
    public void testUpdateRequirementNoName() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirements/624", 200, REQUIREMENT_REPLY_DATA3, "request");
        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        requirement.update(null, "some description", new HashMap<>(), Requirement.Criticality.UNDEFINED, Requirement.Status.APPROVED);
        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement\",\"current_version\":{\"_type\":\"requirement-version\",\"criticality\":\"UNDEFINED\",\"description\":\"some description\",\"status\":\"APPROVED\"},\"custom_fields\":[]}"));
    }

    @Test
    public void testUpdateRequirementNoDescription() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirements/624", 200, REQUIREMENT_REPLY_DATA3, "request");
        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        requirement.update("myRequirement", null, new HashMap<>(), Requirement.Criticality.UNDEFINED, Requirement.Status.APPROVED);
        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement\",\"current_version\":{\"_type\":\"requirement-version\",\"name\":\"myRequirement\",\"criticality\":\"UNDEFINED\",\"status\":\"APPROVED\"},\"custom_fields\":[]}"));
    }

    @Test
    public void testUpdateRequirementWithCustomFields() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirements/624", 200, REQUIREMENT_REPLY_DATA3, "request");
        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        requirement.update("myRequirement", null, Map.of("APP", List.of("comp1", "comp2")), Requirement.Criticality.UNDEFINED, Requirement.Status.APPROVED);
        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement\",\"current_version\":{\"_type\":\"requirement-version\",\"name\":\"myRequirement\",\"criticality\":\"UNDEFINED\",\"status\":\"APPROVED\"},\"custom_fields\":[{\"value\":[\"comp1\",\"comp2\"],\"code\":\"APP\"}]}"));
    }

    @Test
    public void testUpdateRequirementNoCriticality() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirements/624", 200, REQUIREMENT_REPLY_DATA3, "request");
        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        requirement.update("myRequirement", "some description", new HashMap<>(), null, Requirement.Status.APPROVED);
        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement\",\"current_version\":{\"_type\":\"requirement-version\",\"name\":\"myRequirement\",\"description\":\"some description\",\"status\":\"APPROVED\"},\"custom_fields\":[]}"));
    }

    @Test
    public void testUpdateRequirementNoStatus() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/requirements/624", 200, REQUIREMENT_REPLY_DATA3, "request");
        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        requirement.update("myRequirement", "some description", new HashMap<>(), Requirement.Criticality.UNDEFINED, null);
        verify(patchRequest).body(new JSONObject("{\"_type\":\"requirement\",\"current_version\":{\"_type\":\"requirement-version\",\"name\":\"myRequirement\",\"criticality\":\"UNDEFINED\",\"description\":\"some description\"},\"custom_fields\":[]}"));
    }


    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Cannot update requirement")
    public void testUpdateRequirementWithError() {
        RequestBodyEntity patchRequest = (RequestBodyEntity) createServerMock("PATCH", "/requirements/624", 200, "{}", "requestBodyEntity");
        when(patchRequest.asJson()).thenThrow(UnirestException.class);
        Requirement requirement = new Requirement("https://localhost:4321/requirements/624", "requirement", 624, "sample requirement 62-4");
        requirement.update("myRequirement", "some description", new HashMap<>(), Requirement.Criticality.UNDEFINED, null);
    }

}
