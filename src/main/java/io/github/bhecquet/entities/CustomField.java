package io.github.bhecquet.entities;

import kong.unirest.core.json.JSONObject;

public class CustomField {
    private String code;
    private String label;
    private String value;

    public CustomField(String code, String label, String value) {
        this.code = code;
        this.label = label;
        this.value = value;
    }

    public static CustomField fromJson(JSONObject json) {
        return new CustomField(json.getString("code"),
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

    public String getValue() {
        return value;
    }
}
