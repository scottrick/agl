package com.hatfat.agl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.hatfat.agl.shaders.AglShaderManager;

/**
 * Created by scottrick on 7/31/14.
 */
public class AglSurfaceView extends GLSurfaceView {

    private AglRenderer renderer;

    public AglSurfaceView(Context context){
        super(context);

        //setup the shader manager with a context
        AglShaderManager.get().setContext(context);

        setEGLContextClientVersion(2);

        this.renderer = new AglRenderer();
        setRenderer(renderer);
    }

    public void setScene(AglScene scene) {
        this.renderer.setScene(scene);
    }
}
