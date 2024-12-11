package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Team extends Entity {

    public static final String TEAMS_URL = "teams";

    protected Team(String url, String type, int id, String name) {
        super(url, type, id, name);
    }

    @Override
    public void completeDetails() {
        throw new NotImplementedException();
    }

    public static Team fromJson(JSONObject json) {
        try {
            return new Team(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_NAME));
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create Team from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public static List<Team> getAllTeams() {

        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(apiRootUrl + TEAMS_URL + "?sort=id"));

            List<Team> teams = new ArrayList<>();
            if (json.has(FIELD_EMBEDDED)) {
                for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_TEAMS).toList()) {
                    Team newTeam = Team.fromJson(folderJson);
                    try {
                        newTeam.readCustomFields(folderJson.getJSONArray(FIELD_CUSTOM_FIELDS));
                    } catch (JSONException e) {
                    }
                    teams.add(newTeam);
                }
            }
            return teams;
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot get list of teams: %s", e.getMessage()));
        }
    }

}
