package com.hatfat.agl;

import android.content.Context;
import android.opengl.GLES20;
import android.os.AsyncTask;
import android.util.Log;

import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.component.LightComponent;
import com.hatfat.agl.component.RenderableComponent;
import com.hatfat.agl.component.transform.Transform;
import com.hatfat.agl.component.camera.CameraComponent;
import com.hatfat.agl.component.camera.PerspectiveCameraComponent;
import com.hatfat.agl.entity.AglEntity;
import com.hatfat.agl.render.AglRenderable;
import com.hatfat.agl.system.AglSystem;
import com.hatfat.agl.system.CameraSystem;
import com.hatfat.agl.system.TransformModifierSystem;
import com.hatfat.agl.util.Matrix;
import com.hatfat.agl.util.Vec3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AglScene {

    public enum SceneState {
        NOT_SETUP,
        SETTING_UP_BACKGROUND,
        READY_FOR_GL_SETUP,
        SETTING_UP_GL_THREAD,
        READY_FOR_RENDER,
    }

    protected HashMap<AglRenderable, List<AglEntity>> renderableHashMap;

    /* entity management */
    private int numEntities    = 0;
    private int maxNumEntities = 5 * 1000;
    private final AglEntity[] entities;

    /* entity hashmap */
    private HashMap<Integer, AglEntity> entityHashMap;
    private int nextEntityId = 1; //start at 1;

    /* systems that are used in this scene */
    private List<AglSystem> systems = new LinkedList<>();

    //so we can quickly grab the camera system
    private CameraSystem cameraSystem;

    /* just keep track of the global light here for now */
    protected AglEntity globalLightEntity;

    /* scene state info */
    protected boolean isPaused;
    private SceneState sceneState = SceneState.NOT_SETUP;

    private Context context;

    public AglScene(Context context, boolean shouldAddDefaultCamera) {
        this.context = context;

        systems.add(new TransformModifierSystem());
        systems.add(cameraSystem = new CameraSystem());

        entities = new AglEntity[maxNumEntities];
        renderableHashMap = new HashMap<>();
        entityHashMap = new HashMap<>();

        if (shouldAddDefaultCamera) {
            addDefaultCamera();
        }

        globalLightEntity = new AglEntity("default global light");
        globalLightEntity.addComponent(new LightComponent());
        globalLightEntity.addComponent(new Transform(new Vec3(0.0f, 0.0f, 1.0f)));
        addEntity(globalLightEntity);
    }

    private void addDefaultCamera() {
        AglEntity cameraEntity = new AglEntity("Default Perspective Camera");
        cameraEntity.addComponent(new PerspectiveCameraComponent(
                new Vec3(0.0f, 0.0f, 10.0f),
                new Vec3(0.0f, 0.0f, 0.0f),
                new Vec3(0.0f, 1.0f, 0.0f),
                60.0f, 1.0f, 0.1f, 1000.0f));

        addCameraEntity(cameraEntity);
    }

    public final void setupSceneBackground() {
        sceneState = SceneState.SETTING_UP_BACKGROUND;
        final long setupStartTime = java.lang.System.currentTimeMillis();

        new AsyncTask<Void, Void, Void>() {
            @Override protected Void doInBackground(Void... params) {
                setupSceneBackgroundWork();
                return null;
            }

            @Override protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                long setupEndTime = java.lang.System.currentTimeMillis();
                Log.i("AglScene", AglScene.this.getClass().getSimpleName() + " background setup took " + (setupEndTime - setupStartTime) + " milliseconds.");

                sceneState = SceneState.READY_FOR_GL_SETUP;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected void setupSceneBackgroundWork() {
        //override in subclass
    }

    public final void setupSceneGL(AglRenderer renderer) {
        sceneState = SceneState.SETTING_UP_GL_THREAD;
        final long setupStartTime = java.lang.System.currentTimeMillis();

        setupSceneGLWork(renderer);

        long setupEndTime = java.lang.System.currentTimeMillis();
        Log.i("AglScene", AglScene.this.getClass().getSimpleName() + " GL setup took " + (setupEndTime - setupStartTime) + " milliseconds.");

        sceneState = SceneState.READY_FOR_RENDER;
    }

    protected void setupSceneGLWork(AglRenderer renderer) {
        //override in subclass
    }

    public void destroyScene(AglRenderer renderer) {
        removeAllEntities();

        //update the scene state;
        sceneState = SceneState.NOT_SETUP;
    }

    public void addEntities(List<AglEntity> entities) {
        for (AglEntity entity : entities) {
            addEntity(entity);
        }
    }

    public void addEntity(AglEntity entity) {
        if (numEntities + 1 >= maxNumEntities) {
            throw new RuntimeException("Can't add more entities!");
        }

        entities[numEntities] = entity;
        numEntities++;

        addEntityToRenderableHashMap(entity);
        addEntityToEntityHashMap(entity);
    }

    public void removeEntity(AglEntity entity) {
        int modifierIndex = -1;

        for (int i = 0; i < numEntities; i++) {
            if (entities[i] == entity) {
                modifierIndex = i;
                break;
            }
        }

        if (modifierIndex < 0) {
            //modifier was not on this entity!
            return;
        }

        entities[modifierIndex] = entities[numEntities - 1]; //swap the last modifier with the modifier we are removing

        numEntities--;

        entities[numEntities] = null;

        removeEntityFromRenderableHashMap(entity);
        removeEntityFromEntityHashMap(entity);
    }

    private void addEntityToEntityHashMap(AglEntity entity) {
        entity.entityId = nextEntityId;
        nextEntityId++;

        //add to the hashMap
        entityHashMap.put(entity.entityId, entity);
    }

    private void removeEntityFromEntityHashMap(AglEntity entity) {
        entity.entityId = 0;

        //remove from the hashmap!
        entityHashMap.remove(entity.entityId);
    }

    private void addEntityToRenderableHashMap(AglEntity entity) {
        List<RenderableComponent> renderableComponents = entity.getComponentsByType(
                ComponentType.RENDERABLE);

        //update the renderables hashmap for each renderable component
        for (RenderableComponent renderableComponent : renderableComponents) {
            List<AglEntity> renderableEntityList = renderableHashMap.get(renderableComponent.getRenderable());

            if (renderableEntityList == null) {
                renderableEntityList = new LinkedList<>();
                renderableHashMap.put(renderableComponent.getRenderable(), renderableEntityList);
            }

            renderableEntityList.add(entity);
        }
    }

    private void removeEntityFromRenderableHashMap(AglEntity entity) {
        List<RenderableComponent> renderableComponents = entity.getComponentsByType(
                ComponentType.RENDERABLE);

        for (RenderableComponent renderableComponent : renderableComponents) {
            //update the renderables hashmap
            List<AglEntity> renderableEntityList = renderableHashMap
                    .get(renderableComponent.getRenderable());

            if (renderableEntityList == null) {
                throw new RuntimeException("shouldn't ever happen...");
            }

            renderableEntityList.remove(entity);

            if (renderableEntityList.size() <= 0) {
                //no entities with this type of renderable left, so we can remove this list
                renderableHashMap.remove(renderableComponent.getRenderable());
            }
        }
    }

    protected void removeAllEntities() {
        for (int i = 0; i < numEntities; i++) {
            entities[i] = null;
        }

        numEntities = 0;
        renderableHashMap.clear();
    }

    public void render(AglRenderer renderer) {
        int activeProgram = 0;
        float viewMatrix[];
        float projMatrix[];
        Matrix modelMatrix = new Matrix();

        LightComponent lightComponent = globalLightEntity.getComponentByType(ComponentType.LIGHT);
        Transform lightTransform = globalLightEntity.getComponentByType(ComponentType.TRANSFORM);

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
                GLES20.glUniform4f(lightColorUniformLocation, lightComponent.lightColor.r, lightComponent.lightColor.g, lightComponent.lightColor.b, lightComponent.lightColor.a);
            }

            currentRenderable.prepareRender(activeProgram);

            List<AglEntity> currentEntities = renderableHashMap.get(currentRenderable);
            for (AglEntity entity : currentEntities) {
                List<RenderableComponent> renderableComponents = entity.getComponentsByType(
                        ComponentType.RENDERABLE);
                Transform transform = entity.getComponentByType(ComponentType.TRANSFORM);

                for (RenderableComponent renderableComponent : renderableComponents) {
                    if (!renderableComponent.getRenderable().equals(currentRenderable)) {
                        continue;
                    }

                    if (renderableComponent == null || transform == null) {
                        //entity doesn't have required components to render
                        continue;
                    }

                    if (!renderableComponent.shouldRender()) {
                        continue;
                    }

                    Vec3 lightTransformAbsPos = lightTransform.getAbsolutePos(this);
                    Vec3 transformAbsPos = transform.getAbsolutePos(this);

                    GLES20.glUniform3f(lightDirUniformLocation,
                            lightTransformAbsPos.x - transformAbsPos.x,
                            lightTransformAbsPos.y - transformAbsPos.y,
                            lightTransformAbsPos.z - transformAbsPos.z);

                    //render each entity of this renderable type
                    transform.getModelMatrix(this, modelMatrix);
                    GLES20.glUniformMatrix4fv(modelUniformLocation, 1, false, modelMatrix.m, 0);

                    currentRenderable.render();
                }
            }

            currentRenderable.cleanupRender();
        }

        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("AglScene", "glError: " + error);
        }
    }

    public void update(float time, float deltaTime) {
        if (isPaused) {
            deltaTime = 0.0f;
        }

        for (AglSystem system : systems) {
            system.updateSystem(this, deltaTime);
        }
    }

    public boolean isReadyToRender() {
        return getSceneState() == SceneState.READY_FOR_RENDER;
    }

    public void addCameraEntity(AglEntity cameraEntity) {
        addEntity(cameraEntity);
        cameraSystem.setActiveCameraEntity(cameraEntity);
    }

    public CameraComponent getCamera() {
        return cameraSystem.getActiveCameraComponent();
    }

    public AglEntity getGlobalLight() {
        return globalLightEntity;
    }

    protected Context getContext() {
        return context;
    }

    public SceneState getSceneState() {
        return sceneState;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void pause() {
        isPaused = true;
    }

    public void unpause() {
        isPaused = false;
    }

    public int getNumEntities() {
        return numEntities;
    }

    public AglEntity[] getEntities() {
        return entities;
    }

    public AglEntity getEntityById(int entityId) {
        return entityHashMap.get(entityId);
    }
}
