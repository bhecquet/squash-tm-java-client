package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpRequestWithBody;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestTestCase extends SquashTMTest {

    public static final String TEST_CASE_REPLY_DATA = "{" +
            "  \"_type\" : \"test-case\"," +
            "  \"id\" : 12," +
            "  \"name\" : \"walking test\"," +
            "  \"reference\" : \"TC1\"," +
            "  \"kind\" : \"STANDARD\"," +
            "  \"project\" : {" +
            "    \"_type\" : \"project\"," +
            "    \"id\" : 14," +
            "    \"name\" : \"sample project\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/projects/14\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"path\" : \"/sample project/sample folder/walking test\"," +
            "  \"parent\" : {" +
            "    \"_type\" : \"test-case-folder\"," +
            "    \"id\" : 237," +
            "    \"name\" : \"sample folder\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/test-case-folders/237\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"created_by\" : \"User-1\"," +
            "  \"created_on\" : \"2017-06-15T10:00:00.000+0000\"," +
            "  \"last_modified_by\" : \"User-1\"," +
            "  \"last_modified_on\" : \"2017-06-15T10:00:00.000+0000\"," +
            "  \"importance\" : \"LOW\"," +
            "  \"status\" : \"WORK_IN_PROGRESS\"," +
            "  \"nature\" : {" +
            "    \"code\" : \"NAT_USER_TESTING\"" +
            "  }," +
            "  \"type\" : {" +
            "    \"code\" : \"TYP_EVOLUTION_TESTING\"" +
            "  }," +
            "  \"prerequisite\" : \"prereq\"," +
            "  \"description\" : \"some description\"," +
            "  \"automated_test\" : {" +
            "    \"_type\" : \"automated-test\"," +
            "    \"id\" : 2," +
            "    \"name\" : \"script_custom_field_params_all.ta\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/automated-tests/2\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"custom_fields\" : [ {" +
            "    \"code\" : \"CF_TXT\"," +
            "    \"label\" : \"test level\"," +
            "    \"value\" : \"mandatory\"" +
            "  }, {" +
            "    \"code\" : \"CF_TAGS\"," +
            "    \"label\" : \"see also\"," +
            "    \"value\" : [ \"walking\", \"bipedal\" ]" +
            "  } ]," +
            "  \"steps\" : [ {" +
            "    \"_type\" : \"action-step\"," +
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
            "  }, {" +
            "    \"_type\" : \"action-step\"," +
            "    \"id\" : 166," +
            "    \"action\" : \"<p>move ${second_foot}&nbsp;forward</p>\\n\"," +
            "    \"expected_result\" : \"<p>and another step !</p>\\n\"," +
            "    \"index\" : 1," +
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
            "        \"href\" : \"https://localhost:4321/test-steps/166\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"call-step\"," +
            "    \"id\" : 167," +
            "    \"delegate_parameter_values\" : false," +
            "    \"called_test_case\" : {" +
            "      \"_type\" : \"test-case\"," +
            "      \"id\" : 239," +
            "      \"name\" : \"victory dance\"," +
            "      \"_links\" : {" +
            "        \"self\" : {" +
            "          \"href\" : \"https://localhost:4321/test-cases/239\"" +
            "        }" +
            "      }" +
            "    }," +
            "    \"called_dataset\" : null," +
            "    \"index\" : 2," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/test-steps/167\"" +
            "      }" +
            "    }" +
            "  }]," +
            "  \"parameters\" : [ {" +
            "    \"_type\" : \"parameter\"," +
            "    \"id\" : 1," +
            "    \"name\" : \"first_foot\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/parameters/1\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"parameter\"," +
            "    \"id\" : 2," +
            "    \"name\" : \"second_foot\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/parameters/2\"" +
            "      }" +
            "    }" +
            "  } ]," +
            "  \"datasets\" : [ {" +
            "    \"_type\" : \"dataset\"," +
            "    \"id\" : 1," +
            "    \"name\" : \"right handed people\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/datasets/1\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"dataset\"," +
            "    \"id\" : 2," +
            "    \"name\" : \"left handed people\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/datasets/2\"" +
            "      }" +
            "    }" +
            "  } ]," +
            "  \"language\" : \"\"," +
            "  \"script\" : \"\"," +
            "  \"verified_requirements\" : [ {" +
            "    \"_type\" : \"requirement-version\"," +
            "    \"id\" : 255," +
            "    \"name\" : \"Must have legs\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/255\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"unauthorized-resource\"," +
            "    \"resource_type\" : \"requirement-version\"," +
            "    \"resource_id\" : 256," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/256\"" +
            "      }" +
            "    }" +
            "  } ]," +
            "  \"attachments\" : [ ]," +
            "  \"_links\" : {" +
            "    \"self\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/12\"" +
            "    }," +
            "    \"project\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/14\"" +
            "    }," +
            "    \"steps\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/12/steps\"" +
            "    }," +
            "    \"parameters\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/12/parameters\"" +
            "    }," +
            "    \"datasets\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/12/datasets\"" +
            "    }," +
            "    \"attachments\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/12/attachments\"" +
            "    }" +
            "  }" +
            "}";

    public static final String TEST_CASE_REPLY_DATA2 = "{" +
            "  \"_type\" : \"test-case\"," +
            "  \"id\" : 239," +
            "  \"name\" : \"walking testé\"," +
            "  \"reference\" : \"TC2\"," +
            "  \"kind\" : \"STANDARD\"," +
            "  \"project\" : {" +
            "    \"_type\" : \"project\"," +
            "    \"id\" : 14," +
            "    \"name\" : \"sample project\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/projects/14\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"path\" : \"/sample project/sample folder/walking test2\"," +
            "  \"parent\" : {" +
            "    \"_type\" : \"test-case-folder\"," +
            "    \"id\" : 237," +
            "    \"name\" : \"sample folder\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/test-case-folders/237\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"created_by\" : \"User-1\"," +
            "  \"created_on\" : \"2017-06-15T10:00:00.000+0000\"," +
            "  \"last_modified_by\" : \"User-1\"," +
            "  \"last_modified_on\" : \"2017-06-15T10:00:00.000+0000\"," +
            "  \"importance\" : \"LOW\"," +
            "  \"status\" : \"WORK_IN_PROGRESS\"," +
            "  \"nature\" : {" +
            "    \"code\" : \"NAT_USER_TESTING\"" +
            "  }," +
            "  \"type\" : {" +
            "    \"code\" : \"TYP_EVOLUTION_TESTING\"" +
            "  }," +
            "  \"prerequisite\" : \"prereq\"," +
            "  \"description\" : \"some description\"," +
            "  \"automated_test\" : {" +
            "    \"_type\" : \"automated-test\"," +
            "    \"id\" : 2," +
            "    \"name\" : \"script_custom_field_params_all.ta\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/automated-tests/2\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"custom_fields\" : [ {" +
            "    \"code\" : \"CF_TXT\"," +
            "    \"label\" : \"test level\"," +
            "    \"value\" : \"mandatory\"" +
            "  }, {" +
            "    \"code\" : \"CF_TAGS\"," +
            "    \"label\" : \"see also\"," +
            "    \"value\" : [ \"walking\", \"bipedal\" ]" +
            "  } ]," +
            "  \"steps\" : [ {" +
            "    \"_type\" : \"action-step\"," +
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
            "  } ]," +
            "  \"parameters\" : [ {" +
            "    \"_type\" : \"parameter\"," +
            "    \"id\" : 1," +
            "    \"name\" : \"first_foot\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/parameters/1\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"parameter\"," +
            "    \"id\" : 2," +
            "    \"name\" : \"second_foot\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/parameters/2\"" +
            "      }" +
            "    }" +
            "  } ]," +
            "  \"datasets\" : [ {" +
            "    \"_type\" : \"dataset\"," +
            "    \"id\" : 1," +
            "    \"name\" : \"right handed people\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/datasets/1\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"dataset\"," +
            "    \"id\" : 2," +
            "    \"name\" : \"left handed people\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/datasets/2\"" +
            "      }" +
            "    }" +
            "  } ]," +
            "  \"language\" : \"\"," +
            "  \"script\" : \"\"," +
            "  \"verified_requirements\" : [ {" +
            "    \"_type\" : \"requirement-version\"," +
            "    \"id\" : 255," +
            "    \"name\" : \"Must have legs\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/255\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"unauthorized-resource\"," +
            "    \"resource_type\" : \"requirement-version\"," +
            "    \"resource_id\" : 256," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/requirement-versions/256\"" +
            "      }" +
            "    }" +
            "  } ]," +
            "  \"attachments\" : [ ]," +
            "  \"_links\" : {" +
            "    \"self\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/239\"" +
            "    }," +
            "    \"project\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/14\"" +
            "    }," +
            "    \"steps\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/239/steps\"" +
            "    }," +
            "    \"parameters\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/239/parameters\"" +
            "    }," +
            "    \"datasets\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/239/datasets\"" +
            "    }," +
            "    \"attachments\" : {" +
            "      \"href\" : \"https://localhost:4321/test-cases/239/attachments\"" +
            "    }" +
            "  }" +
            "}";

    @BeforeMethod
    public void init() {
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testExists() {
        createServerMock("GET", "/test-cases/12", 200, TEST_CASE_REPLY_DATA);
        Assert.assertEquals(TestCase.get(12).getId(), 12);
    }

    /**
     * Test case does not exist
     */
    @Test(expectedExceptions = SquashTmException.class)
    public void testNotExist() {
        createServerMock("GET", "/test-cases/12", 404, "{" +
                "    \"exception\": \"javax.persistence.EntityNotFoundException\"," +
                "    \"message\": \"Unable to find org.squashtest.tm.domain.testcase.TestCase with id 23\"" +
                "}");
        TestCase.get(12);
    }

    /**
     * Error retrieving test case
     */
    @Test(expectedExceptions = SquashTmException.class)
    public void testExistsInError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/test-cases/12", 200, "{}", "requestBodyEntity");
        when(getRequest.asJson()).thenThrow(UnirestException.class);

        TestCase.get(12);
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

        TestCase.fromJson(json);
    }

    @Test
    public void testCompleteDetails() {
        createServerMock("GET", "/test-cases/12", 200, TEST_CASE_REPLY_DATA);
        createServerMock("GET", "/test-cases/239", 200, TEST_CASE_REPLY_DATA2);
        createServerMock("GET", "/test-cases/240", 200, TEST_CASE_REPLY_DATA2);

        TestCase testCase = new TestCase("https://localhost:4321/test-cases/12", "test-case", 12, "walking test");
        testCase.completeDetails();

        Assert.assertEquals(testCase.getId(), 12);
        Assert.assertEquals(testCase.getAutomatedTestReference(), "");
        Assert.assertEquals(testCase.getPath(), "/sample project/sample folder/walking test");
        Assert.assertEquals(testCase.getDescription(), "some description");
        Assert.assertEquals(testCase.getProjectId(), 14);
        Assert.assertEquals(testCase.getProjectName(), "sample project");
        Assert.assertEquals(testCase.getAuthor(), "User-1");
        Assert.assertEquals(testCase.getNature(), "NAT_USER_TESTING");
        Assert.assertEquals(testCase.getTctype(), "TYP_EVOLUTION_TESTING");
        Assert.assertEquals(testCase.getReference(), "TC1");
        Assert.assertEquals(testCase.getStatus(), "WORK_IN_PROGRESS");
        Assert.assertEquals(testCase.getImportance(), "LOW");
        Assert.assertEquals(testCase.getPrerequisite(), "prereq");
        Assert.assertEquals(testCase.getRequirementNumber(), 2);
        Assert.assertEquals(testCase.getTestSteps().size(), 3);
        Assert.assertEquals(testCase.getCustomFields().size(), 2);
        Assert.assertEquals(testCase.getCustomFields().get(0).getCode(), "CF_TXT");
    }

    @Test
    public void testCompleteDetails2() {
        createServerMock("GET", "/test-cases/12", 200, TEST_CASE_REPLY_DATA.replace("\"id\" : 12,", "\"id\" : 12,\"automated_test_reference\" : \"repo01/src/resources/script_custom_field_params_all.ta#Test_case_238\","));
        createServerMock("GET", "/test-cases/239", 200, TEST_CASE_REPLY_DATA2);
        createServerMock("GET", "/test-cases/240", 200, TEST_CASE_REPLY_DATA2);

        TestCase testCase = new TestCase("https://localhost:4321/test-cases/12", "test-case", 12, "walking test");
        testCase.completeDetails();

        Assert.assertEquals(testCase.getId(), 12);
        Assert.assertEquals(testCase.getAutomatedTestReference(), "repo01/src/resources/script_custom_field_params_all.ta#Test_case_238");
        Assert.assertEquals(testCase.getPath(), "/sample project/sample folder/walking test");
        Assert.assertEquals(testCase.getDescription(), "some description");
        Assert.assertEquals(testCase.getProjectId(), 14);
        Assert.assertEquals(testCase.getProjectName(), "sample project");
        Assert.assertEquals(testCase.getAuthor(), "User-1");
        Assert.assertEquals(testCase.getNature(), "NAT_USER_TESTING");
        Assert.assertEquals(testCase.getTctype(), "TYP_EVOLUTION_TESTING");
        Assert.assertEquals(testCase.getReference(), "TC1");
        Assert.assertEquals(testCase.getStatus(), "WORK_IN_PROGRESS");
        Assert.assertEquals(testCase.getImportance(), "LOW");
        Assert.assertEquals(testCase.getPrerequisite(), "prereq");
        Assert.assertEquals(testCase.getRequirementNumber(), 2);
        Assert.assertEquals(testCase.getTestSteps().size(), 3);
        Assert.assertEquals(testCase.getCustomFields().size(), 2);
        Assert.assertEquals(testCase.getCustomFields().get(0).getCode(), "CF_TXT");
    }

    @Test
    public void testUpdateCustomFields() {
        HttpRequestWithBody patchRequest = (HttpRequestWithBody) createServerMock("PATCH", "/test-cases/12", 200, TEST_CASE_REPLY_DATA);

        TestCase testCase = new TestCase("https://localhost:4321/test-cases/12", "test-case", 12, "walking test");
        testCase.updateCustomField("foo", "bar");

        verify(patchRequest).body(new JSONObject("{\"_type\":\"test-case\",\"custom_fields\":[{\"value\":\"bar\",\"code\":\"foo\"}]}"));
    }
}
