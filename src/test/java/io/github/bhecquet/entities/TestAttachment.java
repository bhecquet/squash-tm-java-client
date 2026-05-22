package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;

public class TestAttachment extends SquashTMTest {

    private static final String ATTACHMENT_REPLY_DATA = "{"
            + "\"_type\":\"attachment\","
            + "\"id\":42,"
            + "\"name\":\"screenshot.png\","
            + "\"size\":1024,"
            + "\"fileType\":\"image/png\","
            + "\"addedOn\":\"2025-01-15T10:30:00.000+0000\","
            + "\"_links\":{\"self\":{\"href\":\"https://localhost:4321/attachments/42\"}}"
            + "}";

    private static final String ATTACHMENT_REPLY_DATA_NO_NAME = "{"
            + "\"_type\":\"attachment\","
            + "\"id\":43,"
            + "\"size\":512,"
            + "\"fileType\":\"text/plain\","
            + "\"addedOn\":\"2025-02-20T14:00:00.000+0000\","
            + "\"_links\":{\"self\":{\"href\":\"https://localhost:4321/attachments/43\"}}"
            + "}";

    @BeforeMethod
    public void init() {
        Attachment.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    // ── fromJson ──────────────────────────────────────────────────────────────

    @Test
    public void testFromJson() {
        Attachment attachment = Attachment.fromJson(new JSONObject(ATTACHMENT_REPLY_DATA));
        Assert.assertEquals(attachment.getId(), 42);
        Assert.assertEquals(attachment.getName(), "screenshot.png");
        Assert.assertEquals(attachment.getType(), "attachment");
        Assert.assertEquals(attachment.getUrl(), "https://localhost:4321/attachments/42");
    }

    @Test
    public void testFromJsonWithoutName() {
        Attachment attachment = Attachment.fromJson(new JSONObject(ATTACHMENT_REPLY_DATA_NO_NAME));
        Assert.assertEquals(attachment.getId(), 43);
        Assert.assertEquals(attachment.getName(), "");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonMissingId() {
        Attachment.fromJson(new JSONObject("{"
                + "\"_type\":\"attachment\","
                + "\"size\":100,"
                + "\"fileType\":\"text/plain\","
                + "\"addedOn\":\"2025-01-01T00:00:00.000+0000\","
                + "\"_links\":{\"self\":{\"href\":\"https://localhost:4321/attachments/1\"}}"
                + "}"));
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonMissingLinks() {
        Attachment.fromJson(new JSONObject("{"
                + "\"_type\":\"attachment\","
                + "\"id\":1,"
                + "\"size\":100,"
                + "\"fileType\":\"text/plain\","
                + "\"addedOn\":\"2025-01-01T00:00:00.000+0000\""
                + "}"));
    }

    // ── get ────────────────────────────────────────────────────────────────────

    @Test
    public void testGet() {
        createServerMock("GET", "/attachments/42", 200, ATTACHMENT_REPLY_DATA);
        Attachment attachment = Attachment.get(42);
        Assert.assertEquals(attachment.getId(), 42);
        Assert.assertEquals(attachment.getName(), "screenshot.png");
        Assert.assertEquals(attachment.getType(), "attachment");
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testGetWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/attachments/42", 200, ATTACHMENT_REPLY_DATA);
        when(getRequest.asJson()).thenThrow(UnirestException.class);
        Attachment.get(42);
    }

    // ── completeDetails ────────────────────────────────────────────────────────

    @Test
    public void testCompleteDetails() {
        // Premier appel pour créer l'objet, second pour compléter les détails
        createServerMock("GET", "/attachments/42", 200, ATTACHMENT_REPLY_DATA);
        Attachment attachment = Attachment.get(42);

        // completeDetails fait un GET sur l'URL de l'attachment
        createServerMock("GET", "/attachments/42", 200, "{"
                + "\"_type\":\"attachment\","
                + "\"id\":42,"
                + "\"name\":\"screenshot.png\","
                + "\"size\":2048,"
                + "\"fileType\":\"image/jpeg\","
                + "\"addedOn\":\"2025-03-10T08:00:00.000+0000\","
                + "\"_links\":{\"self\":{\"href\":\"https://localhost:4321/attachments/42\"}}"
                + "}");
        attachment.completeDetails();
        // Vérifie que l'appel ne lève pas d'exception
        Assert.assertEquals(attachment.getId(), 42);
    }
}

