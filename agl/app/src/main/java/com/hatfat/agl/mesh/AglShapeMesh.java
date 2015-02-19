package com.hatfat.agl.mesh;

import com.hatfat.agl.AglWireframe;
import com.hatfat.agl.util.Vec3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AglShapeMesh {

    private List<AglShape> hexagons;
    private List<AglShape> pentagons;

    private Set<AglPoint> points;

    private AglPoint[] cachedPointsArray;

    public AglShapeMesh() {
        this.hexagons = new LinkedList<>();
        this.pentagons = new LinkedList<>();

        this.points = new TreeSet<>();
    }

    public static AglShapeMesh makeFromMesh(AglMesh mesh) {
        AglShapeMesh shapeMesh = new AglShapeMesh();

        mesh.setupTriangleNeighbors();

        //make the starting 12 pentagons
        for (AglPoint point : mesh.startingPoints) {
            AglShape pentagon = new AglShape(point);

            for (AglTriangle triangle : point.triangles) {
                pentagon.addTriangle(triangle);
                shapeMesh.addTrianglePoints(triangle);
            }

            shapeMesh.pentagons.add(pentagon);
        }

        HashSet<AglTriangle> usedTriangles = new HashSet<>();

        for (AglShape pentagon : shapeMesh.pentagons) {
            usedTriangles.addAll(pentagon.getTriangles());
        }

        List<AglShape> shapesToCheck = new LinkedList<>();
        shapesToCheck.addAll(shapeMesh.pentagons);

        while (shapesToCheck.size() > 0) {
            AglShape shape = shapesToCheck.get(0);

            for (AglTriangle triangle : shape.getTriangles()) {
                //find the edge for this triangle in the shape

                AglTriangle edgeNeighbor = null;

                if (triangle.pointA.equals(shape.getCenter())) {
                    edgeNeighbor = triangle.neighborBC;
                }
                else if (triangle.pointB.equals(shape.getCenter())) {
                    edgeNeighbor = triangle.neighborCA;
                }
                else if (triangle.pointC.equals(shape.getCenter())) {
                    edgeNeighbor = triangle.neighborAB;
                }

                if (edgeNeighbor == null) {
                    throw new RuntimeException("Should always have a valid edge neighbor.");
                }

                if (!usedTriangles.contains(edgeNeighbor)) {
                    //haven't added this triangle shape yet!
                    AglPoint edgeCenter = null;

                    if (!triangle.containsPoint(edgeNeighbor.pointA)) {
                        edgeCenter = edgeNeighbor.pointA;
                    }
                    else if (!triangle.containsPoint(edgeNeighbor.pointB)) {
                        edgeCenter = edgeNeighbor.pointB;
                    }
                    else if (!triangle.containsPoint(edgeNeighbor.pointC)) {
                        edgeCenter = edgeNeighbor.pointC;
                    }

                    if (edgeCenter == null) {
                        throw new RuntimeException("Should always have an edge center.");
                    }

                    AglShape hexagon = new AglShape(edgeCenter);

                    for (AglTriangle hexTriangle : edgeCenter.triangles) {
                        hexagon.addTriangle(hexTriangle);
                        shapeMesh.addTrianglePoints(hexTriangle);
                    }

                    usedTriangles.addAll(hexagon.getTriangles());
                    shapeMesh.hexagons.add(hexagon);
                    shapesToCheck.add(hexagon);
                }
            }

            shapesToCheck.remove(0);
        }

        return shapeMesh;
    }

    private void addPoint(AglPoint point) {
        if (!points.contains(point)) {
            points.add(point);

            //cachedPointsArray needs to be updated
            cachedPointsArray = null;
        }
    }

    private void addTrianglePoints(AglTriangle triangle) {
        addPoint(triangle.pointA);
        addPoint(triangle.pointB);
        addPoint(triangle.pointC);
    }

    private AglPoint[] getAglPointArray() {
        if (cachedPointsArray == null) {
            cachedPointsArray = new AglPoint[points.size()];
            cachedPointsArray = points.toArray(cachedPointsArray);
        }

        return cachedPointsArray;
    }

    public float[] getVertexArray() {
        float[] vertexArray = new float[points.size() * 3];
        AglPoint[] pointArray = getAglPointArray();

        for (int i = 0; i < pointArray.length; i++) {
            AglPoint point = pointArray[i];

            vertexArray[i * 3 + 0] = point.p.x;
            vertexArray[i * 3 + 1] = point.p.y;
            vertexArray[i * 3 + 2] = point.p.z;
        }

        return vertexArray;
    }

    public List<AglShape> getPentagons() {
        return pentagons;
    }

    public int getNumPentagons() {
        return pentagons.size();
    }

    public List<AglShape> getHexagons() {
        return hexagons;
    }

    public int getNumHexagons() {
        return hexagons.size();
    }

    public int getNumPoints() {
        return points.size();
    }

    public AglWireframe createWireframe() {
        List<AglPoint> pointList = Arrays.asList(getAglPointArray());

        float[] vertexArray = getVertexArray();
        int numVertices = getNumVertices();

        int[] indicesArray = new int[pentagons.size() * 5 * 2 + hexagons.size() * 6 * 2];

        for (int i = 0; i < pentagons.size(); i++) {
            AglShape pentagon = pentagons.get(i);

            int currentIndex = 0;

            for (int j = 0; j < pentagon.getTriangles().size(); j++) {
                AglTriangle triangle = pentagon.getTriangles().get(j);

                if (!triangle.pointA.equals(pentagon.getCenter())) {
                    int index = pointList.indexOf(triangle.pointA);
                    indicesArray[i * 10 + currentIndex] = index;
                    currentIndex++;
                }

                if (!triangle.pointB.equals(pentagon.getCenter())) {
                    int index = pointList.indexOf(triangle.pointB);
                    indicesArray[i * 10 + currentIndex] = index;
                    currentIndex++;
                }

                if (!triangle.pointC.equals(pentagon.getCenter())) {
                    int index = pointList.indexOf(triangle.pointC);
                    indicesArray[i * 10 + currentIndex] = index;
                    currentIndex++;
                }
            }
        }

        for (int i = 0; i < hexagons.size(); i++) {
            AglShape hexagon = hexagons.get(i);

            int currentIndex = 0;

            for (int j = 0; j < hexagon.getTriangles().size(); j++) {
                AglTriangle triangle = hexagon.getTriangles().get(j);

                if (!triangle.pointA.equals(hexagon.getCenter())) {
                    int index = pointList.indexOf(triangle.pointA);
                    indicesArray[i * 12 + currentIndex] = index;
                    currentIndex++;
                }

                if (!triangle.pointB.equals(hexagon.getCenter())) {
                    int index = pointList.indexOf(triangle.pointB);
                    indicesArray[i * 12 + currentIndex] = index;
                    currentIndex++;
                }

                if (!triangle.pointC.equals(hexagon.getCenter())) {
                    int index = pointList.indexOf(triangle.pointC);
                    indicesArray[i * 12 + currentIndex] = index;
                    currentIndex++;
                }
            }
        }

        AglWireframe wireframe = new AglWireframe(vertexArray, numVertices, indicesArray, indicesArray.length);

        return wireframe;
    }

    public int getNumVertices() {
        return points.size();
    }

    public void writeToDiskAsStrings(String filename) throws IOException {
        FileWriter out = null;

        try {
            out = new FileWriter(filename);

            AglPoint[] pointArray = getAglPointArray();
            List<AglPoint> pointList = Arrays.asList(pointArray);

            for (int i = 0; i < pointArray.length; i++) {
                AglPoint point = pointArray[i];
                out.write("p " + point.p.x + " " + point.p.y + " " + point.p.z + "\n");
            }

            List<AglShape> shapes = new LinkedList<>();
            shapes.addAll(pentagons);
            shapes.addAll(hexagons);

            for (AglShape shape : shapes) {
                out.write("s " + shape.getTriangles().size() + "\n");

                for (AglTriangle triangle : shape.getTriangles()) {
                    out.write("t "
                            + pointList.indexOf(triangle.pointA) + " "
                            + pointList.indexOf(triangle.pointB) + " "
                            + pointList.indexOf(triangle.pointC) + "\n");
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private final byte POINT = 1;
    private final byte SHAPE = 2;
    private final byte TRIANGLE = 3;

    public void writeToDiskAsBytes(String filename) throws IOException {
        FileOutputStream fileOutputStream = null;
        DataOutputStream out = null;

        try {
            fileOutputStream = new FileOutputStream(filename);
            out = new DataOutputStream(fileOutputStream);

            AglPoint[] pointArray = getAglPointArray();
            List<AglPoint> pointList = Arrays.asList(pointArray);

            for (int i = 0; i < pointArray.length; i++) {
                AglPoint point = pointArray[i];

                out.writeByte(POINT);
                out.writeFloat(point.p.x);
                out.writeFloat(point.p.y);
                out.writeFloat(point.p.z);
            }

            List<AglShape> shapes = new LinkedList<>();
            shapes.addAll(pentagons);
            shapes.addAll(hexagons);

            for (AglShape shape : shapes) {
                out.writeByte(SHAPE);
                out.writeInt(shape.getTriangles().size());

                for (AglTriangle triangle : shape.getTriangles()) {
                    out.writeByte(TRIANGLE);
                    out.writeInt(pointList.indexOf(triangle.pointA));
                    out.writeInt(pointList.indexOf(triangle.pointB));
                    out.writeInt(pointList.indexOf(triangle.pointC));
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void readFromDiskAsBytes(String filename) throws IOException {
        FileInputStream fileInputStream = null;
        DataInputStream in = null;

        try {
            fileInputStream = new FileInputStream(filename);
            in = new DataInputStream(fileInputStream);

            while (true) {
                byte dataType = in.readByte();

                switch (dataType) {
                    case POINT:
                    {
                        float x = in.readFloat();
                        float y = in.readFloat();
                        float z = in.readFloat();
                        AglPoint point = new AglPoint(new Vec3(x, y, z));
                        points.add(point);
                    }
                        break;
                    case SHAPE:
                    {
                        int numTriangles = in.readInt();

                        //read in each triangle
                        for (int i = 0; i < numTriangles; i++) {
                            dataType = in.readByte();

                            if (dataType != TRIANGLE) {
                                throw new RuntimeException("MUST BE A TRIANGLE");
                            }

                            int triA = in.readInt();
                            int triB = in.readInt();
                            int triC = in.readInt();

//                            AglTriangle triangle = new AglTriangle()
                        }
                    }
                        break;
                    default:
                        throw new RuntimeException("Shouldn't happen!");
                }
            }
        }
        catch (EOFException e) {
            //end of file, we are done!
        }
        catch (IOException e) {
            System.out.println(e);
        }
        finally {
            if (in != null) {
                in.close();
            }
        }
    }

//    public float[] getOutlineVertexArray() {
//        float[] vertexArray = new float[pentagons.size() * 5 * 2 * 3];
//
//        for (AglShape pentagon : pentagons) {
//            for (AglTriangle triangle : pentagon.getTriangles()) {
//
//            }
//        }
//
//        for (int i = 0; i < points.size(); i++) {
//            AglPoint point = points.get(i);
//
//            vertexArray[i * 3 + 0] = point.p.x;
//            vertexArray[i * 3 + 1] = point.p.y;
//            vertexArray[i * 3 + 2] = point.p.z;
//        }
//
//        return vertexArray;
//    }

//    public int[] getIndexArray() {
//        int[] indexArray = new int[pentagons.size() * 3 * 5];
//
//        for (int i = 0; i < pentagons.size(); i++) {
//            AglShape shape = pentagons.get(i);
//
//            for (int j = 0; j < shape.getTriangles().size(); j++) {
//                AglTriangle triangle = shape.getTriangles().get(j);
//
//                indexArray[i * 3 * 5 + j * 3 + 0] = points.indexOf(triangle.pointA);
//                indexArray[i * 3 * 5 + j * 3 + 1] = points.indexOf(triangle.pointB);
//                indexArray[i * 3 * 5 + j * 3 + 2] = points.indexOf(triangle.pointC);
//            }
//        }
//
//        return indexArray;
//    }
}
