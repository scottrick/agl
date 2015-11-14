package com.hatfat.agl.component;

import com.hatfat.agl.util.Vec3;
public class MovementComponent extends AglComponent {

    private Vec3 velocity;
    private Vec3 acceleration;

    public MovementComponent() {
        super(ComponentType.MOVEMENT);

        velocity = new Vec3();
        acceleration = new Vec3();
    }

    public MovementComponent(Vec3 velocity, Vec3 acceleration) {
        super(ComponentType.MOVEMENT);

        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public Vec3 getVelocity() {
        return velocity;
    }

    public Vec3 getAcceleration() {
        return acceleration;
    }
}
