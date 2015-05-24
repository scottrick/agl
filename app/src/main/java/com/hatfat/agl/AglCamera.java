package com.hatfat.agl;

public interface AglCamera {
    float[] getViewMatrix();
    float[] getProjMatrix();

    void setAspectRatio(float aspectRatio);
}
