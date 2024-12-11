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

    @Test
    public void testSetClearances() {
        //Simulate get Project
        createServerMock("GET", "/projects?projectName=temp+v8", 200, "{  \"_type\": \"project\",  \"id\": 1281,  \"description\": \"\",  \"label\": \"\",  \"name\": \"temp v8\",  \"active\": true,  \"attachments\": [],  \"_links\": {    \"self\": {      \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/projects/1281\"    },    \"requirements\": {      \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/projects/1281/requirements-library/content\"    },    \"test-cases\": {      \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/projects/1281/test-cases-library/content\"    },    \"campaigns\": {      \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/projects/1281/campaigns-library/content\"    },    \"clearances\": {      \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/projects/1281/clearances\"    },    \"attachments\": {      \"href\": \"http://squash-tm-dev.covea.priv/squash/api/rest/latest/projects/attachments\"    }  }}", "request");
        //Simulate get Teams
        createServerMock("GET", "/teams?sort=id", 200, "{  \"_embedded\": {    \"teams\": [      {        \"_type\": \"team\",        \"id\": 8,        \"name\": \"CDSV_LeMans_Chartres\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/8\"          }        }      },      {        \"_type\": \"team\",        \"id\": 30,        \"name\": \"VMOA_IARD_EQUIPE\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/30\"          }        }      },      {        \"_type\": \"team\",        \"id\": 32,        \"name\": \"VNR\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/32\"          }        }      },      {        \"_type\": \"team\",        \"id\": 54,        \"name\": \"OBSOLETE - - - - - VMOE CHARTRES\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/54\"          }        }      },      {        \"_type\": \"team\",        \"id\": 130,        \"name\": \"VALIDATION PTR9\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/130\"          }        }      },      {        \"_type\": \"team\",        \"id\": 138,        \"name\": \"ETUDES_ AIA\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/138\"          }        }      },      {        \"_type\": \"team\",        \"id\": 146,        \"name\": \"ETUDES_SINISTRES\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/146\"          }        }      },      {        \"_type\": \"team\",        \"id\": 159,        \"name\": \"VMOA_EPARGNE_EQUIPE\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/159\"          }        }      },      {        \"_type\": \"team\",        \"id\": 183,        \"name\": \"VMOA AFFINITY\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/183\"          }        }      },      {        \"_type\": \"team\",        \"id\": 195,        \"name\": \"ETUDES IARD\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/195\"          }        }      },      {        \"_type\": \"team\",        \"id\": 205,        \"name\": \"VALIDATION PTR5\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/205\"          }        }      },      {        \"_type\": \"team\",        \"id\": 237,        \"name\": \"VMOA SISM\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/237\"          }        }      },      {        \"_type\": \"team\",        \"id\": 248,        \"name\": \"VMOE SISM invités\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/248\"          }        }      },      {        \"_type\": \"team\",        \"id\": 256,        \"name\": \"PTR5 invités\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/256\"          }        }      },      {        \"_type\": \"team\",        \"id\": 265,        \"name\": \"Pilotes de versions\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/265\"          }        }      },      {        \"_type\": \"team\",        \"id\": 307,        \"name\": \"Equipe DCM Chef de projet\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/307\"          }        }      },      {        \"_type\": \"team\",        \"id\": 311,        \"name\": \"Equipe DCM Invité\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/311\"          }        }      },      {        \"_type\": \"team\",        \"id\": 377,        \"name\": \"AM_EPARGNE_EQUIPE\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/377\"          }        }      },      {        \"_type\": \"team\",        \"id\": 384,        \"name\": \"PARAM_EPARGNE_EQUIPE\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/384\"          }        }      },      {        \"_type\": \"team\",        \"id\": 641,        \"name\": \"VMOA VMOE SISM invités\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/641\"          }        }      },      {        \"_type\": \"team\",        \"id\": 895,        \"name\": \"TS SIRH Paie invités\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/895\"          }        }      },      {        \"_type\": \"team\",        \"id\": 1080,        \"name\": \"GMF-TAU-VIE-Invites\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/1080\"          }        }      },      {        \"_type\": \"team\",        \"id\": 1127,        \"name\": \"AMOA  LMC INDEMNISATION\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/1127\"          }        }      },      {        \"_type\": \"team\",        \"id\": 1169,        \"name\": \"GMF-TAU-VIE-Testeurs-Avancés\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/1169\"          }        }      },      {        \"_type\": \"team\",        \"id\": 2054,        \"name\": \"CDV_NIORT\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/2054\"          }        }      },      {        \"_type\": \"team\",        \"id\": 2325,        \"name\": \"GMF-TAU-VIE-Chef de projet\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/2325\"          }        }      },      {        \"_type\": \"team\",        \"id\": 2706,        \"name\": \"Responsable des CDV\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/2706\"          }        }      },      {        \"_type\": \"team\",        \"id\": 2773,        \"name\": \"Recette_EPM\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/2773\"          }        }      },      {        \"_type\": \"team\",        \"id\": 3156,        \"name\": \"EQUIPE AMOA CMP MMA\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/3156\"          }        }      },      {        \"_type\": \"team\",        \"id\": 3335,        \"name\": \"SUIVI PRODUCTION DAV\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/3335\"          }        }      },      {        \"_type\": \"team\",        \"id\": 3844,        \"name\": \"Testeur_Archivistes\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/3844\"          }        }      },      {        \"_type\": \"team\",        \"id\": 3883,        \"name\": \"Val_SCS\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/3883\"          }        }      },      {        \"_type\": \"team\",        \"id\": 3924,        \"name\": \"P9\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/3924\"          }        }      },      {        \"_type\": \"team\",        \"id\": 3930,        \"name\": \"MMA_SINISTRES_TESTEUR_REFERENT\",        \"_links\": {          \"self\": {            \"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/3930\"          }        }      }    ]  },  \"_links\": {    \"self\": {      \"href\": \"http://squash-tm-dev.covea.priv/squash/api/rest/latest/teams?page=0&size=50&sort=id,asc\"    }  },  \"page\": {    \"size\": 50,    \"totalElements\": 34,    \"totalPages\": 1,    \"number\": 0  }}", "request");
        //Simulate Post new clearance
        createServerMock("POST", "/clearances/2/users/384", 200, "{\"content\": {\"automated_test_writer\": {\"_type\": \"profile\",\"id\": 2,\"name\": \"TestEditor\",\"type\": \"system\",\"users\": [{\"_type\": \"team\",\"id\": 384,\"name\": \"PARAM_EPARGNE_EQUIPE\",\"_links\": {\"self\": {\"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/384\"}}},{\"_type\": \"user\",\"id\": 1434,\"login\": \"S047432\",\"_links\": {\"self\": {\"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/users/1434\"}}}]}},\"_links\": {\"self\": {\"href\": \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/projects/1281/clearances\"}}}", "request");

        Project project = new Project("https://localhost:4321", "project", 1281, "temp v8");
        Clearance clear = project.setClearances("2", "384");

        Assert.assertEquals(clear.id, 2);
        Assert.assertEquals(clear.name, "TestEditor");
        Assert.assertEquals(clear.type, "profile");

    }

    @Test
    public void testDeleteClearances() {
        createServerMock("DELETE", "/users/384", 204, "", "request");

        Project project = new Project("https://localhost:4321", "project", 1281, "project");
        Assert.assertTrue(project.deleteClearances("384").isEmpty());
    }

}
