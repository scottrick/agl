package com.hatfat.agl.modifiers;

import com.hatfat.agl.AglNode;

public interface Modifier {
    void update(float time, float deltaTime, AglNode node);
}
