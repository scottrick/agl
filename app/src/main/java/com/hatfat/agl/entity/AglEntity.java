package com.hatfat.agl.entity;

import com.hatfat.agl.component.AglComponent;
import com.hatfat.agl.component.ComponentType;

import java.util.LinkedList;
import java.util.List;

public class AglEntity {

    /* entity scene id */
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
        }
        else {
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

    /**
     * Adds a given list of components to this entity.
     *
     * @param components The list of components to add.
     */
    public void addComponents(List<AglComponent> components) {
        for (AglComponent component : components) {
            addComponent(component);
        }
    }

    /**
     * Adds the given compoent to this entity.
     * Will throw a RuntimeException if there are too many components.
     *
     * @param component The entity to add.
     */
    public void addComponent(AglComponent component) {
        if (numComponents + 1 >= maxNumComponents) {
            throw new RuntimeException("Can't add more components!");
        }

        components[numComponents] = component;
        numComponents++;
    }

    /**
     * Removes the given component from this entity.
     *
     * @param component The component to remove.
     */
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

    /**
     * Remove all components from this entity.
     */
    protected void removeAllComponents() {
        for (int i = 0; i < numComponents; i++) {
            components[i] = null;
        }

        numComponents = 0;
    }

    /**
     * Returns the first component of the given type.
     *
     * @param type type of component to retrieve.
     * @param <T>
     * @return The first component of the given type we found; null if there are none.
     */
    @SuppressWarnings("unchecked")
    public <T extends AglComponent> T getComponentByType(ComponentType type) {
        for (int i = 0; i < numComponents; i++) {
            if (components[i].type == type) {
                return (T) components[i];
            }
        }

        return null;
    }

    /**
     * Returns all of the components of the given type.
     *
     * @param type type of the components to retrieve.
     * @param <T>
     * @return The list of components that were found.  Empty list if there were none.
     */
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

    /**
     * Returns the entity's name, mainly for debugging purposes.
     *
     * @return The entity's name.
     */
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
