package com.hatfat.agl.app;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;

import com.hatfat.agl.AglLoadingScene;
import com.hatfat.agl.AglScene;
import com.hatfat.agl.events.AglFpsUpdatedEvent;
import com.hatfat.agl.shaders.AglShaderManager;
import com.hatfat.agl.textures.AglTextureManager;
import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by scottrick on 7/31/14.
 */
public class AglRenderer implements GLSurfaceView.Renderer {

    @Inject
    Bus bus;

    @Inject
    Handler mainHandler;

    private AglShaderManager shaderManager;
    private AglTextureManager textureManager;

    private AglScene loadingScene;
    private AglScene currentScene;
    private AglScene nextScene;

    private int surfaceWidth;
    private int surfaceHeight;

    private long gameTime = 0;
    private long currentTime = 0;
    private long previousTime = 0;
    private long timeAccumulator = 0;
    private long timeDiff = 0;

    private int framesPerSecond = 40;
    private long milliPerFrame = 1000 / framesPerSecond;
    private float secondsPerFrame = 1.0f / (float)framesPerSecond;

    private int totalFpsCount = 0;
    private float avgFps = 0.0f;
    private int currentFpsCount = 0;
    private int previousFpsCount = 0;
    private long frameAccumulator = 0;

    public AglRenderer(Context context) {
        this.shaderManager = new AglShaderManager(context);
        this.textureManager = new AglTextureManager(context);

        //default scene
        this.currentScene = new AglScene(context);
        this.loadingScene = new AglLoadingScene(context);

        AglApplication.get(context).inject(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        currentTime = System.currentTimeMillis();

        dumpInfo();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        surfaceWidth = width;
        surfaceHeight = height;

        updateCameraAspectRatio();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        doSceneManagement();

        if (currentScene != null) {
            previousTime = currentTime;
            currentTime = System.currentTimeMillis();

            timeDiff = currentTime - previousTime;

            gameTime += timeDiff;
            timeAccumulator += timeDiff;

            while (timeAccumulator >= milliPerFrame) {
                currentScene.update(0.0f, secondsPerFrame);
                loadingScene.update(0.0f, secondsPerFrame);
                timeAccumulator -= milliPerFrame;
            }

            frameAccumulator += timeDiff;
            currentFpsCount++;
            totalFpsCount++;

            if (frameAccumulator >= 1000) {
                frameAccumulator -= 1000;

                previousFpsCount = currentFpsCount;
                currentFpsCount = 0;

                avgFps = totalFpsCount / (gameTime / 1000);

                notifyFpsChanged();
            }

            if (currentScene.isReadyToRender()) {
                currentScene.render(this);
            }
            else {
                loadingScene.render(this);
            }
        }
    }

    private void notifyFpsChanged() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                bus.post(new AglFpsUpdatedEvent(previousFpsCount));
            }
        });
    }

    public void setNextScene(AglScene scene) {
        this.nextScene = scene;
    }

    //call only from the GL thread
    private void doSceneManagement() {
        if (nextScene != null) {
            //we are changing to a new scene
            if (currentScene != null && currentScene != nextScene) {
                currentScene.destroyScene(this);
                currentScene = null;
            }

            currentScene = nextScene;
            nextScene = null;
        }

        //if the current scene needs to be setup, lets do it
        handleSceneSetup(currentScene);
        handleSceneSetup(loadingScene);

        updateCameraAspectRatio();
    }

    private void handleSceneSetup(AglScene scene) {
        if (scene != null) {
            switch (scene.getSceneState()) {
                case NOT_SETUP:
                    scene.setupSceneBackground();
                    break;
                case READY_FOR_GL_SETUP:
                    scene.setupSceneGL(this);
                    break;
                default:
                    //nothing to setup!
                    break;
            }
        }
    }

    private void updateCameraAspectRatio() {
        if (currentScene != null) {
            currentScene.getCamera().setAspectRatio((float) surfaceWidth / (float) surfaceHeight);
        }
        if (loadingScene != null) {
            loadingScene.getCamera().setAspectRatio((float) surfaceWidth / (float) surfaceHeight);
        }
    }

    public AglShaderManager getShaderManager() {
        return shaderManager;
    }

    public AglTextureManager getTextureManager() {
        return textureManager;
    }

    public AglScene getScene() {
        return currentScene;
    }

    private void dumpInfo() {
        String version = GLES20.glGetString(GLES20.GL_VERSION);
        String renderer = GLES20.glGetString(GLES20.GL_RENDERER);
        String vendor = GLES20.glGetString(GLES20.GL_VENDOR);
        String shaderLang = GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION);
        String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);

        Log.i("AglRenderer", "GL_VERSION: " + version);
        Log.i("AglRenderer", "GL_RENDERER: " + renderer);
        Log.i("AglRenderer", "GL_VENDOR: " + vendor);
        Log.i("AglRenderer", "GL_SHADERLANG: " + shaderLang);
        Log.i("AglRenderer", "GL_EXTENSIONS: " + extensions);
    }

    public void aglResume() {

    }

    public void aglPause() {
        if (currentScene != null) {
            currentScene.destroyScene(this);
        }

        if (loadingScene != null) {
            loadingScene.destroyScene(this);
        }

        shaderManager.clearPrograms();
        textureManager.clearTextures();
    }

    public int getFps() {
        return previousFpsCount;
    }
}
