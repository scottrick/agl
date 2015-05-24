package com.hatfat.agl.util;

public class PosQuat {
    public Vec3 pos;
    public Quat quat;

    public PosQuat(final Vec3 pos, final Quat quat) {
        this.pos = new Vec3(pos);
        this.quat = new Quat(quat);
    }

    public String toString() {
        return "pos (" + pos.x + ", " + pos.y + ", " + pos.z + "), quat (" + quat.x + ", " + quat.y + ", " + quat.z + ", " + quat.w + ")";
    }
}
