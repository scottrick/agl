package com.hatfat.agl.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hatfat.agl.R;
import com.hatfat.agl.base.AglScene;
import com.hatfat.agl.events.AglFpsUpdatedEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class AglActivity extends Activity implements View.OnTouchListener {

    @Inject
    Bus bus;

    private Object busEventListener;

    protected AglSurfaceView aglSurfaceView;

    private TextView fpsView;

    /* by default, AglActivities will take over the whole screen */
    private boolean shouldFullscreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AglApplication app = AglApplication.get(this);
        app.inject(this);

        busEventListener = new Object() {
            @Subscribe
            public void handleAglFpsUpdatedEvent(AglFpsUpdatedEvent event) {
                AglActivity.this.handleAglFpsUpdatedEvent(event);
            }
        };

        bus.register(busEventListener);

        setContentView(R.layout.base_layout);

        aglSurfaceView = (AglSurfaceView) findViewById(R.id.base_layout_agl_surface_view);
        fpsView = (TextView) findViewById(R.id.base_layout_fps_view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        aglSurfaceView.setOnTouchListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        aglSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        aglSurfaceView.onResume();
    }

    @Override public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (shouldFullscreen && hasFocus) {
            int visibility =
                      View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                visibility = visibility | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(visibility);
        }
    }

    protected void setShouldFullscreen(boolean shouldFullscreen) {
        this.shouldFullscreen = shouldFullscreen;
    }

    protected AglScene getScene() {
        return aglSurfaceView.getScene();
    }

    private void handleAglFpsUpdatedEvent(AglFpsUpdatedEvent event) {
        fpsView.setText(String.format(getResources().getString(R.string.fps_label_text), event.getFps()));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getScene() == null) {
            return false;
        }

        GestureDetector gestureDetector = getScene().getGestureDetector();
        if (gestureDetector == null) {
            return false;
        }

        gestureDetector.onTouchEvent(event);
        return true;
    }
}
