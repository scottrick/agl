package com.hatfat.agl;

import android.opengl.Matrix;

import com.hatfat.agl.util.Vec3;

public class AglOrthographicCamera implements AglCamera {

    private Vec3 eye;
    private Vec3 center;
    private Vec3 up;

    private float aspectRatio = 60.0f; //default starting aspect ratio
    private float width;

    private float left;
    private float right;
    private float bottom;
    private float top;
    private float near;
    private float far;

    public AglOrthographicCamera(Vec3 eye, Vec3 center, Vec3 up,
                                 float width,
                                 float near, float far) {
        this.eye = eye;
        this.center = center;
        this.up = up;

        this.width = width;
        this.near = near;
        this.far = far;
    }

    @Override
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        updateBounds();
    }

    protected void setWidth(float width) {
        this.width = width;
        updateBounds();
    }

    private void updateBounds() {
        float height = width / aspectRatio;

        this.left = -width / 2.0f;
        this.right = width / 2.0f;
        this.bottom = -height / 2.0f;
        this.top = height / 2.0f;
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
        Matrix.orthoM(projMatrix, 0, left, right, bottom, top, near, far);
        return projMatrix;
    }

    protected float getWidth() {
        return width;
    }

    protected Vec3 getEye() {
        return eye;
    }

    protected Vec3 getCenter() {
        return center;
    }

    protected Vec3 getUp() {
        return up;
    }
}
