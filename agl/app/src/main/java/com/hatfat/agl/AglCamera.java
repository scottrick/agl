package com.hatfat.agl;

public interface AglCamera {
    public float[] getViewMatrix();
    public float[] getProjMatrix();

    public void setAspectRatio(float aspectRatio);
}
