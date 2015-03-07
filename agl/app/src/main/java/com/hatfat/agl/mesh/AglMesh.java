package com.hatfat.agl.mesh;

import com.hatfat.agl.AglColoredGeometry;
import com.hatfat.agl.AglWireframe;
import com.hatfat.agl.mesh.gen.AglGenTriangle;
import com.hatfat.agl.util.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import test.TestRenderableFactory;

public class AglMesh {

    protected final ArrayList<AglPoint> points;
    private final List<AglTriangle> triangles;

    //maps points by index to a list of triangles they are in
    private HashMap<Integer, Set<AglTriangle>> pointMap = null;

    public static AglMesh makeIcosahedron() {
        ArrayList<AglPoint> points = new ArrayList<>();
        ArrayList<AglTriangle> triangles = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            Vec3 newPoint = new Vec3(
                    TestRenderableFactory.icosahedronVertices[i * 3 + 0],
                    TestRenderableFactory.icosahedronVertices[i * 3 + 1],
                    TestRenderableFactory.icosahedronVertices[i * 3 + 2]);

            AglPoint p = new AglPoint(newPoint);

            points.add(p);
        }

        for (int i = 0; i < 20; i++) {
            AglTriangle newTriangle = new AglGenTriangle(
                    TestRenderableFactory.icosahedronElements[i * 3 + 0],
                    TestRenderableFactory.icosahedronElements[i * 3 + 1],
                    TestRenderableFactory.icosahedronElements[i * 3 + 2]);

            triangles.add(newTriangle);
        }

        return new AglMesh(points, triangles);
    }

    public AglMesh(ArrayList<AglPoint> points, ArrayList<AglTriangle> triangles) {
        this.points = points;
        this.triangles = triangles;
    }

    public int getNumTriangles() {
        return triangles.size();
    }

    public int getNumVertices() {
        return points.size();
    }

    public HashMap<Integer, Set<AglTriangle>> getPointMap() {
        return pointMap;
    }

    public float[] getVertexArray() {
        float[] vertices = new float[points.size() * 3];

        for (int i = 0; i < points.size(); i++) {
            AglPoint point = points.get(i);

            vertices[i * 3 + 0] = point.p.x;
            vertices[i * 3 + 1] = point.p.y;
            vertices[i * 3 + 2] = point.p.z;
        }

        return vertices;
    }

    public List<AglTriangle> getTriangles() {
        return triangles;
    }

    public ArrayList<AglPoint> getPoints() {
        return points;
    }

    private void addPointToPointMap(Integer point, AglTriangle triangle, HashMap<Integer, Set<AglTriangle>> pointMap) {
        Set<AglTriangle> triangleSet = pointMap.get(point);

        if (triangleSet == null) {
            triangleSet = new HashSet<>();
            pointMap.put(point, triangleSet);
        }

        triangleSet.add(triangle);
    }

    private void generatePointMap() {
        if (pointMap == null) {
            pointMap = new HashMap<>();

            for (AglTriangle triangle : triangles) {
                addPointToPointMap(triangle.pointA, triangle, pointMap);
                addPointToPointMap(triangle.pointB, triangle, pointMap);
                addPointToPointMap(triangle.pointC, triangle, pointMap);
            }
        }
    }

    public void setupTriangleNeighbors() {
        generatePointMap();

        List<AglTriangle> trianglesToCheck = new LinkedList<>();
        AglGenTriangle triangle;
        AglGenTriangle tri;

        for (AglTriangle aglTriangle : triangles) {
            triangle = (AglGenTriangle) aglTriangle;

            if (triangle.neighborAB != null
                    && triangle.neighborBC != null
                    && triangle.neighborCA != null) {
                //this triangle was already taken care of when its neighbors were done
                continue;
            }

            trianglesToCheck.clear();

            trianglesToCheck.addAll(pointMap.get(triangle.pointA));
            trianglesToCheck.addAll(pointMap.get(triangle.pointB));
            trianglesToCheck.addAll(pointMap.get(triangle.pointC));

            for (AglTriangle aglTri : trianglesToCheck) {
                if (aglTri == triangle) {
                    continue;
                }

                tri = (AglGenTriangle) aglTri;

                if (triangle.neighborAB == null) {
                    //still looking for the AB neighbor
                    if (tri.containsPoint(triangle.pointA) && tri.containsPoint(triangle.pointB)) {
                        triangle.neighborAB = tri;
                        tri.setAppropriateNeighbor(triangle);
                        continue;
                    }
                }

                if (triangle.neighborBC == null) {
                    //still looking for the BC neighbor
                    if (tri.containsPoint(triangle.pointB) && tri.containsPoint(triangle.pointC)) {
                        triangle.neighborBC = tri;
                        tri.setAppropriateNeighbor(triangle);
                        continue;
                    }
                }

                if (triangle.neighborCA == null) {
                    //still looking for the CA neighbor
                    if (tri.containsPoint(triangle.pointC) && tri.containsPoint(triangle.pointA)) {
                        triangle.neighborCA = tri;
                        tri.setAppropriateNeighbor(triangle);
                        continue;
                    }
                }
            }
        }
    }

    public AglMesh splitMesh() {
        //first setup all the triangle neighbors
        setupTriangleNeighbors();

        //make our new points list from the previous points
        ArrayList<AglPoint> newPoints = new ArrayList<>(points);
        AglGenTriangle triangle;

        //and add in the center point for each triangle
        for (AglTriangle tri : triangles) {
            triangle = (AglGenTriangle) tri;

            AglPoint pointA = points.get(triangle.pointA);
            AglPoint pointB = points.get(triangle.pointB);
            AglPoint pointC = points.get(triangle.pointC);

            Vec3 center = new Vec3(
                    (pointA.p.x + pointB.p.x + pointC.p.x) / 3.0f,
                    (pointA.p.y + pointB.p.y + pointC.p.y) / 3.0f,
                    (pointA.p.z + pointB.p.z + pointC.p.z) / 3.0f);
            center.normalize();

            triangle.pointCenter = newPoints.size();
            newPoints.add(new AglPoint(center));
        }

        ArrayList<AglTriangle> newTriangles = new ArrayList<>();

        //add the split triangles for each neighbor (that hasn't been split yet)
        for (AglTriangle tri : triangles) {
            triangle = (AglGenTriangle) tri;

            if (triangle.neighborAB != null) {
                //add the two triangles for this neighbor
                AglTriangle tri1 = new AglGenTriangle(triangle.pointCenter, triangle.pointA, triangle.neighborAB.pointCenter);
                AglTriangle tri2 = new AglGenTriangle(triangle.pointCenter, triangle.neighborAB.pointCenter, triangle.pointB);
                newTriangles.add(tri1);
                newTriangles.add(tri2);

                triangle.neighborAB.removeNeighbor(triangle);
                triangle.neighborAB = null;
            }

            if (triangle.neighborBC != null) {
                //add the two triangles for this neighbor
                AglTriangle tri1 = new AglGenTriangle(triangle.pointCenter, triangle.pointB, triangle.neighborBC.pointCenter);
                AglTriangle tri2 = new AglGenTriangle(triangle.pointCenter, triangle.neighborBC.pointCenter, triangle.pointC);
                newTriangles.add(tri1);
                newTriangles.add(tri2);

                triangle.neighborBC.removeNeighbor(triangle);
                triangle.neighborBC = null;
            }

            if (triangle.neighborCA != null) {
                //add the two triangles for this neighbor
                AglTriangle tri1 = new AglGenTriangle(triangle.pointCenter, triangle.pointC, triangle.neighborCA.pointCenter);
                AglTriangle tri2 = new AglGenTriangle(triangle.pointCenter, triangle.neighborCA.pointCenter, triangle.pointA);
                newTriangles.add(tri1);
                newTriangles.add(tri2);

                triangle.neighborCA.removeNeighbor(triangle);
                triangle.neighborCA = null;
            }
        }

        return new AglMesh(newPoints, newTriangles);
    }

    public AglColoredGeometry toColoredGeometryRenderable() {
        float[] vertices = getVertexArray();
        int numVertices = getNumVertices();

        float[] newVertices = new float[numVertices * 10];

        for (int i = 0; i < numVertices; i++) {
            newVertices[i * 10 + 0] = vertices[i * 3 + 0]; //x
            newVertices[i * 10 + 1] = vertices[i * 3 + 1]; //y
            newVertices[i * 10 + 2] = vertices[i * 3 + 2]; //z
            newVertices[i * 10 + 3] = 0.9f; //r
            newVertices[i * 10 + 4] = 0.9f; //g
            newVertices[i * 10 + 5] = 0.9f; //b
            newVertices[i * 10 + 6] = 0.9f; //a
            newVertices[i * 10 + 7] = vertices[i * 3 + 0]; //normal x
            newVertices[i * 10 + 8] = vertices[i * 3 + 1]; //normal y
            newVertices[i * 10 + 9] = vertices[i * 3 + 2]; //normal z
        }

        int[] elements = new int[triangles.size() * 3];

        for (int i = 0; i < triangles.size(); i++) {
            AglTriangle triangle = triangles.get(i);
            elements[i * 3 + 0] = triangle.pointA;
            elements[i * 3 + 1] = triangle.pointB;
            elements[i * 3 + 2] = triangle.pointC;
        }

        AglColoredGeometry coloredGeometry = new AglColoredGeometry(newVertices, numVertices, elements, elements.length);

        return coloredGeometry;
    }

    public AglWireframe toWireframeRenderable() {
        float[] vertexArray = getVertexArray();
        int numVertices = getNumVertices();

        int[] indicesArray = new int[triangles.size() * 6];

        for (int i = 0; i < triangles.size(); i++) {
            AglTriangle triangle = triangles.get(i);

            indicesArray[(i * 6) + 0] = triangle.pointA;
            indicesArray[(i * 6) + 1] = triangle.pointB;
            indicesArray[(i * 6) + 2] = triangle.pointB;
            indicesArray[(i * 6) + 3] = triangle.pointC;
            indicesArray[(i * 6) + 4] = triangle.pointC;
            indicesArray[(i * 6) + 5] = triangle.pointA;
        }

        AglWireframe wireframe = new AglWireframe(vertexArray, numVertices, indicesArray, indicesArray.length);

        return wireframe;
    }
}
