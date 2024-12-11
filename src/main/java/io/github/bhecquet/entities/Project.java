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
    public final static String TEAMS_URL = "teams";
    public static final String TEST_CASES_URL = "test-cases";

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

    //teamsNames au format XXX,YYY,ZZZ ou AAA si un seul Id
    public Clearance setClearances(String profileId, String teamIds) {
        try {
            JSONObject json = getJSonResponse(buildPostRequest(url + String.format("/clearances/%s/users/%s", profileId, teamIds)));
            return new Clearance(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getJSONObject("content").getJSONObject("automated_test_writer").getString(FIELD_TYPE),
                    json.getJSONObject("content").getJSONObject("automated_test_writer").getInt(FIELD_ID),
                    json.getJSONObject("content").getJSONObject("automated_test_writer").getString(FIELD_NAME));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot set clearances for project %s and profile %s", name, profileId), e);
        }
    }

    //teamsIds au format XXX,YYY,ZZZ ou AAA si un seul Id
    public JSONObject deleteClearances(String teamIds) {
        try {
            return getJSonResponse(buildDeleteRequest(url + String.format("/users/%s", teamIds)));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot set clearances for project %s and team %s", name, teamIds), e);
        }
    }

    /**
     * Renvoie la liste des cas de test associés à un projet
     *
     * @return
     */
    public List<TestCase> getTestCases() {
        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(String.format("%s%s/%d/%s?sort=id", apiRootUrl, PROJECTS_URL, id, TEST_CASES_URL)));

            List<TestCase> testCases = new ArrayList<>();
            if (json.has(FIELD_EMBEDDED)) {
                for (JSONObject testCaseJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_TEST_CASES).toList()) {
                    testCases.add(TestCase.fromJson(testCaseJson));
                }
            }
            return testCases;
        } catch (UnirestException e) {
            throw new SquashTmException("Impossible de récupérer la liste des cas de test", e);
        }
    }

}
