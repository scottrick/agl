package com.hatfat.agl;

import android.content.Context;

import com.hatfat.agl.app.AglApplication;
import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.component.ModifierComponent;
import com.hatfat.agl.component.RenderableComponent;
import com.hatfat.agl.component.Transform;
import com.hatfat.agl.entity.AglEntity;
import com.hatfat.agl.mesh.TestRenderableFactory;
import com.hatfat.agl.modifiers.SpinModifier;
import com.hatfat.agl.render.AglRenderable;
import com.hatfat.agl.util.AglRandom;
import com.hatfat.agl.util.Vec3;

import java.util.Random;

import javax.inject.Inject;

public class AglLoadingScene extends AglScene {

    @Inject AglRandom random;

    public AglLoadingScene(Context context) {
        super(context, true);

        AglApplication app = AglApplication.get(context);
        app.inject(this);
    }

    @Override protected void setupSceneGLWork(AglRenderer renderer) {
        super.setupSceneGLWork(renderer);

        AglEntity loadingEntity = new AglEntity("Loading Entity");

        AglRenderable loadingRenderable = TestRenderableFactory.createIcosahedronWireframe();

        RenderableComponent renderableComponent = new RenderableComponent(loadingRenderable);
        ModifierComponent modifierComponent = new ModifierComponent();
        Transform transform = new Transform();

        Random rand = random.get();
        Vec3 spinVec = new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        spinVec.normalize();

        modifierComponent.addModifier(new SpinModifier(300.0f, spinVec));

        loadingEntity.addComponent(modifierComponent);
        loadingEntity.addComponent(renderableComponent);
        loadingEntity.addComponent(transform);

        addEntity(loadingEntity);
    }
}
