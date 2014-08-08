package test;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.hatfat.agl.AglScene;
import com.hatfat.agl.shaders.AglShader;

/**
 * Created by scottrick on 8/7/14.
 */
public class TestScene extends AglScene {

    private boolean initialized = false;
    private Context context;

    public TestScene(Context context) {
        super();

        this.context = context;
    }

    @Override
    public void render() {
        if (!initialized) {
            initialize();
        }
    }

    private void initialize() {
        AglShader testVertShader = new AglShader("shaders/basic.vert", context);
        AglShader testFragShader = new AglShader("shaders/basic.frag", context);

        initialized = true;
    }
}
