package com.hatfat.agl.system;

import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.component.camera.CameraComponent;
import com.hatfat.agl.entity.AglEntity;

import java.util.Arrays;

public class CameraSystem extends AglSystem {

    private CameraComponent activeCameraComponent;

    public CameraSystem() {
        super(Arrays.asList(ComponentType.CAMERA));
    }

    @Override
    void updateEntity(AglEntity entity, float deltaTime) {
//        entity.getComponentsByType(ComponentType.CAMERA)
    }

    public CameraComponent getActiveCameraComponent() {
        return activeCameraComponent;
    }

    public void setActiveCameraEntity(AglEntity entity) {
        CameraComponent newActiveCameraComponent = entity.getComponentByType(ComponentType.CAMERA);

        if (activeCameraComponent != null) {
            activeCameraComponent.setActiveCamera(false);
            activeCameraComponent = null;
        }

        if (newActiveCameraComponent != null) {
            activeCameraComponent = newActiveCameraComponent;
            activeCameraComponent.setActiveCamera(true);
        }
    }
}
