package com.hatfat.agl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;


public class MainActivity extends Activity {

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new AglSurfaceView(this);
        setContentView(glSurfaceView);
    }
}
