package com.hatfat.agl.util;

public class Matrix {
    public float m[];

    public Matrix() {
        m = new float[16];

        setIdentity();
    }

    public void setIdentity() {
        m[0]   = 1.0f;
        m[1]   = 0.0f;
        m[2]   = 0.0f;
        m[3]   = 0.0f;
        m[4]   = 0.0f;
        m[5]   = 1.0f;
        m[6]   = 0.0f;
        m[7]   = 0.0f;
        m[8]   = 0.0f;
        m[9]   = 0.0f;
        m[10]  = 1.0f;
        m[11]  = 0.0f;
        m[12]  = 0.0f;
        m[13]  = 0.0f;
        m[14]  = 0.0f;
        m[15]  = 1.0f;
    }

    public void translate(Vec3 t) {
        m[12] += t.x;
        m[13] += t.y;
        m[14] += t.z;
    }
}
