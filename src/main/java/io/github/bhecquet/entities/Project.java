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
        return getFromUrl(apiRootUrl + String.format("%s?projectName=%s", PROJECTS_URL, projectName));
    }

    public static Project getFromUrl(String url) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(url)));
        } catch (UnirestException e) {
            throw new ConfigurationException(String.format("Projet at url %s does not exist or can't be accessed", url));
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
        return getCampaigns("path,name,reference");
    }

    public List<Campaign> getCampaigns(String fields) {

        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(url + CAMPAIGNS_URL + "?sort=id&fields=" + fields));

            List<Campaign> campaigns = new ArrayList<>();
            if (json.has(FIELD_EMBEDDED)) {
                for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_CAMPAIGNS).toList()) {
                    Campaign newCampaign = Campaign.fromJson(folderJson);
                    try {
                        newCampaign.setPath(folderJson.getString("path"));
                    } catch (JSONException e) {
                    }
                    try {
                        newCampaign.readCustomFields(folderJson.getJSONArray(FIELD_CUSTOM_FIELDS));
                    } catch (JSONException e) {
                    }
                    campaigns.add(newCampaign);
                }
            }
            return campaigns;
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot get list of campaigns for project %s: %s", name, e.getMessage()));
        }
    }


}
