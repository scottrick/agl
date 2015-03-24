package com.hatfat.agl;

import android.content.Context;
import android.opengl.GLES20;
import android.os.AsyncTask;
import android.util.Log;

import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.util.Color;
import com.hatfat.agl.util.Matrix;
import com.hatfat.agl.util.Vec3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by scottrick on 7/31/14.
 */
public class AglScene implements AglUpdateable {

    public enum SceneState {
        NOT_SETUP,
        SETTING_UP_BACKGROUND,
        READY_FOR_GL_SETUP,
        SETTING_UP_GL_THREAD,
        READY_FOR_RENDER,
    }

    protected AglCamera camera;
    protected HashMap<AglRenderable, List<AglNode>> renderableHashMap;

    protected int numNodes = 0;
    protected int maxNumNodes = 5 * 1000;
    protected AglNode[] nodes;

    protected AglPointLight globalLight;

    private SceneState sceneState = SceneState.NOT_SETUP;

    private Context context;

    //scratch
    protected int updateInt = 0;

    public AglScene(Context context) {
        this.context = context;

        nodes = new AglNode[maxNumNodes];
        renderableHashMap = new HashMap<>();

        camera = new AglPerspectiveCamera(
                new Vec3(0.0f, 0.0f, 10.0f),
                new Vec3(0.0f, 0.0f, 0.0f),
                new Vec3(0.0f, 1.0f, 0.0f),
                60.0f, 1.0f, 0.1f, 1000.0f);

        globalLight = new AglPointLight(new Vec3(0.0f, 0.0f, 1.0f), new Color(1.0f, 1.0f, 1.0f, 1.0f));
    }

    public final void setupSceneBackground() {
        sceneState = SceneState.SETTING_UP_BACKGROUND;
        final long setupStartTime = System.currentTimeMillis();

        new AsyncTask<Void, Void, Void>() {
            @Override protected Void doInBackground(Void... params) {
                setupSceneBackgroundWork();
                return null;
            }

            @Override protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                long setupEndTime = System.currentTimeMillis();
                Log.i("AglScene", AglScene.this.getClass().getSimpleName() + " background setup took " + (setupEndTime - setupStartTime) + " milliseconds.");

                sceneState = SceneState.READY_FOR_GL_SETUP;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected void setupSceneBackgroundWork() {
        //override in subclass
    }

    public final void setupSceneGL() {
        sceneState = SceneState.SETTING_UP_GL_THREAD;
        final long setupStartTime = System.currentTimeMillis();

        setupSceneGLWork();

        long setupEndTime = System.currentTimeMillis();
        Log.i("AglScene", AglScene.this.getClass().getSimpleName() + " GL setup took " + (setupEndTime - setupStartTime) + " milliseconds.");

        sceneState = SceneState.READY_FOR_RENDER;
    }

    protected void setupSceneGLWork() {
        //override in subclass
    }

    public void destroyScene() {
        removeAllNodes();

        //update the scene state;
        sceneState = SceneState.NOT_SETUP;
    }

    public void addNodes(List<AglNode> nodes) {
        for (AglNode node : nodes) {
            addNode(node);
        }
    }

    public void addNode(AglNode node) {
        if (numNodes + 1 >= maxNumNodes) {
            throw new RuntimeException("Can't add more nodes!");
        }

        nodes[numNodes] = node;
        numNodes++;

        addNodeToRenderableHashMap(node);
    }

    public void removeNode(AglNode node) {
        int modifierIndex = -1;

        for (int i = 0; i < numNodes; i++) {
            if (nodes[i] == node) {
                modifierIndex = i;
                break;
            }
        }

        if (modifierIndex < 0) {
            //modifier was not on this node!
            return;
        }

        nodes[modifierIndex] = nodes[numNodes - 1]; //swap the last modifier with the modifier we are removing

        numNodes--;

        nodes[numNodes] = null;

        removeNodeFromRenderableHashMap(node);
    }

    private void addNodeToRenderableHashMap(AglNode node) {
        //update the renderables hashmap
        List<AglNode> renderableNodeList = renderableHashMap.get(node.getRenderable());

        if (renderableNodeList == null) {
            renderableNodeList = new LinkedList<>();
            renderableHashMap.put(node.getRenderable(), renderableNodeList);
        }

        renderableNodeList.add(node);
    }

    private void removeNodeFromRenderableHashMap(AglNode node) {
        //update the renderables hashmap
        List<AglNode> renderableNodeList = renderableHashMap.get(node.getRenderable());

        if (renderableNodeList == null) {
            throw new RuntimeException("shouldn't ever happen...");
        }

        renderableNodeList.remove(node);

        if (renderableNodeList.size() <= 0) {
            //no nodes with this type of renderable left, so we can remove this list
            renderableHashMap.remove(node.getRenderable());
        }
    }

    protected void removeAllNodes() {
        for (int i = 0; i < numNodes; i++) {
            nodes[i] = null;
        }

        numNodes = 0;
        renderableHashMap.clear();
    }

    public void render(AglRenderer renderer) {
        int activeProgram = 0;
        float viewMatrix[];
        float projMatrix[];
        Matrix modelMatrix = new Matrix();

        int projectionUniformLocation = 0;
        int viewUniformLocation = 0;
        int modelUniformLocation = 0;
        int lightDirUniformLocation = 0;
        int lightColorUniformLocation = 0;

        for (AglRenderable currentRenderable : renderableHashMap.keySet()) {
            //setup for the current renderable
            int nextProgram = renderer.getShaderManager().getShaderProgram(currentRenderable.getShaderProgramName()).getShaderProgram();

            if (nextProgram != activeProgram) {
                //need to load a new program
                activeProgram = nextProgram;
                GLES20.glUseProgram(activeProgram);

                projectionUniformLocation = GLES20.glGetUniformLocation(activeProgram, "proj");
                viewUniformLocation = GLES20.glGetUniformLocation(activeProgram, "view");
                modelUniformLocation = GLES20.glGetUniformLocation(activeProgram, "model");
                lightDirUniformLocation = GLES20.glGetUniformLocation(activeProgram, "lightDir");
                lightColorUniformLocation = GLES20.glGetUniformLocation(activeProgram, "lightColor");

                viewMatrix = getCamera().getViewMatrix();
                projMatrix = getCamera().getProjMatrix();

                GLES20.glUniformMatrix4fv(projectionUniformLocation, 1, false, projMatrix, 0);
                GLES20.glUniformMatrix4fv(viewUniformLocation, 1, false, viewMatrix, 0);
                GLES20.glUniform3f(lightDirUniformLocation, globalLight.lightDir.x, globalLight.lightDir.y, globalLight.lightDir.z);
                GLES20.glUniform4f(lightColorUniformLocation, globalLight.lightColor.r, globalLight.lightColor.g, globalLight.lightColor.b, globalLight.lightColor.a);
            }

            currentRenderable.prepareRender(activeProgram);

            List<AglNode> currentNodes = renderableHashMap.get(currentRenderable);
            for (AglNode node : currentNodes) {
                //render each node of this renderable type
                if (!node.shouldRender) {
                    continue;
                }

                node.getModelMatrix(modelMatrix);
                GLES20.glUniformMatrix4fv(modelUniformLocation, 1, false, modelMatrix.m, 0);

                currentRenderable.render();
            }

            currentRenderable.cleanupRender();
        }

        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("BasicScene", "glError: " + error);
        }
    }

    @Override
    public void update(float time, float deltaTime) {
        for (updateInt = 0; updateInt < numNodes; updateInt++) {
            nodes[updateInt].update(time, deltaTime);
        }
    }

    protected void setCamera(AglCamera camera) {
        this.camera = camera;
    }

    public AglCamera getCamera() {
        return camera;
    }

    public AglPointLight getGlobalLight() {
        return globalLight;
    }

    protected Context getContext() {
        return context;
    }

    public SceneState getSceneState() {
        return sceneState;
    }
}
