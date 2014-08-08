package com.hatfat.agl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by scottrick on 7/31/14.
 */
public class AglRenderer implements GLSurfaceView.Renderer {

    private AglScene scene;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.8f, 0.0f, 1.0f);

        dumpInfo();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if (scene != null) {
            scene.render();
        }
    }

    public void setScene(AglScene scene) {
        this.scene = scene;
    }

    private void dumpInfo()
    {
        String version = GLES20.glGetString(GLES20.GL_VERSION);
        String renderer = GLES20.glGetString(GLES20.GL_RENDERER);
        String vendor = GLES20.glGetString(GLES20.GL_VENDOR);
        String shaderLang = GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION);

        Log.i("AglRenderer", "GL_VERSION: " + version);
        Log.i("AglRenderer", "GL_RENDERER: " + renderer);
        Log.i("AglRenderer", "GL_VENDOR: " + vendor);
        Log.i("AglRenderer", "GL_SHADERLANG: " + shaderLang);
    }
}
