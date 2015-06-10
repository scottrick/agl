package com.hatfat.agl.component;

public abstract class AglComponent {

    public final ComponentType type;

    public AglComponent(ComponentType type) {
        this.type = type;
    }
}
