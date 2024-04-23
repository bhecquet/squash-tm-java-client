package fr.bhecquet.entities;

import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCustomField {

    @Test
    public void testFromJson() {
        JSONObject json = new JSONObject("{" +
                "        \"code\" : \"CUF_Z\"," +
                "        \"label\" : \"Cuf Z\"," +
                "        \"value\" : \"value of Z\"" +
                "      }");
        CustomField customField = CustomField.fromJson(json);
        Assert.assertEquals(customField.getCode(), "CUF_Z");
        Assert.assertEquals(customField.getLabel(), "Cuf Z");
        Assert.assertEquals(customField.getValue(), "value of Z");
    }

    @Test
    public void testFromJsonNoValue() {
        JSONObject json = new JSONObject("{" +
                "        \"code\" : \"CUF_Z\"," +
                "        \"label\" : \"Cuf Z\"" +
                "      }");
        CustomField customField = CustomField.fromJson(json);
        Assert.assertEquals(customField.getCode(), "CUF_Z");
        Assert.assertEquals(customField.getLabel(), "Cuf Z");
        Assert.assertEquals(customField.getValue(), "");
    }
}
