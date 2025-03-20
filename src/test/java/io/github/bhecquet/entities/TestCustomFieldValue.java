package io.github.bhecquet.entities;

import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class TestCustomFieldValue {

    @Test
    public void testFromJson() {
        JSONObject json = new JSONObject("{" +
                "        \"code\" : \"CUF_Z\"," +
                "        \"label\" : \"Cuf Z\"," +
                "        \"value\" : \"value of Z\"" +
                "      }");
        CustomFieldValue customField = CustomFieldValue.fromJson(json);
        Assert.assertEquals(customField.getCode(), "CUF_Z");
        Assert.assertEquals(customField.getLabel(), "Cuf Z");
        Assert.assertEquals(customField.getValue(), "value of Z");
    }

    @Test
    public void testFromJsonListValue() {
        JSONObject json = new JSONObject("{" +
                "        \"code\" : \"CUF_Z\"," +
                "        \"label\" : \"Cuf Z\"," +
                "        \"value\" : [ \"foo\", \"bar2\" ]" +
                "      }");
        CustomFieldValue customField = CustomFieldValue.fromJson(json);
        Assert.assertEquals(customField.getCode(), "CUF_Z");
        Assert.assertEquals(customField.getLabel(), "Cuf Z");
        Assert.assertEquals(customField.getValue(), List.of("foo", "bar2"));
    }

    @Test
    public void testFromJsonNoValue() {
        JSONObject json = new JSONObject("{" +
                "        \"code\" : \"CUF_Z\"," +
                "        \"label\" : \"Cuf Z\"" +
                "      }");
        CustomFieldValue customField = CustomFieldValue.fromJson(json);
        Assert.assertEquals(customField.getCode(), "CUF_Z");
        Assert.assertEquals(customField.getLabel(), "Cuf Z");
        Assert.assertEquals(customField.getValue(), "");
    }

    @Test
    public void testFromJsonNullValue() {
        JSONObject json = new JSONObject("{" +
                "        \"code\" : \"CUF_Z\"," +
                "        \"label\" : \"Cuf Z\"," +
                "        \"value\" : null" +
                "      }");
        CustomFieldValue customField = CustomFieldValue.fromJson(json);
        Assert.assertEquals(customField.getCode(), "CUF_Z");
        Assert.assertEquals(customField.getLabel(), "Cuf Z");
        Assert.assertEquals(customField.getValue(), "");
    }
}
