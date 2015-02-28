package test;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.hatfat.agl.AglCamera;
import com.hatfat.agl.AglNode;
import com.hatfat.agl.AglPerspectiveCamera;
import com.hatfat.agl.AglRenderable;
import com.hatfat.agl.AglScene;
import com.hatfat.agl.mesh.AglBBMesh;
import com.hatfat.agl.mesh.AglMesh;
import com.hatfat.agl.modifiers.SpinModifier;
import com.hatfat.agl.util.Vec3;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class TestScene extends AglScene {

    private Random rand;

    private int activeNodeIndex;
    private AglNode[] wireframeNodes;
    private AglNode[] meshNodes;

    public TestScene(Context context) {
        super(context);

        rand = new Random();

        AglCamera camera = new AglPerspectiveCamera(
                new Vec3(0.0f, 0.0f, 2.35f),
                new Vec3(0.0f, 0.0f, 0.0f),
                new Vec3(0.0f, 1.0f, 0.0f),
                60.0f, 1.0f, 0.1f, 100.0f);

        setCamera(camera);
    }

    @Override
    public void setupScene() {
        super.setupScene();

        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
        GLES20.glPolygonOffset(1.0f, 1.0f);

        int numTestNodes = 8;
        activeNodeIndex = 0;
        wireframeNodes = new AglNode[numTestNodes];
        meshNodes = new AglNode[numTestNodes];

        Vec3 spinVec = new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        spinVec = new Vec3(0.0f, 1.0f, 0.0f);
        spinVec.normalize();

        AglMesh testMesh = null;
        float rotateSpeed = 3.0f;

        boolean newWay = true;

        long setupStartTime = System.currentTimeMillis();

        if (newWay) {
            Context context = getContext();

            for (int i = 0; i < numTestNodes; i++) {
                String resourceName = "mesh" + i;
                int resId = context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());

                InputStream in = context.getResources().openRawResource(resId);

                AglBBMesh shapeMesh = null;

                try {
                    shapeMesh = AglBBMesh.readFromStreamAsBytes(in);
                }
                catch (IOException e) {
                    Log.e("TestScene", "Error loading BB mesh resources.");
                }

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
        }
        else {
            for (int i = 0; i < numTestNodes; i++) {
                if (testMesh == null) {
                    testMesh = AglMesh.makeIcosahedron();
                } else {
                    testMesh = testMesh.splitMesh();
                }

                AglBBMesh shapeMesh = AglBBMesh.makeFromMesh(testMesh);
                AglRenderable wireframeRenderable = shapeMesh.toWireframeRenderable();

//            AglRenderable wireframeRenderable = testMesh.toWireframeRenderable();

                AglRenderable meshRenderable = testMesh.toColoredGeometryRenderable();
//            AglRenderable pentagonRenderable = shapeMesh.createWireframe();

                AglNode meshNode = new AglNode(new Vec3(0.0f, 0.0f, 0.0f), meshRenderable);
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

        long setupEndTime = System.currentTimeMillis();
        Log.i("TestScene", "[" + numTestNodes + "] TestScene setup took " + (setupEndTime - setupStartTime) + " milliseconds.");

        meshNodes[activeNodeIndex].setShouldRender(true);
        wireframeNodes[activeNodeIndex].setShouldRender(true);
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
