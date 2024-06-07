package io.github.bhecquet.entities;


import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Campaign extends Entity {


    public static final String CAMPAIGNS_URL = "campaigns";
    private static final String CAMPAIGN_URL = "campaigns/%s";
    public static final String ITERATIONS_URL = "/iterations";

    private static final String FIELD_SCHEDULE_START_DATE = "scheduled_start_date";
    private static final String FIELD_SCHEDULE_END_DATE = "scheduled_end_date";
    private static final String FIELD_ACTUAL_START_DATE = "actual_start_date";
    private static final String FIELD_ACTUAL_END_DATE = "actual_end_date";
    private static final String FIELD_ITERATIONS = "iterations";

    private int projectId;
    private String projectName;
    private String scheduleStartDate;
    private String scheduleEndDate;
    private String actualStartDate;
    private String actualEndDate;
    private List<Iteration> iterations;
    private String path;

    public Campaign(int id) {
        super("", "campaign", id, null);
    }

    public Campaign(String url, String type, int id, String name) {
        super(url, type, id, name);
        iterations = new ArrayList<>();
    }

    public Campaign(String url, String type, int id, String name, String path) {
        super(url, type, id, name);
        iterations = new ArrayList<>();
        this.path = path;
    }


    /**
     * Get list of all campaigns
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Campaign> getAll() {
        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(apiRootUrl + CAMPAIGNS_URL));

            List<Campaign> campaigns = new ArrayList<>();
            if (json.has(FIELD_EMBEDDED)) {
                for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_CAMPAIGNS).toList()) {
                    campaigns.add(Campaign.fromJson(folderJson));
                }
            }
            return campaigns;
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot get all campaigns", e);
        }
    }

    /**
     * get iterations for the current campaign
     */
    @SuppressWarnings("unchecked")
    public List<Iteration> getIterations() {
        if (!iterations.isEmpty()) {
            return iterations;
        }
        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(url + String.format(ITERATIONS_URL, id)));

            iterations = new ArrayList<>();
            if (json.has(FIELD_EMBEDDED)) {
                for (JSONObject iterationJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_ITERATIONS).toList()) {
                    Iteration iteration = Iteration.fromJson(iterationJson);
                    iterations.add(iteration);
                    iteration.completeDetails(iterationJson);

                }
            }
            return iterations;
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot get list of iterations for campaign %s", name));
        }
    }

    public static Campaign fromJson(JSONObject json) {
        try {
            return new Campaign(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_NAME));
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create Campaign from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    /**
     * Creates a campaign if it does not exist
     * Folder tree will also be created if necessary
     *
     * @param campaignName name of the campaign to create
     * @param folderPath   folder path to which campaign will be created. e.g: foo/bar
     */
    public static Campaign create(Project project, String campaignName, String folderPath, Map<String, Object> customFields) {

        CampaignFolder parentFolder = CampaignFolder.createCampaignFolderTree(project, folderPath);

        if (folderPath == null) {
            folderPath = "";
        }

        // do not create campaign if it exists
        for (Campaign campaign : project.getCampaigns()) {
            if (campaign.getName().equals(campaignName) && campaign.getPath().equals(String.format("/%s/%s/%s", project.getName(), folderPath, campaignName).replace("//", "/"))) {
                return campaign;
            }
        }
        return Campaign.create(project, campaignName, parentFolder, customFields);
    }

    /**
     * CReates the campaign
     *
     * @param project
     * @param campaignName
     * @param parentFolder
     * @return
     */
    public static Campaign create(Project project, String campaignName, CampaignFolder parentFolder, Map<String, Object> customFields) {

        try {

            JSONObject body = new JSONObject();
            body.put(FIELD_TYPE, TYPE_CAMPAIGN);
            body.put(FIELD_NAME, campaignName);
            body.put("status", "PLANNED");

            JSONObject parent = new JSONObject();
            if (parentFolder == null) {
                parent.put(FIELD_ID, project.id);
                parent.put(FIELD_TYPE, TYPE_PROJECT);
            } else {
                parent.put(FIELD_ID, parentFolder.id);
                parent.put(FIELD_TYPE, "campaign-folder");
            }
            body.put("parent", parent);

            if (customFields != null && !customFields.isEmpty()) {
                JSONArray cFields = new JSONArray();
                for (Map.Entry<String, Object> customField : customFields.entrySet()) {
                    cFields.put(Map.of("code", customField.getKey(), "value", customField.getValue()));
                }
                body.put(FIELD_CUSTOM_FIELDS, cFields);
            }

            JSONObject json = getJSonResponse(buildPostRequest(apiRootUrl + CAMPAIGNS_URL).body(body));

            return fromJson(json);

        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot create campaign %s", campaignName), e);
        }
    }

    @Override
    public void completeDetails() {
        JSONObject json = getJSonResponse(Unirest.get(url));
        completeDetails(json);
    }

    private void completeDetails(JSONObject json) {
        projectId = json.getJSONObject(TYPE_PROJECT).getInt(FIELD_ID);
        projectName = json.getJSONObject(TYPE_PROJECT).getString(FIELD_NAME);
        scheduleStartDate = json.optString(FIELD_SCHEDULE_START_DATE, "");
        scheduleEndDate = json.optString(FIELD_SCHEDULE_END_DATE, "");
        actualStartDate = json.optString(FIELD_ACTUAL_START_DATE, "");
        actualEndDate = json.optString(FIELD_ACTUAL_END_DATE, "");
        path = json.getString(FIELD_PATH);

        for (JSONObject jsonIteration : (List<JSONObject>) json.getJSONArray(FIELD_ITERATIONS).toList()) {
            iterations.add(Iteration.fromJson(jsonIteration));
        }
        readCustomFields(json.getJSONArray(FIELD_CUSTOM_FIELDS));
    }

    public static Campaign get(int id) {
        try {
            JSONObject json = getJSonResponse(buildGetRequest(apiRootUrl + String.format(CAMPAIGN_URL, id)));
            Campaign campaign = fromJson(json);
            campaign.completeDetails(json);
            return campaign;
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Campaign %d does not exist", id));
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getScheduleStartDate() {
        return scheduleStartDate;
    }

    public String getScheduleEndDate() {
        return scheduleEndDate;
    }

    public String getActualStartDate() {
        return actualStartDate;
    }

    public String getActualEndDate() {
        return actualEndDate;
    }

}
