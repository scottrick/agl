package com.hatfat.agl.mesh;

import java.util.LinkedList;
import java.util.List;

public class AglShape {
    private List<AglTriangle> triangles; //the triangles that make up this shape
    private AglPoint center; //the p at the center of the shape, and in all the triangles

    public AglShape(AglPoint center) {
        this.center = center;
        this.triangles = new LinkedList<>();
    }

    public void addTriangle(AglTriangle triangle) {
        this.triangles.add(triangle);
    }

    public List<AglTriangle> getTriangles() {
        return triangles;
    }

    public AglPoint getCenter() {
        return center;
    }
}
