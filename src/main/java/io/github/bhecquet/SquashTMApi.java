package io.github.bhecquet;


import io.github.bhecquet.entities.*;
import io.github.bhecquet.exceptions.ConfigurationException;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;

public class SquashTMApi {

    private String url;
    private String user;
    private String password;
    private Project currentProject;


    private void initConnection(String url, String user, String password) {
        this.url = url + "/api/rest/latest/";
        this.url = this.url.replace("//", "/").replace(":/", "://"); // in case of double '/' in URL
        this.user = user;
        this.password = password;

        Entity.configureEntity(user, password, this.url);
        testConnection();
    }

    public SquashTMApi(String url, String user, String password, String projectName) {
        initConnection(url, user, password);

        currentProject = Project.get(projectName);
    }

    public SquashTMApi(String url, String user, String password, int iterationId) {
        initConnection(url, user, password);
        Iteration iteration = Iteration.get(iterationId);

        currentProject = iteration.getProject();
    }

    /**
     * Test we can join Squash TM server
     */
    public void testConnection() {
        try {
            HttpResponse<JsonNode> json = Unirest.get(url + Project.PROJECTS_URL)
                    .basicAuth(user, password)
                    .asJson();

            if (json.getStatus() != 200) {
                throw new ConfigurationException(String.format("Error when contactin Squash TM server API %s: %s", url, json.getStatusText()));
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
        TestPlanItemExecution execution = testPlanItem.createExecution();
        execution.setResult(result, comment);
    }

    public Project getCurrentProject() {
        return currentProject;
    }
}
