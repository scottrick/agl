package com.hatfat.agl.entity;

import com.hatfat.agl.component.AglComponent;
import com.hatfat.agl.component.ComponentType;

import java.util.LinkedList;
import java.util.List;

public class AglEntity {

    /* entity scene index id */
    public int entityId;

    /* entity name, used for debugging */
    private String entityName;

    /* components */
    protected int numComponents    = 0;
    protected int maxNumComponents = 8;
    protected AglComponent[] components;

    public AglEntity(String entityName) {
        if (entityName != null) {
            this.entityName = entityName;
        } else {
            this.entityName = this.getClass().getSimpleName();
        }

        init();
    }

    public AglEntity() {
        this.entityName = this.getClass().getSimpleName();

        init();
    }

    private void init() {
        components = new AglComponent[maxNumComponents];
    }

    public void addComponents(List<AglComponent> components) {
        for (AglComponent component : components) {
            addComponent(component);
        }
    }

    public void addComponent(AglComponent component) {
        if (numComponents + 1 >= maxNumComponents) {
            throw new RuntimeException("Can't add more components!");
        }

        components[numComponents] = component;
        numComponents++;
    }

    public void removeComponent(AglComponent component) {
        int modifierIndex = -1;

        for (int i = 0; i < numComponents; i++) {
            if (components[i] == component) {
                modifierIndex = i;
                break;
            }
        }

        if (modifierIndex < 0) {
            //component was not removed
            return;
        }

        components[modifierIndex] = components[numComponents - 1]; //swap the last component with the component we are removing

        numComponents--;

        components[numComponents] = null;
    }

    protected void removeAllNodes() {
        for (int i = 0; i < numComponents; i++) {
            components[i] = null;
        }

        numComponents = 0;
    }

    @SuppressWarnings("unchecked")
    public <T extends AglComponent> T getComponentByType(ComponentType type) {
        for (int i = 0; i < numComponents; i++) {
            if (components[i].type == type) {
                return (T) components[i];
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends AglComponent> List<T> getComponentsByType(ComponentType type) {
        List<T> matchingComponents = new LinkedList<>();

        for (int i = 0; i < numComponents; i++) {
            if (components[i].type == type) {
                matchingComponents.add((T)components[i]);
            }
        }

        return matchingComponents;
    }

    public final String getEntityName() {
        return entityName;
    }

    @Override public String toString() {
        String result = entityName;

        if (numComponents > 0) {
            result += "\n--  ";
        }
        else {
            result += "\n--  No components!";
        }

        for (int i = 0; i < numComponents; i++) {
            AglComponent component = components[i];
            result += component.getClass().getSimpleName();

            if (i + 1 < numComponents) {
                result += ", ";
            }
        }

        return result;
    }
}
