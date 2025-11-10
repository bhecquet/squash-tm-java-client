package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.ExecutionStepToExcludeException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExecutionStep extends Step {

    private static final String EXECUTION_URL = "executions/%d";
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

    protected List<CustomFieldValue> testStepCustomFields;

    public ExecutionStep(String url, int id, String name) {
        this(url, "", id, name);
    }

    public ExecutionStep(String url, String type, int id, String name) {
        super(url, type, id, name);
    }

    public ExecutionStep(String url, String type, int id, String name, String comment, String status, String lastExecutedBy, String lastExecutedOn, int referencedStepId) {
        super(url, type, id, name);
        this.comment = comment;
        this.status = status;
        this.lastExecutedBy = lastExecutedBy;
        this.lastExecutedOn = lastExecutedOn;
        this.referencedStepId = referencedStepId;
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

    public List<CustomFieldValue> getTestStepCustomFields() {
        return testStepCustomFields;
    }

    @Override
    public void completeDetails() {
        JSONObject json = getJSonResponse(buildGetRequest(url));

        status = json.getString(FIELD_EXECUTION_STATUS);
        order = json.getInt(FIELD_EXECUTION_STEP_ORDER);
        expectedResult = json.optString(FIELD_EXPECTED_RESULT, "");
        action = json.optString(FIELD_ACTION, "");
        comment = json.optString(FIELD_COMMENT, "");
        lastExecutedBy = json.optString(FIELD_LAST_EXECUTED_BY, "");
        lastExecutedOn = json.optString(FIELD_LAST_EXECUTED_ON, "");
        try {
            // when "referenced_test_step" is null, exclude line
            referencedStepId = json.getJSONObject(FIELD_REFERENCED_TEST_STEP).getInt(FIELD_ID);
        } catch (JSONException e) {
            throw new ExecutionStepToExcludeException();
        }

        readCustomFields(json.getJSONArray(FIELD_CUSTOM_FIELDS));

        testStepCustomFields = new ArrayList<>();
        for (JSONObject field : (List<JSONObject>) json.getJSONArray(FIELD_TEST_STEP_CUSTOM_FIELDS).toList()) {
            testStepCustomFields.add(CustomFieldValue.fromJson(field));
        }
    }

    public static ExecutionStep get(int id) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format(EXECUTION_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("L'exécution step %d n'existe pas", id));
        }
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

    public static ExecutionStep fromJsonFull(JSONObject json) {
        try {
            return new ExecutionStep(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID),
                    json.getString(FIELD_ACTION),
                    json.getString(FIELD_COMMENT),
                    json.getString(FIELD_EXECUTION_STATUS),
                    json.getString(FIELD_LAST_EXECUTED_BY),
                    json.getString(FIELD_LAST_EXECUTED_ON),
                    json.getInt(FIELD_REFERENCED_TEST_STEP)
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
