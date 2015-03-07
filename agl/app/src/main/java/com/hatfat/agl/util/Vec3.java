package com.hatfat.agl.util;

public class Vec3 {
    public float x;
    public float y;
    public float z;

    //used in calculations
    private float scratch;

    public Vec3() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Vec3(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(final Vec3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public void set(Vec3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void scale(float scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
    }

    public float getMagnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public void normalize() {
        boolean normalizeResult = normalizeWork();

        while (normalizeResult) {
            normalizeResult = normalizeWork();
        }
    }

    private boolean normalizeWork() {
        //scratch is MAGNITUDE
        scratch = (x * x) + (y * y) + (z * z);
        float error = Math.abs(scratch - 1.0f);

        if (error < Util.NORMALIZATION_ALLOWABLE_ERROR) {
            return false;
        }

        scratch = Util.invSqrt(scratch);

        x *= scratch;
        y *= scratch;
        z *= scratch;

        return true;
    }

    public static Vec3 crossProduct(Vec3 a, Vec3 b) {
        Vec3 result = new Vec3();
        result.x = (a.y * b.z) - (b.y * a.z);
        result.y = - (a.x * b.z) + (b.x * a.z);
        result.z = (a.x * b.y) - (a.y * b.x);
        return result;
    }

    public static float dotProduct(Vec3 a, Vec3 b) {
        return
                a.x * b.x +
                a.y * b.y +
                a.z * b.z;
    }

    public static float calculateAngleRadians(Vec3 a, Vec3 b) {
        float dot = dotProduct(a, b);
        dot = dot / (a.getMagnitude() * b.getMagnitude());
        return (float) Math.acos(dot);
    }

    public void rotateBy(Quat q) {
        Vec3 p2 = new Vec3();
        p2.x = q.w * q.w * x + 2 * q.y * q.w * z - 2 * q.z * q.w * y + q.x * q.x * x + 2 * q.y * q.x * y + 2 * q.z * q.x * z - q.z * q.z * x - q.y * q.y * x;
        p2.y = 2 * q.x * q.y * x + q.y * q.y * y + 2 * q.z * q.y * z + 2 * q.w * q.z * x - q.z * q.z * y + q.w * q.w * y - 2 * q.x * q.w * z - q.x * q.x * y;
        p2.z = 2 * q.x * q.z * x + 2 * q.y * q.z * y + q.z * q.z * z - 2 * q.w * q.y * x - q.y * q.y * z + 2 * q.w * q.x * y - q.x * q.x * z + q.w * q.w * z;

        this.x = p2.x;
        this.y = p2.y;
        this.z = p2.z;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vec3)) {
            return false;
        }

        if (o == this) {
            return true;
        }

        Vec3 point = (Vec3) o;

        return (
                this.x == point.x &&
                this.y == point.y &&
                this.z == point.z);
    }

    @Override
    public int hashCode() {
        return (int)this.x * 3 + (int)this.y * 4 + (int)this.z * 5;
    }
}
