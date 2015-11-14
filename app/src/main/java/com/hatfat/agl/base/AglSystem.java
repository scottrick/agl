package com.hatfat.agl.base;

import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.entity.AglEntity;

import java.util.List;

public abstract class AglSystem {

    /* the scene this system is being use in */
    private AglScene scene;

    /* the ComponentTypes this system operates on */
    private final List<ComponentType> types;

    public AglSystem(List<ComponentType> types) {
        this.types = types;
    }

    public final void updateSystem(float deltaTime) {
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
                //passed all the typeId filters!
                updateEntity(entity, deltaTime);
            }
        }
    }

    protected final AglScene getScene() {
        return scene;
    }

    final void setScene(AglScene scene) {
        this.scene = scene;
    }

    /* create any OpenGL renderables here for use later */
    public abstract void prepareRenderables(AglRenderer renderer);

    /* update for an entity that matches the types filter list */
    public abstract void updateEntity(AglEntity entity, float deltaTime);
}
