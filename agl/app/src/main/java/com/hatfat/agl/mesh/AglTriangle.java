package com.hatfat.agl.mesh;

import com.hatfat.agl.util.Vec3;

public class AglTriangle {
    public final Vec3 pointA;
    public final Vec3 pointB;
    public final Vec3 pointC;

    public final Vec3 pointCenter;

    public AglTriangle neighborAB;
    public AglTriangle neighborBC;
    public AglTriangle neighborCA;

    public AglTriangle(final Vec3 pointA, final Vec3 pointB, final Vec3 pointC) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;

        this.pointCenter = new Vec3(
                (pointA.x + pointB.x + pointC.x) / 3.0f,
                (pointA.y + pointB.y + pointC.y) / 3.0f,
                (pointA.z + pointB.z + pointC.z) / 3.0f);

        this.pointCenter.normalize();
    }

    public boolean containsPoint(Vec3 point) {
        return point == pointA || point == pointB || point == pointC;
    }

    public void removeNeighbor(AglTriangle neighbor) {
        if (neighbor == neighborAB) {
            neighborAB = null;
        }

        if (neighbor == neighborBC) {
            neighborBC = null;
        }

        if (neighbor == neighborCA) {
            neighborCA = null;
        }
    }
}
