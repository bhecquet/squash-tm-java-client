package io.github.bhecquet;

import io.github.bhecquet.entities.Campaign;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestRealSquashInstance {

    private SquashTMApi api;

    @BeforeMethod(alwaysRun = true)
    public void init() {
        String squashUrl = System.getProperty("squashUrl");
        String squashUser = System.getProperty("squashUser");
        String squashPassword = System.getProperty("squashPassword");
        String squashProject = System.getProperty("squashProject");

        if (squashUrl == null || squashUser == null || squashPassword == null || squashProject == null) {
            throw new SkipException("Squash not configured, provide 'squashUrl', 'squashUser', 'squashPassword', 'squashProject' as properties");
        }

        api = new SquashTMApi(squashUrl, squashUser, squashPassword, squashProject);
    }

    @Test
    public void testCreateCampaign() {
        Campaign campaign = Campaign.create(api.getCurrentProject(), "testCampaign", (String) null);
    }
}
