package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.*;
import kong.unirest.core.json.JSONObject;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestCampaign extends SquashTMTest {

    private Project project;

    @Mock
    private CampaignFolder campaignFolder;

    @BeforeMethod
    public void init() {
        project = spy(new Project("https://localhost:4321/projects/1", "project", 1, "project"));
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    //For code coverage only I don't get the meaning of this constructor
    @Test
    public void testConstructor() {
        Campaign camp = new Campaign(12);
        Assert.assertEquals(camp.getId(), 12);
        Assert.assertNull(camp.getName());
    }

    @Test
    public void testGetAll() {

        createServerMock("GET", "/campaigns", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"campaigns\" : [ {" +
                "      \"_type\" : \"campaign\"," +
                "      \"id\" : 41," +
                "      \"name\" : \"sample campaign 1\"," +
                "      \"reference\" : \"SAMP_CAMP_1\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaigns/41\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"campaign\"," +
                "      \"id\" : 46," +
                "      \"name\" : \"sample campaign 2\"," +
                "      \"reference\" : \"SAMP_CAMP_2\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaigns/46\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns?page=0&size=2\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns?page=0&size=2\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns?page=1&size=2\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns?page=1&size=2\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 2," +
                "    \"totalElements\" : 4," +
                "    \"totalPages\" : 2," +
                "    \"number\" : 1" +
                "  }" +
                "}");
        List<Campaign> campaigns = Campaign.getAll();
        Assert.assertEquals(campaigns.size(), 2);
        Assert.assertEquals(campaigns.get(0).getName(), "sample campaign 1");
        Assert.assertEquals(campaigns.get(0).getId(), 41);
        Assert.assertEquals(campaigns.get(0).getUrl(), "https://localhost:4321/campaigns/41");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetAllWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/campaigns", 200, "{}", "requestBodyEntity");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);

        Campaign.getAll();
    }

    @Test
    public void testGetIterations() {
        createServerMock("GET", "/campaign/7/iterations", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"iterations\" : [ {" +
                "      \"_type\" : \"iteration\"," +
                "      \"id\" : 10," +
                "      \"name\" : \"sample iteration 1\"," +
                "      \"reference\" : \"SAMP_IT_1\"," +
                "      \"description\" : \"<p>This iteration is a sample one...</p>\"," +
                "      \"parent\" : {" +
                "        \"_type\" : \"campaign\"," +
                "        \"id\" : 36," +
                "        \"name\" : \"sample parent campaign\"," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/campaigns/36\"" +
                "          }" +
                "        }" +
                "      }," +
                "      \"created_by\" : \"User-1\"," +
                "      \"created_on\" : \"2017-07-21T10:00:00.000+0000\"," +
                "      \"last_modified_by\" : \"admin\"," +
                "      \"last_modified_on\" : \"2017-07-22T10:00:00.000+0000\"," +
                "      \"scheduled_start_date\" : null," +
                "      \"scheduled_end_date\" : null," +
                "      \"actual_start_date\" : \"2017-08-01T10:00:00.000+0000\"," +
                "      \"actual_end_date\" : \"2017-08-30T10:00:00.000+0000\"," +
                "      \"actual_start_auto\" : false," +
                "      \"actual_end_auto\" : false," +
                "      \"custom_fields\" : [ {" +
                "        \"code\" : \"CUF_Z\"," +
                "        \"label\" : \"Cuf Z\"," +
                "        \"value\" : \"value of Z\"" +
                "      }, {" +
                "        \"code\" : \"CUF_Y\"," +
                "        \"label\" : \"Cuf Y\"," +
                "        \"value\" : \"value of Y\"" +
                "      } ]," +
                "      \"test_suites\" : [ {" +
                "        \"_type\" : \"test-suite\"," +
                "        \"id\" : 88," +
                "        \"name\" : null," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/test-suites/88\"" +
                "          }" +
                "        }" +
                "      }, {" +
                "        \"_type\" : \"test-suite\"," +
                "        \"id\" : 11," +
                "        \"name\" : null," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/test-suites/11\"" +
                "          }" +
                "        }" +
                "      }, {" +
                "        \"_type\" : \"test-suite\"," +
                "        \"id\" : 14," +
                "        \"name\" : null," +
                "        \"_links\" : {" +
                "          \"self\" : {" +
                "            \"href\" : \"https://localhost:4321/test-suites/14\"" +
                "          }" +
                "        }" +
                "      } ]," +
                "      \"attachments\" : [ ]," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/iterations/10\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/36/iterations?page=0&size=1\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/36/iterations?page=0&size=1\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/36/iterations?page=1&size=1\"" +
                "    }," +
                "    \"next\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/36/iterations?page=2&size=1\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/36/iterations?page=2&size=1\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 1," +
                "    \"totalElements\" : 3," +
                "    \"totalPages\" : 3," +
                "    \"number\" : 1" +
                "  }" +
                "}");


        Campaign campaign = new Campaign("https://localhost:4321/campaign/7",
                "campaign",
                7,
                "campaign"
        );
        List<Iteration> iterations = campaign.getIterations();
        Assert.assertEquals(iterations.size(), 1);
        Assert.assertEquals(iterations.get(0).getName(), "sample iteration 1");
        Assert.assertEquals(iterations.get(0).getId(), 10);
        Assert.assertEquals(iterations.get(0).getUrl(), "https://localhost:4321/iterations/10");
        Assert.assertEquals(iterations.get(0).getReference(), "SAMP_IT_1"); // check we have completed iteration details

    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetAllIterationsWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/campaign/7/iterations", 200, "{}", "requestBodyEntity");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);

        Campaign campaign = new Campaign("https://localhost:4321/campaign/7",
                "campaign",
                7,
                "campaign"
        );
        campaign.getIterations();
    }

    /**
     * Check case where no campaigns are available
     */

    @Test
    public void testGetAllNoCampaigns() {
        createServerMock("GET", "/campaigns", 200, "{" +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns?page=0&size=2\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns?page=0&size=2\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns?page=1&size=2\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns?page=1&size=2\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 2," +
                "    \"totalElements\" : 4," +
                "    \"totalPages\" : 2," +
                "    \"number\" : 1" +
                "  }" +
                "}");
        List<Campaign> campaigns = Campaign.getAll();
        Assert.assertEquals(campaigns.size(), 0);
    }

    @Test
    public void testFromJson() {

        JSONObject json = new JSONObject();
        json.put("_type", "campaign");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaigns/41\"" +
                "        }}"));

        Campaign campaign = Campaign.fromJson(json);
        Assert.assertEquals(campaign.getId(), 1);
        Assert.assertEquals(campaign.getName(), "foo");
        Assert.assertEquals(campaign.getUrl(), "https://localhost:4321/campaigns/41");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {

        JSONObject json = new JSONObject();
        json.put("_type", "campaign");
        json.put("id", 1);
        json.put("name", "foo");

        Campaign.fromJson(json);
    }

    @Test
    public void testCreateCampaignNoFolder() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaigns", 200, "{" +
                "  \"_type\" : \"campaign\"," +
                "  \"id\" : 332," +
                "  \"name\" : \"Campaign Test\"," +
                "  \"reference\" : \"ABCD\"," +
                "  \"description\" : \"<p>Sed eget rhoncus sapien. Nam et pulvinar nisi. su Do</p>\"," +
                "  \"status\" : \"PLANNED\"," +
                "  \"project\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 44," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/44\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"path\" : \"/sample project/campaign folder/Campaign Test\"," +
                "  \"parent\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 44," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/44\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"created_by\" : \"admin\"," +
                "  \"created_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"last_modified_by\" : \"admin\"," +
                "  \"last_modified_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"scheduled_start_date\" : \"2021-08-31T10:00:00.000+0000\"," +
                "  \"scheduled_end_date\" : \"2031-09-29T10:00:00.000+0000\"," +
                "  \"actual_start_date\" : \"2034-09-29T10:00:00.000+0000\"," +
                "  \"actual_end_date\" : \"2035-09-29T10:00:00.000+0000\"," +
                "  \"actual_start_auto\" : false," +
                "  \"actual_end_auto\" : false," +
                "  \"custom_fields\" : [ {" +
                "    \"code\" : \"CUF_A\"," +
                "    \"label\" : \"Cuf A\"," +
                "    \"value\" : \"value of A\"" +
                "  }, {" +
                "    \"code\" : \"CUF_B\"," +
                "    \"label\" : \"Cuf B\"," +
                "    \"value\" : \"value of B\"" +
                "  } ]," +
                "  \"iterations\" : [ ]," +
                "  \"test_plan\" : [ ]," +
                "  \"attachments\" : [ ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/332\"" +
                "    }" +
                "  }" +
                "}", "request");
        Campaign.create(project, "myCampaign", (CampaignFolder) null, new HashMap<>());
        verify(postRequest).body(new JSONObject("{\"_type\":\"campaign\",\"name\":\"myCampaign\",\"status\":\"PLANNED\",\"parent\":{\"id\":1,\"_type\":\"project\"}}"));
    }

    @Test
    public void testCreateCampaignWithCustomFields() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaigns", 200, "{" +
                "  \"_type\" : \"campaign\"," +
                "  \"id\" : 332," +
                "  \"name\" : \"Campaign Test\"," +
                "  \"reference\" : \"ABCD\"," +
                "  \"description\" : \"<p>Sed eget rhoncus sapien. Nam et pulvinar nisi. su Do</p>\"," +
                "  \"status\" : \"PLANNED\"," +
                "  \"project\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 44," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/44\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"path\" : \"/sample project/campaign folder/Campaign Test\"," +
                "  \"parent\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 44," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/44\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"created_by\" : \"admin\"," +
                "  \"created_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"last_modified_by\" : \"admin\"," +
                "  \"last_modified_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"scheduled_start_date\" : \"2021-08-31T10:00:00.000+0000\"," +
                "  \"scheduled_end_date\" : \"2031-09-29T10:00:00.000+0000\"," +
                "  \"actual_start_date\" : \"2034-09-29T10:00:00.000+0000\"," +
                "  \"actual_end_date\" : \"2035-09-29T10:00:00.000+0000\"," +
                "  \"actual_start_auto\" : false," +
                "  \"actual_end_auto\" : false," +
                "  \"custom_fields\" : [ {" +
                "    \"code\" : \"CUF_A\"," +
                "    \"label\" : \"Cuf A\"," +
                "    \"value\" : \"value of A\"" +
                "  }, {" +
                "    \"code\" : \"CUF_B\"," +
                "    \"label\" : \"Cuf B\"," +
                "    \"value\" : \"value of B\"" +
                "  } ]," +
                "  \"iterations\" : [ ]," +
                "  \"test_plan\" : [ ]," +
                "  \"attachments\" : [ ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/332\"" +
                "    }" +
                "  }" +
                "}", "request");
        Campaign.create(project, "myCampaign", (CampaignFolder) null, Map.of("APP", List.of("comp1", "comp2")));
        verify(postRequest).body(new JSONObject("{\"_type\":\"campaign\",\"name\":\"myCampaign\",\"status\":\"PLANNED\",\"parent\":{\"id\":1,\"_type\":\"project\"},\"custom_fields\":[{\"value\":[\"comp1\",\"comp2\"],\"code\":\"APP\"}]}"));
    }

    @Test
    public void testCreateCampaignWithFolder() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaigns", 200, "{" +
                "  \"_type\" : \"campaign\"," +
                "  \"id\" : 332," +
                "  \"name\" : \"Campaign Test\"," +
                "  \"reference\" : \"ABCD\"," +
                "  \"description\" : \"<p>Sed eget rhoncus sapien. Nam et pulvinar nisi. su Do</p>\"," +
                "  \"status\" : \"PLANNED\"," +
                "  \"project\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 44," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/44\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"path\" : \"/sample project/campaign folder/Campaign Test\"," +
                "  \"parent\" : {" +
                "    \"_type\" : \"campaign-folder\"," +
                "    \"id\" : 7," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/campaign-folders/7\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"created_by\" : \"admin\"," +
                "  \"created_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"last_modified_by\" : \"admin\"," +
                "  \"last_modified_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"scheduled_start_date\" : \"2021-08-31T10:00:00.000+0000\"," +
                "  \"scheduled_end_date\" : \"2031-09-29T10:00:00.000+0000\"," +
                "  \"actual_start_date\" : \"2034-09-29T10:00:00.000+0000\"," +
                "  \"actual_end_date\" : \"2035-09-29T10:00:00.000+0000\"," +
                "  \"actual_start_auto\" : false," +
                "  \"actual_end_auto\" : false," +
                "  \"custom_fields\" : [ {" +
                "    \"code\" : \"CUF_A\"," +
                "    \"label\" : \"Cuf A\"," +
                "    \"value\" : \"value of A\"" +
                "  }, {" +
                "    \"code\" : \"CUF_B\"," +
                "    \"label\" : \"Cuf B\"," +
                "    \"value\" : \"value of B\"" +
                "  } ]," +
                "  \"iterations\" : [ ]," +
                "  \"test_plan\" : [ ]," +
                "  \"attachments\" : [ ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/332\"" +
                "    }" +
                "  }" +
                "}", "request");

        CampaignFolder campaignFolder2 = new CampaignFolder(
                "https://localhost:4321/campaign-folders/7",
                7,
                "folder",
                project,
                null);
        Campaign.create(project, "myCampaign", campaignFolder2, new HashMap<>());
        verify(postRequest).body(new JSONObject("{\"_type\":\"campaign\",\"name\":\"myCampaign\",\"status\":\"PLANNED\",\"parent\":{\"id\":7,\"_type\":\"campaign-folder\"}}"));
    }

    @Test
    public void testCreateCampaignWithFolder2() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaigns", 200, "{" +
                "  \"_type\" : \"campaign\"," +
                "  \"id\" : 332," +
                "  \"name\" : \"Campaign Test\"," +
                "  \"reference\" : \"ABCD\"," +
                "  \"description\" : \"<p>Sed eget rhoncus sapien. Nam et pulvinar nisi. su Do</p>\"," +
                "  \"status\" : \"PLANNED\"," +
                "  \"project\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 44," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/44\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"path\" : \"/sample project/campaign folder/Campaign Test\"," +
                "  \"parent\" : {" +
                "    \"_type\" : \"campaign-folder\"," +
                "    \"id\" : 7," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/campaign-folders/7\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"created_by\" : \"admin\"," +
                "  \"created_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"last_modified_by\" : \"admin\"," +
                "  \"last_modified_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"scheduled_start_date\" : \"2021-08-31T10:00:00.000+0000\"," +
                "  \"scheduled_end_date\" : \"2031-09-29T10:00:00.000+0000\"," +
                "  \"actual_start_date\" : \"2034-09-29T10:00:00.000+0000\"," +
                "  \"actual_end_date\" : \"2035-09-29T10:00:00.000+0000\"," +
                "  \"actual_start_auto\" : false," +
                "  \"actual_end_auto\" : false," +
                "  \"custom_fields\" : [ {" +
                "    \"code\" : \"CUF_A\"," +
                "    \"label\" : \"Cuf A\"," +
                "    \"value\" : \"value of A\"" +
                "  }, {" +
                "    \"code\" : \"CUF_B\"," +
                "    \"label\" : \"Cuf B\"," +
                "    \"value\" : \"value of B\"" +
                "  } ]," +
                "  \"iterations\" : [ ]," +
                "  \"test_plan\" : [ ]," +
                "  \"attachments\" : [ ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/332\"" +
                "    }" +
                "  }" +
                "}", "request");

        try (MockedStatic mockedCampaignFolder = mockStatic(CampaignFolder.class);) {

            mockedCampaignFolder.when(() -> CampaignFolder.createCampaignFolderTree(project, "folder1/folder2")).thenReturn(campaignFolder);
            doReturn(new ArrayList<>()).when(project).getCampaigns();

            Campaign.create(project, "myCampaign", "folder1/folder2", new HashMap<>());
            verify(postRequest).body(new JSONObject("{\"_type\":\"campaign\",\"name\":\"myCampaign\",\"status\":\"PLANNED\",\"parent\":{\"id\":0,\"_type\":\"campaign-folder\"}}"));
            mockedCampaignFolder.verify(() -> CampaignFolder.createCampaignFolderTree(eq(project), eq("folder1/folder2")));
        }
    }

    /**
     * If campaign exists do not recreate it
     */
    @Test
    public void testDoNotCreateCampaign() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaigns", 200, "{}", "request");

        try (MockedStatic mockedCampaignFolder = mockStatic(CampaignFolder.class);) {

            mockedCampaignFolder.when(() -> CampaignFolder.createCampaignFolderTree(project, "folder1/folder2")).thenReturn(campaignFolder);
            doReturn(List.of(new Campaign("https://localhost:4321/campaigns/1", "campaign", 1, "myCampaign", "/project/myCampaign"),
                    new Campaign("https://localhost:4321/campaigns/1", "campaign", 2, "myCampaign", "/project/folder1/folder2/myCampaign"))).when(project).getCampaigns();

            Campaign campaign = Campaign.create(project, "myCampaign", "folder1/folder2", new HashMap<>());
            Assert.assertEquals(campaign.getId(), 2); // check this is the campaign within the requested folder which is choosen
            verify(postRequest, never()).body(any(JSONObject.class));
            mockedCampaignFolder.verify(() -> CampaignFolder.createCampaignFolderTree(eq(project), eq("folder1/folder2")));
        }
    }

    /**
     * If campaign exists do not recreate it
     */
    @Test
    public void testDoNotCreateCampaign2() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaigns", 200, "{}", "request");

        try (MockedStatic mockedCampaignFolder = mockStatic(CampaignFolder.class);) {

            mockedCampaignFolder.when(() -> CampaignFolder.createCampaignFolderTree(project, "folder1/folder2")).thenReturn(campaignFolder);
            doReturn(List.of(new Campaign("https://localhost:4321/campaigns/1", "campaign", 1, "myCampaign", "/project/myCampaign"),
                    new Campaign("https://localhost:4321/campaigns/1", "campaign", 2, "myCampaign", "/project/folder1/folder2/myCampaign"))).when(project).getCampaigns();

            Campaign campaign = Campaign.create(project, "myCampaign", (String) null, null);
            Assert.assertEquals(campaign.getId(), 1); // check this is the campaign within the requested folder which is choosen
            verify(postRequest, never()).body(any(JSONObject.class));
            mockedCampaignFolder.verify(() -> CampaignFolder.createCampaignFolderTree(eq(project), isNull()));
        }
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testCreateCampaignWithError() {
        RequestBodyEntity postRequest = (RequestBodyEntity) createServerMock("POST", "/campaigns", 200, "{}", "requestBodyEntity");
        when(postRequest.asJson()).thenThrow(UnirestException.class);

        CampaignFolder campaignFolder2 = new CampaignFolder(
                "https://localhost:4321/campaign-folders/7",
                7,
                "folder",
                project,
                null);
        Campaign.create(project, "myCampaign", campaignFolder2, new HashMap<>());
    }

    @Test
    public void testCompleteDetails() {
        createServerMock("GET", "/campaigns/1", 200, "{" +
                "  \"_type\" : \"campaign\"," +
                "  \"id\" : 1," +
                "  \"name\" : \"TNR\"," +
                "  \"reference\" : \"\"," +
                "  \"description\" : \"\"," +
                "  \"status\" : \"UNDEFINED\"," +
                "  \"project\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 1215," +
                "    \"name\" : \"project1\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/1215\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"path\" : \"/project1/Selenium Grid/TNR\"," +
                "  \"parent\" : {" +
                "    \"_type\" : \"campaign-folder\"," +
                "    \"id\" : 63030," +
                "    \"name\" : \"Selenium Grid\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/campaign-folders/63030\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"created_by\" : \"xxx\"," +
                "  \"created_on\" : \"2024-04-09T13:02:34.171+00:00\"," +
                "  \"last_modified_by\" : \"xxx\"," +
                "  \"last_modified_on\" : \"2024-04-09T13:04:02.224+00:00\"," +
                "  \"actual_start_date\" : \"2024-04-09T13:03:49.169+00:00\"," +
                "  \"scheduled_start_date\" : \"2024-08-01T10:00:00.000+00:00\"," +
                "  \"scheduled_end_date\" : \"2024-08-31T10:00:00.000+00:00\"," +
                "  \"actual_end_date\" : \"2024-04-09T13:04:02.220+00:00\"," +
                "  \"actual_start_auto\" : true," +
                "  \"actual_end_auto\" : true," +
                "  \"custom_fields\" : [ {" +
                "    \"code\" : \"CUF_A\"," +
                "    \"label\" : \"Cuf A\"," +
                "    \"value\" : \"value of A\"" +
                "  }, {" +
                "    \"code\" : \"CUF_B\"," +
                "    \"label\" : \"Cuf B\"," +
                "    \"value\" : \"value of B\"" +
                "  } ]," +
                "  \"iterations\" : [ {" +
                "    \"_type\" : \"iteration\"," +
                "    \"id\" : 128368," +
                "    \"name\" : \"Avril 2024\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/iterations/12\"" +
                "      }" +
                "    }" +
                "  } ]," +
                "  \"test_plan\" : [ ]," +
                "  \"attachments\" : [ ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1\"" +
                "    }," +
                "    \"project\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/1215\"" +
                "    }," +
                "    \"iterations\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/iterations\"" +
                "    }," +
                "    \"test-plan\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/test-plan\"" +
                "    }," +
                "    \"attachments\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/attachments\"" +
                "    }," +
                "    \"issues\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/issues\"" +
                "    }" +
                "  }" +
                "}");

        Campaign campaign = new Campaign("https://localhost:4321/campaigns/1", "campaign", 1, "TNR");
        campaign.completeDetails();

        Assert.assertEquals(campaign.getId(), 1);
        Assert.assertEquals(campaign.getName(), "TNR");
        Assert.assertEquals(campaign.getActualEndDate(), "2024-04-09T13:04:02.220+00:00");
        Assert.assertEquals(campaign.getActualStartDate(), "2024-04-09T13:03:49.169+00:00");
        Assert.assertEquals(campaign.getIterations().size(), 1);
        Assert.assertEquals(campaign.getIterations().get(0).getName(), "Avril 2024");
        Assert.assertEquals(campaign.getPath(), "/project1/Selenium Grid/TNR");
        Assert.assertEquals(campaign.getProjectId(), 1215);
        Assert.assertEquals(campaign.getProjectName(), "project1");
        Assert.assertEquals(campaign.getScheduleStartDate(), "2024-08-01T10:00:00.000+00:00");
        Assert.assertEquals(campaign.getScheduleEndDate(), "2024-08-31T10:00:00.000+00:00");
        Assert.assertEquals(campaign.getCustomFields().size(), 2);
    }


    @Test
    public void testCompleteDetails2() {
        createServerMock("GET", "/campaigns/1", 200, "{" +
                "  \"_type\" : \"campaign\"," +
                "  \"id\" : 1," +
                "  \"name\" : \"TNR\"," +
                "  \"reference\" : \"\"," +
                "  \"description\" : \"\"," +
                "  \"status\" : \"UNDEFINED\"," +
                "  \"project\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 1215," +
                "    \"name\" : \"project1\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/1215\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"path\" : \"/project1/Selenium Grid/TNR\"," +
                "  \"parent\" : {" +
                "    \"_type\" : \"campaign-folder\"," +
                "    \"id\" : 63030," +
                "    \"name\" : \"Selenium Grid\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/campaign-folders/63030\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"created_by\" : \"xxx\"," +
                "  \"created_on\" : \"2024-04-09T13:02:34.171+00:00\"," +
                "  \"last_modified_by\" : \"xxx\"," +
                "  \"last_modified_on\" : \"2024-04-09T13:04:02.224+00:00\"," +
                "  \"actual_start_date\" : null," +
                "  \"actual_end_date\" : null," +
                "  \"actual_start_auto\" : true," +
                "  \"actual_end_auto\" : true," +
                "  \"custom_fields\" : []," +
                "  \"iterations\" : [ {" +
                "    \"_type\" : \"iteration\"," +
                "    \"id\" : 128368," +
                "    \"name\" : \"Avril 2024\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/iterations/12\"" +
                "      }" +
                "    }" +
                "  } ]," +
                "  \"test_plan\" : [ ]," +
                "  \"attachments\" : [ ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1\"" +
                "    }," +
                "    \"project\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/1215\"" +
                "    }," +
                "    \"iterations\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/iterations\"" +
                "    }," +
                "    \"test-plan\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/test-plan\"" +
                "    }," +
                "    \"attachments\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/attachments\"" +
                "    }," +
                "    \"issues\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/issues\"" +
                "    }" +
                "  }" +
                "}");

        Campaign campaign = new Campaign("https://localhost:4321/campaigns/1", "campaign", 1, "TNR");
        campaign.completeDetails();

        Assert.assertEquals(campaign.getActualEndDate(), "");
        Assert.assertEquals(campaign.getActualStartDate(), "");
        Assert.assertEquals(campaign.getScheduleEndDate(), "");
        Assert.assertEquals(campaign.getScheduleStartDate(), "");
        Assert.assertEquals(campaign.getCustomFields().size(), 0);
    }


    @Test
    public void testGetFromId() {
        createServerMock("GET", "/campaigns/1", 200, "{" +
                "  \"_type\" : \"campaign\"," +
                "  \"id\" : 1," +
                "  \"name\" : \"TNR\"," +
                "  \"reference\" : \"\"," +
                "  \"description\" : \"\"," +
                "  \"status\" : \"UNDEFINED\"," +
                "  \"project\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 1215," +
                "    \"name\" : \"project1\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/1215\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"path\" : \"/project1/Selenium Grid/TNR\"," +
                "  \"parent\" : {" +
                "    \"_type\" : \"campaign-folder\"," +
                "    \"id\" : 63030," +
                "    \"name\" : \"Selenium Grid\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/campaign-folders/63030\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"created_by\" : \"xxx\"," +
                "  \"created_on\" : \"2024-04-09T13:02:34.171+00:00\"," +
                "  \"last_modified_by\" : \"xxx\"," +
                "  \"last_modified_on\" : \"2024-04-09T13:04:02.224+00:00\"," +
                "  \"actual_start_date\" : \"2024-04-09T13:03:49.169+00:00\"," +
                "  \"actual_end_date\" : \"2024-04-09T13:04:02.220+00:00\"," +
                "  \"actual_start_auto\" : true," +
                "  \"actual_end_auto\" : true," +
                "  \"custom_fields\" : [ {" +
                "    \"code\" : \"CUF_A\"," +
                "    \"label\" : \"Cuf A\"," +
                "    \"value\" : \"value of A\"" +
                "  }, {" +
                "    \"code\" : \"CUF_B\"," +
                "    \"label\" : \"Cuf B\"," +
                "    \"value\" : \"value of B\"" +
                "  } ]," +
                "  \"iterations\" : [ {" +
                "    \"_type\" : \"iteration\"," +
                "    \"id\" : 128368," +
                "    \"name\" : \"Avril 2024\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/iterations/12\"" +
                "      }" +
                "    }" +
                "  } ]," +
                "  \"test_plan\" : [ ]," +
                "  \"attachments\" : [ ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1\"" +
                "    }," +
                "    \"project\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/1215\"" +
                "    }," +
                "    \"iterations\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/iterations\"" +
                "    }," +
                "    \"test-plan\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/test-plan\"" +
                "    }," +
                "    \"attachments\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/attachments\"" +
                "    }," +
                "    \"issues\" : {" +
                "      \"href\" : \"https://localhost:4321/campaigns/1/issues\"" +
                "    }" +
                "  }" +
                "}");

        Campaign campaign = Campaign.get(1);

        Assert.assertEquals(campaign.getId(), 1);
        Assert.assertEquals(campaign.getName(), "TNR");
        Assert.assertEquals(campaign.getPath(), "/project1/Selenium Grid/TNR");
    }


    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Campaign 1 does not exist")
    public void testGetFromIdWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/campaigns/1", 404, "{}");
        when(getRequest.asJson()).thenThrow(new UnirestException("Cannot get"));

        Campaign.get(1);
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "request to https://localhost:4321 failed\\[404\\]: null")
    public void testGetFromIdWithError2() {
        createServerMock("GET", "/campaigns/1", 404, "{}");

        Campaign.get(1);
    }
}
