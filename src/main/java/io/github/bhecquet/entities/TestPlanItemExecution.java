package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a test case in Squash TM
 */
public class TestPlanItemExecution extends Entity {

    public enum ExecutionStatus {
        RUNNING,
        READY,
        SUCCESS,
        FAILURE,
        BLOCKED
    }


    public static final String FIELD_EXECUTION_STATUS = "execution_status";

    List<ExecutionStep> steps;

    public TestPlanItemExecution(String url, String type, int id, String name) {
        super(url, type, id, name);
        steps = new ArrayList<>();
    }

    @Override
    public void completeDetails() {
        throw new NotImplementedException();
    }

    public static TestPlanItemExecution fromJson(JSONObject json) {

        try {
            TestPlanItemExecution testPlanItemExecution = new TestPlanItemExecution(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_NAME)
            );

            JSONArray steps = json.optJSONArray("execution_steps");
            if (steps != null) {
                for (JSONObject jsonStep : (List<JSONObject>) steps.toList()) {
                    testPlanItemExecution.steps.add(ExecutionStep.fromJson(jsonStep));
                }
            }

            return testPlanItemExecution;
        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create TestPlanItemException from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public void setResult(ExecutionStatus result, String comment) {

        if (!steps.isEmpty()) {
            steps.get(0).setStatus(result);
            if (comment != null) {
                steps.get(0).setComment(comment);
            }
        }

        JSONObject body = new JSONObject();
        body.put("_type", "execution");
        body.put(FIELD_EXECUTION_STATUS, result.toString());
        try {
            getJSonResponse(buildPatchRequest(String.format("%s?fields=execution_status", url)).body(body));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot set result for execution %d", id));
        }
    }


    public List<ExecutionStep> getSteps() {
        return steps;
    }


    public void setSteps(List<ExecutionStep> steps) {
        this.steps = steps;
    }
}
