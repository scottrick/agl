package com.hatfat.agl.shaders;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by scottrick on 8/7/14.
 */
public class AglShader {

    private int shader;
    private String filename;

    public AglShader(String filename, Context context) {
        this.filename = filename;

        compile(context);
    }

    void destroy() {
        GLES20.glDeleteShader(shader);
        shader = 0;
    }

    void compile(final Context context) {
        if (shader != 0) {
            Log.e("AglShader", "Compile error: shader is already compiled.");
            return;
        }

        StringBuffer textBuffer = new StringBuffer();

        try {
            InputStream inputStream = context.getAssets().open(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = reader.readLine()) != null) {
                textBuffer.append(line).append("\n");
            }
        }
        catch (IOException e) {
            Log.e("AglShader", "Compile error: " + e.getMessage());
        }

        String shaderText = textBuffer.toString();

        if (filename.endsWith(".vert")) {
            shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        }
        else if (filename.endsWith(".frag")) {
            shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        }
        else {
            Log.e("AglShader", "Compile error: invalid shader extension.");
        }

        GLES20.glShaderSource(shader, shaderText);
        GLES20.glCompileShader(shader);

        int status[] = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);

        if (status[0] == GLES20.GL_TRUE) {
            Log.d("AglShader", filename + " compiled successfully.");
        }
        else {
            //error
            String errorMessage = GLES20.glGetShaderInfoLog(shader);
            Log.e("AglShader", "ERROR compiling \"" + filename + "\":\n" + errorMessage);
        }
    }

    public int getShader() {
        return shader;
    }

    public String getFilename() {
        return filename;
    }
}
