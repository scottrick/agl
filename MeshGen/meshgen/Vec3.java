package meshgen;

import meshgen.Util;

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

    public Vec3(final Vec3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
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
