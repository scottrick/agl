package com.hatfat.agl.modifiers;

import com.hatfat.agl.AglNode;
import com.hatfat.agl.AglUpdateable;

public class Modifier implements AglUpdateable {

    protected AglNode node;

    public Modifier(AglNode node) {
        this.node = node;
    }

    @Override
    public void update(float time, float deltaTime) {

    }
}
