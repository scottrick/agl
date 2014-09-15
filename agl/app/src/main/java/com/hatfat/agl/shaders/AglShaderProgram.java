package com.hatfat.agl.shaders;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Created by scottrick on 8/8/14.
 */
public class AglShaderProgram {

    private int shaderProgram;

    private AglShader vertShader;
    private AglShader fragShader;

    public AglShaderProgram(String vertShaderFilename, String fragShaderFilename, Context context) {
        vertShader = new AglShader(vertShaderFilename, context);
        fragShader = new AglShader(fragShaderFilename, context);

        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertShader.getShader());
        GLES20.glAttachShader(shaderProgram, fragShader.getShader());
        GLES20.glLinkProgram(shaderProgram);
    }

    public int getShaderProgram() {
        return shaderProgram;
    }

    void destroy() {
        GLES20.glDeleteProgram(shaderProgram);
        shaderProgram = 0;

        vertShader.destroy();
        vertShader = null;

        fragShader.destroy();
        fragShader = null;
    }
}
