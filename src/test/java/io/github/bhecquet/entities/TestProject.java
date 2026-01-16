package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.ConfigurationException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.*;
import kong.unirest.core.json.JSONObject;
import org.mockito.MockedStatic;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    public void testGetProjectFromNameWithCharsToEncode() {
        createServerMock("GET", "/projects?projectName=foo%5B", 200, "{\n" +
                "  \"_type\" : \"project\",\n" +
                "  \"id\" : 367,\n" +
                "  \"description\" : \"<p>This project is the main sample project</p>\",\n" +
                "  \"label\" : \"Main Sample Project\",\n" +
                "  \"name\" : \"foo[\",\n" +
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

        Project project = Project.get("foo[");
        Assert.assertEquals(project.getName(), "foo[");
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


    @Test
    public void testGetRequirements() {
        createServerMock("GET", "/projects/14/requirements?sort=id&fields=path,name,reference", 200, "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"requirements\" : [ {\n" +
                "      \"_type\" : \"requirement\",\n" +
                "      \"id\" : 122,\n" +
                "      \"name\" : \"requirement 1\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:4321/api/rest/latest/requirements/122\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"requirement\",\n" +
                "      \"id\" : 147,\n" +
                "      \"name\" : \"requirement 2\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:4321/api/rest/latest/requirements/147\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"requirement\",\n" +
                "      \"id\" : 255,\n" +
                "      \"name\" : \"requirement 3\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:4321/api/rest/latest/requirements/255\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"first\" : {\n" +
                "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/14/requirements?page=0&size=3&sort=id,desc\"\n" +
                "    },\n" +
                "    \"prev\" : {\n" +
                "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/14/requirements?page=1&size=3&sort=id,desc\"\n" +
                "    },\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/14/requirements?page=2&size=3&sort=id,desc\"\n" +
                "    },\n" +
                "    \"next\" : {\n" +
                "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/14/requirements?page=3&size=3&sort=id,desc\"\n" +
                "    },\n" +
                "    \"last\" : {\n" +
                "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/14/requirements?page=3&size=3&sort=id,desc\"\n" +
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
        List<Requirement> requirements = project.getRequirements();
        Assert.assertEquals(requirements.size(), 3);
        Assert.assertEquals(requirements.get(0).getName(), "requirement 1");
        Assert.assertNull(requirements.get(0).getPath());
    }

    @Test
    public void testGetRequirementsWithPath() {
        createServerMock("GET", "/projects/14/requirements?sort=id&fields=path,name,reference", 200, "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"requirements\" : [ {\n" +
                "      \"_type\" : \"requirement\",\n" +
                "      \"id\" : 122,\n" +
                "      \"name\" : \"requirement 1\",\n" +
                "      \"path\" : \"folder1/folder2\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:4321/api/rest/latest/requirements/122\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"requirement\",\n" +
                "      \"id\" : 147,\n" +
                "      \"name\" : \"requirement 2\",\n" +
                "      \"path\" : \"folder1/folder2\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:4321/api/rest/latest/requirements/147\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"requirement\",\n" +
                "      \"id\" : 255,\n" +
                "      \"name\" : \"requirement 3\",\n" +
                "      \"path\" : \"folder1\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:4321/api/rest/latest/requirements/255\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"first\" : {\n" +
                "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/14/requirements?page=0&size=3&sort=id,desc\"\n" +
                "    },\n" +
                "    \"prev\" : {\n" +
                "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/14/requirements?page=1&size=3&sort=id,desc\"\n" +
                "    },\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/14/requirements?page=2&size=3&sort=id,desc\"\n" +
                "    },\n" +
                "    \"next\" : {\n" +
                "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/14/requirements?page=3&size=3&sort=id,desc\"\n" +
                "    },\n" +
                "    \"last\" : {\n" +
                "      \"href\" : \"http://localhost:4321/api/rest/latest/projects/14/requirements?page=3&size=3&sort=id,desc\"\n" +
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
        List<Requirement> requirements = project.getRequirements();
        Assert.assertEquals(requirements.size(), 3);
        Assert.assertEquals(requirements.get(0).getName(), "requirement 1");
        Assert.assertEquals(requirements.get(0).getPath(), "folder1/folder2");
    }


    @Test(expectedExceptions = SquashTmException.class)
    public void testGetRequirementsWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/projects/14/requirements?sort=id&fields=path,name,reference", 200, "{}", "requestBodyEntity");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);

        Project project = new Project("https://localhost:4321/projects/14", "project", 14, "myProject");
        project.getRequirements();
    }

    @Test
    public void testSetClearance() {
        Project project = new Project("https://localhost:4321/projects/367", "project", 367, "myProject");

        createServerMock("POST", "/projects/367/clearances/7/users/486,521", 200, "{\n" +
                "  \"content\" : {\n" +
                "    \"test_designer\" : {\n" +
                "      \"_type\" : \"profile\",\n" +
                "      \"id\" : 7,\n" +
                "      \"name\" : \"TestDesigner\",\n" +
                "      \"type\" : \"system\",\n" +
                "      \"users\" : [ {\n" +
                "        \"_type\" : \"user\",\n" +
                "        \"id\" : 486,\n" +
                "        \"login\" : \"User-1\",\n" +
                "        \"_links\" : {\n" +
                "          \"self\" : {\n" +
                "            \"href\" : \"https://localhost:4321/users/486\"\n" +
                "          }\n" +
                "        }\n" +
                "      }, {\n" +
                "        \"_type\" : \"user\",\n" +
                "        \"id\" : 521,\n" +
                "        \"login\" : \"User-2\",\n" +
                "        \"_links\" : {\n" +
                "          \"self\" : {\n" +
                "            \"href\" : \"https://localhost:4321/users/521\"\n" +
                "          }\n" +
                "        }\n" +
                "      } ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/367/clearances\"\n" +
                "    }\n" +
                "  }\n" +
                "}");

        // no error
        project.setClearances(7, List.of(486, 521));
    }


    @Test(expectedExceptions = SquashTmException.class)
    public void testSetClearanceWithError() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/projects/367/clearances/7/users/486,521", 200, "{}", "request");
        when(postRequest.asJson()).thenThrow(UnirestException.class);

        Project project = new Project("https://localhost:4321/projects/367", "project", 367, "myProject");
        project.setClearances(7, List.of(486, 521));
    }

    @Test
    public void testDeleteClearance() {
        Project project = new Project("https://localhost:4321/projects/367", "project", 367, "myProject");

        createServerMock("DELETE", "/projects/367/users/486,521", 200, "");
        project.deleteClearances(List.of(486, 521));
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testDeleteClearanceWithError() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("DELETE", "/projects/367/users/486,521", 200, "", "request");
        when(postRequest.asJson()).thenThrow(UnirestException.class);

        Project project = new Project("https://localhost:4321/projects/367", "project", 367, "myProject");
        project.deleteClearances(List.of(486, 521));
    }

    @Test
    public void testGetTestCases() {
        createServerMock("GET", "/projects/14/test-cases?sort=id", 200, "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"test-cases\" : [ {\n" +
                "      \"_type\" : \"test-case\",\n" +
                "      \"id\" : 122,\n" +
                "      \"name\" : \"test case 1\",\n" +
                "      \"reference\" : \"TC-1\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/test-cases/122\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"scripted-test-case\",\n" +
                "      \"id\" : 222,\n" +
                "      \"name\" : \"scripted test case 1\",\n" +
                "      \"reference\" : \"STC-1\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/test-cases/222\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"_type\" : \"keyword-test-case\",\n" +
                "      \"id\" : 322,\n" +
                "      \"name\" : \"keyword test case 1\",\n" +
                "      \"reference\" : \"KTC-1\",\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"https://localhost:4321/test-cases/322\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"_links\" : {\n" +
                "    \"first\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/test-cases?page=0&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"prev\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/test-cases?page=1&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/test-cases?page=2&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"next\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/test-cases?page=3&size=3&sort=name,desc\"\n" +
                "    },\n" +
                "    \"last\" : {\n" +
                "      \"href\" : \"https://localhost:4321/projects/14/test-cases?page=3&size=3&sort=name,desc\"\n" +
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
        List<TestCase> testCases = project.getTestCases();
        Assert.assertEquals(testCases.size(), 3);
        Assert.assertEquals(testCases.get(0).getName(), "test case 1");
        Assert.assertEquals(testCases.get(0).getUrl(), "https://localhost:4321/test-cases/122");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetTestCasesWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/projects/14/test-cases?sort=id", 200, "{}");
        when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenThrow(UnirestException.class);
        Project project = new Project("https://localhost:4321/projects/14", "project", 14, "myProject");
        project.getTestCases();
    }

    @Test
    public void testBindCustomField() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/projects/14/custom-fields/CAMPAIGN", 200, "{}", "request");
        Project project = new Project("https://localhost:4321/projects/14", "project", 14, "myProject");
        try (MockedStatic<CustomField> mockedCf = mockStatic(CustomField.class)) {
            mockedCf.when(() -> CustomField.getByCode("FOO")).thenReturn(new CustomField("https://localhost:4321/custom-fields/12", "custom-field", 12, "foo", "Foo", "CODE"));
            project.bindCustomField("FOO", "CAMPAIGN");
            verify(postRequest).body("cufId=12");
        }
    }

    @Test(expectedExceptions = ConfigurationException.class, expectedExceptionsMessageRegExp = "No custom field with code FOO exist in this instance")
    public void testBindCustomFieldNoMatchingCustomField() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/projects/14/custom-fields/CAMPAIGN", 200, "{}", "request");
        Project project = new Project("https://localhost:4321/projects/14", "project", 14, "myProject");
        try (MockedStatic<CustomField> mockedCf = mockStatic(CustomField.class)) {
            mockedCf.when(() -> CustomField.getByCode("FOO")).thenReturn(null);
            project.bindCustomField("FOO", "CAMPAIGN");
            verify(postRequest).body("cufId=12");
        }
    }

    @Test(expectedExceptions = ConfigurationException.class, expectedExceptionsMessageRegExp = ".*Entity type CAMP is not allowed.*")
    public void testBindCustomFieldBadEntityType() {
        Project project = new Project("https://localhost:4321/projects/14", "project", 14, "myProject");
        try (MockedStatic<CustomField> mockedCf = mockStatic(CustomField.class)) {
            mockedCf.when(() -> CustomField.getByCode("FOO")).thenReturn(new CustomField("https://localhost:4321/custom-fields/12", "custom-field", 12, "foo", "Foo", "CODE"));
            project.bindCustomField("FOO", "CAMP");
        }
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Cannot bing CUF")
    public void testBindCustomFieldError2() {
        RequestBodyEntity postRequest = (RequestBodyEntity) createServerMock("POST", "/projects/14/custom-fields/CAMPAIGN", 200, "OK", "requestBodyEntity");
        when(postRequest.asString()).thenThrow(UnirestException.class);
        Project project = new Project("https://localhost:4321/projects/14", "project", 14, "myProject");
        try (MockedStatic<CustomField> mockedCf = mockStatic(CustomField.class)) {
            mockedCf.when(() -> CustomField.getByCode("FOO")).thenReturn(new CustomField("https://localhost:4321/custom-fields/12", "custom-field", 12, "foo", "Foo", "CODE"));
            project.bindCustomField("FOO", "CAMPAIGN");
        }
    }
}
