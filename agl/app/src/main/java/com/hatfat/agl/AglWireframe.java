package com.hatfat.agl;

import android.opengl.GLES20;

import com.hatfat.agl.util.Color;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class AglWireframe implements AglRenderable {

    private int vbo;
    private int ebo;

    private int numElements;

    private float lineWidth = 1.0f;

    //hold on to update vertex information until we can update it (has to happen on gl thread)
    private FloatBuffer updateVertexBuffer;

    //default wireframe color
    private Color wireframeColor = new Color(0.9f, 0.9f, 0.0f, 1.0f);

    public AglWireframe(float vertices[], int numVertices, int elements[], int numElements) {
        ByteBuffer vbb  = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer ibb = ByteBuffer.allocateDirect(elements.length * 4);
        ibb.order(ByteOrder.nativeOrder());
        IntBuffer indexBuffer = ibb.asIntBuffer();
        indexBuffer.put(elements);
        indexBuffer.position(0);

        int buffers[] = new int[2];

        GLES20.glGenBuffers(2, buffers, 0);

        vbo = buffers[0];
        ebo = buffers[1];

        //setup the vbo buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, AglDef.SIZEOF_FLOAT * numVertices * 3, vertexBuffer, GLES20.GL_STATIC_DRAW);

        //setup ebo
        this.numElements = numElements;
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, AglDef.SIZEOF_INT * numElements, indexBuffer, GLES20.GL_STATIC_DRAW);

        float[] lineWidth = new float[2];
        GLES20.glGetFloatv(GLES20.GL_ALIASED_LINE_WIDTH_RANGE, lineWidth, 0);

        //use lineWidth 2.0f if supported
        this.lineWidth = Math.min(lineWidth[1], 2.0f);
    }

    @Override
    public void prepareRender(int shaderProgram) {
        if (verticesNeedUpdate()) {
            updateVerticesWork();
        }

        GLES20.glLineWidth(lineWidth);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ebo);

        int posAttrib = GLES20.glGetAttribLocation(shaderProgram, "position");
        GLES20.glEnableVertexAttribArray(posAttrib);
        GLES20.glVertexAttribPointer(posAttrib, 3, GLES20.GL_FLOAT, false, 3 * AglDef.SIZEOF_FLOAT, 0);

        int wireframeColorUniformLocation = GLES20.glGetUniformLocation(shaderProgram, "color");
        GLES20.glUniform4f(wireframeColorUniformLocation, wireframeColor.r, wireframeColor.g, wireframeColor.b, wireframeColor.a);
    }

    @Override
    public void render() {
        GLES20.glDrawElements(GLES20.GL_LINES, numElements, GLES20.GL_UNSIGNED_INT, 0);
    }

    @Override
    public void cleanupRender() {
        GLES20.glLineWidth(1.0f);
    }

    public void updateWithVertices(float vertices[], int numVertices) {
        ByteBuffer updateByteBuffer  = ByteBuffer.allocateDirect(vertices.length * 4);
        updateByteBuffer.order(ByteOrder.nativeOrder());
        updateVertexBuffer = updateByteBuffer.asFloatBuffer();
        updateVertexBuffer.put(vertices);
        updateVertexBuffer.position(0);
    }

    private void updateVerticesWork() {
        //bind the vbo buffer so we can update it
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, AglDef.SIZEOF_FLOAT * updateVertexBuffer.capacity(),
                updateVertexBuffer);

        updateVertexBuffer = null;
    }

    private boolean verticesNeedUpdate() {
        return updateVertexBuffer != null;
    }

    public void setWireframeColor(Color wireframeColor) {
        this.wireframeColor = wireframeColor;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public String getShaderProgramName() {
        return "shaders/wireframe";
    }
}
