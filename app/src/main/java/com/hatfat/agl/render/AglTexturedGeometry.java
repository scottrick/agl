package com.hatfat.agl.render;

import android.opengl.GLES20;

import com.hatfat.agl.AglDef;
import com.hatfat.agl.textures.AglTexture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class AglTexturedGeometry implements AglRenderable {

    private int vbo;
    private int ebo;

    private boolean isLit;
    private AglTexture texture;

    private int numElements;

    public AglTexturedGeometry(float vertices[], int numVertices, int elements[], int numElements,
            AglTexture texture, boolean isLit) {
        this.isLit = isLit;

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
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
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, AglDef.SIZEOF_FLOAT * numVertices * 8, vertexBuffer, GLES20.GL_STATIC_DRAW);

        //setup ebo
        this.numElements = numElements;
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, AglDef.SIZEOF_INT * numElements, indexBuffer, GLES20.GL_STATIC_DRAW);

        //store texture
        this.texture = texture;
    }

    @Override
    public void prepareRender(int shaderProgram) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ebo);

        int posAttrib = GLES20.glGetAttribLocation(shaderProgram, "position");
        GLES20.glEnableVertexAttribArray(posAttrib);
        GLES20.glVertexAttribPointer(posAttrib, 3, GLES20.GL_FLOAT, false, 8 * AglDef.SIZEOF_FLOAT, 0);

        int textureAttrib = GLES20.glGetAttribLocation(shaderProgram, "texture");
        GLES20.glEnableVertexAttribArray(textureAttrib);
        GLES20.glVertexAttribPointer(textureAttrib, 2, GLES20.GL_FLOAT, false, 8 * AglDef.SIZEOF_FLOAT, 3 * AglDef.SIZEOF_FLOAT);

        int normalAttrib = GLES20.glGetAttribLocation(shaderProgram, "normal");
        GLES20.glEnableVertexAttribArray(normalAttrib);
        GLES20.glVertexAttribPointer(normalAttrib, 3, GLES20.GL_FLOAT, false, 8 * AglDef.SIZEOF_FLOAT, 5 * AglDef.SIZEOF_FLOAT);

        int textureSamplerUniform = GLES20.glGetUniformLocation(shaderProgram, "textureSampler");

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getTexture());

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(textureSamplerUniform, 0);
    }

    @Override
    public void render() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numElements, GLES20.GL_UNSIGNED_INT, 0);
    }

    @Override public void cleanupRender() {

    }

    @Override public String getShaderProgramName() {
        if (isLit) {
            return "shaders/basicTextureWithLighting";
        }
        else {
            return "shaders/basicTexture";
        }
    }
}
