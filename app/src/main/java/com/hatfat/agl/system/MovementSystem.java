package com.hatfat.agl.system;

import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.component.MovementComponent;
import com.hatfat.agl.component.transform.Transform;
import com.hatfat.agl.entity.AglEntity;
import com.hatfat.agl.util.Vec3;

import java.util.Arrays;

public class MovementSystem extends AglSystem {

    private Vec3 scratch = new Vec3();

    public MovementSystem() {
        super(Arrays.asList(ComponentType.MOVEMENT, ComponentType.TRANSFORM));
    }

    @Override void updateEntity(AglEntity entity, float deltaTime) {
        Transform transform = entity.getComponentByType(ComponentType.TRANSFORM);
        MovementComponent movement = entity.getComponentByType(ComponentType.MOVEMENT);

        scratch.set(movement.getVelocity());
        scratch.scale(deltaTime);

        transform.posQuat.pos.add(scratch);

        scratch.set(movement.getAcceleration());
        scratch.scale(deltaTime);

        movement.getVelocity().add(scratch);
    }
}
