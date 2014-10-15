package meshgen;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class AglShapeMesh {
    private List<AglShape> hexagons;
    private List<AglShape> pentagons;

    private List<AglPoint> points;

    public AglShapeMesh() {
        this.hexagons = new LinkedList();
        this.pentagons = new LinkedList();

        this.points = new LinkedList();
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

        HashSet<AglTriangle> usedTriangles = new HashSet();

        for (AglShape pentagon : shapeMesh.pentagons) {
            usedTriangles.addAll(pentagon.getTriangles());
        }

        List<AglShape> shapesToCheck = new LinkedList();
        shapesToCheck.addAll(shapeMesh.pentagons);

        int hack = 0;

        while (shapesToCheck.size() > 0) {
            hack++;

            if (hack % 500 == 0) {
                System.out.println("  --> shapes left " + shapesToCheck.size());
            }

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
        }
    }

    private void addTrianglePoints(AglTriangle triangle) {
        addPoint(triangle.pointA);
        addPoint(triangle.pointB);
        addPoint(triangle.pointC);
    }

    public float[] getVertexArray() {
        float[] vertexArray = new float[points.size() * 3];

        for (int i = 0; i < points.size(); i++) {
            AglPoint point = points.get(i);

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

    // public AglWireframe createWireframe() {
    //     float[] vertexArray = getVertexArray();
    //     int numVertices = getNumVertices();

    //     int[] indicesArray = new int[pentagons.size() * 5 * 2 + hexagons.size() * 6 * 2];

    //     for (int i = 0; i < pentagons.size(); i++) {
    //         AglShape pentagon = pentagons.get(i);

    //         int currentIndex = 0;

    //         for (int j = 0; j < pentagon.getTriangles().size(); j++) {
    //             AglTriangle triangle = pentagon.getTriangles().get(j);

    //             if (!triangle.pointA.equals(pentagon.getCenter())) {
    //                 int index = points.indexOf(triangle.pointA);
    //                 indicesArray[i * 10 + currentIndex] = index;
    //                 currentIndex++;
    //             }

    //             if (!triangle.pointB.equals(pentagon.getCenter())) {
    //                 int index = points.indexOf(triangle.pointB);
    //                 indicesArray[i * 10 + currentIndex] = index;
    //                 currentIndex++;
    //             }

    //             if (!triangle.pointC.equals(pentagon.getCenter())) {
    //                 int index = points.indexOf(triangle.pointC);
    //                 indicesArray[i * 10 + currentIndex] = index;
    //                 currentIndex++;
    //             }
    //         }
    //     }

    //     for (int i = 0; i < hexagons.size(); i++) {
    //         AglShape hexagon = hexagons.get(i);

    //         int currentIndex = 0;

    //         for (int j = 0; j < hexagon.getTriangles().size(); j++) {
    //             AglTriangle triangle = hexagon.getTriangles().get(j);

    //             if (!triangle.pointA.equals(hexagon.getCenter())) {
    //                 int index = points.indexOf(triangle.pointA);
    //                 indicesArray[i * 12 + currentIndex] = index;
    //                 currentIndex++;
    //             }

    //             if (!triangle.pointB.equals(hexagon.getCenter())) {
    //                 int index = points.indexOf(triangle.pointB);
    //                 indicesArray[i * 12 + currentIndex] = index;
    //                 currentIndex++;
    //             }

    //             if (!triangle.pointC.equals(hexagon.getCenter())) {
    //                 int index = points.indexOf(triangle.pointC);
    //                 indicesArray[i * 12 + currentIndex] = index;
    //                 currentIndex++;
    //             }
    //         }
    //     }

    //     AglWireframe wireframe = new AglWireframe(vertexArray, numVertices, indicesArray, indicesArray.length);

    //     return wireframe;
    // }

    public float[] getOutlineVertexArray() {
        float[] vertexArray = new float[pentagons.size() * 5 * 2 * 3];

        for (AglShape pentagon : pentagons) {
            for (AglTriangle triangle : pentagon.getTriangles()) {

            }
        }

        for (int i = 0; i < points.size(); i++) {
            AglPoint point = points.get(i);

            vertexArray[i * 3 + 0] = point.p.x;
            vertexArray[i * 3 + 1] = point.p.y;
            vertexArray[i * 3 + 2] = point.p.z;
        }

        return vertexArray;
    }

    public int getNumVertices() {
        return points.size();
    }

    public int[] getIndexArray() {
        int[] indexArray = new int[pentagons.size() * 3 * 5];

        for (int i = 0; i < pentagons.size(); i++) {
            AglShape shape = pentagons.get(i);

            for (int j = 0; j < shape.getTriangles().size(); j++) {
                AglTriangle triangle = shape.getTriangles().get(j);

                indexArray[i * 3 * 5 + j * 3 + 0] = points.indexOf(triangle.pointA);
                indexArray[i * 3 * 5 + j * 3 + 1] = points.indexOf(triangle.pointB);
                indexArray[i * 3 * 5 + j * 3 + 2] = points.indexOf(triangle.pointC);
            }
        }

        return indexArray;
    }

    public int getNumTriangles() {
        return pentagons.size() * 5;
    }
}