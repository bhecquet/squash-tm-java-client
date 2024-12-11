package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.NotImplementedException;

public class Clearance extends Entity {


    protected Clearance(String url, String type, int id, String name) {
        super(url, type, id, name);
    }

    @Override
    public void completeDetails() {
        throw new NotImplementedException();
    }
}
