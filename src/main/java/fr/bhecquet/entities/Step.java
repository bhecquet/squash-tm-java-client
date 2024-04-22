package fr.bhecquet.entities;

import fr.bhecquet.exceptions.NotImplementedException;

public abstract class Step extends Entity {

    protected String expectedResult;
    protected int order;
    protected String action;

    public Step(String url, String type, int id, Object o) {
        super(url, type, id, null);
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
