package io.github.bhecquet.entities;


import io.github.bhecquet.exceptions.ConfigurationException;
import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Project extends Entity {

    public static final String PROJECTS_URL = "projects";
    public static final String PROJECT_URL = "projects/%d";
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

    /**
     * Get project from its name
     *
     * @param projectName
     * @return
     */
    public static Project get(String projectName) {
        return getFromUrl(apiRootUrl + String.format("%s?projectName=%s", PROJECTS_URL, URLEncoder.encode(projectName, StandardCharsets.UTF_8)));
    }

    /**
     * Get projet from its ID
     *
     * @param id
     * @return
     */
    public static Project get(int id) {
        return getFromUrl((apiRootUrl + String.format(PROJECT_URL, id)));
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

    /**
     * Adds permissions to a project
     *
     * @param profileId   ID of the profile to affect users / teams
     * @param userTeamIds List of user/team ids
     */
    public Clearance setClearances(int profileId, List<Integer> userTeamIds) {
        try {
            JSONObject json = getJSonResponse(buildPostRequest(url + String.format("/clearances/%d/users/%s", profileId, userTeamIds.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(",")))));
            return new Clearance(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    "clearance",
                    0,
                    "");
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot set clearances for project %s and profile %d", name, profileId), e);
        }
    }

    /**
     * Remove permissions of given users to the project
     *
     * @param userTeamIds ID of the user/team for which we remove permission
     * @return
     */
    public JSONObject deleteClearances(List<Integer> userTeamIds) {
        try {
            return getJSonResponse(buildDeleteRequest(url + String.format("/users/%s", userTeamIds.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(",")))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot set clearances for project %s and team %s", name, userTeamIds.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(","))), e);
        }
    }

    /**
     * Return list of testcases from project
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
            throw new SquashTmException("Cannot get list of test cases", e);
        }
    }

    /**
     * Bind the custom field searched by its code to the provided entityType
     *
     * @param customFieldCode
     * @param entityType      One of "REQUIREMENT_FOLDER", "CAMPAIGN_FOLDER", "TESTCASE_FOLDER", "TEST_CASE", "TEST_STEP", "CAMPAIGN", "ITERATION", "TEST_SUITE", "REQUIREMENT_VERSION", "EXECUTION, EXECUTION_STEP"
     */
    public void bindCustomField(String customFieldCode, String entityType) {
        List<String> allowedEntities = List.of("REQUIREMENT_FOLDER", "CAMPAIGN_FOLDER", "TESTCASE_FOLDER", "TEST_CASE", "TEST_STEP", "CAMPAIGN", "ITERATION", "TEST_SUITE", "REQUIREMENT_VERSION", "EXECUTION, EXECUTION_STEP");
        CustomField customField = CustomField.getByCode(customFieldCode);
        if (customField == null) {
            throw new SquashTmException(String.format("No custom field with code %s exist in this instance", customFieldCode));
        }

        if (!allowedEntities.contains(entityType)) {
            throw new SquashTmException(String.format("Entity type %s is not allowed: REQUIREMENT_FOLDER, CAMPAIGN_FOLDER, TESTCASE_FOLDER, TEST_CASE, TEST_STEP, CAMPAIGN, ITERATION, TEST_SUITE, REQUIREMENT_VERSION, EXECUTION, EXECUTION_STEP", entityType));
        }

        buildPostRequest(url + "/custom-fields/" + entityType)
                .queryString("cufId", customField.getId())
                .body(String.format("cufId=" + customField.getId()))
                .asString()
                .ifFailure(response -> {
                    throw new SquashTmException(String.format("Cannot bind custom field '%s' to project %s: %s", customFieldCode, name, response.getBody()));
                });
    }

}
