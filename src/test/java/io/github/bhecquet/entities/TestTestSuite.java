package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;

public class TestTestSuite extends SquashTMTest {

    public static String TEST_SUITE_REPLY_DATA = "{" +
            "  \"_type\" : \"test-suite\"," +
            "  \"id\" : 9," +
            "  \"name\" : \"sample test suite\"," +
            "  \"description\" : \"<p>this is a sample test suite</p>\"," +
            "  \"uuid\" : \"2f7198zd-eb2e-4379-f82d-ddc207c866bd\"," +
            "  \"status\" : \"READY\"," +
            "  \"parent\" : {" +
            "    \"_type\" : \"iteration\"," +
            "    \"id\" : 101," +
            "    \"name\" : \"second iteration\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/iterations/101\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"created_by\" : \"admin\"," +
            "  \"created_on\" : \"2017-07-12T10:00:00.000+00:00\"," +
            "  \"last_modified_by\" : \"admin\"," +
            "  \"last_modified_on\" : \"2017-07-12T10:00:00.000+00:00\"," +
            "  \"custom_fields\" : [ {" +
            "    \"code\" : \"CF_TXT\"," +
            "    \"label\" : \"cuf text\"," +
            "    \"value\" : \"the_value\"" +
            "  }, {" +
            "    \"code\" : \"CF_TAG\"," +
            "    \"label\" : \"cuf tag\"," +
            "    \"value\" : [ \"tag_1\", \"tag_2\" ]" +
            "  } ]," +
            "  \"test_plan\" : [ {" +
            "    \"_type\" : \"iteration-test-plan-item\"," +
            "    \"id\" : 80," +
            "    \"execution_status\" : \"READY\"," +
            "    \"referenced_test_case\" : {" +
            "      \"_type\" : \"test-case\"," +
            "      \"id\" : 90," +
            "      \"name\" : \"first test case\"," +
            "      \"_links\" : {" +
            "        \"self\" : {" +
            "          \"href\" : \"https://localhost:4321/test-cases/90\"" +
            "        }" +
            "      }" +
            "    }," +
            "    \"referenced_dataset\" : {" +
            "      \"_type\" : \"dataset\"," +
            "      \"id\" : 5," +
            "      \"name\" : \"dataset\"," +
            "      \"_links\" : {" +
            "        \"self\" : {" +
            "          \"href\" : \"https://localhost:4321/datasets/5\"" +
            "        }" +
            "      }" +
            "    }," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/iteration-test-plan-items/80\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"iteration-test-plan-item\"," +
            "    \"id\" : 41," +
            "    \"execution_status\" : \"SUCCESS\"," +
            "    \"referenced_test_case\" : {" +
            "      \"_type\" : \"scripted-test-case\"," +
            "      \"id\" : 91," +
            "      \"name\" : \"scripted test case 2\"," +
            "      \"_links\" : {" +
            "        \"self\" : {" +
            "          \"href\" : \"https://localhost:4321/test-cases/91\"" +
            "        }" +
            "      }" +
            "    }," +
            "    \"referenced_dataset\" : {" +
            "      \"_type\" : \"dataset\"," +
            "      \"id\" : 9," +
            "      \"name\" : \"dataset\"," +
            "      \"_links\" : {" +
            "        \"self\" : {" +
            "          \"href\" : \"https://localhost:4321/datasets/9\"" +
            "        }" +
            "      }" +
            "    }," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/iteration-test-plan-items/41\"" +
            "      }" +
            "    }" +
            "  }, {" +
            "    \"_type\" : \"iteration-test-plan-item\"," +
            "    \"id\" : 95," +
            "    \"execution_status\" : \"SUCCESS\"," +
            "    \"referenced_test_case\" : {" +
            "      \"_type\" : \"keyword-test-case\"," +
            "      \"id\" : 105," +
            "      \"name\" : \"keyword test case 3\"," +
            "      \"_links\" : {" +
            "        \"self\" : {" +
            "          \"href\" : \"https://localhost:4321/test-cases/105\"" +
            "        }" +
            "      }" +
            "    }," +
            "    \"referenced_dataset\" : {" +
            "      \"_type\" : \"dataset\"," +
            "      \"id\" : 18," +
            "      \"name\" : \"dataset\"," +
            "      \"_links\" : {" +
            "        \"self\" : {" +
            "          \"href\" : \"https://localhost:4321/datasets/18\"" +
            "        }" +
            "      }" +
            "    }," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/iteration-test-plan-items/95\"" +
            "      }" +
            "    }" +
            "  } ]," +
            "  \"attachments\" : [ ]," +
            "  \"_links\" : {" +
            "    \"self\" : {" +
            "      \"href\" : \"https://localhost:4321/test-suites/9\"" +
            "    }," +
            "    \"project\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/15\"" +
            "    }," +
            "    \"iteration\" : {" +
            "      \"href\" : \"https://localhost:4321/iterations/101\"" +
            "    }," +
            "    \"test-plan\" : {" +
            "      \"href\" : \"https://localhost:4321/test-suites/9/test-plan\"" +
            "    }," +
            "    \"attachments\" : {" +
            "      \"href\" : \"https://localhost:4321/test-suites/9/attachments\"" +
            "    }," +
            "    \"issues\" : {" +
            "      \"href\" : \"https://localhost:4321/test-suites/9/issues\"" +
            "    }" +
            "  }" +
            "}";

    @BeforeMethod
    public void init() {
        TestSuite.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testFromJson() {
        TestSuite testSuite = TestSuite.fromJson(new JSONObject("{" +
                "    \"_type\" : \"test-suite\"," +
                "    \"id\" : 1," +
                "    \"name\" : \"Suite_1\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/test-suites/1\"" +
                "      }" +
                "    }" +
                "  }"));
        Assert.assertEquals(testSuite.getId(), 1);
        Assert.assertEquals(testSuite.getName(), "Suite_1");
        Assert.assertEquals(testSuite.getUrl(), "https://localhost:4321/test-suites/1");
    }

    /**
     * No problem if name is missing
     */
    @Test
    public void testFromJsonWrongFormat() {
        TestSuite testSuite = TestSuite.fromJson(new JSONObject("{" +
                "    \"_type\" : \"test-suite\"," +
                "    \"id\" : 1," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/test-suites/1\"" +
                "      }" +
                "    }" +
                "  }"));
        Assert.assertEquals(testSuite.getName(), "");

    }

    @Test
    public void testCompleteDetails() {
        createServerMock("GET", "/test-suites/9", 200, TEST_SUITE_REPLY_DATA);
        TestSuite testSuite = new TestSuite("https://localhost:4321/test-suites/9", "test-suite", 9, "sample test suite");
        testSuite.completeDetails();

        Assert.assertEquals(testSuite.getIterationTestPlanItems().size(), 3);
        Assert.assertEquals(testSuite.getIterationTestPlanItems().get(0).getStatus(), "READY");
        Assert.assertEquals(testSuite.getIterationTestPlanItems().get(0).getTestSuite(), testSuite);
    }

    @Test
    public void testGet() {
        createServerMock("GET", "/test-suites/9", 200, TEST_SUITE_REPLY_DATA);
        TestSuite testSuite = TestSuite.get(9);

        Assert.assertEquals(testSuite.getIterationTestPlanItems().size(), 3);
        Assert.assertEquals(testSuite.getIterationTestPlanItems().get(0).getStatus(), "READY");
        Assert.assertEquals(testSuite.getIterationTestPlanItems().get(0).getTestSuite(), testSuite);
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "request to https://localhost:4321 failed\\[404\\]: null")
    public void testGetInError() {
        createServerMock("GET", "/test-suites/9", 404, TEST_SUITE_REPLY_DATA);
        TestSuite.get(9);
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Test suite 9 does not exist")
    public void testGetInError2() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/test-suites/9", 404, TEST_SUITE_REPLY_DATA);
        when(getRequest.asJson()).thenThrow(new UnirestException("error"));
        TestSuite.get(9);
    }
}
