package com.hatfat.agl.mesh;

import com.hatfat.agl.AglColoredGeometry;
import com.hatfat.agl.AglWireframe;
import com.hatfat.agl.mesh.gen.AglGenTriangle;

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
public class AglBBMesh extends AglMesh {

    public final static int NUM_PENTAGONS = 12;

    private List<AglShape> shapes;

    public AglBBMesh(
            ArrayList<AglPoint> points,
            ArrayList<AglShape> shapes,
            ArrayList<AglTriangle> triangles) {
        super(points, triangles);

        this.shapes = Collections.unmodifiableList(shapes);
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
        ArrayList<AglShape> shapes = new ArrayList<>();

        mesh.setupTriangleNeighbors();

        HashMap<AglShape, Integer> shapeIndexMap = new HashMap<>();

        AglGenTriangle triangle;
        AglGenTriangle genTri;

        //make the starting 12 pentagons
        for (int i = 0; i < 12; i++) {
            Set<AglTriangle> triangleSet = mesh.getPointMap().get(i);
            ArrayList<Integer> outerPoints = createOuterPointsFromTriangles(triangleSet, i);

            AglShape pentagon = new AglShape(outerPoints, new ArrayList(triangleSet), i);
            shapeIndexMap.put(pentagon, shapes.size());
            shapes.add(pentagon);

            for (AglTriangle tri : triangleSet) {
                triangle = (AglGenTriangle) tri;
                triangle.shape = pentagon;
            }
        }

        HashSet<AglTriangle> usedTriangles = new HashSet<>();
        for (AglShape shape : shapes) {
            usedTriangles.addAll(shape.getTriangles());
        }

        List<AglShape> shapesToCheck = new LinkedList<>();
        shapesToCheck.addAll(shapes);

        while (shapesToCheck.size() > 0) {
            AglShape shape = shapesToCheck.get(0);

            for (AglTriangle tri : shape.getTriangles()) {
                triangle = (AglGenTriangle) tri;

                //find the edge for this triangle in the shape
                AglTriangle edgeNeighbor = null;

                if (triangle.pointA == shape.getCenterIndex()) {
                    edgeNeighbor = triangle.neighborBC;
                }
                else if (triangle.pointB == shape.getCenterIndex()) {
                    edgeNeighbor = triangle.neighborCA;
                }
                else if (triangle.pointC == shape.getCenterIndex()) {
                    edgeNeighbor = triangle.neighborAB;
                }

                if (edgeNeighbor == null) {
                    throw new RuntimeException("Should always have a valid edge neighbor.");
                }

                if (!usedTriangles.contains(edgeNeighbor)) {
                    //haven't added this triangle shape yet!
                    int edgeCenter = -1;

                    if (!triangle.containsPoint(edgeNeighbor.pointA)) {
                        edgeCenter = edgeNeighbor.pointA;
                    }
                    else if (!triangle.containsPoint(edgeNeighbor.pointB)) {
                        edgeCenter = edgeNeighbor.pointB;
                    }
                    else if (!triangle.containsPoint(edgeNeighbor.pointC)) {
                        edgeCenter = edgeNeighbor.pointC;
                    }

                    if (edgeCenter < 0) {
                        throw new RuntimeException("Should always have an edge center.");
                    }

                    Set<AglTriangle> triangleSet = mesh.getPointMap().get(edgeCenter);
                    ArrayList<Integer> outerPoints = createOuterPointsFromTriangles(triangleSet, edgeCenter);

                    AglShape hexagon = new AglShape(outerPoints, new ArrayList(triangleSet), edgeCenter);
                    shapeIndexMap.put(hexagon, shapes.size());
                    shapes.add(hexagon);

                    usedTriangles.addAll(hexagon.getTriangles());
                    shapesToCheck.add(hexagon);

                    for (AglTriangle setTri : triangleSet) {
                        genTri = (AglGenTriangle) setTri;
                        genTri.shape = hexagon;
                    }
                }
            }

            shapesToCheck.remove(0);
        }

        ArrayList<AglTriangle> triangles = new ArrayList<>();
        for (AglShape shape : shapes) {
            triangles.addAll(shape.getTriangles());
        }

        //setup shape neighbors
        ////////////////////////////////////////////////////////////////////////////////////
        for (AglShape shape : shapes) {
            int currentShapeIndex = shapeIndexMap.get(shape);

            for (AglTriangle shapeTri : shape.getTriangles()) {
                triangle = (AglGenTriangle) shapeTri;

                int indexAB = shapeIndexMap.get(triangle.neighborAB.shape);
                if (indexAB != currentShapeIndex) {
                    shape.addNeighbor(indexAB);
                }

                int indexBC = shapeIndexMap.get(triangle.neighborBC.shape);
                if (indexBC != currentShapeIndex) {
                    shape.addNeighbor(indexBC);
                }

                int indexCA = shapeIndexMap.get(triangle.neighborCA.shape);
                if (indexCA != currentShapeIndex) {
                    shape.addNeighbor(indexCA);
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////

        AglBBMesh shapeMesh = new AglBBMesh(mesh.getPoints(), shapes, triangles);
        return shapeMesh;
    }

    public List<AglShape> getShapes() {
        return shapes;
    }

    public int getNumShapes() {
        return shapes.size();
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
            newVertices[i * 10 + 6] = 1.0f; //a
            newVertices[i * 10 + 7] = vertices[i * 3 + 0]; //normal x
            newVertices[i * 10 + 8] = vertices[i * 3 + 1]; //normal y
            newVertices[i * 10 + 9] = vertices[i * 3 + 2]; //normal z
        }

        int[] elements = new int[NUM_PENTAGONS * 15 + (getNumShapes() - NUM_PENTAGONS) * 18];
        int currentOffset = 0;

        for (AglShape shape : shapes) {
            for (int j = 0; j < shape.getOuterPoints().size(); j++) {
                int index1 = shape.getOuterPoint(j);
                int index2 = shape.getOuterPoint((j + 1) % shape.getOuterPoints().size());

                elements[currentOffset + j * 3 + 0] = index2;
                elements[currentOffset + j * 3 + 1] = shape.getCenterIndex();
                elements[currentOffset + j * 3 + 2] = index1;
            }

            currentOffset += shape.getOuterPoints().size() * 3;
        }

        AglColoredGeometry coloredGeometry = new AglColoredGeometry(newVertices, numVertices, elements, elements.length);

        return coloredGeometry;
    }

    public AglWireframe toWireframeRenderable() {
        float[] vertexArray = getVertexArray();
        int numVertices = getNumVertices();

        int numPerPentagon = 10;
        int numPerHexagon = 12;
        int[] indicesArray = new int[NUM_PENTAGONS * numPerPentagon + (getNumShapes() - NUM_PENTAGONS) * numPerHexagon];

        int currentOffset = 0;
        int numOuterPoints = 0;

        for (AglShape shape : shapes) {
            numOuterPoints = shape.getOuterPoints().size();

            for (int i = 0; i < numOuterPoints; i++) {
                indicesArray[currentOffset + i * 2 + 0] = shape.getOuterPoint((i + 0) % numOuterPoints);
                indicesArray[currentOffset + i * 2 + 1] = shape.getOuterPoint((i + 1) % numOuterPoints);
            }

            currentOffset += numOuterPoints * 2;
        }

        AglWireframe wireframe = new AglWireframe(vertexArray, numVertices, indicesArray, indicesArray.length);

        return wireframe;
    }

    public int getNumVertices() {
        return points.size();
    }

    //creates a new AglBBMesh, where no vertices are shared between shapes.
    public AglBBMesh createNewMeshWithNoVertexSharing() {

        ArrayList<AglPoint> newPoints = new ArrayList<>();
        ArrayList<AglShape> newShapes = new ArrayList<>();

        for (AglShape shape : shapes) {
            int currentPointIndexStart = newPoints.size();

            //add the outer points
            for (Integer pointIndex : shape.getOuterPoints()) {
                AglPoint point = points.get(pointIndex);
                AglPoint newPoint = new AglPoint(point);
                newPoints.add(newPoint);
            }

            //add the center point
            AglPoint centerPoint = points.get(shape.getCenterIndex());
            AglPoint newCenterPoint = new AglPoint(centerPoint);
            newPoints.add(newCenterPoint);

            ArrayList<Integer> newOuterPoints = new ArrayList<>();
            ArrayList<AglTriangle> newTriangles = new ArrayList<>();
            int newCenterIndex = currentPointIndexStart + shape.getOuterPoints().size();

            for (int i = 0; i < shape.getOuterPoints().size(); i++) {
                newOuterPoints.add(currentPointIndexStart + i);
            }

            for (int i = 0; i < newOuterPoints.size(); i++) {
                int pointB = newOuterPoints.get(i);
                int pointC = newOuterPoints.get((i + 1) % newOuterPoints.size());

                newTriangles.add(new AglTriangle(newCenterIndex, pointB, pointC));
            }

            newShapes.add(new AglShape(newOuterPoints, newTriangles, newCenterIndex, shape.getNeighborIndices()));
        }

        ArrayList<AglTriangle> newTriangles = new ArrayList<>();
        for (AglShape shape : shapes) {
            newTriangles.addAll(shape.getTriangles());
        }

        return new AglBBMesh(newPoints, newShapes, newTriangles);
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
        ArrayList<AglShape> shapes = new ArrayList<>();

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
                            shapes.add(newShape);
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

        ArrayList<AglTriangle> triangles = new ArrayList<>();
        for (AglShape shape : shapes) {
            triangles.addAll(shape.getTriangles());
        }

        return new AglBBMesh(points, shapes, triangles);
    }

    private static class Segment {
        public int pointA;
        public int pointB;
    }
}
