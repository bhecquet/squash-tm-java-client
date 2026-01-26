package io.github.bhecquet.entities;

public class ParentEntity {

    private Entity parent;

    public ParentEntity(Entity parentEntity) {
        this.parent = parentEntity;
    }

    public int getId() {
        return parent.getId();
    }

    public String getType() {
        return parent.getType();
    }
}
