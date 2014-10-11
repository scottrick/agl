package com.hatfat.agl.util;

public class Color {
    public float r;
    public float g;
    public float b;
    public float a;

    //used in calculations
    private float scratch;

    public Color(final float r, final float g, final float b, final float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
