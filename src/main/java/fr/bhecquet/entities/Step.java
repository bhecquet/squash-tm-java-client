package fr.bhecquet.entities;

import fr.bhecquet.exceptions.NotImplementedException;

public abstract class Step extends Entity {

    protected String expectedResult;
    protected int order;
    protected String action;

    protected Step(String url, String type, int id, Object o) {
        super(url, type, id, o != null ? o.toString() : null);
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public int getOrder() {
        return order;
    }

    public String getAction() {
        return action;
    }

    @Override
    public void completeDetails() {
        throw new NotImplementedException();
    }
}
