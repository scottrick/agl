package test;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.hatfat.agl.AglSurfaceView;


public class TestActivity extends Activity {

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AglSurfaceView aglSurfaceView = new AglSurfaceView(this);
        glSurfaceView = aglSurfaceView;
        setContentView(glSurfaceView);

        aglSurfaceView.setScene(new TestScene(this));
    }
}
