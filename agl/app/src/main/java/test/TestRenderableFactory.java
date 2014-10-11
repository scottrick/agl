package test;

import com.hatfat.agl.AglColoredGeometry;
import com.hatfat.agl.AglWireframe;
import com.hatfat.agl.mesh.AglMesh;
import com.hatfat.agl.mesh.AglTriangle;

public class TestRenderableFactory {

    public final static float icosahedronVerticesWithColorsAndNormals[] = {
        //vertex (x, y, z), color (r, g, b, a),    normal (x, y, z)
        0f,  -0.525731f,  0.850651f,        1f,  0f,  0f,  1f,     0f,  -0.525731f,  0.850651f,
        0.850651f,  0f,  0.525731f,        0f,  1f,  0f,  1f,      0.850651f,  0f,  0.525731f,
        0.850651f,  0f,  -0.525731f,        0f,  0f,  1f,  1f,      0.850651f, 0f,  -0.525731f,
        -0.850651f,  0f,  -0.525731f,        1f,  1f,  0f,  1f,     -0.850651f,  0f,  -0.525731f,

        -0.850651f,  0f,  0.525731f,        1f,  0f,  1f,  1f,     -0.850651f,  0f,  0.525731f,
        -0.525731f,  0.850651f,  0f,        0f,  1f,  1f,  1f,      -0.525731f,  0.850651f,  0f,
        0.525731f,  0.850651f,  0f,        1f, .5f,  0f,  1f,      0.525731f,  0.850651f,  0f,
        0.525731f,  -0.850651f,  0f,        0f, .5f,  1f,  1f,     0.525731f,  -0.850651f,  0f,

        -0.525731f,  -0.850651f,  0f,        1f,  0f,  0f,  1f,     -0.525731f,  -0.850651f,  0f,
        0f,  -0.525731f,  -0.850651f,        0f,  1f,  0f,  1f,      0f,  -0.525731f,  -0.850651f,
        0f,  0.525731f,  -0.850651f,        0f,  0f,  1f,  1f,      0f,  0.525731f,  -0.850651f,
        0f,  0.525731f,  0.850651f,        1f,  1f,  0f,  1f,     0f,  0.525731f,  0.850651f,
    };

    public final static float icosahedronVertices[] = {
            //vertex (x, y, z)
            0f,  -0.525731f,  0.850651f,
            0.850651f,  0f,  0.525731f,
            0.850651f,  0f,  -0.525731f,
            -0.850651f,  0f,  -0.525731f,

            -0.850651f,  0f,  0.525731f,
            -0.525731f,  0.850651f,  0f,
            0.525731f,  0.850651f,  0f,
            0.525731f,  -0.850651f,  0f,

            -0.525731f,  -0.850651f,  0f,
            0f,  -0.525731f,  -0.850651f,
            0f,  0.525731f,  -0.850651f,
            0f,  0.525731f,  0.850651f,
    };

    public static final int icosahedronElements[] = {
            1,  2,  6,
            1,  7,  2,
            3,  4,  5,
            4,  3,  8,
            6,  5,  11,
            5,  6,  10,
            9,  10, 2,
            10, 9,  3,
            7,  8,  9,
            8,  7,  0,
            11,  0,  1,
            0,  11,  4,
            6,  2,  10,
            1,  6,  11,
            3,  5,  10,
            5,  4,  11,
            2,  7,  9,
            7,  1,  0,
            3,  9,  8,
            4,  8,  0,
    };

    public static AglColoredGeometry createIcosahedron() {
        return new AglColoredGeometry(icosahedronVerticesWithColorsAndNormals, 12, icosahedronElements, icosahedronElements.length);
    }

    public static AglWireframe createIcosahedronWireframe() {
        return createWireFrameFromTriangles(icosahedronVertices, 12, icosahedronElements, icosahedronElements.length / 3);
    }

    public static AglWireframe createWireFrameFromTriangles(float[] vertices, int numVertices, int[] elements, int numTriangles) {

        int[] wireframeElements = new int[numTriangles * 6];

        for (int i = 0; i < numTriangles; i++) {
            wireframeElements[i * 6 + 0] = elements[i * 3 + 0];
            wireframeElements[i * 6 + 1] = elements[i * 3 + 1];
            wireframeElements[i * 6 + 2] = elements[i * 3 + 1];
            wireframeElements[i * 6 + 3] = elements[i * 3 + 2];
            wireframeElements[i * 6 + 4] = elements[i * 3 + 2];
            wireframeElements[i * 6 + 5] = elements[i * 3 + 0];
        }

        AglWireframe wireframe = new AglWireframe(vertices, numVertices, wireframeElements, wireframeElements.length);

        return wireframe;
    }

    public static AglColoredGeometry createColoredRenderableFromMesh(AglMesh mesh) {

        float[] vertices = mesh.getVertexArray();
        int numVertices = mesh.getNumVertices();

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

        int numTriangles = mesh.getNumTriangles();
        int[] elements = new int[numTriangles * 3];

        for (int i = 0; i < numTriangles; i++) {
            AglTriangle triangle = mesh.getTriangle(i);
            elements[i * 3 + 0] = mesh.indexForPoint(triangle.pointA);
            elements[i * 3 + 1] = mesh.indexForPoint(triangle.pointB);
            elements[i * 3 + 2] = mesh.indexForPoint(triangle.pointC);
        }

        AglColoredGeometry coloredGeometry = new AglColoredGeometry(newVertices, numVertices, elements, elements.length);

        return coloredGeometry;
    }

    public static AglColoredGeometry createCube() {
        float vertices[] = {
                //vertex (x, y, z), color (r, g, b, a),    normal (x, y, z)
                -1f,  1f,  1f,        1f,  0f,  0f,  1f,     -1f,  1f,  1f,
                 1f,  1f,  1f,        0f,  1f,  0f,  1f,      1f,  1f,  1f,
                 1f, -1f,  1f,        0f,  0f,  1f,  1f,      1f, -1f,  1f,
                -1f, -1f,  1f,        1f,  1f,  0f,  1f,     -1f, -1f,  1f,
                -1f,  1f, -1f,        1f,  0f,  1f,  1f,     -1f,  1f, -1f,
                 1f,  1f, -1f,        0f,  1f,  1f,  1f,      1f,  1f, -1f,
                 1f, -1f, -1f,        1f, .5f,  0f,  1f,      1f, -1f, -1f,
                -1f, -1f, -1f,        0f, .5f,  1f,  1f,     -1f, -1f, -1f,
        };

        int elements[] = {
                0, 2, 1, //front
                0, 3, 2,
                2, 6, 1, //right
                1, 6, 5,
                0, 4, 7, //left
                0, 7, 3,
                4, 5, 6, //back
                4, 6, 7,
                0, 5, 4, //top
                0, 1, 5,
                3, 7, 6, //bottom
                3, 6, 2,
        };

        AglColoredGeometry cube = new AglColoredGeometry(vertices, 8, elements, elements.length);

        return cube;
    }

    public static AglColoredGeometry createFlatCube() {
        float vertices[] = {
                //vertex (x, y, z), color (r, g, b, a),    normal (x, y, z)


                //FRONT
               -1f,  1f,  1f,        1f,  0f,  0f,  1f,      0f,  0f,  1f,   //1
                1f,  1f,  1f,        1f,  0f,  0f,  1f,      0f,  0f,  1f,   //2
                1f, -1f,  1f,        1f,  0f,  0f,  1f,      0f,  0f,  1f,   //3
               -1f, -1f,  1f,        1f,  0f,  0f,  1f,      0f,  0f,  1f,   //4

                //RIGHT
                1f,  1f,  1f,        0f,  1f,  0f,  1f,      1f,  0f,  0f,   //4
                1f,  1f, -1f,        0f,  1f,  0f,  1f,      1f,  0f,  0f,   //5
                1f, -1f, -1f,        0f,  1f,  0f,  1f,      1f,  0f,  0f,   //6
                1f, -1f,  1f,        0f,  1f,  0f,  1f,      1f,  0f,  0f,   //7

                //LEFT
               -1f,  1f,  1f,        0f,  0f,  1f,  1f,     -1f,  0f,  0f,   //8
               -1f,  1f, -1f,        0f,  0f,  1f,  1f,     -1f,  0f,  0f,   //9
               -1f, -1f, -1f,        0f,  0f,  1f,  1f,     -1f,  0f,  0f,   //10
               -1f, -1f,  1f,        0f,  0f,  1f,  1f,     -1f,  0f,  0f,   //11

                //BACK
               -1f,  1f, -1f,        1f,  1f,  0f,  1f,      0f,  0f, -1f,   //12
                1f,  1f, -1f,        1f,  1f,  0f,  1f,      0f,  0f, -1f,   //13
                1f, -1f, -1f,        1f,  1f,  0f,  1f,      0f,  0f, -1f,   //14
               -1f, -1f, -1f,        1f,  1f,  0f,  1f,      0f,  0f, -1f,   //15

                //TOP
               -1f,  1f, -1f,        1f,  0f,  1f,  1f,      0f,  1f,  0f,   //16
                1f,  1f, -1f,        1f,  0f,  1f,  1f,      0f,  1f,  0f,   //17
                1f,  1f,  1f,        1f,  0f,  1f,  1f,      0f,  1f,  0f,   //18
               -1f,  1f,  1f,        1f,  0f,  1f,  1f,      0f,  1f,  0f,   //19

                //BOTTOM
               -1f, -1f, -1f,        0f,  1f,  1f,  1f,      0f, -1f,  0f,   //20
                1f, -1f, -1f,        0f,  1f,  1f,  1f,      0f, -1f,  0f,   //21
                1f, -1f,  1f,        0f,  1f,  1f,  1f,      0f, -1f,  0f,   //22
               -1f, -1f,  1f,        0f,  1f,  1f,  1f,      0f, -1f,  0f,   //23
        };

        int elements[] = {
                0, 2, 1, //front
                0, 3, 2,

                6, 5, 4, //right
                6, 4, 7,

                8, 9, 10, //left
                8, 10, 11,

                12, 13, 14, //back
                12, 14, 15,

                16, 18, 17, //top
                16, 19, 18,

                20, 21, 22, //bottom
                20, 22, 23,
        };

        AglColoredGeometry flatCube = new AglColoredGeometry(vertices, 24, elements, elements.length);

        return flatCube;
    }

    public static AglColoredGeometry createPyramid() {
        float vertices[] = {
                //vertex (x, y, z), color (r, g, b, a)
                0f,  1f,   0f,        1f,  0f,  0f,  1f,     0f,  1f,   0f,
              -.7f,  0f, -.7f,        0f,  1f,  0f,  1f,   -.7f,  0f, -.7f,
               .7f,  0f, -.7f,        0f,  0f,  1f,  1f,    .7f,  0f, -.7f,
                0f,  0f,   1f,        1f,  1f,  1f,  1f,     0f,  0f,   1f,
        };

        int elements[] = {
                0, 1, 2,
                0, 2, 3,
                0, 3, 1,
                1, 3, 2,
        };

        AglColoredGeometry pyramid = new AglColoredGeometry(vertices, 4, elements, elements.length);

        return pyramid;
    }

    public static AglColoredGeometry createFlatPyramid() {
        float vertices[] = {
                //BACK
                0f,  1f,   0f,        1f,  0f,  0f,  1f,     0f,  1f,  -1f,  //0
               -1f, -1f,  -1f,        1f,  0f,  0f,  1f,     0f,  1f,  -1f,  //1
                1f, -1f,  -1f,        1f,  0f,  0f,  1f,     0f,  1f,  -1f,  //2

                //FRONT LEFT
                0f,  1f,   0f,        0f,  1f,  0f,  1f,    -1f,  1f,  1f,   //3
               -1f, -1f,  -1f,        0f,  1f,  0f,  1f,    -1f,  1f,  1f,   //4
                0f, -1f,   1f,        0f,  1f,  0f,  1f,    -1f,  1f,  1f,   //5

                //FRONT RIGHT
                0f,  1f,   0f,        0f,  0f,  1f,  1f,     1f,  1f,  1f,   //6
                1f, -1f,  -1f,        0f,  0f,  1f,  1f,     1f,  1f,  1f,   //7
                0f, -1f,   1f,        0f,  0f,  1f,  1f,     1f,  1f,  1f,   //8

                //BOTTOM
               -1f, -1f,  -1f,        1f,  1f,  0f,  1f,     0f,  -1f,  0f,  //9
                1f, -1f,  -1f,        1f,  1f,  0f,  1f,     0f,  -1f,  0f,  //10
                0f, -1f,   1f,        1f,  1f,  0f,  1f,     0f,  -1f,  0f,  //11
        };

        int elements[] = {
                0, 2, 1, //BACK

                3, 4, 5, //FRONT LEFT

                6, 8, 7, //FRONT RIGHT

                9, 10, 11,
        };

        AglColoredGeometry pyramid = new AglColoredGeometry(vertices, 12, elements, elements.length);

        return pyramid;
    }
}