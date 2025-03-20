package io.github.bhecquet.entities;

import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONObject;

public class CustomFieldValue {
    public static final String FIELD_VALUE = "value";
    private String code;
    private String label;
    private Object value;

    public CustomFieldValue(String code, String label, Object value) {

        this.code = code;
        this.label = label;
        this.value = value;
    }

    public static CustomFieldValue fromJson(JSONObject json) {
        if (!json.has(FIELD_VALUE)) {
            json.put(FIELD_VALUE, "");
        }
        if (json.get(FIELD_VALUE) instanceof JSONArray) {
            return new CustomFieldValue(json.getString("code"),
                    json.getString("label"),
                    json.getJSONArray(FIELD_VALUE).toList());
        }

        return new CustomFieldValue(json.getString("code"),
                json.getString("label"),
                json.optString(FIELD_VALUE, "")
        );
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public Object getValue() {
        return value;
    }
}
