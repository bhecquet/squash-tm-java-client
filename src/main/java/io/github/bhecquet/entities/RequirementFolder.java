package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequirementFolder extends Entity {

    private static final Map<Project, EntityCache<RequirementFolder>> requirementFolderCaches = new ConcurrentHashMap<>();
    public static final String REQUIREMENT_FOLDER_URL = "requirement-folders";
    public static final String REQUIREMENT_FOLDER_TREE_URL = REQUIREMENT_FOLDER_URL + "/" + "tree/%s";
    public static final String REQUIREMENT_FOLDER_CONTENT_URL = "requirement-folders/%s/content";
    private static final String FIELD_DESCRIPTION = "description";

    private Project project;
    private Entity parent;
    private String description;

    public RequirementFolder(String url, String type, int id, String name, Project project, Entity parent) {
        super(url, type, id, name);
        this.project = project;
        this.parent = parent;
    }

    /**
     * Get all requirement folders (should not be used on large instances)
     */
    public static List<RequirementFolder> getAll() {
        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(apiRootUrl + REQUIREMENT_FOLDER_URL));

            List<RequirementFolder> requirementFolders = new ArrayList<>();
            if (json.has("_embedded")) {
                for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_REQUIREMENT_FOLDERS).toList()) {
                    requirementFolders.add(RequirementFolder.fromJson(folderJson));
                }
            }
            return requirementFolders;
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot get all requirement folders", e);
        }
    }


    /**
     * Get all requirement folders for this project
     */
    public static List<RequirementFolder> getAll(Project project) {
        try {
            JSONArray json = getArrayJSonResponse(buildGetRequest(apiRootUrl + String.format(REQUIREMENT_FOLDER_TREE_URL, project.getId())));

            List<RequirementFolder> requirementFolders = new ArrayList<>();
            if (!json.isEmpty()) {
                // we request for only one project, so take the first element
                for (JSONObject folderJson : (List<JSONObject>) json.getJSONObject(0).getJSONArray("folders").toList()) {
                    requirementFolders.addAll(readRequirementFolderFromTree(folderJson, project, null));
                }
            }

            return requirementFolders;
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot get all requirement folders", e);
        }
    }

    private static List<RequirementFolder> readRequirementFolderFromTree(JSONObject folderJson, Project project, Entity parent) {
        List<RequirementFolder> requirementFolders = new ArrayList<>();
        RequirementFolder requirementFolder = new RequirementFolder(folderJson.getString("url"),
                folderJson.getString(FIELD_TYPE),
                folderJson.getInt(FIELD_ID),
                folderJson.getString(FIELD_NAME),
                project,
                parent);

        requirementFolders.add(requirementFolder);

        // add children
        for (Object childJson : folderJson.getJSONArray("children")) {
            requirementFolders.addAll(readRequirementFolderFromTree((JSONObject) childJson, project, requirementFolder));
        }
        return requirementFolders;
    }

    /**
     * Creates all the requirement folders from the provided path
     *
     * @param project    the project to which this folder belongs
     * @param folderPath path of folders. e.g: foo/bar/myFolder will create (or check existence) of 3 folders
     * @return the final requirement folder
     */
    public static RequirementFolder createRequirementFolderTree(Project project, String folderPath) {
        return RequirementFolder.createRequirementFolderTree(project, folderPath == null ? new ArrayList<>() : Arrays.asList(folderPath.split("/")));
    }

    /**
     * Creates all the requirement folders from the provided path
     *
     * @param project    the project to which this folder belongs
     * @param folderPath path of folders. as a list
     * @return the final requirement folder
     */
    public static RequirementFolder createRequirementFolderTree(Project project, List<String> folderPath) {
        if (folderPath == null) {
            folderPath = new ArrayList<>();
        }

        // create folder where requirement will be located
        RequirementFolder parentFolder = null;
        for (String folderName : folderPath) {

            if (folderName.trim().isEmpty()) {
                continue;
            }

            parentFolder = createRequirementFolders(project, folderName, parentFolder);
        }

        return parentFolder;
    }

    private static RequirementFolder createRequirementFolders(Project project, String folderName, RequirementFolder parentFolder) {

        // creates the cache
        requirementFolderCaches.putIfAbsent(project, new EntityCache<>());
        List<RequirementFolder> requirementFolders = requirementFolderCaches.get(project).getAll(RequirementFolder::getAll, project);

        boolean folderExists = false;
        for (RequirementFolder existingFolder : requirementFolders) {
            if (existingFolder.getName().equals(folderName)
                    && (existingFolder.getProject() == null || existingFolder.getProject() != null && existingFolder.getProject().getId() == project.getId())
                    && (existingFolder.getParent() == null
                    || parentFolder == null && existingFolder.getParent() != null && existingFolder.getParent() instanceof Project
                    || (parentFolder != null && existingFolder.getParent() != null && existingFolder.getParent() instanceof RequirementFolder && existingFolder.getParent().getId() == parentFolder.getId()))) {
                folderExists = true;
                parentFolder = existingFolder;
                break;
            }
        }

        if (!folderExists) {
            parentFolder = RequirementFolder.create(project, parentFolder, folderName);
        }

        return parentFolder;
    }

    public static RequirementFolder get(int id) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format("%s/%d", REQUIREMENT_FOLDER_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Requirement folder %d does not exist", id));
        }
    }

    public JSONObject asJson() {
        JSONObject json = new JSONObject();
        json.put(FIELD_TYPE, TYPE_REQUIREMENT_FOLDER);
        json.put(FIELD_ID, id);
        json.put(FIELD_NAME, name);
        return json;
    }

    public static RequirementFolder fromJson(JSONObject json) {

        try {
            Entity parent = null;
            if (json.has(FIELD_PARENT)) {
                parent = Entity.entityFromJson(json.getJSONObject(FIELD_PARENT));
            }

            Project project = null;
            if (json.has(TYPE_PROJECT)) {
                project = Project.fromJson(json.getJSONObject(TYPE_PROJECT));
            }

            RequirementFolder requirementFolder = new RequirementFolder(json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_NAME),
                    project,
                    parent
            );
            requirementFolder.completeDetails(json);
            return requirementFolder;
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create RequirementFolder from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }

    }

    public static List<Entity> getContent(int folderId) {
        List<Entity> content = new ArrayList<>();
        JSONObject jsonContent = getJSonResponse(buildGetRequest(apiRootUrl + String.format(REQUIREMENT_FOLDER_CONTENT_URL, folderId)));
        if (jsonContent.has("_embedded")) {
            for (JSONObject reqorfolderJson : (List<JSONObject>) jsonContent.getJSONObject(FIELD_EMBEDDED).getJSONArray("content").toList()) {
                if (reqorfolderJson.getString(FIELD_TYPE).equals(TYPE_REQUIREMENT_FOLDER)) {
                    content.add(RequirementFolder.fromJson(reqorfolderJson));
                } else if (reqorfolderJson.getString(FIELD_TYPE).equals(TYPE_REQUIREMENT)) {
                    content.add(Requirement.fromJson(reqorfolderJson));
                }
            }
        }
        return content;
    }

    public static RequirementFolder create(Project project, RequirementFolder parent, String requirementFolderName) {
        try {

            JSONObject body = new JSONObject();
            body.put(FIELD_TYPE, TYPE_REQUIREMENT_FOLDER);
            body.put(FIELD_NAME, requirementFolderName);
            if (parent != null) {
                body.put(FIELD_PARENT, parent.asJson());
            } else {
                body.put(FIELD_PARENT, project.asJson());
            }

            JSONObject json = getJSonResponse(buildPostRequest(apiRootUrl + REQUIREMENT_FOLDER_URL).body(body));
            RequirementFolder requirementFolder = RequirementFolder.fromJson(json);

            requirementFolderCaches.putIfAbsent(project, new EntityCache<>());
            requirementFolderCaches.get(project).add(requirementFolder);

            return requirementFolder;

        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot create requirement %s", requirementFolderName), e);
        }

    }

    /**
     * Update requirement folder
     *
     * @param name        the updated name or null if we should not update it
     * @param description the updated description or null if we should not update it
     * @return this
     */
    public RequirementFolder update(String name, String description) {
        try {
            JSONObject body = new JSONObject();
            body.put(FIELD_TYPE, "requirement-folder");

            if (name != null && !name.isEmpty()) {
                body.put(FIELD_NAME, name);
            } else {
                body.put(FIELD_NAME, this.name);
            }
            if (description != null) {
                body.put(FIELD_DESCRIPTION, description);
            }

            JSONObject json = getJSonResponse(buildPatchRequest(String.format(apiRootUrl + REQUIREMENT_FOLDER_URL + "/%d", id)).body(body));

            completeDetails(json);
            return this;
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot update requirement folder", e);
        }
    }

    @Override
    public void completeDetails() {
        JSONObject json = getJSonResponse(buildGetRequest(url));
        completeDetails(json);
    }

    private void completeDetails(JSONObject json) {
        try {
            name = json.optString(FIELD_NAME, "");
        } catch (JSONException e) {/* ignore */}
        try {
            description = json.optString(FIELD_DESCRIPTION, "");
        } catch (JSONException e) {/* ignore */}
    }

    public static String getRequirementFolderUrl() {
        return REQUIREMENT_FOLDER_URL;
    }

    public Project getProject() {
        return project;
    }

    public Entity getParent() {
        return parent;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public static Map<Project, EntityCache<RequirementFolder>> getRequirementFolderCaches() {
        return requirementFolderCaches;
    }

    public String getDescription() {
        return description;
    }
}