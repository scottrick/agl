package com.hatfat.agl.modifiers;

import com.hatfat.agl.component.Transform;
import com.hatfat.agl.util.Vec3;

public class PositionModifier implements Modifier {

    private final Vec3 velocity;

    private float lastDeltaTime = 0.0f;
    private Vec3  scratch       = new Vec3();

    public PositionModifier(Vec3 velocity) {
        this.velocity = velocity;
    }

    @Override public void update(float deltaTime, Transform transform) {
        if (lastDeltaTime != deltaTime) {
            lastDeltaTime = deltaTime;
            scratch.set(velocity);
            scratch.scale(deltaTime);
        }

        transform.posQuat.pos.add(scratch);
    }
}
