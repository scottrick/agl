package com.hatfat.agl;

import com.hatfat.agl.util.Vec3;

public class AglPointLight {
    public Vec3 lightDir;
    public Vec3 lightColor;

    public AglPointLight(Vec3 lightDir, Vec3 lightColor) {
        this.lightDir = lightDir;
        this.lightColor = lightColor;
    }
}
