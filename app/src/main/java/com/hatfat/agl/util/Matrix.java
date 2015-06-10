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

    public void set(Matrix other) {
        m[0]   = other.m[0];
        m[1]   = other.m[1];
        m[2]   = other.m[2];
        m[3]   = other.m[3];
        m[4]   = other.m[4];
        m[5]   = other.m[5];
        m[6]   = other.m[6];
        m[7]   = other.m[7];
        m[8]   = other.m[8];
        m[9]   = other.m[9];
        m[10]  = other.m[10];
        m[11]  = other.m[11];
        m[12]  = other.m[12];
        m[13]  = other.m[13];
        m[14]  = other.m[14];
        m[15]  = other.m[15];
    }

    public static Matrix multiplyBy(Matrix m1, Matrix m2) {
        Matrix result = new Matrix();

        //////////////////////////////////////////////
        // ROW ONE
        //////////////////////////////////////////////
        result.m[0] =
                m1.m[0] * m2.m[0] +
                m1.m[1] * m2.m[4] +
                m1.m[2] * m2.m[8] +
                m1.m[3] * m2.m[12];

        result.m[1] =
                m1.m[0] * m2.m[1] +
                m1.m[1] * m2.m[5] +
                m1.m[2] * m2.m[9] +
                m1.m[3] * m2.m[13];

        result.m[2] =
                m1.m[0] * m2.m[2] +
                m1.m[1] * m2.m[6] +
                m1.m[2] * m2.m[10] +
                m1.m[3] * m2.m[14];

        result.m[3] =
                m1.m[0] * m2.m[3] +
                m1.m[1] * m2.m[7] +
                m1.m[2] * m2.m[11] +
                m1.m[3] * m2.m[15];

        //////////////////////////////////////////////
        // ROW TWO
        //////////////////////////////////////////////
        result.m[4] =
                m1.m[4] * m2.m[0] +
                m1.m[5] * m2.m[4] +
                m1.m[6] * m2.m[8] +
                m1.m[7] * m2.m[12];

        result.m[5] =
                m1.m[4] * m2.m[1] +
                m1.m[5] * m2.m[5] +
                m1.m[6] * m2.m[9] +
                m1.m[7] * m2.m[13];

        result.m[6] =
                m1.m[4] * m2.m[2] +
                m1.m[5] * m2.m[6] +
                m1.m[6] * m2.m[10] +
                m1.m[7] * m2.m[14];

        result.m[7] =
                m1.m[4] * m2.m[3] +
                m1.m[5] * m2.m[7] +
                m1.m[6] * m2.m[11] +
                m1.m[7] * m2.m[15];

        //////////////////////////////////////////////
        // ROW THREE
        //////////////////////////////////////////////
        result.m[8] =
                m1.m[8] * m2.m[0] +
                m1.m[9] * m2.m[4] +
                m1.m[10] * m2.m[8] +
                m1.m[11] * m2.m[12];

        result.m[9] =
                m1.m[8] * m2.m[1] +
                m1.m[9] * m2.m[5] +
                m1.m[10] * m2.m[9] +
                m1.m[11] * m2.m[13];

        result.m[10] =
                m1.m[8] * m2.m[2] +
                m1.m[9] * m2.m[6] +
                m1.m[10] * m2.m[10] +
                m1.m[11] * m2.m[14];

        result.m[11] =
                m1.m[8] * m2.m[3] +
                m1.m[9] * m2.m[7] +
                m1.m[10] * m2.m[11] +
                m1.m[11] * m2.m[15];

        //////////////////////////////////////////////
        // ROW FOUR
        //////////////////////////////////////////////
        result.m[12] =
                m1.m[12] * m2.m[0] +
                m1.m[13] * m2.m[4] +
                m1.m[14] * m2.m[8] +
                m1.m[15] * m2.m[12];

        result.m[13] =
                m1.m[12] * m2.m[1] +
                m1.m[13] * m2.m[5] +
                m1.m[14] * m2.m[9] +
                m1.m[15] * m2.m[13];

        result.m[14] =
                m1.m[12] * m2.m[2] +
                m1.m[13] * m2.m[6] +
                m1.m[14] * m2.m[10] +
                m1.m[15] * m2.m[14];

        result.m[15] =
                m1.m[12] * m2.m[3] +
                m1.m[13] * m2.m[7] +
                m1.m[14] * m2.m[11] +
                m1.m[15] * m2.m[15];

        return result;
    }

    public void setScale(Vec3 s) {
        setIdentity();
        m[0] = s.x;
        m[5] = s.y;
        m[10] = s.z;
    }

    public void translate(Vec3 t) {
        m[12] += t.x;
        m[13] += t.y;
        m[14] += t.z;
    }

    public Vec3 getPositionOffset() {
        return new Vec3(m[12], m[13], m[14]);
    }
}
