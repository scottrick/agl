package test;

import com.hatfat.agl.AglColoredGeometry;

public class TestRenderableFactory {

    public static AglColoredGeometry createIcosahedron() {
        float vertices[] = {
                //vertex (x, y, z), color (r, g, b),    normal (x, y, z)
                0f,  -0.525731f,  0.850651f,        1f,  0f,  0f,     0f,  -0.525731f,  0.850651f,
                0.850651f,  0f,  0.525731f,        0f,  1f,  0f,      0.850651f,  0f,  0.525731f,
                0.850651f,  0f,  -0.525731f,        0f,  0f,  1f,      0.850651f, 0f,  -0.525731f,
                -0.850651f,  0f,  -0.525731f,        1f,  1f,  0f,     -0.850651f,  0f,  -0.525731f,

                -0.850651f,  0f,  0.525731f,        1f,  0f,  1f,     -0.850651f,  0f,  0.525731f,
                -0.525731f,  0.850651f,  0f,        0f,  1f,  1f,      -0.525731f,  0.850651f,  0f,
                0.525731f,  0.850651f,  0f,        1f, .5f,  0f,      0.525731f,  0.850651f,  0f,
                0.525731f,  -0.850651f,  0f,        0f, .5f,  1f,     0.525731f,  -0.850651f,  0f,

                -0.525731f,  -0.850651f,  0f,        1f,  0f,  0f,     -0.525731f,  -0.850651f,  0f,
                0f,  -0.525731f,  -0.850651f,        0f,  1f,  0f,      0f,  -0.525731f,  -0.850651f,
                0f,  0.525731f,  -0.850651f,        0f,  0f,  1f,      0f,  0.525731f,  -0.850651f,
                0f,  0.525731f,  0.850651f,        1f,  1f,  0f,     0f,  0.525731f,  0.850651f,
        };

        short elements[] = {
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

        AglColoredGeometry cube = new AglColoredGeometry(vertices, 12, elements, elements.length);

        return cube;
    }

    public static AglColoredGeometry createCube() {
        float vertices[] = {
                //vertex (x, y, z), color (r, g, b),    normal (x, y, z)
                -1f,  1f,  1f,        1f,  0f,  0f,     -1f,  1f,  1f,
                 1f,  1f,  1f,        0f,  1f,  0f,      1f,  1f,  1f,
                 1f, -1f,  1f,        0f,  0f,  1f,      1f, -1f,  1f,
                -1f, -1f,  1f,        1f,  1f,  0f,     -1f, -1f,  1f,
                -1f,  1f, -1f,        1f,  0f,  1f,     -1f,  1f, -1f,
                 1f,  1f, -1f,        0f,  1f,  1f,      1f,  1f, -1f,
                 1f, -1f, -1f,        1f, .5f,  0f,      1f, -1f, -1f,
                -1f, -1f, -1f,        0f, .5f,  1f,     -1f, -1f, -1f,
        };

        short elements[] = {
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
                //vertex (x, y, z), color (r, g, b),    normal (x, y, z)


                //FRONT
               -1f,  1f,  1f,        1f,  0f,  0f,      0f,  0f,  1f,   //1
                1f,  1f,  1f,        1f,  0f,  0f,      0f,  0f,  1f,   //2
                1f, -1f,  1f,        1f,  0f,  0f,      0f,  0f,  1f,   //3
               -1f, -1f,  1f,        1f,  0f,  0f,      0f,  0f,  1f,   //4

                //RIGHT
                1f,  1f,  1f,        0f,  1f,  0f,      1f,  0f,  0f,   //4
                1f,  1f, -1f,        0f,  1f,  0f,      1f,  0f,  0f,   //5
                1f, -1f, -1f,        0f,  1f,  0f,      1f,  0f,  0f,   //6
                1f, -1f,  1f,        0f,  1f,  0f,      1f,  0f,  0f,   //7

                //LEFT
               -1f,  1f,  1f,        0f,  0f,  1f,     -1f,  0f,  0f,   //8
               -1f,  1f, -1f,        0f,  0f,  1f,     -1f,  0f,  0f,   //9
               -1f, -1f, -1f,        0f,  0f,  1f,     -1f,  0f,  0f,   //10
               -1f, -1f,  1f,        0f,  0f,  1f,     -1f,  0f,  0f,   //11

                //BACK
               -1f,  1f, -1f,        1f,  1f,  0f,      0f,  0f, -1f,   //12
                1f,  1f, -1f,        1f,  1f,  0f,      0f,  0f, -1f,   //13
                1f, -1f, -1f,        1f,  1f,  0f,      0f,  0f, -1f,   //14
               -1f, -1f, -1f,        1f,  1f,  0f,      0f,  0f, -1f,   //15

                //TOP
               -1f,  1f, -1f,        1f,  0f,  1f,      0f,  1f,  0f,   //16
                1f,  1f, -1f,        1f,  0f,  1f,      0f,  1f,  0f,   //17
                1f,  1f,  1f,        1f,  0f,  1f,      0f,  1f,  0f,   //18
               -1f,  1f,  1f,        1f,  0f,  1f,      0f,  1f,  0f,   //19

                //BOTTOM
               -1f, -1f, -1f,        0f,  1f,  1f,      0f, -1f,  0f,   //20
                1f, -1f, -1f,        0f,  1f,  1f,      0f, -1f,  0f,   //21
                1f, -1f,  1f,        0f,  1f,  1f,      0f, -1f,  0f,   //22
               -1f, -1f,  1f,        0f,  1f,  1f,      0f, -1f,  0f,   //23
        };

        short elements[] = {
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
                //vertex (x, y, z), color (r, g, b)
                0f,  1f,   0f,        1f,  0f,  0f,     0f,  1f,   0f,
              -.7f,  0f, -.7f,        0f,  1f,  0f,   -.7f,  0f, -.7f,
               .7f,  0f, -.7f,        0f,  0f,  1f,    .7f,  0f, -.7f,
                0f,  0f,   1f,        1f,  1f,  1f,     0f,  0f,   1f,
        };

        short elements[] = {
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
                0f,  1f,   0f,        1f,  0f,  0f,     0f,  1f,  -1f,  //0
               -1f, -1f,  -1f,        1f,  0f,  0f,     0f,  1f,  -1f,  //1
                1f, -1f,  -1f,        1f,  0f,  0f,     0f,  1f,  -1f,  //2

                //FRONT LEFT
                0f,  1f,   0f,        0f,  1f,  0f,    -1f,  1f,  1f,   //3
               -1f, -1f,  -1f,        0f,  1f,  0f,    -1f,  1f,  1f,   //4
                0f, -1f,   1f,        0f,  1f,  0f,    -1f,  1f,  1f,   //5

                //FRONT RIGHT
                0f,  1f,   0f,        0f,  0f,  1f,     1f,  1f,  1f,   //6
                1f, -1f,  -1f,        0f,  0f,  1f,     1f,  1f,  1f,   //7
                0f, -1f,   1f,        0f,  0f,  1f,     1f,  1f,  1f,   //8

                //BOTTOM
               -1f, -1f,  -1f,        1f,  1f,  0f,     0f,  -1f,  0f,  //9
                1f, -1f,  -1f,        1f,  1f,  0f,     0f,  -1f,  0f,  //10
                0f, -1f,   1f,        1f,  1f,  0f,     0f,  -1f,  0f,  //11
        };

        short elements[] = {
                0, 2, 1, //BACK

                3, 4, 5, //FRONT LEFT

                6, 8, 7, //FRONT RIGHT

                9, 10, 11,
        };

        AglColoredGeometry pyramid = new AglColoredGeometry(vertices, 12, elements, elements.length);

        return pyramid;
    }
}