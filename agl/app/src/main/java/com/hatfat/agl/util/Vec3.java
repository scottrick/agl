package com.hatfat.agl.util;

public class Vec3 {
    public float x;
    public float y;
    public float z;

    //used in calculations
    private float scratch;

    public Vec3(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void normalize() {
        //scratch is MAGNITUDE
        scratch = (x * x) + (y * y) + (z * z);

        if (Math.abs(scratch - 1.0f) < Util.NORMALIZATION_ALLOWABLE_ERROR) {
            return;
        }

        scratch = Util.invSqrt(scratch);

        x *= scratch;
        y *= scratch;
        z *= scratch;
    }
}
