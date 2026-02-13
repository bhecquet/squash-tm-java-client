package io.github.bhecquet;

import io.github.bhecquet.entities.Campaign;
import io.github.bhecquet.entities.Project;
import io.github.bhecquet.entities.Requirement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRealSquashInstance {

    private SquashTMApi api;

    @BeforeMethod(alwaysRun = true)
    public void init() {
        String squashUrl = System.getProperty("squashUrl");
        String squashPassword = System.getProperty("squashPassword");

        if (squashUrl == null || squashPassword == null) {
            throw new SkipException("Squash not configured, provide 'squashUrl', 'squashPassword' as properties");
        }

        api = new SquashTMApi(squashUrl, squashPassword, false);
    }

    @Test
    public void testCreateCampaign() {

        String squashProject = System.getProperty("squashProject");
        Project project = Project.get(squashProject);
        Campaign.create(project, "campaignCF", (String) null,
                Map.of("TAGS", List.of("comp1", "comp2"))
        );
    }

    @Test
    public void testBindCustomField() {
        String squashProject = System.getProperty("squashProject");
        Project project = Project.get(squashProject);
        project.bindCustomField("TAGS", "CAMPAIGN");

    }

    @Test
    public void testRequirements() {
        String squashProject = System.getProperty("squashProject");
        Project project = Project.get(squashProject);
        Assert.assertFalse(Requirement.getAll(project).isEmpty());
        Requirement newReq = Requirement.create(project, false, "Req from API", "Req description", new HashMap<>(), "foo/bar", Requirement.Criticality.CRITICAL, "M1", Requirement.Status.WORK_IN_PROGRESS);
        newReq.update("Name updated", null, null, null, null);
    }
}
