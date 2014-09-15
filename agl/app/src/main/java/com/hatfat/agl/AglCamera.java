package com.hatfat.agl;

public class AglCamera {

    private float fov;
    private float aspectRatio;

//    private Vector3f eye;
//    private Vector3f center;
//    private Vector3f up;

    public AglCamera() {
//        eye = new Vector3f(0.0f, 0.0f, 5.0f);
//        center = new Vector3f(0.0f, 0.0f, 0.0f);
//        up = new Vector3f(0.0f, 1.0f, 0.0f);
        fov = 60.0f;
    }

    void update(double time, double deltaTime) {

    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

//    public Matrix4f getProjectionMatrix() {
//        // Setup projection matrix
//        Matrix4f projectionMatrix = new Matrix4f();
//        float near_plane = 0.1f;
//        float far_plane = 100.0f;
//
//        float y_scale = this.coTangent(this.degreesToRadians(fov / 2.0f));
//        float x_scale = y_scale / aspectRatio;
//        float frustum_length = far_plane - near_plane;
//
//        float top = near_plane * (float)Math.tan(this.degreesToRadians(fov / 2.0f));
//        float bottom = -1.0f * top;
//        float right = top * aspectRatio;
//        float left = - 1.0f * right;
//
//        projectionMatrix.m00 = x_scale;
//        projectionMatrix.m11 = y_scale;
//        projectionMatrix.m20 = (right + left) / (right - left);
//        projectionMatrix.m21 = (top + bottom) / (top - bottom);
//        projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
//        projectionMatrix.m23 = -1;
//        projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
//        projectionMatrix.m33 = 0;
//
//        return projectionMatrix;
//    }

    private float coTangent(float radians) {
        return 1.0f / (float)Math.tan(radians);
    }

    private float degreesToRadians(float degrees) {
        return degrees * (float)Math.PI / 180.0f;
    }

//    public Matrix4f getViewMatrix() {
//        Vector3f l = Vector3f.sub(center, eye, null).normalise(null);
//        Vector3f s = Vector3f.cross(l, up, null).normalise(null);
//        Vector3f u = Vector3f.cross(s, l, null);
//
//        Matrix4f result = new Matrix4f();
//        result.m00 = s.x;
//        result.m10 = s.y;
//        result.m20 = s.z;
//        result.m01 = u.x;
//        result.m11 = u.y;
//        result.m21 = u.z;
//        result.m02 = -l.x;
//        result.m12 = -l.y;
//        result.m22 = -l.z;
//        result.m03 = -eye.x;
//        result.m13 = -eye.y;
//        result.m23 = -eye.z;
//
//        return result;
//    }
}
