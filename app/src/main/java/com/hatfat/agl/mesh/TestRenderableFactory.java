package com.hatfat.agl.mesh;

import com.hatfat.agl.render.AglBumpMappedGeometry;
import com.hatfat.agl.render.AglColoredGeometry;
import com.hatfat.agl.render.AglLineSegments;
import com.hatfat.agl.render.AglTexturedGeometry;
import com.hatfat.agl.render.AglWireframe;
import com.hatfat.agl.textures.AglTexture;

public class TestRenderableFactory {

    public final static float icosahedronVerticesWithColorsAndNormals[] = {
        //vertex (x, y, z), color (r, g, b, a),    normal (x, y, z)
        0f,  -0.525731f,  0.850651f,        0.1f,  0.9f,  0.1f,  1f,     0f,  -0.525731f,  0.850651f,
        0.850651f,  0f,  0.525731f,        0.1f,  0.9f,  0.1f,  1f,      0.850651f,  0f,  0.525731f,
        0.850651f,  0f,  -0.525731f,        0.1f,  0.9f,  0.1f,  1f,      0.850651f, 0f,  -0.525731f,
        -0.850651f,  0f,  -0.525731f,        0.1f,  0.9f,  0.1f,  1f,     -0.850651f,  0f,  -0.525731f,

        -0.850651f,  0f,  0.525731f,        0.1f,  0.9f,  0.1f,  1f,     -0.850651f,  0f,  0.525731f,
        -0.525731f,  0.850651f,  0f,        0.1f,  0.9f,  0.1f,  1f,      -0.525731f,  0.850651f,  0f,
        0.525731f,  0.850651f,  0f,        0.1f,  0.9f,  0.1f,  1f,      0.525731f,  0.850651f,  0f,
        0.525731f,  -0.850651f,  0f,        0.1f,  0.9f,  0.1f,  1f,     0.525731f,  -0.850651f,  0f,

        -0.525731f,  -0.850651f,  0f,        0.1f,  0.9f,  0.1f,  1f,     -0.525731f,  -0.850651f,  0f,
        0f,  -0.525731f,  -0.850651f,        0.1f,  0.9f,  0.1f,  1f,      0f,  -0.525731f,  -0.850651f,
        0f,  0.525731f,  -0.850651f,        0.1f,  0.9f,  0.1f,  1f,      0f,  0.525731f,  -0.850651f,
        0f,  0.525731f,  0.850651f,        0.1f,  0.9f,  0.1f,  1f,     0f,  0.525731f,  0.850651f,
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

    public static AglLineSegments createOrigin() {
        final float vertices[] = {
                //vertex (x, y, z),             color (r, g, b, a),
                0.0f, 0.0f, 0.0f,               1.0f, 0.2f, 0.2f, 1.0f,     // X-Axis  RED
                5.0f, 0.0f, 0.0f,               1.0f, 0.2f, 0.2f, 1.0f,

                0.0f, 0.0f, 0.0f,               0.2f, 1.0f, 0.2f, 1.0f,     // Y-Axis  GREEN
                0.0f, 5.0f, 0.0f,               0.2f, 1.0f, 0.2f, 1.0f,

                0.0f, 0.0f, 0.0f,               0.2f, 0.2f, 1.0f, 1.0f,     // Z-Axis  BLUE
                0.0f, 0.0f, 5.0f,               0.2f, 0.2f, 1.0f, 1.0f,
        };

        final int elements[] = {
                0, 1,
                2, 3,
                4, 5,
        };

        AglLineSegments lineSegments = new AglLineSegments(vertices, 6, elements, elements.length);

        return lineSegments;
    }

    public static AglTexturedGeometry createTextureCube(AglTexture texture, boolean isLit) {

        float vertices[] = {
                //vertex (x, y, z), texture (s, t),    normal (x, y, z)

                //FRONT
                -1f,  1f,  1f,        0f,  1f,      0f,  0f,  1f,   //1
                 1f,  1f,  1f,        1f,  1f,      0f,  0f,  1f,   //2
                 1f, -1f,  1f,        1f,  0f,      0f,  0f,  1f,   //3
                -1f, -1f,  1f,        0f,  0f,      0f,  0f,  1f,   //4

                //RIGHT
                1f,  1f,  1f,        0f,  1f,      1f,  0f,  0f,   //4
                1f,  1f, -1f,        1f,  1f,      1f,  0f,  0f,   //5
                1f, -1f, -1f,        1f,  0f,      1f,  0f,  0f,   //6
                1f, -1f,  1f,        0f,  0f,      1f,  0f,  0f,   //7

                //LEFT
                -1f,  1f,  1f,        0f,  1f,     -1f,  0f,  0f,   //8
                -1f,  1f, -1f,        1f,  1f,     -1f,  0f,  0f,   //9
                -1f, -1f, -1f,        1f,  0f,     -1f,  0f,  0f,   //10
                -1f, -1f,  1f,        0f,  0f,     -1f,  0f,  0f,   //11

                //BACK
                -1f,  1f, -1f,        0f,  1f,      0f,  0f, -1f,   //12
                1f,   1f, -1f,        1f,  1f,      0f,  0f, -1f,   //13
                1f,  -1f, -1f,        1f,  0f,      0f,  0f, -1f,   //14
                -1f, -1f, -1f,        0f,  0f,      0f,  0f, -1f,   //15

                //TOP
                -1f,  1f, -1f,        0f,  1f,      0f,  1f,  0f,   //16
                 1f,  1f, -1f,        1f,  1f,      0f,  1f,  0f,   //17
                 1f,  1f,  1f,        1f,  0f,      0f,  1f,  0f,   //18
                -1f,  1f,  1f,        0f,  0f,      0f,  1f,  0f,   //19

                //BOTTOM
                -1f, -1f, -1f,        0f,  1f,      0f, -1f,  0f,   //20
                 1f, -1f, -1f,        1f,  1f,      0f, -1f,  0f,   //21
                 1f, -1f,  1f,        1f,  0f,      0f, -1f,  0f,   //22
                -1f, -1f,  1f,        0f,  0f,      0f, -1f,  0f,   //23
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

        AglTexturedGeometry texturedCube = new AglTexturedGeometry(vertices, 24, elements, elements.length, texture, isLit);

        return texturedCube;
    }

    public static AglTexturedGeometry createTextureSquare(AglTexture texture) {
        float vertices[] = {
                //vertex (x, y, z), texture (s, t), normal(x, y, z)
                //FRONT
                -0.5f,  0.5f,  0.0f,        0f,  1f,        0f,  0f,  1f,   //1
                 0.5f,  0.5f,  0.0f,        1f,  1f,        0f,  0f,  1f,   //2
                 0.5f, -0.5f,  0.0f,        1f,  0f,        0f,  0f,  1f,   //3
                -0.5f, -0.5f,  0.0f,        0f,  0f,        0f,  0f,  1f,   //4
        };

        int elements[] = {
                0, 2, 1, //front
                0, 3, 2,
        };

        AglTexturedGeometry texturedGeometry = new AglTexturedGeometry(vertices, 4, elements, elements.length, texture, false);

        return texturedGeometry;
    }

    public static AglBumpMappedGeometry createNormalMappedTextureCube(AglTexture texture, AglTexture normalTexture, AglTexture specularTexture) {

        float vertices[] = {
                //vertex (x, y, z), texture (s, t),    normal (x, y, z),    tangent (x, y, z),     bitangent (x, y, z)

                //FRONT
                -1f,  1f,  1f,        0f,  1f,      0f,  0f,  1f,      0f,  0f,  0f,      0f,  0f,  0f,      //0
                 1f,  1f,  1f,        1f,  1f,      0f,  0f,  1f,      0f,  0f,  0f,      0f,  0f,  0f,      //1
                 1f, -1f,  1f,        1f,  0f,      0f,  0f,  1f,      0f,  0f,  0f,      0f,  0f,  0f,      //2
                -1f, -1f,  1f,        0f,  0f,      0f,  0f,  1f,      0f,  0f,  0f,      0f,  0f,  0f,      //3

                 //RIGHT
                 1f,  1f,  1f,        0f,  1f,      1f,  0f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //4
                 1f,  1f, -1f,        1f,  1f,      1f,  0f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //5
                 1f, -1f, -1f,        1f,  0f,      1f,  0f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //6
                 1f, -1f,  1f,        0f,  0f,      1f,  0f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //7

                //LEFT
                -1f,  1f,  1f,        0f,  1f,     -1f,  0f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //8
                -1f,  1f, -1f,        1f,  1f,     -1f,  0f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //9
                -1f, -1f, -1f,        1f,  0f,     -1f,  0f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //10
                -1f, -1f,  1f,        0f,  0f,     -1f,  0f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //11

                //BACK
                -1f,  1f, -1f,        0f,  1f,      0f,  0f, -1f,      0f,  0f,  0f,      0f,  0f,  0f,      //12
                1f,   1f, -1f,        1f,  1f,      0f,  0f, -1f,      0f,  0f,  0f,      0f,  0f,  0f,      //13
                1f,  -1f, -1f,        1f,  0f,      0f,  0f, -1f,      0f,  0f,  0f,      0f,  0f,  0f,      //14
                -1f, -1f, -1f,        0f,  0f,      0f,  0f, -1f,      0f,  0f,  0f,      0f,  0f,  0f,      //15

                //TOP
                -1f,  1f, -1f,        0f,  1f,      0f,  1f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //16
                 1f,  1f, -1f,        1f,  1f,      0f,  1f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //17
                 1f,  1f,  1f,        1f,  0f,      0f,  1f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //18
                -1f,  1f,  1f,        0f,  0f,      0f,  1f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //19

                //BOTTOM
                -1f, -1f, -1f,        0f,  1f,      0f, -1f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //20
                 1f, -1f, -1f,        1f,  1f,      0f, -1f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //21
                 1f, -1f,  1f,        1f,  0f,      0f, -1f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //22
                -1f, -1f,  1f,        0f,  0f,      0f, -1f,  0f,      0f,  0f,  0f,      0f,  0f,  0f,      //23
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

        AglBumpMappedGeometry normalMappedCube = new AglBumpMappedGeometry(vertices, 24, elements, elements.length, texture, normalTexture, specularTexture);

        return normalMappedCube;
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