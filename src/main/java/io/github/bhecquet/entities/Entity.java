package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.*;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Entity {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String MIMETYPE_APPLICATION_JSON = "application/json";
    protected static final String FIELD_NAME = "name";
    protected static final String FIELD_ID = "id";
    protected static final String FIELD_TYPE = "_type";
    protected static final String FIELD_EMBEDDED = "_embedded";
    protected static final String FIELD_CUSTOM_FIELDS = "custom_fields";
    protected static final String FIELD_PARENT = "parent";
    protected static final String FIELD_CAMPAIGNS = "campaigns";
    protected static final String FIELD_CAMPAIGN_FOLDERS = "campaign-folders";
    protected static final String FIELD_PROJECTS = "projects";
    protected static final String FIELD_PATH = "path";

    protected static final String TYPE_PROJECT = "project";
    protected static final String TYPE_CAMPAIGN_FOLDER = "campaign-folder";
    protected static final String TYPE_CAMPAIGN = "campaign";
    protected static final String TYPE_ITERATION = "iteration";

    private static String user;
    private static String password;

    protected static String apiRootUrl;
    protected List<CustomField> customFields;
    protected String url;
    protected int id;
    protected String name;
    protected String type;

    public static void configureEntity(String user, String password, String apiRootUrl) {
        Entity.user = user;
        Entity.password = password;
        Entity.apiRootUrl = apiRootUrl;
    }

    protected Entity(String url, int id, String name) {
        this(url, null, id, name);
    }

    protected Entity(String url, String type, int id, String name) {
        this.url = url.replace("http://", "https://");
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public abstract void completeDetails();

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    protected static GetRequest buildGetRequest(String url) {
        return Unirest.get(updateUrl(url)).basicAuth(user, password).headerReplace(HEADER_CONTENT_TYPE, MIMETYPE_APPLICATION_JSON);
    }

    protected static HttpRequestWithBody buildPostRequest(String url) {
        return Unirest.post(updateUrl(url)).basicAuth(user, password).headerReplace(HEADER_CONTENT_TYPE, MIMETYPE_APPLICATION_JSON);
    }

    protected static HttpRequestWithBody buildPatchRequest(String url) {
        return Unirest.patch(updateUrl(url)).basicAuth(user, password).headerReplace(HEADER_CONTENT_TYPE, MIMETYPE_APPLICATION_JSON);
    }

    /**
     * As POST and PATCH requests won't follow redirect (HTTP RFC 2616), and if squash is behind a reverse proxy which does the HTTPS end point, all URLs that squash will
     * reply will be in HTTP
     * So, replace http by https if root url is in https
     *
     * @param url
     * @return
     */
    private static String updateUrl(String url) {
        if (Entity.apiRootUrl.startsWith("https://")) {
            return url.replace("http://", "https://");
        } else {
            return url;
        }
    }

    /**
     * Retrieve a list of objects among multiple pages
     * Search keys in "_embedded" value to accumulate them
     *
     * @param request
     * @return
     */
    protected static JSONObject getPagedJSonResponse(HttpRequest<?> request) {
        JSONObject finalJson = null;

        PagedList<JsonNode> result = request
                .queryString("size", 20)
                .queryString("sort", "id") // we add sorting as paging produces odd results (not all data are provided)
                .asPaged(
                        r -> ((HttpRequest) r).asJson(),
                        r -> {
                            JSONObject links = ((HttpResponse<JsonNode>) r).getBody().getObject().getJSONObject("_links");
                            if (links.has("next")) {
                                return links.getJSONObject("next").getString("href");
                            } else {
                                return null;
                            }
                        }
                );

        for (Object json : result.toArray()) {
            if (finalJson == null) {
                finalJson = ((HttpResponse<JsonNode>) json).getBody().getObject();
            } else {
                for (String key : ((HttpResponse<JsonNode>) json).getBody().getObject().getJSONObject(FIELD_EMBEDDED).keySet()) {
                    for (JSONObject entity : (List<JSONObject>) ((HttpResponse<JsonNode>) json).getBody().getObject().getJSONObject(FIELD_EMBEDDED).getJSONArray(key).toList()) {
                        finalJson.getJSONObject(FIELD_EMBEDDED).accumulate(key, entity);
                    }
                }
            }
        }

        return finalJson;
    }

    /**
     * get JSONObject in reply to the request
     * if reply code is 204 or if body is empty, object is empty
     *
     * @param request
     * @return
     */
    protected static JSONObject getJSonResponse(HttpRequest<?> request) {
        JsonNode responseBody = getJSonNodeResponse(request);
        if (responseBody == null) {
            return new JSONObject();
        } else {
            return responseBody.getObject();
        }
    }

    /**
     * get JSONArray in reply to the request
     * if reply code is 204 or if body is empty, array is empty
     *
     * @param request
     * @return
     */
    protected static JSONArray getArrayJSonResponse(HttpRequest<?> request) {
        JsonNode responseBody = getJSonNodeResponse(request);
        if (responseBody == null) {
            return new JSONArray();
        } else {
            return responseBody.getArray();
        }
    }

    private static JsonNode getJSonNodeResponse(HttpRequest<?> request) {

        HttpResponse<JsonNode> response = request.asJson();

        if (response.getStatus() >= 400) {
            if (response.getBody() != null) {
                throw new SquashTmException(String.format("request to %s failed[%d]: %s", request.getUrl(), response.getStatus(), response.getBody().toPrettyString()));
            } else {
                throw new SquashTmException(String.format("request to %s failed", request.getUrl()));
            }
        }

        if (response.getStatus() == 204) {
            return null;
        }

        return response.getBody();
    }

    protected void readCustomFields(JSONArray customFieldsJson) {
        customFields = new ArrayList<>();
        for (JSONObject field : (List<JSONObject>) customFieldsJson.toList()) {
            customFields.add(CustomField.fromJson(field));
        }
    }
    
    /**
     * Update custom field
     *
     * @param customFieldName  technical name of the field (called "code" in API)
     * @param customFieldValue
     */
    public void updateCustomField(String customFieldName, String customFieldValue) {

        JSONObject json = new JSONObject();
        json.put(FIELD_TYPE, type);
        JSONArray customFields = new JSONArray();
        customFields.put(Map.of("code", customFieldName, "value", customFieldValue));
        json.put(FIELD_CUSTOM_FIELDS, customFields);

        getJSonResponse(buildPatchRequest(url).body(json));
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }
}
