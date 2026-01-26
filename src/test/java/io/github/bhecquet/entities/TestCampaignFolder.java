package io.github.bhecquet.entities;


import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.*;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestCampaignFolder extends SquashTMTest {

    public static final String CAMPAIGN_FOLDER_POST_REPLY_DATA = "{" +
            "  \"_type\" : \"campaign-folder\"," +
            "  \"id\" : 33," +
            "  \"name\" : \"Campaign folder 1\"," +
            "  \"project\" : {" +
            "    \"_type\" : \"project\"," +
            "    \"id\" : 14," +
            "    \"name\" : \"Test Project 1\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/projects/14\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"path\" : \"/Test Project 1/Campaign folder 1\"," +
            "  \"parent\" : {" +
            "    \"_type\" : \"project\"," +
            "    \"id\" : 14," +
            "    \"name\" : \"Test Project 1\"," +
            "    \"_links\" : {" +
            "      \"self\" : {" +
            "        \"href\" : \"https://localhost:4321/projects/14\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"created_by\" : \"admin\"," +
            "  \"created_on\" : \"2017-06-15T10:00:00.000+0000\"," +
            "  \"last_modified_by\" : \"admin\"," +
            "  \"last_modified_on\" : \"2017-06-15T10:00:00.000+0000\"," +
            "  \"description\" : null," +
            "  \"custom_fields\" : [ {" +
            "    \"code\" : \"cuf1\"," +
            "    \"label\" : \"Lib Cuf1\"," +
            "    \"value\" : \"Cuf1 Value\"" +
            "  }, {" +
            "    \"code\" : \"cuf2\"," +
            "    \"label\" : \"Lib Cuf2\"," +
            "    \"value\" : \"true\"" +
            "  } ]," +
            "  \"attachments\" : [ ]," +
            "  \"_links\" : {" +
            "    \"self\" : {" +
            "      \"href\" : \"https://localhost:4321/campaign-folders/33\"" +
            "    }," +
            "    \"project\" : {" +
            "      \"href\" : \"https://localhost:4321/projects/14\"" +
            "    }," +
            "    \"content\" : {" +
            "      \"href\" : \"https://localhost:4321/campaign-folders/33/content\"" +
            "    }," +
            "    \"attachments\" : {" +
            "      \"href\" : \"https://localhost:4321/campaign-folders/33/attachments\"" +
            "    }" +
            "  }" +
            "}";
    public static final String GET_ALL_FOLDERS_BY_PROJECT_REPLY_DATA = "[ {"
            + "  \"_type\" : \"project\","
            + "  \"id\" : 10,"
            + "  \"name\" : \"project-1\","
            + "  \"folders\" : [ {"
            + "    \"_type\" : \"campaign-folder\","
            + "    \"id\" : 100,"
            + "    \"name\" : \"folder1\","
            + "    \"url\" : \"https://localhost:4321/campaign-folders/100\","
            + "    \"children\" : [ {"
            + "      \"_type\" : \"campaign-folder\","
            + "      \"id\" : 1000,"
            + "      \"name\" : \"sub-folder1\","
            + "      \"url\" : \"https://localhost:4321/campaign-folders/19519\","
            + "      \"children\" : [ {"
            + "        \"_type\" : \"campaign-folder\","
            + "        \"id\" : 10000,"
            + "        \"name\" : \"sub-sub-folder1\","
            + "        \"url\" : \"https://localhost:4321/campaign-folders/19880\","
            + "        \"children\" : [ ]"
            + "      }, {"
            + "        \"_type\" : \"campaign-folder\","
            + "        \"id\" : 10001,"
            + "        \"name\" : \"sub-sub-folder2\","
            + "        \"url\" : \"https://localhost:4321/campaign-folders/19881\","
            + "        \"children\" : [ ]"
            + "      } ]"
            + "    } ]"
            + "  }, {"
            + "    \"_type\" : \"campaign-folder\","
            + "    \"id\" : 101,"
            + "    \"name\" : \"folder2\","
            + "    \"url\" : \"https://localhost:4321/campaign-folders/101\","
            + "    \"children\" : [ ]"
            + "  } ]"
            + "}]";
    public static final String CAMPAIGN_FOLDER_100_REPLY_DATA = "{"
            + "  \"_type\" : \"campaign-folder\","
            + "  \"id\" : 100,"
            + "  \"name\" : \"folder1\","
            + "  \"project\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"path\" : \"/Mangrove/folder1\","
            + "  \"parent\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"created_by\" : \"User-1\","
            + "  \"created_on\" : \"2011-09-30T10:00:00.000+00:00\","
            + "  \"last_modified_by\" : \"admin\","
            + "  \"last_modified_on\" : \"2017-06-16T10:00:00.000+00:00\","
            + "  \"description\" : \"<p>where all the old campaigns go</p>\","
            + "  \"custom_fields\" : [ {"
            + "    \"code\" : \"CF_TXT\","
            + "    \"label\" : \"test level\","
            + "    \"value\" : \"mandatory\""
            + "  }, {"
            + "    \"code\" : \"CF_TAGS\","
            + "    \"label\" : \"see also\","
            + "    \"value\" : [ \"walking\", \"bipedal\" ]"
            + "  } ],"
            + "  \"attachments\" : [ ],"
            + "  \"_links\" : {"
            + "    \"self\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/100\""
            + "    },"
            + "    \"project\" : {"
            + "      \"href\" : \"https://localhost:4321/projects/14\""
            + "    },"
            + "    \"content\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/100/content\""
            + "    },"
            + "    \"attachments\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/100/attachments\""
            + "    }"
            + "  }"
            + "}";
    public static final String CAMPAIGN_FOLDER_101_REPLY_DATA = "{"
            + "  \"_type\" : \"campaign-folder\","
            + "  \"id\" : 101,"
            + "  \"name\" : \"folder2\","
            + "  \"project\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"path\" : \"/Mangrove/folder2\","
            + "  \"parent\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"created_by\" : \"User-1\","
            + "  \"created_on\" : \"2011-09-30T10:00:00.000+00:00\","
            + "  \"last_modified_by\" : \"admin\","
            + "  \"last_modified_on\" : \"2017-06-16T10:00:00.000+00:00\","
            + "  \"description\" : \"<p>where all the old campaigns go</p>\","
            + "  \"custom_fields\" : [ {"
            + "    \"code\" : \"CF_TXT\","
            + "    \"label\" : \"test level\","
            + "    \"value\" : \"mandatory\""
            + "  }, {"
            + "    \"code\" : \"CF_TAGS\","
            + "    \"label\" : \"see also\","
            + "    \"value\" : [ \"walking\", \"bipedal\" ]"
            + "  } ],"
            + "  \"attachments\" : [ ],"
            + "  \"_links\" : {"
            + "    \"self\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/101\""
            + "    },"
            + "    \"project\" : {"
            + "      \"href\" : \"https://localhost:4321/projects/14\""
            + "    },"
            + "    \"content\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/101/content\""
            + "    },"
            + "    \"attachments\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/101/attachments\""
            + "    }"
            + "  }"
            + "}";
    public static final String CAMPAIGN_FOLDER_1000_REPLY_DATA = "{"
            + "  \"_type\" : \"campaign-folder\","
            + "  \"id\" : 1000,"
            + "  \"name\" : \"sub-folder1\","
            + "  \"project\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"path\" : \"/Mangrove/folder1/sub-folder1\","
            + "  \"parent\" : {"
            + "    \"_type\" : \"campaign-folder\","
            + "    \"id\" : 100,"
            + "    \"name\" : \"folder1\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/campaign-folders/100\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"created_by\" : \"User-1\","
            + "  \"created_on\" : \"2011-09-30T10:00:00.000+00:00\","
            + "  \"last_modified_by\" : \"admin\","
            + "  \"last_modified_on\" : \"2017-06-16T10:00:00.000+00:00\","
            + "  \"description\" : \"<p>where all the old campaigns go</p>\","
            + "  \"custom_fields\" : [],"
            + "  \"attachments\" : [ ],"
            + "  \"_links\" : {"
            + "    \"self\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/1000\""
            + "    },"
            + "    \"project\" : {"
            + "      \"href\" : \"https://localhost:4321/projects/14\""
            + "    },"
            + "    \"content\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/1000/content\""
            + "    },"
            + "    \"attachments\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/1000/attachments\""
            + "    }"
            + "  }"
            + "}";
    public static final String CAMPAIGN_FOLDER_10000_REPLY_DATA = "{"
            + "  \"_type\" : \"campaign-folder\","
            + "  \"id\" : 10000,"
            + "  \"name\" : \"sub-sub-folder1\","
            + "  \"project\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"path\" : \"/Mangrove/folder1/sub-folder1/sub-sub-folder1\","
            + "  \"parent\" : {"
            + "    \"_type\" : \"campaign-folder\","
            + "    \"id\" : 1000,"
            + "    \"name\" : \"sub-folder1\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/campaign-folders/1000\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"created_by\" : \"User-1\","
            + "  \"created_on\" : \"2011-09-30T10:00:00.000+00:00\","
            + "  \"last_modified_by\" : \"admin\","
            + "  \"last_modified_on\" : \"2017-06-16T10:00:00.000+00:00\","
            + "  \"description\" : \"<p>where all the old campaigns go</p>\","
            + "  \"custom_fields\" : [],"
            + "  \"attachments\" : [ ],"
            + "  \"_links\" : {"
            + "    \"self\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/10000\""
            + "    },"
            + "    \"project\" : {"
            + "      \"href\" : \"https://localhost:4321/projects/14\""
            + "    },"
            + "    \"content\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/10000/content\""
            + "    },"
            + "    \"attachments\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/10000/attachments\""
            + "    }"
            + "  }"
            + "}";
    public static final String CAMPAIGN_FOLDER_10001_REPLY_DATA = "{"
            + "  \"_type\" : \"campaign-folder\","
            + "  \"id\" : 10001,"
            + "  \"name\" : \"sub-sub-folder2\","
            + "  \"project\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"path\" : \"/Mangrove/folder1/sub-folder1/sub-sub-folder2\","
            + "  \"parent\" : {"
            + "    \"_type\" : \"campaign-folder\","
            + "    \"id\" : 1000,"
            + "    \"name\" : \"sub-folder2\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/campaign-folders/1000\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"created_by\" : \"User-1\","
            + "  \"created_on\" : \"2011-09-30T10:00:00.000+00:00\","
            + "  \"last_modified_by\" : \"admin\","
            + "  \"last_modified_on\" : \"2017-06-16T10:00:00.000+00:00\","
            + "  \"description\" : \"<p>where all the old campaigns go</p>\","
            + "  \"custom_fields\" : [],"
            + "  \"attachments\" : [ ],"
            + "  \"_links\" : {"
            + "    \"self\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/10001\""
            + "    },"
            + "    \"project\" : {"
            + "      \"href\" : \"https://localhost:4321/projects/14\""
            + "    },"
            + "    \"content\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/10001/content\""
            + "    },"
            + "    \"attachments\" : {"
            + "      \"href\" : \"https://localhost:4321/campaign-folders/10001/attachments\""
            + "    }"
            + "  }"
            + "}";
    private Project project;

    @BeforeMethod
    public void init() {
        project = new Project("https://localhost:4321/projects/14", "project", 14, "Test Project 1");
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testGetAll() {
        createServerMock("GET", "/campaign-folders", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"campaign-folders\" : [ {" +
                "      \"_type\" : \"campaign-folder\"," +
                "      \"id\" : 100," +
                "      \"name\" : \"qualification\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folders/100\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"campaign-folder\"," +
                "      \"id\" : 101," +
                "      \"name\" : \"CP-18.01\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folders/101\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"campaign-folder\"," +
                "      \"id\" : 102," +
                "      \"name\" : \"DX-U17\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folders/102\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=0&size=3\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=0&size=3\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=1&size=3\"" +
                "    }," +
                "    \"next\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=2&size=3\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=3&size=3\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 3," +
                "    \"totalElements\" : 10," +
                "    \"totalPages\" : 4," +
                "    \"number\" : 1" +
                "  }" +
                "}");
        List<CampaignFolder> campaignFolders = CampaignFolder.getAll();
        Assert.assertEquals(campaignFolders.size(), 3);
        Assert.assertEquals(campaignFolders.get(0).getName(), "qualification");
        Assert.assertEquals(campaignFolders.get(0).getId(), 100);
        Assert.assertEquals(campaignFolders.get(0).getUrl(), "https://localhost:4321/campaign-folders/100");
    }

    @Test
    public void testGetAllWithChildren() {
        createServerMock("GET", "/campaign-folders", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"campaign-folders\" : [ {" +
                "      \"_type\" : \"campaign-folder\"," +
                "      \"id\" : 100," +
                "      \"name\" : \"qualification\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folders/100\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"campaign-folder\"," +
                "      \"id\" : 101," +
                "      \"name\" : \"CP-18.01\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folders/101\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"campaign-folder\"," +
                "      \"id\" : 102," +
                "      \"name\" : \"DX-U17\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folders/102\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=0&size=3\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=0&size=3\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=1&size=3\"" +
                "    }," +
                "    \"next\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=2&size=3\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=3&size=3\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 3," +
                "    \"totalElements\" : 10," +
                "    \"totalPages\" : 4," +
                "    \"number\" : 1" +
                "  }" +
                "}");
        List<CampaignFolder> campaignFolders = CampaignFolder.getAll();
        Assert.assertEquals(campaignFolders.size(), 3);
        Assert.assertEquals(campaignFolders.get(0).getName(), "qualification");
        Assert.assertEquals(campaignFolders.get(0).getId(), 100);
        Assert.assertEquals(campaignFolders.get(0).getUrl(), "https://localhost:4321/campaign-folders/100");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetAllWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/campaign-folders", 200, "{}", "requestBodyEntity");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);

        CampaignFolder.getAll();
    }

    /**
     * Check case where no campaigns are available
     */

    @Test
    public void testGetAllNoFolder() {
        createServerMock("GET", "/campaign-folders", 200, "{" +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=0&size=3\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=0&size=3\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=1&size=3\"" +
                "    }," +
                "    \"next\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=2&size=3\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders?page=3&size=3\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 3," +
                "    \"totalElements\" : 10," +
                "    \"totalPages\" : 4," +
                "    \"number\" : 1" +
                "  }" +
                "}");
        List<CampaignFolder> campaignFolders = CampaignFolder.getAll();
        Assert.assertEquals(campaignFolders.size(), 0);
    }


    @Test
    public void testGetAllByProject() {

        createServerMock("GET", "/campaign-folders/tree/14", 200, GET_ALL_FOLDERS_BY_PROJECT_REPLY_DATA);

        List<CampaignFolder> campaignFolders = CampaignFolder.getAll(project);
        Assert.assertEquals(campaignFolders.size(), 5);
        Assert.assertEquals(campaignFolders.get(0).getName(), "folder1");
        Assert.assertEquals(campaignFolders.get(0).getId(), 100);
        Assert.assertEquals(campaignFolders.get(0).getUrl(), "https://localhost:4321/campaign-folders/100");
        Assert.assertEquals(campaignFolders.get(0).getProject(), project);
        Assert.assertNull(campaignFolders.get(0).getParent());
        Assert.assertEquals(campaignFolders.get(1).getId(), 1000);
        Assert.assertEquals(campaignFolders.get(1).getParent(), campaignFolders.get(0));
        Assert.assertEquals(campaignFolders.get(1).getProject(), project);
        Assert.assertEquals(campaignFolders.get(2).getId(), 10000);
        Assert.assertEquals(campaignFolders.get(3).getId(), 10001);
        Assert.assertEquals(campaignFolders.get(4).getId(), 101);
    }


    @Test(expectedExceptions = SquashTmException.class)
    public void testGetAllByProjectWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/campaign-folders/tree/14", 200, "{}", "requestBodyEntity");
        when(getRequest.asJson()).thenThrow(UnirestException.class);

        CampaignFolder.getAll(project);
    }

    @Test
    public void testGetAllByProjectNoFolder() {

        createServerMock("GET", "/campaign-folders/tree/14", 200, "[ {"
                + "  \"_type\" : \"project\","
                + "  \"id\" : 10,"
                + "  \"name\" : \"project-1\","
                + "  \"folders\" : []"
                + "}]");


        List<CampaignFolder> campaignFolders = CampaignFolder.getAll(project);
        Assert.assertEquals(campaignFolders.size(), 0);
    }

    @Test
    public void testFromJsonNoParent() {

        JSONObject json = new JSONObject();
        json.put("_type", "campaign-folder");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folder/1\"" +
                "        }}"));

        CampaignFolder campaignFolder = CampaignFolder.fromJson(json);
        Assert.assertEquals(campaignFolder.getId(), 1);
        Assert.assertEquals(campaignFolder.getName(), "foo");
        Assert.assertEquals(campaignFolder.getUrl(), "https://localhost:4321/campaign-folder/1");
        Assert.assertNull(campaignFolder.getParent());
        Assert.assertNull(campaignFolder.getProject());
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {

        JSONObject json = new JSONObject();
        json.put("_type", "campaign-folder");
        json.put("id", 1);
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folder/1\"" +
                "        }}"));

        CampaignFolder.fromJson(json);
    }

    @Test
    public void testFromJsonWithProject() {

        JSONObject json = new JSONObject();
        json.put("_type", "campaign-folder");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folder/1\"" +
                "        }}"));
        json.put("project", new JSONObject("{" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 10," +
                "    \"name\" : \"Mangrove\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/project/10\"" +
                "      }" +
                "    }}"));

        CampaignFolder campaignFolder = CampaignFolder.fromJson(json);
        Assert.assertEquals(campaignFolder.getId(), 1);
        Assert.assertEquals(campaignFolder.getName(), "foo");
        Assert.assertEquals(campaignFolder.getUrl(), "https://localhost:4321/campaign-folder/1");
        Assert.assertTrue(campaignFolder.getProject() instanceof Project);
        Assert.assertEquals(campaignFolder.getProject().getId(), 10);
    }

    @Test
    public void testFromJsonParentFolder() {

        JSONObject json = new JSONObject();
        json.put("_type", "campaign-folder");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folder/1\"" +
                "        }}"));
        json.put("parent", new JSONObject("{" +
                "    \"_type\" : \"campaign-folder\"," +
                "    \"id\" : 10," +
                "    \"name\" : \"Mangrove\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/campaign-folder/10\"" +
                "      }" +
                "    }}"));

        CampaignFolder campaignFolder = CampaignFolder.fromJson(json);
        Assert.assertEquals(campaignFolder.getId(), 1);
        Assert.assertEquals(campaignFolder.getName(), "foo");
        Assert.assertEquals(campaignFolder.getUrl(), "https://localhost:4321/campaign-folder/1");
        Assert.assertTrue(campaignFolder.getParent() instanceof CampaignFolder);
        Assert.assertEquals(campaignFolder.getParent().getId(), 10);
    }

    @Test
    public void testFromJsonParentIsProject() {

        JSONObject json = new JSONObject();
        json.put("_type", "campaign-folder");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaign-folder/1\"" +
                "        }}"));
        json.put("parent", new JSONObject("{" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 10," +
                "    \"name\" : \"Mangrove\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/project/10\"" +
                "      }" +
                "    }}"));

        CampaignFolder campaignFolder = CampaignFolder.fromJson(json);
        Assert.assertEquals(campaignFolder.getId(), 1);
        Assert.assertEquals(campaignFolder.getName(), "foo");
        Assert.assertEquals(campaignFolder.getUrl(), "https://localhost:4321/campaign-folder/1");
        Assert.assertTrue(campaignFolder.getParent() instanceof Project);
        Assert.assertEquals(campaignFolder.getParent().getId(), 10);
    }

    @Test
    public void testAsJson() {
        CampaignFolder campaignFolder = new CampaignFolder(
                "https://localhost:4321/campaign-folders/7",
                7,
                "folder",
                project,
                null);
        JSONObject json = campaignFolder.asJson();
        Assert.assertEquals(json.getInt("id"), 7);
        Assert.assertEquals(json.getString("name"), "folder");
        Assert.assertEquals(json.getString("_type"), "campaign-folder");
    }

    @Test
    public void testCreateCampaignFolderNoParent() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaign-folders", 200, CAMPAIGN_FOLDER_POST_REPLY_DATA, "request");
        CampaignFolder campaignFolder = CampaignFolder.create(project, null, "Campaign folder 1");
        verify(postRequest).body(new JSONObject(" {\"_type\":\"campaign-folder\",\"name\":\"Campaign folder 1\",\"parent\":{\"_type\":\"project\",\"id\":14,\"name\":\"Test Project 1\"}}"));
        Assert.assertEquals(campaignFolder.getId(), 33);
        Assert.assertEquals(campaignFolder.getName(), "Campaign folder 1");
        Assert.assertEquals(campaignFolder.getParent().getId(), 14);
    }

    @Test
    public void testCreateCampaignFolderWithParent() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaign-folders", 200, "{" +
                "  \"_type\" : \"campaign-folder\"," +
                "  \"id\" : 33," +
                "  \"name\" : \"Campaign folder 1\"," +
                "  \"project\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 14," +
                "    \"name\" : \"Test Project 1\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/14\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"path\" : \"/Test Project 1/Campaign folder 1\"," +
                "  \"parent\" : {" +
                "    \"_type\" : \"campaign-folder\"," +
                "    \"id\" : 10," +
                "    \"name\" : \"Campaign folder parent\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/campaign-folders/10\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"created_by\" : \"admin\"," +
                "  \"created_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"last_modified_by\" : \"admin\"," +
                "  \"last_modified_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"description\" : null," +
                "  \"custom_fields\" : [ {" +
                "    \"code\" : \"cuf1\"," +
                "    \"label\" : \"Lib Cuf1\"," +
                "    \"value\" : \"Cuf1 Value\"" +
                "  }, {" +
                "    \"code\" : \"cuf2\"," +
                "    \"label\" : \"Lib Cuf2\"," +
                "    \"value\" : \"true\"" +
                "  } ]," +
                "  \"attachments\" : [ ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders/33\"" +
                "    }," +
                "    \"project\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14\"" +
                "    }," +
                "    \"content\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders/33/content\"" +
                "    }," +
                "    \"attachments\" : {" +
                "      \"href\" : \"https://localhost:4321/campaign-folders/33/attachments\"" +
                "    }" +
                "  }" +
                "}", "request");
        CampaignFolder campaignFolderParent = new CampaignFolder("https://localhost:4321/campaign-folders/10", 10, "Campaign folder parent", project, null);
        CampaignFolder campaignFolder = CampaignFolder.create(project, campaignFolderParent, "Campaign folder 1");
        verify(postRequest).body(new JSONObject("{\"_type\":\"campaign-folder\",\"name\":\"Campaign folder 1\",\"parent\":{\"_type\":\"campaign-folder\",\"id\":10,\"name\":\"Campaign folder parent\"}}"));
        Assert.assertEquals(campaignFolder.getId(), 33);
        Assert.assertEquals(campaignFolder.getName(), "Campaign folder 1");
        Assert.assertEquals(campaignFolder.getParent().getId(), 10);
    }


    @Test(expectedExceptions = SquashTmException.class)
    public void testCreateCampaignFolderWithError() {
        RequestBodyEntity postRequest = (RequestBodyEntity) createServerMock("POST", "/campaign-folders", 200, "{}", "requestBodyEntity");
        when(postRequest.asJson()).thenThrow(UnirestException.class);

        CampaignFolder.create(project, null, "Campaign folder 1");
    }

    /**
     * Check we can create a new campaign folder
     */
    @Test
    public void testCreateCampaignFolderTree() {
        // campaign folder creation
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaign-folders", 200, CAMPAIGN_FOLDER_POST_REPLY_DATA, "request");

        // campaign folder tree
        createServerMock("GET", "/campaign-folders/tree/14", 200, GET_ALL_FOLDERS_BY_PROJECT_REPLY_DATA);

        CampaignFolder createdFolder = CampaignFolder.createCampaignFolderTree(project, "foo/bar");
        Assert.assertEquals(createdFolder.getId(), 33);

        // check 2 folders has been created
        verify(postRequest).body(new JSONObject("{\"_type\":\"campaign-folder\",\"name\":\"foo\",\"parent\":{\"_type\":\"project\",\"id\":14,\"name\":\"Test Project 1\"}}"));
        verify(postRequest).body(new JSONObject("{\"_type\":\"campaign-folder\",\"name\":\"bar\",\"parent\":{\"_type\":\"campaign-folder\",\"id\":33,\"name\":\"Campaign folder 1\"}}"));
    }

    @Test
    public void testCreateCampaignFolderTreeWithCache() {
        EntityCache.setEnabled(true);

        // campaign folder creation
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaign-folders", 200, CAMPAIGN_FOLDER_POST_REPLY_DATA, "request");

        // campaign folder tree
        HttpRequest<?> getRequest = createServerMock("GET", "/campaign-folders/tree/14", 200, GET_ALL_FOLDERS_BY_PROJECT_REPLY_DATA);

        CampaignFolder.createCampaignFolderTree(project, "foo/bar");
        CampaignFolder.createCampaignFolderTree(project, "foo/bar2");

        // with cache enabled, we get list of campaign folders only once
        verify(getRequest).asJson();

        // check cache contains the new campaign folder (only one, because mock always returns the same id)
        Assert.assertNotNull(CampaignFolder.getCampaignFolderCaches().get(project).get(33));
    }

    @Test
    public void testCreateCampaignFolderTreeWithoutCache() {
        EntityCache.setEnabled(false);

        // campaign folder creation
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaign-folders", 200, CAMPAIGN_FOLDER_POST_REPLY_DATA, "request");

        // campaign folder tree
        HttpRequest<?> getRequest = createServerMock("GET", "/campaign-folders/tree/14", 200, GET_ALL_FOLDERS_BY_PROJECT_REPLY_DATA);

        CampaignFolder.createCampaignFolderTree(project, "foo/bar");
        CampaignFolder.createCampaignFolderTree(project, "foo/bar2");

        // with cache enabled, we get list of campaign folders twice
        verify(getRequest, times(4)).asJson(); // 2 for campaign creation, 2 for getting campaign folder list

        // check cache does not contain the new campaign folder
        Assert.assertNull(CampaignFolder.getCampaignFolderCaches().get(project).get(33));
    }

    @Test
    public void testCreateCampaignFolderTreeNoFolder() {
        // campaign folder creation
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaign-folders", 200, CAMPAIGN_FOLDER_POST_REPLY_DATA, "request");

        // campaign folder tree
        createServerMock("GET", "/campaign-folders/tree/14", 200, GET_ALL_FOLDERS_BY_PROJECT_REPLY_DATA);

        CampaignFolder createdFolder = CampaignFolder.createCampaignFolderTree(project, (String) null);
        Assert.assertNull(createdFolder);
    }

    /**
     * When a folder already exist do not recreate it
     */
    @Test
    public void testCreateCampaignFolderTreeAlreadyExist() {
        // campaign folder creation
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaign-folders", 200, CAMPAIGN_FOLDER_POST_REPLY_DATA, "request");

        // campaign folder tree
        createServerMock("GET", "/campaign-folders/tree/14", 200, GET_ALL_FOLDERS_BY_PROJECT_REPLY_DATA);

        createServerMock("GET", "/campaign-folders/33", 200, CAMPAIGN_FOLDER_100_REPLY_DATA);

        CampaignFolder createdFolder = CampaignFolder.createCampaignFolderTree(project, "folder1/bar");
        Assert.assertEquals(createdFolder.getId(), 33);

        // check 2 folders has been created
        verify(postRequest, never()).body(new JSONObject("{\"_type\":\"campaign-folder\",\"name\":\"folder1\",\"parent\":{\"_type\":\"project\",\"id\":14,\"name\":\"Test Project 1\"}}"));
        verify(postRequest).body(new JSONObject("{\"_type\":\"campaign-folder\",\"name\":\"bar\",\"parent\":{\"_type\":\"campaign-folder\",\"id\":100,\"name\":\"folder1\"}}"));
    }

    @Test
    public void testCreateCampaignFolderTreeAlreadyExist2() {
        // campaign folder creation
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/campaign-folders", 200, CAMPAIGN_FOLDER_POST_REPLY_DATA, "request");

        // campaign folder tree
        createServerMock("GET", "/campaign-folders/tree/14", 200, GET_ALL_FOLDERS_BY_PROJECT_REPLY_DATA);

        createServerMock("GET", "/campaign-folders/33", 200, CAMPAIGN_FOLDER_100_REPLY_DATA);

        CampaignFolder createdFolder = CampaignFolder.createCampaignFolderTree(project, "folder1/sub-folder1/bar");
        Assert.assertEquals(createdFolder.getId(), 33);

        // check 2 folders has been created
        verify(postRequest, never()).body(new JSONObject("{\"_type\":\"campaign-folder\",\"name\":\"folder1\",\"parent\":{\"_type\":\"project\",\"id\":14,\"name\":\"Test Project 1\"}}"));
        verify(postRequest).body(new JSONObject("{\"_type\":\"campaign-folder\",\"name\":\"bar\",\"parent\":{\"_type\":\"campaign-folder\",\"id\":1000,\"name\":\"sub-folder1\"}}"));
    }

    @Test
    public void testGetFromId() {
        createServerMock("GET", "/campaign-folders/100", 200, CAMPAIGN_FOLDER_100_REPLY_DATA);
        CampaignFolder campaignFolder = CampaignFolder.get(100);
        Assert.assertEquals(campaignFolder.getName(), "folder1");
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Campaign folder 100 does not exist")
    public void testGetFromIdWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/campaign-folders/100", 200, "{}");
        when(getRequest.asJson()).thenThrow(new UnirestException("Cannot get"));
        CampaignFolder campaignFolder = CampaignFolder.get(100);
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "request to https://localhost:4321 failed\\[500\\]: null")
    public void testGetFromIdWithError2() {
        createServerMock("GET", "/campaign-folders/100", 500, "{}");
        CampaignFolder campaignFolder = CampaignFolder.get(100);
    }

    @Test(expectedExceptions = NotImplementedException.class)
    public void testCompleteDetails() {
        createServerMock("GET", "/campaign-folders/100", 200, CAMPAIGN_FOLDER_100_REPLY_DATA);
        CampaignFolder campaignFolder = CampaignFolder.get(100);
        campaignFolder.completeDetails();
    }

    @Test
    public void testSetters() {
        createServerMock("GET", "/campaign-folders/100", 200, CAMPAIGN_FOLDER_100_REPLY_DATA);
        createServerMock("GET", "/campaign-folders/101", 200, CAMPAIGN_FOLDER_101_REPLY_DATA);
        CampaignFolder campaignFolder = CampaignFolder.get(100);
        campaignFolder.setParent(CampaignFolder.get(101));
        campaignFolder.setProject(new Project("url", "project", 1281, "project test"));
        Assert.assertEquals(campaignFolder.getParent().getId(), 101);
        Assert.assertEquals(campaignFolder.getParent().getName(), "folder2");
        Assert.assertEquals(campaignFolder.getProject().getId(), 1281);
        Assert.assertEquals(campaignFolder.getProject().getName(), "project test");
    }

}
