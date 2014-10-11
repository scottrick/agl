package test;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hatfat.agl.R;
import com.hatfat.agl.app.AglActivity;
import com.hatfat.agl.util.AglRandom;
import com.hatfat.agl.util.Vec3;

import javax.inject.Inject;

public class TestActivity extends AglActivity implements View.OnTouchListener {

    @Inject
    AglRandom rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TestScene aglScene = new TestScene();
        aglSurfaceView.setScene(aglScene);

        aglSurfaceView.setOnTouchListener(this);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.base_layout_content_view);
        View ourView = getLayoutInflater().inflate(R.layout.test_activity_layout, container, false);
        container.addView(ourView);
        
        Button testButton = (Button) ourView.findViewById(R.id.test_activity_layout_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScene().getGlobalLight().lightColor = rand.nextColor();
            }
        });

        Button meshButton = (Button) ourView.findViewById(R.id.test_activity_layout_toggle_button);
        meshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aglScene.toggleMesh();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE: {
                float halfWidth = aglSurfaceView.getWidth() / 2.0f;
                float halfHeight = aglSurfaceView.getHeight() / 2.0f;
                float scale = 1.0f;

                float xValue = (event.getX() - halfWidth) / halfWidth * scale;
                float yValue = (event.getY() - halfHeight) / halfHeight * scale;

                Vec3 lightDir = getScene().getGlobalLight().lightDir;
                lightDir.x = xValue;
                lightDir.y = -yValue;
                lightDir.z = 1.0f;
            }
                break;
        }

        return true;
    }
}
