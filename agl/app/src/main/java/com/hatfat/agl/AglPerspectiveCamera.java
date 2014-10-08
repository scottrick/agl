package com.hatfat.agl;

import android.opengl.Matrix;

import com.hatfat.agl.util.Vec3;

public class AglPerspectiveCamera implements AglCamera {

    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

    private Vec3 eye;
    private Vec3 center;
    private Vec3 up;

    public AglPerspectiveCamera(Vec3 eye, Vec3 center, Vec3 up,
                                float fov, float aspectRatio, float nearPlane, float farPlane) {
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
}
