package io.github.bhecquet;


import io.github.bhecquet.entities.Entity;
import io.github.bhecquet.entities.IterationTestPlanItem;
import io.github.bhecquet.entities.Project;
import io.github.bhecquet.entities.TestPlanItemExecution;
import io.github.bhecquet.exceptions.ConfigurationException;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;

public class SquashTMApi {

    private String url;
    private String user;
    private String password;
    private String apiToken;
    private Project currentProject;


    public String getUser() {
        return user;
    }

    public String getApiToken() {
        return apiToken;
    }

    private void initConnection(String url, String user, String password) {
        this.url = url + "/api/rest/latest/";
        this.url = this.url.replace("//", "/").replace(":/", "://"); // in case of double '/' in URL
        this.user = user;
        this.password = password;

        Entity.configureEntity(user, password, this.url);

    }

    private void initConnectionByToken(String url, String token) {
        this.url = url + "/api/rest/latest/";
        this.url = this.url.replace("//", "/").replace(":/", "://"); // in case of double '/' in URL
        this.apiToken = token;

        Entity.configureEntityByToken(token, this.url);
    }

    public SquashTMApi(String url, String user, String password, boolean checkConnexion) {
        initConnection(url, user, password);
        if (checkConnexion) {
            testConnection(false);
        }

    }

    public SquashTMApi(String url, String apiToken, boolean checkConnexion) {
        initConnectionByToken(url, apiToken);
        if (checkConnexion) {
            testConnection(true);
        }
    }

    /**
     * Test we can join Squash TM server
     */
    public void testConnection(boolean byToken) {
        try {
            HttpResponse<JsonNode> json;
            if (!byToken) {
                json = Unirest.get(url + Project.PROJECTS_URL)
                        .basicAuth(user, password)
                        .asJson();
            } else {
                json = Unirest.get(url + Project.PROJECTS_URL)
                        .header("Authorization", "Bearer " + apiToken)
                        .asJson();
            }

            if (json.getStatus() != 200) {
                throw new ConfigurationException(String.format("Error when contacting Squash TM server API %s: %s", url, json.getStatusText()));
            }
        } catch (UnirestException e) {
            throw new ConfigurationException(String.format("Cannot contact Squash TM server API %s: %s", url, e.getMessage()));
        }


    }

    /**
     * Add an execution result to the test case
     *
     * @param testPlanItem the IterationTestPlanItem which has been executed
     * @param result       Execution status of the test
     */
    public void setExecutionResult(IterationTestPlanItem testPlanItem, TestPlanItemExecution.ExecutionStatus result) {
        setExecutionResult(testPlanItem, result, null);
    }


    /**
     * Add an execution result to the test case
     *
     * @param testPlanItem the IterationTestPlanItem which has been executed
     * @param result       Execution status of the test
     * @param comment      Comment to add to failed step
     */
    public void setExecutionResult(IterationTestPlanItem testPlanItem, TestPlanItemExecution.ExecutionStatus result, String comment) {
        testPlanItem.createExecutionWithResult(result, comment);
    }
}
