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
        Project.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testGetProjectFromName() {
        createServerMock("GET", "/projects?projectName=foo", 200, "{\n" +
                "  \"_type\" : \"project\",\n" +
                "  \"id\" : 367,\n" +
                "  \"description\" : \"<p>This project is the main sample project</p>\",\n" +
                "  \"label\" : \"Main Sample Project\",\n" +
                "  \"name\" : \"foo\",\n" +
                "  \"active\" : true,\n" +
                "  \"attachments\" : [ ],\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367\"\n" +
                "    },\n" +
                "    \"requirements\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367/requirements-library/content\"\n" +
                "    },\n" +
                "    \"test-cases\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367/test-cases-library/content\"\n" +
                "    },\n" +
                "    \"campaigns\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367/campaigns-library/content\"\n" +
                "    },\n" +
                "    \"clearances\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367/clearances\"\n" +
                "    },\n" +
                "    \"attachments\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/attachments\"\n" +
                "    }\n" +
                "  }\n" +
                "}");

        Project project = Project.get("foo");
        Assert.assertEquals(project.getName(), "foo");
        Assert.assertEquals(project.getId(), 367);
        Assert.assertEquals(project.getUrl(), "https://localhost:4321/projects/367");
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testGetProjectFromNameWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/projects?projectName=foo", 200, "{}", "requestBodyEntity");
        when(getRequest.asJson()).thenThrow(UnirestException.class);

        Project.get("foo");
    }

    @Test
    public void testGetProjectFromId() {
        createServerMock("GET", "/projects/367", 200, "{\n" +
                "  \"_type\" : \"project\",\n" +
                "  \"id\" : 367,\n" +
                "  \"description\" : \"<p>This project is the main sample project</p>\",\n" +
                "  \"label\" : \"Main Sample Project\",\n" +
                "  \"name\" : \"foo\",\n" +
                "  \"active\" : true,\n" +
                "  \"attachments\" : [ ],\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367\"\n" +
                "    },\n" +
                "    \"requirements\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367/requirements-library/content\"\n" +
                "    },\n" +
                "    \"test-cases\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367/test-cases-library/content\"\n" +
                "    },\n" +
                "    \"campaigns\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367/campaigns-library/content\"\n" +
                "    },\n" +
                "    \"clearances\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367/clearances\"\n" +
                "    },\n" +
                "    \"attachments\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/attachments\"\n" +
                "    }\n" +
                "  }\n" +
                "}");

        Project project = Project.get(367);
        Assert.assertEquals(project.getName(), "foo");
        Assert.assertEquals(project.getId(), 367);
        Assert.assertEquals(project.getUrl(), "https://localhost:4321/projects/367");
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testGetProjectFromIdWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/projects/367", 200, "{}", "requestBodyEntity");
        when(getRequest.asJson()).thenThrow(UnirestException.class);

        Project.get(367);
    }

    @Test
    public void testGetAll() {
        createServerMock("GET", "/projects", 200, "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"projects\" : [ {\n" +
                "      \"_type\" : \"project\",\n" +
                "      \"id\" : 367,\n" +
                "      \"name\" : \"sample project 1\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/projects/367\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"project\",\n" +
                "      \"id\" : 456,\n" +
                "      \"name\" : \"sample project 2\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/projects/456\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"project\",\n" +
                "      \"id\" : 789,\n" +
                "      \"name\" : \"sample project 3\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/projects/789\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ],\n" +
                "    \"project-templates\" : [ {\n" +
                "      \"_type\" : \"project-template\",\n" +
                "      \"id\" : 971,\n" +
                "      \"name\" : \"project template 4\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/projects/971\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects?page=0&size=4\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"page\" : {\n" +
                "    \"size\" : 4,\n" +
                "    \"totalElements\" : 4,\n" +
                "    \"totalPages\" : 1,\n" +
                "    \"number\" : 0\n" +
                "  }" +
                "}");

        List<Project> projects = Project.getAll();
        Assert.assertEquals(projects.size(), 3);
        Assert.assertEquals(projects.get(0).getName(), "sample project 1");
        Assert.assertEquals(projects.get(1).getId(), 456);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetAllWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/projects", 200, "{}", "requestBodyEntity");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);

        Project.getAll();
    }

    @Test
    public void testAsJson() {
        Project project = new Project("https://localhost:4321/projects/971", "project", 971, "myProject");
        Assert.assertEquals(project.asJson(), new JSONObject("{\"_type\":\"project\",\"id\":971,\"name\":\"myProject\"}"));
    }

    @Test
    public void testFromJson() {
        Project project = Project.fromJson(new JSONObject("{\n" +
                "      \"_type\" : \"project\",\n" +
                "      \"id\" : 789,\n" +
                "      \"name\" : \"myProject\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/projects/789\"\n" +
                "        }\n" +
                "      }" +
                "   }"));
        Assert.assertEquals(project.getName(), "myProject");
        Assert.assertEquals(project.getId(), 789);
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonMissingData() {
        Project project = Project.fromJson(new JSONObject("{\n" +
                "      \"_type\" : \"project\",\n" +
                "      \"id\" : 789,\n" +
                "      \"name\" : \"myProject\"\n" +
                "   }"));
        Assert.assertEquals(project.getName(), "myProject");
        Assert.assertEquals(project.getId(), 789);
    }

    @Test
    public void testGetCampaigns() {
        createServerMock("GET", "/projects/14/campaigns?sort=id&fields=path,name,reference", 200, "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"campaigns\" : [ {\n" +
                "      \"_type\" : \"campaign\",\n" +
                "      \"id\" : 255,\n" +
                "      \"name\" : \"campaign 1\",\n" +
                "      \"reference\" : \"C-1\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/campaigns/255\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"campaign\",\n" +
                "      \"id\" : 122,\n" +
                "      \"name\" : \"campaign 2\",\n" +
                "      \"reference\" : \"C-2\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/campaigns/122\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"campaign\",\n" +
                "      \"id\" : 147,\n" +
                "      \"name\" : \"campaign 3\",\n" +
                "      \"reference\" : \"C-3\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/campaigns/147\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"first\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=0&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"prev\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=1&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=2&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"next\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=3&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"last\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=3&size=3&sort=name,desc\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"page\" : {\n" +
                "    \"size\" : 3,\n" +
                "    \"totalElements\" : 10,\n" +
                "    \"totalPages\" : 4,\n" +
                "    \"number\" : 2\n" +
                "  }\n" +
                "}");

        Project project = new Project("https://localhost:4321/projects/14", "project", 14, "myProject");
        List<Campaign> campaigns = project.getCampaigns();
        Assert.assertEquals(campaigns.size(), 3);
        Assert.assertEquals(campaigns.get(0).getName(), "campaign 1");
        Assert.assertNull(campaigns.get(0).getPath());
    }

    @Test
    public void testGetCampaignsWithPathAndCustomFields() {
        createServerMock("GET", "/projects/14/campaigns?sort=id&fields=path,name,reference", 200, "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"campaigns\" : [ {\n" +
                "      \"_type\" : \"campaign\",\n" +
                "      \"id\" : 255,\n" +
                "      \"name\" : \"campaign 1\",\n" +
                "      \"path\": \"/folder1/folder2\"," +
                "      \"reference\" : \"C-1\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/campaigns/255\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"campaign\",\n" +
                "      \"id\" : 122,\n" +
                "      \"name\" : \"campaign 2\",\n" +
                "      \"reference\" : \"C-2\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/campaigns/122\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"campaign\",\n" +
                "      \"id\" : 147,\n" +
                "      \"name\" : \"campaign 3\",\n" +
                "      \"reference\" : \"C-3\",\n" +
                "      \"custom_fields\" : [ {\n" +
                "          \"code\" : \"cuf_txt_note\",\n" +
                "          \"label\" : \"Cuf A\"," +
                "          \"value\" : \"Star Trek style welcomed but not mandatory\"\n" +
                "        }, {\n" +
                "          \"code\" : \"cuf_tags_see_also\",\n" +
                "          \"label\" : \"Cuf B\"," +
                "          \"value\" : [ \"smart home\", \"sensors\", \"hand gesture\" ]\n" +
                "        } ]," +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/campaigns/147\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"first\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=0&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"prev\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=1&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=2&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"next\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=3&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"last\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/campaigns?page=3&size=3&sort=name,desc\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"page\" : {\n" +
                "    \"size\" : 3,\n" +
                "    \"totalElements\" : 10,\n" +
                "    \"totalPages\" : 4,\n" +
                "    \"number\" : 2\n" +
                "  }\n" +
                "}");

        Project project = new Project("https://localhost:4321/projects/14", "project", 14, "myProject");
        List<Campaign> campaigns = project.getCampaigns();
        Assert.assertEquals(campaigns.size(), 3);
        Assert.assertEquals(campaigns.get(0).getName(), "campaign 1");
        Assert.assertEquals(campaigns.get(0).getPath(), "/folder1/folder2");
        Assert.assertEquals(campaigns.get(2).getCustomFields().size(), 2);
        Assert.assertEquals(campaigns.get(2).getCustomFields().get(0).getCode(), "cuf_txt_note");
    }


    @Test(expectedExceptions = SquashTmException.class)
    public void testGetCampaignsWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/projects/14/campaigns?sort=id&fields=path,name,reference", 200, "{}", "requestBodyEntity");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);

        Project project = new Project("https://localhost:4321/projects/14", "project", 14, "myProject");
        project.getCampaigns();
    }
}
