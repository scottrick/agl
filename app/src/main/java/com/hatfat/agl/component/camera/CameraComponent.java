package com.hatfat.agl.component.camera;

import com.hatfat.agl.component.AglComponent;
import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.util.Vec3;

public abstract class CameraComponent extends AglComponent {

    private boolean activeCamera = false;

    public CameraComponent() {
        super(ComponentType.CAMERA);
    }

    public abstract float[] getViewMatrix();

    public abstract float[] getProjMatrix();

    public abstract void setAspectRatio(float aspectRatio);

    public abstract Vec3 getEye();

    public abstract Vec3 getCenter();

    public abstract Vec3 getUp();

    public void setActiveCamera(boolean activeCamera) {
        this.activeCamera = activeCamera;
    }

    public boolean isActiveCamera() {
        return activeCamera;
    }
}
