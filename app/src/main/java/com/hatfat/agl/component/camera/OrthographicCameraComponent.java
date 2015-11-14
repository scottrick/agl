package com.hatfat.agl.component.camera;

import android.opengl.Matrix;

import com.hatfat.agl.util.Vec3;

public class OrthographicCameraComponent extends CameraComponent {

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

    public OrthographicCameraComponent(
            Vec3 eye, Vec3 center, Vec3 up,
            float width, float near, float far) {

        super();

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
        Matrix.setLookAtM(viewMatrix, 0, eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x,
                up.y, up.z);
        return viewMatrix;
    }

    @Override
    public float[] getProjMatrix() {
        float projMatrix[] = new float[16];
        Matrix.orthoM(projMatrix, 0, left, right, bottom, top, near, far);
        return projMatrix;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return top - bottom;
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

    protected float getLeft() {
        return left;
    }

    protected float getRight() {
        return right;
    }

    protected float getTop() {
        return top;
    }

    protected float getBottom() {
        return bottom;
    }

    protected float getNear() {
        return near;
    }

    protected float getFar() {
        return far;
    }
}
