package com.hatfat.agl;

import android.content.Context;

import com.hatfat.agl.app.AglApplication;
import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.mesh.TestRenderableFactory;
import com.hatfat.agl.modifiers.SpinModifier;
import com.hatfat.agl.render.AglRenderable;
import com.hatfat.agl.util.AglRandom;
import com.hatfat.agl.util.Vec3;

import java.util.Random;

import javax.inject.Inject;

public class AglLoadingScene extends AglScene {

    @Inject AglRandom random;

    private AglNode loadingNode = null;

    public AglLoadingScene(Context context) {
        super(context);

        AglApplication app = AglApplication.get(context);
        app.inject(this);

        AglCamera camera = new AglPerspectiveCamera(
                new Vec3(0.0f, 0.0f, 6.0f),
                new Vec3(0.0f, 0.0f, 0.0f),
                new Vec3(0.0f, 1.0f, 0.0f),
                60.0f, 1.0f, 0.1f, 100.0f);

        setCamera(camera);

        Vec3 newLightDir = new Vec3(0.1f, 0.1f, 1.0f);
        newLightDir.normalize();
        getGlobalLight().lightDir = newLightDir;
    }

    @Override protected void setupSceneGLWork(AglRenderer renderer) {
        super.setupSceneGLWork(renderer);

        AglRenderable loadingRenderable = TestRenderableFactory.createIcosahedronWireframe();
        loadingNode = new AglNode(new Vec3(0.0f, 0.0f, 0.0f), loadingRenderable);

        Random rand = random.get();
        Vec3 spinVec = new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        spinVec.normalize();

        loadingNode.addModifier(new SpinModifier(300.0f, spinVec));

        addNode(loadingNode);
    }
}
