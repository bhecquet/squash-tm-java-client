package io.github.bhecquet.entities;


import io.github.bhecquet.exceptions.ConfigurationException;
import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Project extends Entity {

    public static final String PROJECTS_URL = "projects";
    public static final String CAMPAIGNS_URL = "/campaigns";

    public static final String TYPE_PROJECT = "project";

    public Project(String url, String type, int id, String name) {
        super(url, type, id, name);
    }


    @Override
    public void completeDetails() {
        throw new NotImplementedException();
    }

    public static Project get(String projectName) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format("%s?projectName=%s", PROJECTS_URL, projectName))));
        } catch (UnirestException e) {
            throw new ConfigurationException(String.format("Projet %s does not exist or can't be accessed", projectName));
        }
    }

    /**
     * Returns the list of projects accessible to this user
     *
     * @return
     */
    public static List<Project> getAll() {
        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(apiRootUrl + PROJECTS_URL));

            List<Project> projects = new ArrayList<>();
            if (json.has(FIELD_EMBEDDED)) {
                for (JSONObject projectJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_PROJECTS).toList()) {
                    projects.add(Project.fromJson(projectJson));
                }
            }
            return projects;
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot get all projects", e);
        }
    }

    public JSONObject asJson() {
        JSONObject json = new JSONObject();
        json.put(FIELD_TYPE, TYPE_PROJECT);
        json.put(FIELD_ID, id);
        json.put(FIELD_NAME, name);
        return json;
    }

    public static Project fromJson(JSONObject json) {
        try {
            return new Project(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_NAME));
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create Project from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public List<Campaign> getCampaigns() {
        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(url + CAMPAIGNS_URL + "?sort=id"));

            List<Campaign> campaigns = new ArrayList<>();
            if (json.has(FIELD_EMBEDDED)) {
                for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_CAMPAIGNS).toList()) {
                    campaigns.add(Campaign.fromJson(folderJson));
                }
            }
            return campaigns;
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot get list of campaigns for project %s", name));
        }
    }


}
