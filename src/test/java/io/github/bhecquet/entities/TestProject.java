package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.ConfigurationException;
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

public class TestProject extends SquashTMTest {

    @BeforeMethod
    public void init() {
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testGetAll() {
        createServerMock("GET", "/projects", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"projects\" : [ {" +
                "      \"_type\" : \"project\"," +
                "      \"id\" : 367," +
                "      \"name\" : \"sample project 1\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/projects/367\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"project\"," +
                "      \"id\" : 456," +
                "      \"name\" : \"sample project 2\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/projects/456\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"project\"," +
                "      \"id\" : 789," +
                "      \"name\" : \"sample project 3\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/projects/789\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/projects?page=0&size=3\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 3," +
                "    \"totalElements\" : 3," +
                "    \"totalPages\" : 1," +
                "    \"number\" : 0" +
                "  }" +
                "}");
        List<Project> projects = Project.getAll();
        Assert.assertEquals(projects.size(), 3);
        Assert.assertEquals(projects.get(0).getName(), "sample project 1");
        Assert.assertEquals(projects.get(0).getId(), 367);
        Assert.assertEquals(projects.get(0).getUrl(), "https://localhost:4321/projects/367");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetAllWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/projects", 200, "{}", "requestBodyEntity");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);

        Project.getAll();
    }

    @Test
    public void testGet() {
        createServerMock("GET", "/projects?projectName=sample project", 200,
                "{"
                        + "  \"_type\" : \"project\","
                        + "  \"id\" : 367,"
                        + "  \"description\" : \"<p>This project is the main sample project</p>\","
                        + "  \"label\" : \"Main Sample Project\","
                        + "  \"name\" : \"sample project\","
                        + "  \"active\" : true,"
                        + "  \"attachments\" : [ ],"
                        + "  \"_links\" : {"
                        + "    \"self\" : {"
                        + "      \"href\" : \"https://localhost:4321/projects/367\""
                        + "    },"
                        + "    \"requirements\" : {"
                        + "      \"href\" : \"https://localhost:4321/projects/367/requirements-library/content\""
                        + "    },"
                        + "    \"test-cases\" : {"
                        + "      \"href\" : \"https://localhost:4321/projects/367/test-cases-library/content\""
                        + "    },"
                        + "    \"campaigns\" : {"
                        + "      \"href\" : \"https://localhost:4321/projects/367/campaigns-library/content\""
                        + "    },"
                        + "    \"permissions\" : {"
                        + "      \"href\" : \"https://localhost:4321/projects/367/permissions\""
                        + "    },"
                        + "    \"attachments\" : {"
                        + "      \"href\" : \"https://localhost:4321/projects/attachments\""
                        + "    }"
                        + "  }"
                        + "}");
        Project project = Project.get("sample project");
        Assert.assertEquals(project.getName(), "sample project");
        Assert.assertEquals(project.getId(), 367);
        Assert.assertEquals(project.getUrl(), "https://localhost:4321/projects/367");
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testGetWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/projects?projectName=sample project", 200, "{}", "requestBodyEntity");
        when(getRequest.asJson()).thenThrow(UnirestException.class);

        Project.get("sample project");
    }


    @Test
    public void testAsJson() {
        Project project = new Project("https://localhost:4321/projects/1", "project", 1, "project");
        JSONObject json = project.asJson();
        Assert.assertEquals(json.getInt("id"), 1);
        Assert.assertEquals(json.getString("_type"), "project");
    }


    @Test
    public void testFromJson() {

        JSONObject json = new JSONObject();
        json.put("_type", "project");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/projects/1\"" +
                "        }}"));

        Project project = Project.fromJson(json);
        Assert.assertEquals(project.getId(), 1);
        Assert.assertEquals(project.getName(), "foo");
        Assert.assertEquals(project.getUrl(), "https://localhost:4321/projects/1");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {

        JSONObject json = new JSONObject();
        json.put("_type", "project");
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/projects/1\"" +
                "        }}"));

        Project.fromJson(json);
    }

    @Test
    public void testGetCampaignsInProject() {
        createServerMock("GET", "/projects/44/campaigns?sort=id&fields=path,name,reference", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"campaigns\" : [ {" +
                "      \"_type\" : \"campaign\"," +
                "      \"id\" : 255," +
                "      \"name\" : \"campaign 1\"," +
                "      \"path\" : \"/project/campaign 1\"," +
                "      \"reference\" : \"C-1\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaigns/255\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"campaign\"," +
                "      \"id\" : 122," +
                "      \"name\" : \"campaign 2\"," +
                "      \"path\" : \"/project/campaign 2\"," +
                "      \"reference\" : \"C-2\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaigns/122\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"campaign\"," +
                "      \"id\" : 147," +
                "      \"name\" : \"campaign 3\"," +
                "      \"path\" : \"/project/campaign 3\"," +
                "      \"reference\" : \"C-3\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaigns/147\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=0&size=3&sort=name,desc\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=1&size=3&sort=name,desc\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=2&size=3&sort=name,desc\"" +
                "    }," +
                "    \"next\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=3&size=3&sort=name,desc\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=3&size=3&sort=name,desc\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 3," +
                "    \"totalElements\" : 10," +
                "    \"totalPages\" : 4," +
                "    \"number\" : 2" +
                "  }" +
                "}", "request");

        Project project = new Project("https://localhost:4321/projects/44", "project", 44, "project");
        List<Campaign> campaigns = project.getCampaigns();
        Assert.assertEquals(campaigns.size(), 3);
        Assert.assertEquals(campaigns.get(0).getName(), "campaign 1");
        Assert.assertEquals(campaigns.get(0).getId(), 255);

    }

    @Test
    public void testGetCampaignsInProjectWithOtherFields() {
        createServerMock("GET", "/projects/44/campaigns?sort=id&fields=name", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"campaigns\" : [ {" +
                "      \"_type\" : \"campaign\"," +
                "      \"id\" : 255," +
                "      \"name\" : \"campaign 1\"," +
                "      \"path\" : \"/project/campaign 1\"," +
                "      \"reference\" : \"C-1\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaigns/255\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"campaign\"," +
                "      \"id\" : 122," +
                "      \"name\" : \"campaign 2\"," +
                "      \"path\" : \"/project/campaign 2\"," +
                "      \"reference\" : \"C-2\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaigns/122\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"_type\" : \"campaign\"," +
                "      \"id\" : 147," +
                "      \"name\" : \"campaign 3\"," +
                "      \"path\" : \"/project/campaign 3\"," +
                "      \"reference\" : \"C-3\"," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaigns/147\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=0&size=3&sort=name,desc\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=1&size=3&sort=name,desc\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=2&size=3&sort=name,desc\"" +
                "    }," +
                "    \"next\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=3&size=3&sort=name,desc\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=3&size=3&sort=name,desc\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 3," +
                "    \"totalElements\" : 10," +
                "    \"totalPages\" : 4," +
                "    \"number\" : 2" +
                "  }" +
                "}", "request");

        Project project = new Project("https://localhost:4321/projects/44", "project", 44, "project");
        List<Campaign> campaigns = project.getCampaigns("name");
        Assert.assertEquals(campaigns.size(), 3);
        Assert.assertEquals(campaigns.get(0).getName(), "campaign 1");
        Assert.assertEquals(campaigns.get(0).getId(), 255);

    }

    @Test
    public void testGetCampaignsInProjectWithOtherFields2() {
        createServerMock("GET", "/projects/44/campaigns?sort=id&fields=name,custom_fields", 200, "{" +
                "  \"_embedded\" : {" +
                "    \"campaigns\" : [ {" +
                "      \"_type\" : \"campaign\"," +
                "      \"id\" : 255," +
                "      \"name\" : \"campaign 1\"," +
                "      \"custom_fields\" : [ {\n" +
                "        \"code\" : \"APP\",\n" +
                "        \"label\" : \"app\",\n" +
                "        \"value\" : [ \"\" ]\n" +
                "      }, {\n" +
                "        \"code\" : \"ApplicationTNR\",\n" +
                "        \"label\" : \"Application\",\n" +
                "        \"value\" : null\n" +
                "      } ]," +
                "      \"_links\" : {" +
                "        \"self\" : {" +
                "          \"href\" : \"https://localhost:4321/campaigns/255\"" +
                "        }" +
                "      }" +
                "    } ]" +
                "  }," +
                "  \"_links\" : {" +
                "    \"first\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=0&size=3&sort=name,desc\"" +
                "    }," +
                "    \"prev\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=1&size=3&sort=name,desc\"" +
                "    }," +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=2&size=3&sort=name,desc\"" +
                "    }," +
                "    \"next\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=3&size=3&sort=name,desc\"" +
                "    }," +
                "    \"last\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=3&size=3&sort=name,desc\"" +
                "    }" +
                "  }," +
                "  \"page\" : {" +
                "    \"size\" : 3," +
                "    \"totalElements\" : 10," +
                "    \"totalPages\" : 4," +
                "    \"number\" : 2" +
                "  }" +
                "}", "request");

        Project project = new Project("https://localhost:4321/projects/44", "project", 44, "project");
        List<Campaign> campaigns = project.getCampaigns("name,custom_fields");
        Assert.assertEquals(campaigns.size(), 1);
        Assert.assertEquals(campaigns.get(0).getName(), "campaign 1");
        Assert.assertEquals(campaigns.get(0).getId(), 255);
        Assert.assertEquals(campaigns.get(0).getCustomFields().size(), 2);

    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetCampaignsInProjectWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/projects/44/campaigns?sort=id&fields=path,name,reference", 200, "{}", "request");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);

        Project project = new Project("https://localhost:4321/projects/44", "project", 44, "project");
        project.getCampaigns();

    }
}
