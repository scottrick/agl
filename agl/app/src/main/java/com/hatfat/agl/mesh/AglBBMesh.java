package com.hatfat.agl.mesh;

import com.hatfat.agl.AglColoredGeometry;
import com.hatfat.agl.AglWireframe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

//Agl Bucky Ball Mesh
public class AglBBMesh {

    private final List<AglPoint> points;
    private final HashMap<AglPoint, Integer> indexMap; //map to the points index

    private List<AglShape> pentagons;
    private List<AglShape> hexagons;

    public AglBBMesh(ArrayList<AglPoint> points, ArrayList<AglShape> pentagons,
            ArrayList<AglShape> hexagons) {
        this.points = Collections.unmodifiableList(points);
        this.pentagons = Collections.unmodifiableList(pentagons);
        this.hexagons = Collections.unmodifiableList(hexagons);
        this.indexMap = new HashMap<>();

        init();
    }

    private void init() {
        //build the indexMap
        for (int i = 0; i < points.size(); i++) {
            indexMap.put(points.get(i), i);
        }
    }

    private static ArrayList<Integer> createOuterPointsFromTriangles(Set<AglTriangle> triangleSet, int centerIndex) {
        List<Segment> segments = new LinkedList<>();

        for (AglTriangle triangle : triangleSet) {
            Segment segment = new Segment();

            if (triangle.pointA == centerIndex) {
                segment.pointA = triangle.pointB;
                segment.pointB = triangle.pointC;
            }
            else if (triangle.pointB == centerIndex) {
                segment.pointA = triangle.pointC;
                segment.pointB = triangle.pointA;
            }
            else if (triangle.pointC == centerIndex) {
                segment.pointA = triangle.pointA;
                segment.pointB = triangle.pointB;
            }

            segments.add(segment);
        }

        return createOuterPointsFromSegments(segments);
    }

    private static ArrayList<Integer> createOuterPointsFromSegments(List<Segment> segments) {
        ArrayList<Integer> outerPoints = new ArrayList<>();
        List<Segment> sortedSegments = new LinkedList<>();
        sortedSegments.add(segments.remove(0));

        while (segments.size() > 0) {
            Segment currentLastSegment = sortedSegments.get(sortedSegments.size() - 1);
            Segment nextSegment = null;

            for (Segment segment : segments) {
                if (currentLastSegment.pointB == segment.pointA) {
                    nextSegment = segment;
                    break;
                }
            }

            if (nextSegment == null) {
                throw new RuntimeException("Should always have a next segment!");
            }

            segments.remove(nextSegment);
            sortedSegments.add(nextSegment);
        }

        for (Segment segment : sortedSegments) {
            outerPoints.add(segment.pointA);
        }

        return outerPoints;
    }

    public static AglBBMesh makeFromMesh(AglMesh mesh) {
        ArrayList<AglShape> pentagons = new ArrayList<>();
        ArrayList<AglShape> hexagons = new ArrayList<>();

        mesh.setupTriangleNeighbors();

        //make the starting 12 pentagons
        for (int i = 0; i < 12; i++) {
            Set<AglTriangle> triangleSet = mesh.getPointMap().get(i);
            ArrayList<Integer> outerPoints = createOuterPointsFromTriangles(triangleSet, i);

            AglShape pentagon = new AglShape(outerPoints, new ArrayList(triangleSet), i);
            pentagons.add(pentagon);
        }

        HashSet<AglTriangle> usedTriangles = new HashSet<>();
        for (AglShape pentagon : pentagons) {
            usedTriangles.addAll(pentagon.getTriangles());
        }

        List<AglShape> shapesToCheck = new LinkedList<>();
        shapesToCheck.addAll(pentagons);

        while (shapesToCheck.size() > 0) {
            AglShape shape = shapesToCheck.get(0);

            for (AglTriangle tri : shape.getTriangles()) {
                //find the edge for this triangle in the shape
                AglTriangle edgeNeighbor = null;

                if (tri.pointA == shape.getCenterIndex()) {
                    edgeNeighbor = tri.neighborBC;
                }
                else if (tri.pointB == shape.getCenterIndex()) {
                    edgeNeighbor = tri.neighborCA;
                }
                else if (tri.pointC == shape.getCenterIndex()) {
                    edgeNeighbor = tri.neighborAB;
                }

                if (edgeNeighbor == null) {
                    throw new RuntimeException("Should always have a valid edge neighbor.");
                }

                if (!usedTriangles.contains(edgeNeighbor)) {
                    //haven't added this triangle shape yet!
                    int edgeCenter = -1;

                    if (!tri.containsPoint(edgeNeighbor.pointA)) {
                        edgeCenter = edgeNeighbor.pointA;
                    }
                    else if (!tri.containsPoint(edgeNeighbor.pointB)) {
                        edgeCenter = edgeNeighbor.pointB;
                    }
                    else if (!tri.containsPoint(edgeNeighbor.pointC)) {
                        edgeCenter = edgeNeighbor.pointC;
                    }

                    if (edgeCenter < 0) {
                        throw new RuntimeException("Should always have an edge center.");
                    }

                    Set<AglTriangle> triangleSet = mesh.getPointMap().get(edgeCenter);
                    ArrayList<Integer> outerPoints = createOuterPointsFromTriangles(triangleSet, edgeCenter);

                    AglShape hexagon = new AglShape(outerPoints, new ArrayList(triangleSet), edgeCenter);
                    hexagons.add(hexagon);

                    usedTriangles.addAll(hexagon.getTriangles());
                    shapesToCheck.add(hexagon);
                }
            }

            shapesToCheck.remove(0);
        }

        AglBBMesh shapeMesh = new AglBBMesh(mesh.getPoints(), pentagons, hexagons);
        return shapeMesh;
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

    public int getNumTriangles() {
        return pentagons.size() * 5 + hexagons.size() * 6;
    }

    public int getNumPoints() {
        return points.size();
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

        int[] elements = new int[pentagons.size() * 15 + hexagons.size() * 18];
        int hexagonOffset = pentagons.size() * 15;

        for (int i = 0; i < pentagons.size(); i++) {
            AglShape shape = pentagons.get(i);

            for (int j = 0; j < shape.getOuterPoints().size(); j++) {
                int index1 = shape.getOuterPoint(j);
                int index2 = shape.getOuterPoint((j + 1) % shape.getOuterPoints().size());

                elements[i * 15 + j * 3 + 0] = index2;
                elements[i * 15 + j * 3 + 1] = shape.getCenterIndex();
                elements[i * 15 + j * 3 + 2] = index1;
            }
        }

        for (int i = 0; i < hexagons.size(); i++) {
            AglShape shape = hexagons.get(i);

            for (int j = 0; j < shape.getOuterPoints().size(); j++) {
                int index1 = shape.getOuterPoint(j);
                int index2 = shape.getOuterPoint((j + 1) % shape.getOuterPoints().size());

                elements[i * 18 + j * 3 + 0 + hexagonOffset] = index2;
                elements[i * 18 + j * 3 + 1 + hexagonOffset] = shape.getCenterIndex();
                elements[i * 18 + j * 3 + 2 + hexagonOffset] = index1;
            }
        }

        AglColoredGeometry coloredGeometry = new AglColoredGeometry(newVertices, numVertices, elements, elements.length);

        return coloredGeometry;
    }

    public AglWireframe toWireframeRenderable() {
        float[] vertexArray = getVertexArray();
        int numVertices = getNumVertices();

        int numPerPentagon = 10;
        int numPerHexagon = 12;
        int[] indicesArray = new int[pentagons.size() * numPerPentagon + hexagons.size() * numPerHexagon];

        for (int i = 0; i < pentagons.size(); i++) {
            AglShape pentagon = pentagons.get(i);

            indicesArray[(i * numPerPentagon) + 0] = pentagon.getOuterPoint(0);
            indicesArray[(i * numPerPentagon) + 1] = pentagon.getOuterPoint(1);
            indicesArray[(i * numPerPentagon) + 2] = pentagon.getOuterPoint(1);
            indicesArray[(i * numPerPentagon) + 3] = pentagon.getOuterPoint(2);
            indicesArray[(i * numPerPentagon) + 4] = pentagon.getOuterPoint(2);
            indicesArray[(i * numPerPentagon) + 5] = pentagon.getOuterPoint(3);
            indicesArray[(i * numPerPentagon) + 6] = pentagon.getOuterPoint(3);
            indicesArray[(i * numPerPentagon) + 7] = pentagon.getOuterPoint(4);
            indicesArray[(i * numPerPentagon) + 8] = pentagon.getOuterPoint(4);
            indicesArray[(i * numPerPentagon) + 9] = pentagon.getOuterPoint(0);
        }

        int pentagonOffset = pentagons.size() * numPerPentagon;

        for (int i = 0; i < hexagons.size(); i++) {
            AglShape hexagon = hexagons.get(i);

            indicesArray[(i * numPerHexagon) + 0 + pentagonOffset] = hexagon.getOuterPoint(0);
            indicesArray[(i * numPerHexagon) + 1 + pentagonOffset] = hexagon.getOuterPoint(1);
            indicesArray[(i * numPerHexagon) + 2 + pentagonOffset] = hexagon.getOuterPoint(1);
            indicesArray[(i * numPerHexagon) + 3 + pentagonOffset] = hexagon.getOuterPoint(2);
            indicesArray[(i * numPerHexagon) + 4 + pentagonOffset] = hexagon.getOuterPoint(2);
            indicesArray[(i * numPerHexagon) + 5 + pentagonOffset] = hexagon.getOuterPoint(3);
            indicesArray[(i * numPerHexagon) + 6 + pentagonOffset] = hexagon.getOuterPoint(3);
            indicesArray[(i * numPerHexagon) + 7 + pentagonOffset] = hexagon.getOuterPoint(4);
            indicesArray[(i * numPerHexagon) + 8 + pentagonOffset] = hexagon.getOuterPoint(4);
            indicesArray[(i * numPerHexagon) + 9 + pentagonOffset] = hexagon.getOuterPoint(5);
            indicesArray[(i * numPerHexagon) + 10 + pentagonOffset] = hexagon.getOuterPoint(5);
            indicesArray[(i * numPerHexagon) + 11 + pentagonOffset] = hexagon.getOuterPoint(0);
        }

        AglWireframe wireframe = new AglWireframe(vertexArray, numVertices, indicesArray, indicesArray.length);

        return wireframe;
    }

    public int getNumVertices() {
        return points.size();
    }

    public void writeToDiskAsBytes(String filename) throws IOException {
        FileOutputStream fileOutputStream;
        DataOutputStream out = null;

        try {
            fileOutputStream = new FileOutputStream(filename);
            out = new DataOutputStream(fileOutputStream);

            out.writeByte(AglDisk.POINTS);
            out.writeInt(points.size());

            for (AglPoint point : points) {
                point.writeToDataStream(out);
            }

            List<AglShape> shapes = new LinkedList<>();
            shapes.addAll(pentagons);
            shapes.addAll(hexagons);

            out.writeByte(AglDisk.SHAPES);
            out.writeInt(shapes.size());

            for (AglShape shape : shapes) {
                shape.writeToDataStream(out);
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

    public static AglBBMesh readFromStreamAsBytes(InputStream inputStream) throws IOException {
        DataInputStream in = null;

        ArrayList<AglPoint> points = new ArrayList<>();
        ArrayList<AglShape> pentagons = new ArrayList<>();
        ArrayList<AglShape> hexagons = new ArrayList<>();

        try {
            in = new DataInputStream(inputStream);

            while (true) {
                byte dataType = in.readByte();

                switch (dataType) {
                    case AglDisk.POINTS:
                    {
                        int numPoints = in.readInt();
                        for (int i = 0; i < numPoints; i++) {
                            points.add(AglPoint.readPointFromStream(in));
                        }
                    }
                        break;
                    case AglDisk.SHAPES:
                    {
                        int numShapes = in.readInt();
                        for (int i = 0; i < numShapes; i++) {
                            AglShape newShape = AglShape.readShapeFromStream(in);

                            if (newShape.getNumberOfSides() == 5) {
                                pentagons.add(newShape);
                            }
                            else if (newShape.getNumberOfSides() == 6) {
                                hexagons.add(newShape);
                            }
                            else {
                                throw new RuntimeException("Invalid shape (" + newShape.getNumberOfSides() + " sides)");
                            }
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

        return new AglBBMesh(points, pentagons, hexagons);
    }

    private static class Segment {
        public int pointA;
        public int pointB;
    }
}
