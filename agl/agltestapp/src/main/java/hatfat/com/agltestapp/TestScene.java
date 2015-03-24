package hatfat.com.agltestapp;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.hatfat.agl.AglCamera;
import com.hatfat.agl.AglNode;
import com.hatfat.agl.AglPerspectiveCamera;
import com.hatfat.agl.AglRenderable;
import com.hatfat.agl.AglScene;
import com.hatfat.agl.mesh.AglBBMesh;
import com.hatfat.agl.modifiers.SpinModifier;
import com.hatfat.agl.util.Vec3;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class TestScene extends AglScene {

    private Random rand;

    private int activeNodeIndex;
    private AglBBMesh[] bbMeshes;
    private AglNode[] wireframeNodes;
    private AglNode[] meshNodes;

    int numTestNodes = 7;

    public TestScene(Context context) {
        super(context);

        rand = new Random();

        AglCamera camera = new AglPerspectiveCamera(
                new Vec3(0.0f, 0.0f, 3.5f),
                new Vec3(0.0f, 0.0f, 0.0f),
                new Vec3(0.0f, 1.0f, 0.0f),
                60.0f, 1.0f, 0.1f, 100.0f);

        setCamera(camera);
    }

    @Override protected void setupSceneBackgroundWork() {
        super.setupSceneBackgroundWork();

        activeNodeIndex = 0;
        bbMeshes = new AglBBMesh[numTestNodes];
        wireframeNodes = new AglNode[numTestNodes];
        meshNodes = new AglNode[numTestNodes];

        for (int i = 0; i < numTestNodes; i++) {
            String resourceName = "mesh" + (i + 1);
            int resId = getContext().getResources().getIdentifier(resourceName, "raw", getContext().getPackageName());
            InputStream in = getContext().getResources().openRawResource(resId);

            try {
                bbMeshes[i] = AglBBMesh.readFromStreamAsBytes(in);
            }
            catch (IOException e) {
                Log.e("TestScene", "Error loading BB mesh resources.");
            }
        }
    }

    @Override protected void setupSceneGLWork() {
        super.setupSceneGLWork();

        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
        GLES20.glPolygonOffset(1.0f, 1.0f);

        Vec3 spinVec = new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        spinVec.normalize();

        float rotateSpeed = 3.0f;

        for (int i = 0; i < numTestNodes; i++) {
            AglBBMesh shapeMesh = bbMeshes[i];

            if (shapeMesh != null) {
                AglRenderable wireframeRenderable = shapeMesh.toWireframeRenderable();
                AglRenderable coloredRenderable = shapeMesh.toColoredGeometryRenderable();

                AglNode meshNode = new AglNode(new Vec3(0.0f, 0.0f, 0.0f), coloredRenderable);
                meshNode.addModifier(new SpinModifier(meshNode, rotateSpeed, spinVec));
                meshNode.setShouldRender(false);
                meshNodes[i] = meshNode;
                addNode(meshNode);

                AglNode wireframeNode = new AglNode(new Vec3(0.0f, 0.0f, 0.0f),
                        wireframeRenderable);
                wireframeNode.addModifier(new SpinModifier(wireframeNode, rotateSpeed, spinVec));
                wireframeNode.setShouldRender(false);
                wireframeNodes[i] = wireframeNode;
                addNode(wireframeNode);
            }
        }

        meshNodes[activeNodeIndex].setShouldRender(true);
        wireframeNodes[activeNodeIndex].setShouldRender(true);
        bbMeshes = null; //don't need these anymore!
    }

    @Override
    public void destroyScene() {
        super.destroyScene();

        GLES20.glPolygonOffset(0.0f, 0.0f);
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
    }

    public void toggleMesh() {
        meshNodes[activeNodeIndex].setShouldRender(false);
        wireframeNodes[activeNodeIndex].setShouldRender(false);

        activeNodeIndex = (activeNodeIndex + 1) % meshNodes.length;

        meshNodes[activeNodeIndex].setShouldRender(true);
        wireframeNodes[activeNodeIndex].setShouldRender(true);
    }
}
