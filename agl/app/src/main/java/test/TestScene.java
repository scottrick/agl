package test;

import com.hatfat.agl.AglCamera;
import com.hatfat.agl.AglNode;
import com.hatfat.agl.AglPerspectiveCamera;
import com.hatfat.agl.AglRenderable;
import com.hatfat.agl.AglScene;
import com.hatfat.agl.modifiers.SpinModifier;
import com.hatfat.agl.util.Vec3;

import java.util.Random;

public class TestScene extends AglScene {

    private Random rand;

    public TestScene() {
        rand = new Random();

        AglCamera camera = new AglPerspectiveCamera(
                new Vec3(0.0f, 0.0f, 24.0f),
                new Vec3(0.0f, 0.0f, 0.0f),
                new Vec3(0.0f, 1.0f, 0.0f),
                60.0f, 1.0f, 0.1f, 1000.0f);

        setCamera(camera);
    }

    @Override
    public void setupScene() {
        super.setupScene();

        AglRenderable cubeRenderable = TestRenderableFactory.createFlatCube();
        AglRenderable pyramidRenderable = TestRenderableFactory.createFlatPyramid();
        AglRenderable testRenderable = TestRenderableFactory.createIcosahedron();

        int num = 2;
        AglNode node;

        for (int x = -num; x <= num; x++) {
            for (int y = -num; y <= num; y++) {
                if (false && (x + y) % 2 != 0) {
                    node = new AglNode(new Vec3(3.0f * x, 3.0f * y, 0.0f), cubeRenderable);
                }
                else {
                    node = new AglNode(new Vec3(3.0f * x, 3.0f * y, 0.0f), testRenderable);
                }

                node.addModifier(new SpinModifier(node, x * 60.0f, new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())));
                addNode(node);
            }
        }
    }
}
