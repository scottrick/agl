package com.hatfat.agl.modifiers;

import com.hatfat.agl.AglNode;
import com.hatfat.agl.util.Quat;
import com.hatfat.agl.util.Vec3;

public class SpinModifier implements Modifier {

    private float degreesPerSecond;
    private Vec3 axis;

    private float lastDeltaTime = 0.0f;
    private Quat scratch;

    public SpinModifier(float degreesPerSecond, Vec3 axis) {
        this.degreesPerSecond = degreesPerSecond;
        this.axis = axis;

        scratch = new Quat();
    }

    @Override public void update(float time, float deltaTime, AglNode node) {
        if (lastDeltaTime != deltaTime) {
            lastDeltaTime = deltaTime;
            scratch.setWithRotationInDegrees(degreesPerSecond * deltaTime, axis);
        }

        node.posQuat.quat.rotateBy(scratch);
    }
}
