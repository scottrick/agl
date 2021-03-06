package com.hatfat.agl.component.camera;

import android.opengl.Matrix;

import com.hatfat.agl.util.Vec3;

public class PerspectiveCameraComponent extends CameraComponent {

    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

    private Vec3 eye;
    private Vec3 center;
    private Vec3 up;

    public PerspectiveCameraComponent(Vec3 eye, Vec3 center, Vec3 up,
                                float fov, float aspectRatio, float nearPlane, float farPlane) {
        super();

        this.eye = eye;
        this.center = center;
        this.up = up;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    @Override
    public float[] getViewMatrix() {
        float viewMatrix[] = new float[16];
        Matrix.setLookAtM(viewMatrix, 0, eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
        return viewMatrix;
    }

    @Override
    public float[] getProjMatrix() {
        float projMatrix[] = new float[16];
        Matrix.perspectiveM(projMatrix, 0, fov, aspectRatio, nearPlane, farPlane);
        return projMatrix;
    }

    public Vec3 getEye() {
        return eye;
    }

    public Vec3 getCenter() {
        return center;
    }

    public Vec3 getUp() {
        return up;
    }
}
