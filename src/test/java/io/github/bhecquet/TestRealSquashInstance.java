package io.github.bhecquet;

import io.github.bhecquet.entities.Campaign;
import io.github.bhecquet.entities.Project;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class TestRealSquashInstance {

    private SquashTMApi api;

    @BeforeMethod(alwaysRun = true)
    public void init() {
        String squashUrl = System.getProperty("squashUrl");
        String squashUser = System.getProperty("squashUser");
        String squashPassword = System.getProperty("squashPassword");

        if (squashUrl == null || squashUser == null || squashPassword == null) {
            throw new SkipException("Squash not configured, provide 'squashUrl', 'squashUser', 'squashPassword' as properties");
        }

        api = new SquashTMApi(squashUrl, squashUser, squashPassword, false);
    }

    @Test
    public void testCreateCampaign() {

        String squashProject = System.getProperty("squashProject");
        Project project = Project.get(squashProject);
        Campaign.create(project, "campaignCF", (String) null, Map.of("APP", List.of("comp1", "comp2")));
    }
}
