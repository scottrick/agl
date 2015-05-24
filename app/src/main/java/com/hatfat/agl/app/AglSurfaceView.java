package com.hatfat.agl.app;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.hatfat.agl.AglScene;

/**
 * Created by scottrick on 7/31/14.
 */
public class AglSurfaceView extends GLSurfaceView {

    private AglRenderer renderer;

    public AglSurfaceView(Context context){
        super(context);
        init(context);
    }

    public AglSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);

        this.renderer = new AglRenderer(context);
        setRenderer(renderer);
    }

    public void setScene(AglScene scene) {
        this.renderer.setNextScene(scene);
    }

    public AglScene getScene() {
        return this.renderer.getScene();
    }

    @Override
    public void onResume() {
        super.onResume();
        renderer.aglResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        renderer.aglPause();
    }
}
