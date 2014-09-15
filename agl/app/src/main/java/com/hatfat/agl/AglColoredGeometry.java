package com.hatfat.agl;

import android.opengl.GLES20;

import com.hatfat.agl.shaders.AglShaderManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class AglColoredGeometry implements AglRenderable {

    private int shaderProgram;

    private int vbo;
    private int ebo;

    private int numElements;

    public AglColoredGeometry(float vertices[], int numVertices, short elements[], int numElements) {
        shaderProgram = AglShaderManager.get().getShaderProgram("shaders/coloredGeometry").getShaderProgram();

        ByteBuffer vbb  = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer ibb = ByteBuffer.allocateDirect(elements.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(elements);
        indexBuffer.position(0);

        int buffers[] = new int[2];

        GLES20.glGenBuffers(2, buffers, 0);

        vbo = buffers[0];
        ebo = buffers[1];

        //setup the vertex and element index buffers
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, AglDef.SIZEOF_FLOAT * numVertices * 6, vertexBuffer, GLES20.GL_STATIC_DRAW);

        //setup ebo
        this.numElements = numElements;
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, AglDef.SIZEOF_SHORT * numElements, indexBuffer, GLES20.GL_STATIC_DRAW);
    }

    @Override
    public void prepareRender() {

    }

    @Override
    public void render() {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ebo);

        int posAttrib = GLES20.glGetAttribLocation(shaderProgram, "position");
        GLES20.glEnableVertexAttribArray(posAttrib);
        GLES20.glVertexAttribPointer(posAttrib, 3, GLES20.GL_FLOAT, false, 6 * AglDef.SIZEOF_FLOAT, 0);

        int colorAttrib = GLES20.glGetAttribLocation(shaderProgram, "color");
        GLES20.glEnableVertexAttribArray(colorAttrib);
        GLES20.glVertexAttribPointer(colorAttrib, 3, GLES20.GL_FLOAT, false, 6 * AglDef.SIZEOF_FLOAT, 3 * AglDef.SIZEOF_FLOAT);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numElements, GLES20.GL_UNSIGNED_SHORT, 0);
    }

    @Override
    public int getShaderProgram() {
        return shaderProgram;
    }
}
