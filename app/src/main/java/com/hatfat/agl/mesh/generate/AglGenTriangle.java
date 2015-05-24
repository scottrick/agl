package com.hatfat.agl.mesh.generate;

import com.hatfat.agl.mesh.AglShape;
import com.hatfat.agl.mesh.AglTriangle;

public class AglGenTriangle extends AglTriangle {

    public int pointCenter;
    public AglShape shape;

    public AglGenTriangle neighborAB;
    public AglGenTriangle neighborBC;
    public AglGenTriangle neighborCA;

    public AglGenTriangle(final int pointA, final int pointB, final int pointC) {
        super(pointA, pointB, pointC);
    }

    public void removeNeighbor(AglGenTriangle neighbor) {
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

    public void setAppropriateNeighbor(AglGenTriangle neighbor) {
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
        }
    }
}
