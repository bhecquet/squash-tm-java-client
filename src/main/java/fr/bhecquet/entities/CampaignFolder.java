package fr.bhecquet.entities;


import fr.bhecquet.exceptions.NotImplementedException;
import fr.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CampaignFolder extends Entity {

    public static final String CAMPAIGN_FOLDER_URL = "campaign-folders";
    public static final String CAMPAIGN_FOLDER_TREE_URL = CAMPAIGN_FOLDER_URL + "/" + "tree/%s";

    private Project project;
    private Entity parent;

    public CampaignFolder(String url, int id, String name, Project project, Entity parent) {
        super(url, id, name);
        this.project = project;
        this.parent = parent;
    }

    /**
     * Get all campaign folders (should not be used on large instances)
     *
     * @return
     */
    public static List<CampaignFolder> getAll() {
        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(apiRootUrl + CAMPAIGN_FOLDER_URL));

            List<CampaignFolder> campaignFolders = new ArrayList<>();
            if (json.has("_embedded")) {
                for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_CAMPAIGN_FOLDERS).toList()) {
                    campaignFolders.add(CampaignFolder.fromJson(folderJson));
                }
            }
            return campaignFolders;
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot get all campaign folders", e);
        }
    }

    /**
     * Get all campaign folders for this project
     *
     * @return
     */
    public static List<CampaignFolder> getAll(Project project) {
        try {
            JSONArray json = getArrayJSonResponse(buildGetRequest(apiRootUrl + String.format(CAMPAIGN_FOLDER_TREE_URL, project.getId())));

            List<CampaignFolder> campaignFolders = new ArrayList<>();
            if (!json.isEmpty()) {
                // we request for only one project, so take the first element
                for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(0).getJSONArray("folders").toList()) {
                    campaignFolders.addAll(readCampaignFolderFromTree(folderJson));
                }
            }

            return campaignFolders;
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot get all campaign folders", e);
        }
    }

    private static List<CampaignFolder> readCampaignFolderFromTree(JSONObject folderJson) {
        List<CampaignFolder> campaignFolders = new ArrayList<>();
        CampaignFolder campaignFolder = CampaignFolder.get(folderJson.getInt("id"));
        campaignFolders.add(campaignFolder);

        // add children
        for (Object childJson : folderJson.getJSONArray("children")) {
            campaignFolders.addAll(readCampaignFolderFromTree((JSONObject) childJson));
        }
        return campaignFolders;
    }

    /**
     * Creates all the campaign folders from the provided path
     *
     * @param project    the project to which this folder belongs
     * @param folderPath path of folders. e.g: foo/bar/myFolder will create (or check existence) of 3 folders
     * @return the final campaign folder
     */
    public static CampaignFolder createCampaignFolderTree(Project project, String folderPath) {
        if (folderPath == null) {
            folderPath = "";
        }

        // create folder where campaign will be located
        CampaignFolder parentFolder = null;
        for (String folderName : folderPath.split("/")) {

            if (folderName.isEmpty()) {
                continue;
            }

            parentFolder = createCampaignFolders(project, folderName, parentFolder);
        }

        return parentFolder;
    }

    private static CampaignFolder createCampaignFolders(Project project, String folderName, CampaignFolder parentFolder) {

        List<CampaignFolder> campaignFolders = getAll(project);

        boolean folderExists = false;
        for (CampaignFolder existingFolder : campaignFolders) {
            if (existingFolder.getName().equals(folderName)
                    && (existingFolder.getProject() == null || existingFolder.getProject() != null && existingFolder.getProject().getId() == project.getId())
                    && (existingFolder.getParent() == null
                    || parentFolder == null && existingFolder.getParent() != null && existingFolder.getParent() instanceof Project
                    || (parentFolder != null && existingFolder.getParent() != null && existingFolder.getParent() instanceof CampaignFolder && existingFolder.getParent().getId() == parentFolder.getId()))) {
                folderExists = true;
                parentFolder = existingFolder;
                break;
            }
        }

        if (!folderExists) {
            parentFolder = CampaignFolder.create(project, parentFolder, folderName);
        }

        return parentFolder;
    }

    public static CampaignFolder get(int id) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format("%s/%d", CAMPAIGN_FOLDER_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Campaign folder %d does not exist", id));
        }
    }

    public JSONObject asJson() {
        JSONObject json = new JSONObject();
        json.put(FIELD_TYPE, TYPE_CAMPAIGN_FOLDER);
        json.put(FIELD_ID, id);
        json.put(FIELD_NAME, name);
        return json;
    }

    public static CampaignFolder fromJson(JSONObject json) {

        try {
            Entity parent;
            if (json.has(FIELD_PARENT)) {
                if (TYPE_PROJECT.equals(json.getJSONObject(FIELD_PARENT).getString(FIELD_TYPE))) {
                    parent = Project.fromJson(json.getJSONObject(FIELD_PARENT));
                } else if (TYPE_CAMPAIGN_FOLDER.equals(json.getJSONObject(FIELD_PARENT).getString(FIELD_TYPE))) {
                    parent = CampaignFolder.fromJson(json.getJSONObject(FIELD_PARENT));
                } else {
                    parent = null;
                }
            } else {
                parent = null;
            }

            Project project = null;
            if (json.has(TYPE_PROJECT)) {
                project = Project.fromJson(json.getJSONObject(TYPE_PROJECT));
            }

            return new CampaignFolder(json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_NAME),
                    project,
                    parent
            );
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create CampaignFolder from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }

    }

    public static CampaignFolder create(Project project, CampaignFolder parent, String campaignFolderName) {
        try {

            JSONObject body = new JSONObject();
            body.put(FIELD_TYPE, TYPE_CAMPAIGN_FOLDER);
            body.put(FIELD_NAME, campaignFolderName);
            if (parent != null) {
                body.put(FIELD_PARENT, parent.asJson());
            } else {
                body.put(FIELD_PARENT, project.asJson());
            }

            JSONObject json = getJSonResponse(buildPostRequest(apiRootUrl + CAMPAIGN_FOLDER_URL).body(body));


            return CampaignFolder.fromJson(json);

        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot create campaign %s", campaignFolderName), e);
        }

    }

    @Override
    public void completeDetails() {
        throw new NotImplementedException();
    }

    public static String getCampaignFolderUrl() {
        return CAMPAIGN_FOLDER_URL;
    }

    public Project getProject() {
        return project;
    }

    public Entity getParent() {
        return parent;
    }

}
