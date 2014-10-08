package com.hatfat.agl.modifiers;

import com.hatfat.agl.AglNode;
import com.hatfat.agl.util.Quat;
import com.hatfat.agl.util.Vec3;

public class SpinModifier extends Modifier {

    private float degreesPerSecond;
    private Vec3 axis;

    private float lastDeltaTime = 0.0f;
    private Quat scratch;

    public SpinModifier(AglNode node, float degreesPerSecond, Vec3 axis) {
        super(node);

        this.degreesPerSecond = degreesPerSecond;
        this.axis = axis;

        scratch = new Quat();
    }

    @Override
    public void update(float time, float deltaTime) {
        if (lastDeltaTime != deltaTime) {
            lastDeltaTime = deltaTime;
            scratch.setWithRotationInDegrees(degreesPerSecond * deltaTime, axis);
        }

        node.posQuat.quat.multiply(scratch);
    }
}
