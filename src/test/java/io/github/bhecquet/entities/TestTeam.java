package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.NotImplementedException;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class TestTeam extends SquashTMTest {

    @BeforeMethod
    public void init() {
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testFromJson() {

        JSONObject json = new JSONObject();
        json.put("_type", "team");
        json.put("id", 3930);
        json.put("name", "MMA_SINISTRES_TESTEUR_REFERENT");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/3930\"" +
                "        }}"));

        Team team = Team.fromJson(json);
        Assert.assertEquals(team.getId(), 3930);
        Assert.assertEquals(team.getName(), "MMA_SINISTRES_TESTEUR_REFERENT");
        Assert.assertEquals(team.getUrl(), "https://squash-tm-dev.covea.priv/squash/api/rest/latest/teams/3930");
    }

    @Test
    public void testGetAll() {
        //Simulate get Teams
        createServerMock("GET", "/teams?sort=id", 200, "{  \"_embedded\": {    \"teams\": [      {        \"_type\": \"team\",        \"id\": 8,        \"name\": \"CDSV_LeMans_Chartres\",        \"_links\": {          \"self\": {            \"href\": \"https://localhost:4321/teams/8\"          }        }      },      {        \"_type\": \"team\",        \"id\": 30,        \"name\": \"VMOA_IARD_EQUIPE\",        \"_links\": {          \"self\": {            \"href\": \"https://localhost:4321/teams/30\"          }        }      },      {        \"_type\": \"team\",        \"id\": 32,        \"name\": \"VNR\",        \"_links\": {          \"self\": {            \"href\": \"https://localhost:4321/teams/32\"          }        }      }    ]  },  \"_links\": {    \"self\": {      \"href\": \"https://localhost:4321/teams?page=0&size=50&sort=id,asc\"    }  },  \"page\": {    \"size\": 50,    \"totalElements\": 34,    \"totalPages\": 1,    \"number\": 0  }}", "request");

        List<Team> ts = Team.getAllTeams();

        Assert.assertEquals(ts.size(), 3);
        Assert.assertEquals(ts.get(0).getName(), "CDSV_LeMans_Chartres");
        Assert.assertEquals(ts.get(0).type, "team");
        Assert.assertEquals(ts.get(0).getId(), 8);
        Assert.assertEquals(ts.get(0).getUrl(), "https://localhost:4321/teams/8");
    }

    @Test(expectedExceptions = NotImplementedException.class)
    public void testCompleteDetails() {
        Team tm = new Team("osef", "ouais", 1, "yolo");
        tm.completeDetails();
    }
    
}
