package com.hatfat.agl;

import android.opengl.Matrix;

import com.hatfat.agl.util.Vec3;

public class AglOrthographicCamera implements AglCamera {

    private Vec3 eye;
    private Vec3 center;
    private Vec3 up;

    private float left;
    private float right;
    private float bottom;
    private float top;
    private float near;
    private float far;

    public AglOrthographicCamera(Vec3 eye, Vec3 center, Vec3 up,
                                 float left, float right,
                                 float bottom, float top,
                                 float near, float far) {
        this.eye = eye;
        this.center = center;
        this.up = up;

        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.near = near;
        this.far = far;
    }

    @Override
    public void setAspectRatio(float aspectRatio) {
        float width = 36.0f;
        float height = width / aspectRatio;

        this.left = -width;
        this.right = width;
        this.bottom = -height;
        this.top = height;
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
}
