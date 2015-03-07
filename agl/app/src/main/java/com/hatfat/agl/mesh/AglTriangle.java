package com.hatfat.agl.mesh;

public class AglTriangle {

    public final int pointA; //the indices for the points in the triangle
    public final int pointB;
    public final int pointC;

    public AglTriangle(final int pointA, final int pointB, final int pointC) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;
    }

    public boolean containsPoint(int point) {
        return point == pointA || point == pointB || point == pointC;
    }
}
