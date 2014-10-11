package com.hatfat.agl.mesh;

import com.hatfat.agl.util.Vec3;

import java.util.LinkedList;
import java.util.List;

public class AglPoint {
    public Vec3 p;
    public List<AglTriangle> triangles;

    public AglPoint(Vec3 p) {
        this.p = p;
        this.triangles = new LinkedList();
    }

    public AglPoint(AglPoint fromPoint) {
        this.p = new Vec3(fromPoint.p.x, fromPoint.p.y, fromPoint.p.z);
        this.triangles = new LinkedList();
    }

    public void addTriangle(AglTriangle triangle) {
        this.triangles.add(triangle);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AglPoint)) {
            return false;
        }

        if (o == this) {
            return true;
        }

        AglPoint point = (AglPoint) o;

        return (
                this.p.x == point.p.x &&
                this.p.y == point.p.y &&
                this.p.z == point.p.z);
    }

    @Override
    public int hashCode() {
        return (int)this.p.x * 3 + (int)this.p.y * 4 + (int)this.p.z * 5;
    }
}
