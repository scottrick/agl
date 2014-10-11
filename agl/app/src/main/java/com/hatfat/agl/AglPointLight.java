package com.hatfat.agl;

import com.hatfat.agl.util.Color;
import com.hatfat.agl.util.Vec3;

public class AglPointLight {
    public Vec3 lightDir;
    public Color lightColor;

    public AglPointLight(Vec3 lightDir, Color lightColor) {
        this.lightDir = lightDir;
        this.lightColor = lightColor;
    }
}
