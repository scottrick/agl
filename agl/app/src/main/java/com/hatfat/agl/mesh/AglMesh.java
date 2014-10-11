package com.hatfat.agl.mesh;

import com.hatfat.agl.util.Vec3;

import java.util.LinkedList;
import java.util.List;

import test.TestRenderableFactory;

public class AglMesh {
    protected List<Vec3> points;
    protected List<AglTriangle> triangles;

    public static AglMesh makeIcosahedron() {
        AglMesh mesh = new AglMesh();

        for (int i = 0; i < 12; i++) {
            Vec3 newPoint = new Vec3(
                    TestRenderableFactory.icosahedronVertices[i * 3 + 0],
                    TestRenderableFactory.icosahedronVertices[i * 3 + 1],
                    TestRenderableFactory.icosahedronVertices[i * 3 + 2]);

            mesh.points.add(newPoint);
        }

        for (int i = 0; i < 20; i++) {
            AglTriangle newTriangle = new AglTriangle(
                    mesh.points.get(TestRenderableFactory.icosahedronElements[i * 3 + 0]),
                    mesh.points.get(TestRenderableFactory.icosahedronElements[i * 3 + 1]),
                    mesh.points.get(TestRenderableFactory.icosahedronElements[i * 3 + 2]));

            mesh.triangles.add(newTriangle);
        }

        return mesh;
    }

    public AglMesh() {
        points = new LinkedList<Vec3>();
        triangles = new LinkedList<AglTriangle>();
    }

    public float[] getVertexArray() {
        float[] vertices = new float[points.size() * 3];

        for (int i = 0; i < points.size(); i++) {
            Vec3 point = points.get(i);

            vertices[i * 3 + 0] = point.x;
            vertices[i * 3 + 1] = point.y;
            vertices[i * 3 + 2] = point.z;
        }

        return vertices;
    }

    public int[] getIndexArray() {
        int[] indices = new int[triangles.size() * 3];

        for (int i = 0; i < triangles.size(); i++) {
            AglTriangle triangle = triangles.get(i);

            indices[i * 3 + 0] = points.indexOf(triangle.pointA);
            indices[i * 3 + 1] = points.indexOf(triangle.pointB);
            indices[i * 3 + 2] = points.indexOf(triangle.pointC);
        }

        return indices;
    }

    public int getNumTriangles() {
        return triangles.size();
    }

    public int getNumVertices() {
        return points.size();
    }

    public AglMesh splitMesh() {
        AglMesh newMesh = new AglMesh();

        //first setup all the triangle neighbors
        for (AglTriangle triangle : triangles) {

            //find neighbor AB
            for (AglTriangle triAB : triangles) {
                if (triAB == triangle) {
                    continue;
                }

                if (triAB.containsPoint(triangle.pointA) && triAB.containsPoint(triangle.pointB)) {
                    triangle.neighborAB = triAB;
                    break;
                }
            }

            //find neighbor BC
            for (AglTriangle triBC : triangles) {
                if (triBC == triangle) {
                    continue;
                }

                if (triBC.containsPoint(triangle.pointB) && triBC.containsPoint(triangle.pointC)) {
                    triangle.neighborBC = triBC;
                    break;
                }
            }

            //find neighbor CA
            for (AglTriangle triCA : triangles) {
                if (triCA == triangle) {
                    continue;
                }

                if (triCA.containsPoint(triangle.pointC) && triCA.containsPoint(triangle.pointA)) {
                    triangle.neighborCA = triCA;
                    break;
                }
            }
        }

        //add all of the points to the new mesh
        for (AglTriangle triangle : triangles) {
            newMesh.addPoint(triangle.pointA);
            newMesh.addPoint(triangle.pointB);
            newMesh.addPoint(triangle.pointC);
            newMesh.addPoint(triangle.pointCenter);
        }

        //add the split triangles for each neighbor (that hasn't been split yet)
        for (AglTriangle triangle : triangles) {
            if (triangle.neighborAB != null) {
                //add the two triangles for this neighbor
                AglTriangle tri1 = new AglTriangle(triangle.pointCenter, triangle.pointA, triangle.neighborAB.pointCenter);
                AglTriangle tri2 = new AglTriangle(triangle.pointCenter, triangle.neighborAB.pointCenter, triangle.pointB);
                newMesh.triangles.add(tri1);
                newMesh.triangles.add(tri2);

                triangle.neighborAB.removeNeighbor(triangle);
                triangle.neighborAB = null;
            }

            if (triangle.neighborBC != null) {
                //add the two triangles for this neighbor
                AglTriangle tri1 = new AglTriangle(triangle.pointCenter, triangle.pointB, triangle.neighborBC.pointCenter);
                AglTriangle tri2 = new AglTriangle(triangle.pointCenter, triangle.neighborBC.pointCenter, triangle.pointC);
                newMesh.triangles.add(tri1);
                newMesh.triangles.add(tri2);

                triangle.neighborBC.removeNeighbor(triangle);
                triangle.neighborBC = null;
            }

            if (triangle.neighborCA != null) {
                //add the two triangles for this neighbor
                AglTriangle tri1 = new AglTriangle(triangle.pointCenter, triangle.pointC, triangle.neighborCA.pointCenter);
                AglTriangle tri2 = new AglTriangle(triangle.pointCenter, triangle.neighborCA.pointCenter, triangle.pointA);
                newMesh.triangles.add(tri1);
                newMesh.triangles.add(tri2);

                triangle.neighborCA.removeNeighbor(triangle);
                triangle.neighborCA = null;
            }
        }

        return newMesh;
    }

    //adds a point if its not already in the list
    private void addPoint(Vec3 point) {
        if (!points.contains(point)) {
            points.add(point);
        }
    }

    public int indexForPoint(Vec3 point) {
        return points.indexOf(point);
    }

    public AglTriangle getTriangle(int index) {
        return triangles.get(index);
    }
}
