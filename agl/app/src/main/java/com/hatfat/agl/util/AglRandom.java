package com.hatfat.agl.util;

import java.util.Random;

public class AglRandom {

    private Random random;

    public AglRandom(long seed) {
        random = new Random(seed);
    }

    public AglRandom() {
        random = new Random();
    }

    public Random get() {
        return random;
    }

    public Vec3 nextColor() {
        return new Vec3(random.nextFloat(), random.nextFloat(), random.nextFloat());
    }
}
