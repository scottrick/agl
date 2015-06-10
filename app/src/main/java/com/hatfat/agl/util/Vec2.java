package com.hatfat.agl.util;

public class Vec2 {
    public float x;
    public float y;

    //used in calculations
    private float scratch;

    public Vec2() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vec2(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(final Vec2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    public void set(Vec3 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void scale(float scale) {
        this.x *= scale;
        this.y *= scale;
    }

    public float getMagnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        boolean normalizeResult = normalizeWork();

        while (normalizeResult) {
            normalizeResult = normalizeWork();
        }
    }

    private boolean normalizeWork() {
        //scratch is MAGNITUDE
        scratch = (x * x) + (y * y);
        float error = Math.abs(scratch - 1.0f);

        if (error < Util.NORMALIZATION_ALLOWABLE_ERROR) {
            return false;
        }

        scratch = Util.invSqrt(scratch);

        x *= scratch;
        y *= scratch;

        return true;
    }

    public static float calculateDistanceSquared(Vec2 a, Vec2 b) {
        float x = a.x - b.x;
        float y = a.y - b.y;

        x = x * x;
        y = y * y;

        return x + y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vec2)) {
            return false;
        }

        if (o == this) {
            return true;
        }

        Vec2 point = (Vec2) o;

        return (this.x == point.x && this.y == point.y);
    }

    @Override
    public int hashCode() {
        return (int)this.x * 3 + (int)this.y * 4;
    }
}
