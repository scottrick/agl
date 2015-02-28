package com.hatfat.agl.mesh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AglShape {

    private final List<Integer> outerPoints;
            //the point indices that make up the outside of the shape
    private final int centerIndex; //the p at the center of the shape

    private final List<AglTriangle> triangles;

    public AglShape(ArrayList<Integer> outerPoints, ArrayList<AglTriangle> triangles,
            int centerIndex) {
        this.outerPoints = Collections.unmodifiableList(outerPoints);
        this.triangles = Collections.unmodifiableList(triangles);
        this.centerIndex = centerIndex;
    }

    public int getOuterPoint(int index) {
        return outerPoints.get(index);
    }

    public List<Integer> getOuterPoints() {
        return outerPoints;
    }

    public int getCenterIndex() {
        return centerIndex;
    }

    public int getNumberOfSides() {
        return outerPoints.size();
    }

    public List<AglTriangle> getTriangles() {
        return triangles;
    }

    public void writeToDataStream(DataOutputStream out) throws IOException {
        out.writeInt(outerPoints.size()); //number of outer points

        for (Integer outerPoint : outerPoints) {
            out.writeInt(outerPoint); //write out each outer point index
        }

        out.writeInt(centerIndex); //write out the center point index
    }

    public static AglShape readShapeFromStream(DataInputStream in) throws IOException {
        int numOuterPoints = in.readInt();

        ArrayList<Integer> outerPoints = new ArrayList<>();
        ArrayList<AglTriangle> triangles = new ArrayList<>();

        for (int i = 0; i < numOuterPoints; i++) {
            outerPoints.add(in.readInt());
        }

        int centerIndex = in.readInt();

        for (int i = 0; i < outerPoints.size(); i++) {
            int pointB = outerPoints.get(i);
            int pointC = outerPoints.get((i + 1) % outerPoints.size());

            triangles.add(new AglTriangle(centerIndex, pointB, pointC));
        }

        return new AglShape(outerPoints, triangles, centerIndex);
    }
}
