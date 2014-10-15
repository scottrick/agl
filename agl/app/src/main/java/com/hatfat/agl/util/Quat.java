package com.hatfat.agl.util;

public class Quat {
    public float x;
    public float y;
    public float z;
    public float w;

    //used in calculations
    private float scratch;
    private Quat scratchQuat = null;

    public Quat() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 1.0f;
    }

    public Quat(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void setWithRotationInDegrees(final float angle, final Vec3 axis) {
        setWithRotationInRadians((angle / 180.0f * (float) Math.PI), axis);
    }

    public void setWithRotationInRadians(final float angle, final Vec3 axis) {
        axis.normalize();

        w = (float)Math.cos(angle / 2.0f);
        x = axis.x * (float)Math.sin(angle / 2.0f);
        y = axis.y * (float)Math.sin(angle / 2.0f);
        z = axis.z * (float)Math.sin(angle / 2.0f);

        normalize();
    }

    public void normalize() {
        boolean normalizeResult = normalizeWork();

        while (normalizeResult) {
            normalizeResult = normalizeWork();
        }
    }

    private boolean normalizeWork() {
        scratch = (w * w) + (x * x) + (y * y) + (z * z);

        if (Math.abs(scratch - 1.0f) < Util.NORMALIZATION_ALLOWABLE_ERROR) {
            return false;
        }

        scratch	= Util.invSqrt(scratch);

        w = w * scratch;
        x = x * scratch;
        y = y * scratch;
        z = z * scratch;

        return true;
    }

    public void toMatrix(Matrix matrix) {
        matrix.m[0] = 1.0f - 2.0f * (y * y + z * z);
        matrix.m[1] = 2.0f * (x * y + w * z);
        matrix.m[2] = 2.0f * (x * z - w * y);
        matrix.m[3] = 0;
        matrix.m[4] = 2.0f * (x * y - w * z);
        matrix.m[5] = 1.0f - 2.0f * (x * x + z * z);
        matrix.m[6] = 2.0f * (y * z + w * x);
        matrix.m[7] = 0;
        matrix.m[8] = 2.0f * (w * y + x * z);
        matrix.m[9] = 2.0f * (y * z - w * x);
        matrix.m[10] = 1.0f - 2.0f * (x * x + y * y);
        matrix.m[11] = 0.0f;
        matrix.m[12] = 0.0f;
        matrix.m[13] = 0.0f;
        matrix.m[14] = 0.0f;
        matrix.m[15] = 1.0f;
    }

    public void add(final Quat q) {
        this.x += q.x;
        this.y += q.y;
        this.z += q.z;
        this.w += q.w;
    }

    public void multiply(final Quat q) {
        if (scratchQuat == null) {
            scratchQuat = new Quat();
        }

        scratchQuat.w = (w * q.w) - (x * q.x) - (y * q.y) - (z * q.z);
        scratchQuat.x = (w * q.x) + (x * q.w) + (y * q.z) - (z * q.y);
        scratchQuat.y = (w * q.y) - (x * q.z) + (y * q.w) + (z * q.x);
        scratchQuat.z = (w * q.z) + (x * q.y) - (y * q.x) + (z * q.w);

        this.w = scratchQuat.w;
        this.x = scratchQuat.x;
        this.y = scratchQuat.y;
        this.z = scratchQuat.z;
    }
}
