package test;

import android.opengl.GLES20;

import com.hatfat.agl.AglCamera;
import com.hatfat.agl.AglNode;
import com.hatfat.agl.AglPerspectiveCamera;
import com.hatfat.agl.AglRenderable;
import com.hatfat.agl.AglScene;
import com.hatfat.agl.mesh.AglMesh;
import com.hatfat.agl.mesh.AglShapeMesh;
import com.hatfat.agl.modifiers.SpinModifier;
import com.hatfat.agl.util.Vec3;

import java.util.Random;

public class TestScene extends AglScene {

    private Random rand;

    private int activeNodeIndex;
    private AglNode[] wireframeNodes;
    private AglNode[] meshNodes;

    public TestScene() {
        rand = new Random();

        AglCamera camera = new AglPerspectiveCamera(
                new Vec3(0.0f, 0.0f, 3.35f),
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

        activeNodeIndex = 0;
        int numTestNodes = 5;
        wireframeNodes = new AglNode[numTestNodes];
        meshNodes = new AglNode[numTestNodes];

        Vec3 spinVec = new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        spinVec.normalize();

        AglMesh testMesh = null;
        float rotateSpeed = 4.0f;

        for (int i = 0; i < numTestNodes; i++) {
            if (testMesh == null) {
                testMesh = AglMesh.makeIcosahedron();
            }
            else {
                testMesh = testMesh.splitMesh();
            }

            AglShapeMesh shapeMesh = AglShapeMesh.makeFromMesh(testMesh);

            AglRenderable wireframeRenderable = TestRenderableFactory.createWireFrameFromTriangles(testMesh.getVertexArray(), testMesh.getNumVertices(), testMesh.getIndexArray(), testMesh.getNumTriangles());
            AglRenderable meshRenderable = TestRenderableFactory.createColoredRenderableFromMesh(testMesh);
            AglRenderable pentagonRenderable = shapeMesh.createWireframe();

            AglNode meshNode = new AglNode(new Vec3(0.0f, 0.0f, 0.0f), meshRenderable);
            meshNode.addModifier(new SpinModifier(meshNode, rotateSpeed, spinVec));
            meshNode.setShouldRender(false);
            meshNodes[i] = meshNode;
            addNode(meshNode);

            AglNode wireframeNode = new AglNode(new Vec3(0.0f, 0.0f, 0.0f), pentagonRenderable);
            wireframeNode.addModifier(new SpinModifier(wireframeNode, rotateSpeed, spinVec));
            wireframeNode.setShouldRender(false);
            wireframeNodes[i] = wireframeNode;
            addNode(wireframeNode);
        }

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
