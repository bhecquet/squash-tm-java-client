package io.github.bhecquet.entities;


import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Campaign extends Entity {


    private static final Map<Project, EntityCache<Campaign>> campaignCaches = new ConcurrentHashMap<>();
    public static final String CAMPAIGNS_URL = "campaigns";
    public static final String CAMPAIGNS_FOR_PROJECT_URL = "projects/%d/campaigns";
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
     * @deprecated use getAll(Project) instead
     */
    @SuppressWarnings("unchecked")
    @Deprecated(since = "1.0.25")
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
     * Returns all the campaigns for the project
     *
     * @param project the project
     */
    public static List<Campaign> getAll(Project project) {
        return getAll(project, "path,name,reference");
    }

    /**
     * Returns all the campaigns for the project, given fields
     *
     * @param project the project
     * @param fields  fields to retrieve
     */
    public static List<Campaign> getAll(Project project, String fields) {
        JSONObject json;
        try {
            json = getPagedJSonResponse(buildGetRequest(apiRootUrl + String.format(CAMPAIGNS_FOR_PROJECT_URL, project.getId()) + "?sort=id&fields=" + URLEncoder.encode(fields, StandardCharsets.UTF_8)));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot get list of campaigns for project %s: %s", project.getName(), e.getMessage()));
        }

        List<Campaign> campaigns = new ArrayList<>();
        if (json.has(FIELD_EMBEDDED)) {
            for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_CAMPAIGNS).toList()) {
                Campaign newCampaign = Campaign.fromJson(folderJson);
                try {
                    newCampaign.setPath(folderJson.getString("path"));
                } catch (JSONException e) {
                    // no path present
                }
                try {
                    newCampaign.readCustomFields(folderJson.getJSONArray(FIELD_CUSTOM_FIELDS));
                } catch (JSONException e) {
                    // no custom fields
                }
                campaigns.add(newCampaign);
            }
        }
        return campaigns;

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
                    iteration.completeDetails(iterationJson, true);

                }
            }
            return iterations;
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot get list of iterations for campaign %s", name));
        }
    }

    public static Campaign fromJson(JSONObject json) {
        try {
            Campaign campaign = new Campaign(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_NAME));
            campaign.completeDetails(json);
            return campaign;
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create Campaign from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    /**
     * Creates a campaign if it does not exist
     * Folder tree will also be created if necessary
     *
     * @param campaignName name of the campaign to create
     * @param folderPath   folder path to which campaign will be created. e.g: foo/bar. Beware that this will not work as expected if one of folder contains a "/"
     */
    public static Campaign create(Project project, String campaignName, String folderPath, Map<String, Object> customFields) {
        return create(project, campaignName, folderPath == null ? new ArrayList<>() : Arrays.asList(folderPath.split("/")), customFields);
    }

    /**
     * Creates a campaign if it does not exist
     * Folder tree will also be created if necessary
     *
     * @param campaignName name of the campaign to create
     * @param folderPath   folder path to which campaign will be created. e.g: [foo, bar]
     */
    public static Campaign create(Project project, String campaignName, List<String> folderPath, Map<String, Object> customFields) {

        CampaignFolder parentFolder = CampaignFolder.createCampaignFolderTree(project, folderPath);

        if (folderPath == null) {
            folderPath = new ArrayList<>();
        }

        // do not create campaign if it exists
        campaignCaches.putIfAbsent(project, new EntityCache<>(300));
        for (Campaign campaign : campaignCaches.get(project).getAll(Campaign::getAll, project)) {
            if (campaign.getName().equals(campaignName) && campaign.getPath().equals(String.format("/%s/%s/%s", project.getName(), String.join("/", folderPath), campaignName).replace("//", "/"))) {
                return campaign;
            }
        }
        return create(project, campaignName, parentFolder, customFields);
    }

    /**
     * Creates the campaign
     *
     * @param project      project to create campaign in
     * @param campaignName name of the campaign
     * @param parentFolder the parent folder of the campaign
     * @return the created campaign
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

            Campaign campaign = fromJson(json);
            campaignCaches.putIfAbsent(project, new EntityCache<>(300));
            campaignCaches.get(project).add(campaign);
            return campaign;

        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot create campaign %s", campaignName), e);
        }
    }

    @Override
    public void completeDetails() {
        JSONObject json = getJSonResponse(buildGetRequest(url));
        completeDetails(json);
    }

    private void completeDetails(JSONObject json) {
        try {
            projectId = json.getJSONObject(TYPE_PROJECT).getInt(FIELD_ID);
        } catch (JSONException e) {/* ignore */}
        try {
            projectName = json.getJSONObject(TYPE_PROJECT).getString(FIELD_NAME);
        } catch (JSONException e) {/* ignore */}

        scheduleStartDate = json.optString(FIELD_SCHEDULE_START_DATE, "");
        scheduleEndDate = json.optString(FIELD_SCHEDULE_END_DATE, "");
        actualStartDate = json.optString(FIELD_ACTUAL_START_DATE, "");
        actualEndDate = json.optString(FIELD_ACTUAL_END_DATE, "");
        path = json.optString(FIELD_PATH, "");


        try {
            for (JSONObject jsonIteration : (List<JSONObject>) json.getJSONArray(FIELD_ITERATIONS).toList()) {
                iterations.add(Iteration.fromJson(jsonIteration));
            }
        } catch (JSONException e) {/* ignore */}
        try {
            readCustomFields(json.getJSONArray(FIELD_CUSTOM_FIELDS));
        } catch (JSONException e) {/* ignore */}
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

    public static Map<Project, EntityCache<Campaign>> getCampaignCaches() {
        return campaignCaches;
    }

}
