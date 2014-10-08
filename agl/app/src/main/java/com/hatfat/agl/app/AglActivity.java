package com.hatfat.agl.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.hatfat.agl.AglScene;
import com.hatfat.agl.R;
import com.hatfat.agl.events.AglFpsUpdatedEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class AglActivity extends Activity {

    @Inject
    Bus bus;

    private Object busEventListener;

    protected AglSurfaceView aglSurfaceView;

    private TextView fpsView;

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

    protected AglScene getScene() {
        return aglSurfaceView.getScene();
    }

    private void handleAglFpsUpdatedEvent(AglFpsUpdatedEvent event) {
        fpsView.setText(String.format(getResources().getString(R.string.fps_label_text), event.getFps()));
    }
}
