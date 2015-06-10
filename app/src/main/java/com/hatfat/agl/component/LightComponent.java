package com.hatfat.agl.component;

import com.hatfat.agl.util.Color;

public class LightComponent extends AglComponent {

    public Color lightColor;

    public LightComponent() {
        super(ComponentType.LIGHT);

        this.lightColor = new Color();
    }

    public LightComponent(Color lightColor) {
        super(ComponentType.LIGHT);

        this.lightColor = lightColor;
    }

    public Color getLightColor() {
        return lightColor;
    }
}
