package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomField extends Entity {
    private String code;
    private String label;
    public static final String CUSTOM_FIELDS_URL = "custom-fields";
    public static final String FIELD_CUSTOM_FIELDS = "custom-fields";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_LABEL = "label";

    public CustomField(String url, String type, int id, String name, String label, String code) {
        super(url, type, id, name);
        this.label = label;
        this.code = code;
    }

    /**
     * Returns the custom field by code
     *
     * @param customFieldCode
     * @return
     */
    public static CustomField getByCode(String customFieldCode) {
        return getAll().stream().filter(cf -> cf.getCode().equals(customFieldCode)).findFirst().orElse(null);
    }

    /**
     * returns the list of custom fields squash knows
     *
     * @return
     */
    public static List<CustomField> getAll() {
        try {
            JSONObject json = getPagedJSonResponse(buildGetRequest(apiRootUrl + CUSTOM_FIELDS_URL));

            List<CustomField> customFields = new ArrayList<>();
            if (json.has(FIELD_EMBEDDED)) {
                for (JSONObject customFieldJSon : (List<JSONObject>) json.getJSONObject(FIELD_EMBEDDED).getJSONArray(FIELD_CUSTOM_FIELDS).toList()) {
                    customFields.add(CustomField.fromJson(customFieldJSon));
                }
            }
            return customFields;
        } catch (UnirestException e) {
            throw new SquashTmException("Cannot get all projects", e);
        }
    }


    @Override
    public void completeDetails() {
        // nothing to do
    }

    public static CustomField fromJson(JSONObject json) {

        try {
            return new CustomField(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_NAME),
                    json.getString(FIELD_LABEL),
                    json.getString(FIELD_CODE)
            );
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create CustomField from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
