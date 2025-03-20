package io.github.bhecquet.entities;

import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONObject;

public class CustomFieldValue {
    private String code;
    private String label;
    private Object value;

    public CustomFieldValue(String code, String label, Object value) {

        this.code = code;
        this.label = label;
        this.value = value;
    }

    public static CustomFieldValue fromJson(JSONObject json) {
        if (!json.has("value")) {
            json.put("value", "");
        }
        if (json.get("value") instanceof JSONArray) {
            return new CustomFieldValue(json.getString("code"),
                    json.getString("label"),
                    json.getJSONArray("value").toList());
        }

        return new CustomFieldValue(json.getString("code"),
                json.getString("label"),
                json.optString("value", "")
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
