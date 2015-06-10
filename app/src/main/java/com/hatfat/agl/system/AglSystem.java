package com.hatfat.agl.system;

import com.hatfat.agl.AglScene;
import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.entity.AglEntity;

import java.util.List;

public abstract class AglSystem {

    private final List<ComponentType> types;

    public AglSystem(List<ComponentType> types) {
        this.types = types;
    }

    public final void updateSystem(AglScene scene, float deltaTime) {
        AglEntity entities[] = scene.getEntities();

        for (int i = 0; i < scene.getNumEntities(); i++) {
            AglEntity entity = entities[i];
            boolean passes = true;

            for (ComponentType type : types) {
                if (entity.getComponentByType(type) == null) {
                    passes = false;
                    break;
                }
            }

            if (passes) {
                //passed all the type filters!
                updateEntity(entity, deltaTime);
            }
        }
    }

    /* update for an entity that matches the types filter list */
    abstract void updateEntity(AglEntity entity, float deltaTime);
}
