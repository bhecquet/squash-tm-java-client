package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

public class Attachment extends Entity {

    private static final String FIELD_OWNER = "owner";
    private static final String FIELD_OWNER_ID = "ownerId";

    private int size;
    private String file_type;
    private String added_on;

    private static final String ATTACHMENT_URL = "attachments/%s";

    public Attachment(String url, String type, int id, String name) {
        super(url, type, id, name);
    }

    public static Attachment fromJson(JSONObject json) {
        try {
            Attachment attachment = new Attachment(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.optString(FIELD_NAME, "")
            );
            attachment.completeDetails(json, false);
            return attachment;
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create TestCase from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public static Attachment get(int id) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format(ATTACHMENT_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Test Case %d does not exist", id));
        }
    }

    @Override
    public void completeDetails() {
        JSONObject json = getJSonResponse(buildGetRequest(url));
        completeDetails(json, true);
    }

    /**
     * @param json                     JSON to get
     * @param completeDependentObjects whether we call related objects found in json
     */
    private void completeDetails(JSONObject json, boolean completeDependentObjects) {
        size = json.getInt("size");
        file_type = json.getString("fileType");
        added_on = json.getString("addedOn");
    }
}