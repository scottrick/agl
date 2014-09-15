package test;

import com.hatfat.agl.AglColoredGeometry;

public class TestRenderableFactory {

    public static AglColoredGeometry createCube() {
        float vertices[] = {
                //vertex (x, y, z), color (r, g, b)
                -1f,  1f,  1f,        1f,  0f,  0f,
                 1f,  1f,  1f,        0f,  1f,  0f,
                 1f, -1f,  1f,        0f,  0f,  1f,
                -1f, -1f,  1f,        1f,  1f,  0f,
                -1f,  1f, -1f,        1f,  0f,  1f,
                 1f,  1f, -1f,        0f,  1f,  1f,
                 1f, -1f, -1f,        1f, .5f,  0f,
                -1f, -1f, -1f,        0f, .5f,  1f,
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
}