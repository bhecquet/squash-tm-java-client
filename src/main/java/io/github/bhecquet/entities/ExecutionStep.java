package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.List;

public class ExecutionStep extends Step {

    public static final String EXECUTION_STATUS_URL = "/execution-steps/%d/execution-status";
    private static final String FIELD_TEST_STEP_CUSTOM_FIELDS = "test_step_custom_fields";
    private static final String FIELD_EXECUTION_STATUS = "execution_status";
    private static final String FIELD_EXPECTED_RESULT = "expected_result";
    private static final String FIELD_EXECUTION_STEP_ORDER = "execution_step_order";
    private static final String FIELD_REFERENCED_TEST_STEP = "referenced_test_step";
    private static final String FIELD_ACTION = "action";
    private static final String FIELD_LAST_EXECUTED_BY = "last_executed_by";
    private static final String FIELD_LAST_EXECUTED_ON = "last_executed_on";
    private static final String FIELD_COMMENT = "comment";

    private String status;
    private String lastExecutedBy;
    private String lastExecutedOn;
    private String comment;
    private int referencedStepId;

    protected List<CustomField> testStepCustomFields;

    public ExecutionStep(String url, int id, String name) {
        this(url, "", id, name);
    }

    public ExecutionStep(String url, String type, int id, String name) {
        super(url, type, id, name);
    }


    public int getReferencedStepId() {
        return referencedStepId;
    }

    public String getStatus() {
        return status;
    }

    public String getLastExecutedBy() {
        return lastExecutedBy;
    }

    public String getLastExecutedOn() {
        return lastExecutedOn;
    }

    public String getComment() {
        return comment;
    }

    public List<CustomField> getTestStepCustomFields() {
        return testStepCustomFields;
    }

    @Override
    public void completeDetails() {
        throw new NotImplementedException();
    }

    public static ExecutionStep fromJson(JSONObject json) {
        try {
            return new ExecutionStep(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_ACTION)
            );

        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create execution step from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    public void setStatus(TestPlanItemExecution.ExecutionStatus status) {
        if (status == null) {
            return;
        }
        try {
            getJSonResponse(buildPatchRequest(String.format("%s/execution-status/%s", url, status.toString())));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot set result for execution step %d", id));
        }
    }

    public void setComment(String comment) {

        JSONObject body = new JSONObject();
        body.put("_type", "execution-step");
        body.put(FIELD_COMMENT, comment);
        try {
            getJSonResponse(buildPatchRequest(url).body(body));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Cannot set result for execution step %d", id));
        }
    }


}
