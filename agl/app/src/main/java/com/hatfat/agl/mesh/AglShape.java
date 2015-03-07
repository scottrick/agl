package com.hatfat.agl.mesh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AglShape {

    //the point indices that make up the outside of the shape
    private final List<Integer> outerPoints;

    //the p at the center of the shape
    private final int centerIndex;

    //list of triangles that make up this shape
    private final List<AglTriangle> triangles;

    //set of indices for the shapes that are neighbors
    private Set<Integer> neighbors;

    public AglShape(
            ArrayList<Integer> outerPoints,
            ArrayList<AglTriangle> triangles,
            int centerIndex) {
        this.outerPoints = Collections.unmodifiableList(outerPoints);
        this.triangles = Collections.unmodifiableList(triangles);
        this.neighbors = new HashSet<>();
        this.centerIndex = centerIndex;
    }

    public AglShape(
            ArrayList<Integer> outerPoints,
            ArrayList<AglTriangle> triangles,
            int centerIndex,
            Set<Integer> neighbors) {
        this.outerPoints = Collections.unmodifiableList(outerPoints);
        this.triangles = Collections.unmodifiableList(triangles);
        this.neighbors = neighbors;
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
            //write out each outer point index
            out.writeInt(outerPoint);
        }

        //write out the center point index
        out.writeInt(centerIndex);

        //write out the neighbor indices
        for (Integer neighbor : neighbors) {
            out.writeInt(neighbor);
        }

        if (outerPoints.size() != neighbors.size()) {
            throw new RuntimeException("Invalid outerpoints/neighbors!!!");
        }
    }

    public static AglShape readShapeFromStream(DataInputStream in) throws IOException {
        int numOuterPoints = in.readInt();

        ArrayList<Integer> outerPoints = new ArrayList<>();
        ArrayList<AglTriangle> triangles = new ArrayList<>();
        Set<Integer> neighbors = new HashSet<>();

        for (int i = 0; i < numOuterPoints; i++) {
            outerPoints.add(in.readInt());
        }

        int centerIndex = in.readInt();

        for (int i = 0; i < numOuterPoints; i++) {
            neighbors.add(in.readInt());
        }

        for (int i = 0; i < outerPoints.size(); i++) {
            int pointB = outerPoints.get(i);
            int pointC = outerPoints.get((i + 1) % outerPoints.size());

            triangles.add(new AglTriangle(centerIndex, pointB, pointC));
        }

        return new AglShape(outerPoints, triangles, centerIndex, neighbors);
    }

    public void addNeighbor(Integer neighbor) {
        neighbors.add(neighbor);
    }

    public Set<Integer> getNeighborIndices() {
        return neighbors;
    }

    @Override
    public int hashCode() {
        return centerIndex;
    }
}
