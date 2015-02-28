package com.hatfat.agl.mesh;

public class AglTriangle {

    public final int pointA; //the indices for the points in the triangle
    public final int pointB;
    public final int pointC;

    public int pointCenter; //index of the center point

    public AglTriangle neighborAB;
    public AglTriangle neighborBC;
    public AglTriangle neighborCA;

    public AglTriangle(final int pointA, final int pointB, final int pointC) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;

//        Vec3 center = new Vec3(
//                (pointA.p.x + pointB.p.x + pointC.p.x) / 3.0f,
//                (pointA.p.y + pointB.p.y + pointC.p.y) / 3.0f,
//                (pointA.p.z + pointB.p.z + pointC.p.z) / 3.0f);
//        center.normalize();

//        this.pointCenter = new AglPoint(center);
    }

    public boolean containsPoint(int point) {
//        return point.equals(pointA) || point.equals(pointB) || point.equals(pointC);

        return point == pointA || point == pointB || point == pointC;

//        if (
//                pointA.p.x == point.p.x &&
//                pointA.p.y == point.p.y &&
//                pointA.p.z == point.p.z) {
//            return true;
//        }
//
//        if (
//                pointB.p.x == point.p.x &&
//                pointB.p.y == point.p.y &&
//                pointB.p.z == point.p.z) {
//            return true;
//        }
//
//        if (
//                pointC.p.x == point.p.x &&
//                pointC.p.y == point.p.y &&
//                pointC.p.z == point.p.z) {
//            return true;
//        }
//
//        return false;
    }

    public void removeNeighbor(AglTriangle neighbor) {
        if (neighbor == neighborAB) {
            neighborAB = null;
            return;
        }

        if (neighbor == neighborBC) {
            neighborBC = null;
            return;
        }

        if (neighbor == neighborCA) {
            neighborCA = null;
            return;
        }
    }

    public void setAppropriateNeighbor(AglTriangle neighbor) {
        boolean test = false;

        if (neighbor.containsPoint(pointA) && neighbor.containsPoint(pointB)) {
            neighborAB = neighbor;

            if (test) {
                throw new RuntimeException("SHOUDNT HAPPEN1");
            }

            test = true;
        }

        if (neighbor.containsPoint(pointB) && neighbor.containsPoint(pointC)) {
            neighborBC = neighbor;

            if (test) {
                throw new RuntimeException("SHOUDNT HAPPEN2");
            }

            test = true;
        }

        if (neighbor.containsPoint(pointC) && neighbor.containsPoint(pointA)) {
            neighborCA = neighbor;

            if (test) {
                throw new RuntimeException("SHOUDNT HAPPEN3");
            }

            test = true;
        }
    }
}
