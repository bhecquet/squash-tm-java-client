package fr.bhecquet.entities;

import fr.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Execution extends Entity {

    private static final String EXECUTION_URL = "executions/%d";

    private static final String FIELD_EXECUTION_STEPS = "execution_steps";
    private static final String FIELD_EXECUTION_STATUS = "execution_status";
    private static final String FIELD_EXECUTION_ORDER = "execution_order";
    private static final String FIELD_LAST_EXECUTED_BY = "last_executed_by";
    private static final String FIELD_LAST_EXECUTED_ON = "last_executed_on";

    private List<ExecutionStep> executionSteps;
    private String status;
    private Integer order;
    private String lastExecutedBy;
    private String lastExecutedOn;

    public Execution(String url, String type, int id) {
        super(url, type, id, null);
    }

    public static Execution fromJson(JSONObject json) {
        try {
            return new Execution(
                    json.getJSONObject("_links").getJSONObject("self").getString("href"),
                    json.getString(FIELD_TYPE),
                    json.getInt(FIELD_ID)
            );

        } catch (JSONException e) {
            throw new SquashTmException(String.format("Cannot create execution from JSON [%s] data: %s", json.toString(), e.getMessage()));
        }
    }

    @Override
    public void completeDetails() {
        JSONObject json = getJSonResponse(Unirest.get(String.format("%s", url)));
        executionSteps = new ArrayList<>();

        status = json.getString(FIELD_EXECUTION_STATUS);
        order = json.getInt(FIELD_EXECUTION_ORDER);
        lastExecutedBy = json.optString(FIELD_LAST_EXECUTED_BY, ""); // dans le cas où ce n'est pas exécuté, les données sont absentes
        lastExecutedOn = json.optString(FIELD_LAST_EXECUTED_ON, "");

        for (JSONObject jsonIterationTestPlanItem : (List<JSONObject>) json.getJSONArray(FIELD_EXECUTION_STEPS).toList()) {
            executionSteps.add(ExecutionStep.fromJson(jsonIterationTestPlanItem));
        }

        readCustomFields(json.getJSONArray(FIELD_CUSTOM_FIELDS));
    }

    public static Execution get(int id) {
        try {
            return fromJson(getJSonResponse(buildGetRequest(apiRootUrl + String.format(EXECUTION_URL, id))));
        } catch (UnirestException e) {
            throw new SquashTmException(String.format("Execution %d does not exist", id));
        }
    }

    public List<ExecutionStep> getExecutionSteps() {
        return executionSteps;
    }

    public String getStatus() {
        return status;
    }

    public Integer getOrder() {
        return order;
    }

    public String getLastExecutedBy() {
        return lastExecutedBy;
    }

    public String getLastExecutedOn() {
        return lastExecutedOn;
    }
}
