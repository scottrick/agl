package com.hatfat.agl;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by scottrick on 7/31/14.
 */
public class AglSurfaceView extends GLSurfaceView {

    public AglSurfaceView(Context context){
        super(context);

        setEGLContextClientVersion(2);
        setRenderer(new AglRenderer());
    }
}
