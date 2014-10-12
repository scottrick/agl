package com.hatfat.agl.mesh;

import com.hatfat.agl.util.Vec3;

public class AglTriangle {
    public final AglPoint pointA;
    public final AglPoint pointB;
    public final AglPoint pointC;

    public final AglPoint pointCenter;

    public AglTriangle neighborAB;
    public AglTriangle neighborBC;
    public AglTriangle neighborCA;

    public AglTriangle(final AglPoint pointA, final AglPoint pointB, final AglPoint pointC) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;

        Vec3 center = new Vec3(
                (pointA.p.x + pointB.p.x + pointC.p.x) / 3.0f,
                (pointA.p.y + pointB.p.y + pointC.p.y) / 3.0f,
                (pointA.p.z + pointB.p.z + pointC.p.z) / 3.0f);
        center.normalize();

        this.pointCenter = new AglPoint(center);
    }

    public boolean containsPoint(AglPoint point) {
        return point.equals(pointA) || point.equals(pointB) || point.equals(pointC);
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
