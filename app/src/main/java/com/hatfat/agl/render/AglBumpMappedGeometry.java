package com.hatfat.agl.render;

import android.opengl.GLES20;

import com.hatfat.agl.AglDef;
import com.hatfat.agl.textures.AglTexture;
import com.hatfat.agl.util.Vec2;
import com.hatfat.agl.util.Vec3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class AglBumpMappedGeometry implements AglRenderable {

    private int vbo;
    private int ebo;

    private AglTexture texture;
    private AglTexture normalMap;
    private AglTexture specularMap;

    private int numElements;

    public AglBumpMappedGeometry(float vertices[], int numVertices, int elements[], int numElements,
            AglTexture texture, AglTexture normalMap, AglTexture specularMap) {

//        std::vector<glm::vec3> & tangents,
//        std::vector<glm::vec3> & bitangents

        //first we have to calculate our tangent and biTangent vectores

        int numFloatsPerVertex = 14;

        Vec3 v0 = new Vec3();
        Vec3 v1 = new Vec3();
        Vec3 v2 = new Vec3();

        Vec2 uv0 = new Vec2();
        Vec2 uv1 = new Vec2();
        Vec2 uv2 = new Vec2();

        Vec3 deltaPos1 = new Vec3();
        Vec3 deltaPos2 = new Vec3();

        Vec2 deltaUV1 = new Vec2();
        Vec2 deltaUV2 = new Vec2();

        Vec3 scratch1 = new Vec3();
        Vec3 scratch2 = new Vec3();

        Vec3 tangent = new Vec3();
        Vec3 bitangent = new Vec3();

        int elementIndex0;
        int elementIndex1;
        int elementIndex2;

        for (int i = 0; i < numElements; i += 3) {
            elementIndex0 = elements[i + 0];
            elementIndex1 = elements[i + 1];
            elementIndex2 = elements[i + 2];

            v0.x = vertices[(elementIndex0 * numFloatsPerVertex) + 0];
            v0.y = vertices[(elementIndex0 * numFloatsPerVertex) + 1];
            v0.z = vertices[(elementIndex0 * numFloatsPerVertex) + 2];

            v1.x = vertices[(elementIndex1 * numFloatsPerVertex) + 0];
            v1.y = vertices[(elementIndex1 * numFloatsPerVertex) + 1];
            v1.z = vertices[(elementIndex1 * numFloatsPerVertex) + 2];

            v2.x = vertices[(elementIndex2 * numFloatsPerVertex) + 0];
            v2.y = vertices[(elementIndex2 * numFloatsPerVertex) + 1];
            v2.z = vertices[(elementIndex2 * numFloatsPerVertex) + 2];

            uv0.x = vertices[(elementIndex0 * numFloatsPerVertex) + 3];
            uv0.y = vertices[(elementIndex0 * numFloatsPerVertex) + 4];

            uv1.x = vertices[(elementIndex1 * numFloatsPerVertex) + 3];
            uv1.y = vertices[(elementIndex1 * numFloatsPerVertex) + 4];

            uv2.x = vertices[(elementIndex2 * numFloatsPerVertex) + 3];
            uv2.y = vertices[(elementIndex2 * numFloatsPerVertex) + 4];

            deltaPos1.x = v1.x - v0.x;
            deltaPos1.y = v1.y - v0.y;
            deltaPos1.z = v1.z - v0.z;

            deltaPos2.x = v2.x - v0.x;
            deltaPos2.y = v2.y - v0.y;
            deltaPos2.z = v2.z - v0.z;

            deltaUV1.x = uv1.x - uv0.x;
            deltaUV1.y = uv1.y - uv0.y;

            deltaUV2.x = uv2.x - uv0.x;
            deltaUV2.y = uv2.y - uv0.y;

            float r = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV1.y * deltaUV2.x);

            //calculate the tangent
            scratch1.set(deltaPos1);
            scratch1.scale(deltaUV2.y);

            scratch2.set(deltaPos2);
            scratch2.scale(deltaUV1.y);

            tangent.x = scratch1.x - scratch2.x;
            tangent.y = scratch1.y - scratch2.y;
            tangent.z = scratch1.z - scratch2.z;
            tangent.scale(r);
            tangent.normalize();

            //calculate the bitangent
            scratch1.set(deltaPos2);
            scratch1.scale(deltaUV1.x);

            scratch2.set(deltaPos1);
            scratch2.scale(deltaUV2.x);

            bitangent.x = scratch1.x - scratch2.x;
            bitangent.y = scratch1.y - scratch2.y;
            bitangent.z = scratch1.z - scratch2.z;
            bitangent.scale(r);
            bitangent.normalize();

            //set the tangent data!
            vertices[(elementIndex0 * numFloatsPerVertex) + 8] = tangent.x;
            vertices[(elementIndex0 * numFloatsPerVertex) + 9] = tangent.y;
            vertices[(elementIndex0 * numFloatsPerVertex) + 10] = tangent.z;
            vertices[(elementIndex1 * numFloatsPerVertex) + 8] = tangent.x;
            vertices[(elementIndex1 * numFloatsPerVertex) + 9] = tangent.y;
            vertices[(elementIndex1 * numFloatsPerVertex) + 10] = tangent.z;
            vertices[(elementIndex2 * numFloatsPerVertex) + 8] = tangent.x;
            vertices[(elementIndex2 * numFloatsPerVertex) + 9] = tangent.y;
            vertices[(elementIndex2 * numFloatsPerVertex) + 10] = tangent.z;

            //set the bitangent data!
            vertices[(elementIndex0 * numFloatsPerVertex) + 11] = bitangent.x;
            vertices[(elementIndex0 * numFloatsPerVertex) + 12] = bitangent.y;
            vertices[(elementIndex0 * numFloatsPerVertex) + 13] = bitangent.z;
            vertices[(elementIndex1 * numFloatsPerVertex) + 11] = bitangent.x;
            vertices[(elementIndex1 * numFloatsPerVertex) + 12] = bitangent.y;
            vertices[(elementIndex1 * numFloatsPerVertex) + 13] = bitangent.z;
            vertices[(elementIndex2 * numFloatsPerVertex) + 11] = bitangent.x;
            vertices[(elementIndex2 * numFloatsPerVertex) + 12] = bitangent.y;
            vertices[(elementIndex2 * numFloatsPerVertex) + 13] = bitangent.z;
        }

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
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, AglDef.SIZEOF_FLOAT * numVertices * numFloatsPerVertex, vertexBuffer, GLES20.GL_STATIC_DRAW);

        //setup ebo
        this.numElements = numElements;
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, AglDef.SIZEOF_INT * numElements, indexBuffer, GLES20.GL_STATIC_DRAW);

        //store textures
        this.texture = texture;
        this.normalMap = normalMap;
        this.specularMap = specularMap;
    }

    @Override
    public void prepareRender(int shaderProgram) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ebo);

        int posAttrib = GLES20.glGetAttribLocation(shaderProgram, "position");
        GLES20.glEnableVertexAttribArray(posAttrib);
        GLES20.glVertexAttribPointer(posAttrib, 3, GLES20.GL_FLOAT, false,
                14 * AglDef.SIZEOF_FLOAT, 0);

        int textureAttrib = GLES20.glGetAttribLocation(shaderProgram, "texture");
        GLES20.glEnableVertexAttribArray(textureAttrib);
        GLES20.glVertexAttribPointer(textureAttrib, 2, GLES20.GL_FLOAT, false,
                14 * AglDef.SIZEOF_FLOAT, 3 * AglDef.SIZEOF_FLOAT);

        int normalAttrib = GLES20.glGetAttribLocation(shaderProgram, "normal");
        if (normalAttrib != -1) {
            GLES20.glEnableVertexAttribArray(normalAttrib);
            GLES20.glVertexAttribPointer(normalAttrib, 3, GLES20.GL_FLOAT, false,
                    14 * AglDef.SIZEOF_FLOAT, 5 * AglDef.SIZEOF_FLOAT);
        }

        int tangentAttrib = GLES20.glGetAttribLocation(shaderProgram, "tangent");
        if (tangentAttrib != -1) {
            GLES20.glEnableVertexAttribArray(tangentAttrib);
            GLES20.glVertexAttribPointer(tangentAttrib, 3, GLES20.GL_FLOAT, false,
                    14 * AglDef.SIZEOF_FLOAT, 8 * AglDef.SIZEOF_FLOAT);
        }

        int bitangentAttrib = GLES20.glGetAttribLocation(shaderProgram, "bitangent");
        if (bitangentAttrib != -1) {
            GLES20.glEnableVertexAttribArray(bitangentAttrib);
            GLES20.glVertexAttribPointer(bitangentAttrib, 3, GLES20.GL_FLOAT, false,
                    14 * AglDef.SIZEOF_FLOAT, 11 * AglDef.SIZEOF_FLOAT);
        }

        int textureSamplerUniform = GLES20.glGetUniformLocation(shaderProgram, "textureSampler");
        int normalSamplerUniform = GLES20.glGetUniformLocation(shaderProgram, "normalSampler");
        int specularSamplerUniform = GLES20.glGetUniformLocation(shaderProgram, "specularSampler");

        // Set the active texture unit to texture unit 0 and bind it.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getTexture());
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(textureSamplerUniform, 0);

        // Set the active texture unit to texture unit 1 and bind it.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, normalMap.getTexture());
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 1.
        GLES20.glUniform1i(normalSamplerUniform, 1);

        // Set the active texture unit to texture unit 2 and bind it.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, specularMap.getTexture());
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 2.
        GLES20.glUniform1i(specularSamplerUniform, 2);
    }

    @Override
    public void render() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numElements, GLES20.GL_UNSIGNED_INT, 0);
    }

    @Override public void cleanupRender() {

    }

    @Override public String getShaderProgramName() {
        return "shaders/bumpMappedTexture";
    }
}
