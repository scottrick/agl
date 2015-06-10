package com.hatfat.agl.modifiers;

import com.hatfat.agl.component.Transform;

public interface Modifier {
    void update(float deltaTime, Transform transform);
}
