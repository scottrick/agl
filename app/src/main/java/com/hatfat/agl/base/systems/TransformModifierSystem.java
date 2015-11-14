package com.hatfat.agl.base.systems;

import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.base.AglSystem;
import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.component.ModifierComponent;
import com.hatfat.agl.component.transform.Transform;
import com.hatfat.agl.entity.AglEntity;

import java.util.Arrays;

public class TransformModifierSystem extends AglSystem {

    public TransformModifierSystem() {
        super(Arrays.asList(ComponentType.MODIFIER, ComponentType.TRANSFORM));
    }

    @Override
    public void prepareRenderables(AglRenderer renderer) {

    }

    @Override
    public void updateEntity(AglEntity entity, float deltaTime) {
        Transform transform = entity.getComponentByType(ComponentType.TRANSFORM);
        ModifierComponent modifierComponent = entity.getComponentByType(ComponentType.MODIFIER);
        modifierComponent.update(deltaTime, transform);
    }
}
