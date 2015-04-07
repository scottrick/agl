package com.hatfat.agl;

import android.content.Context;

import com.hatfat.agl.util.Vec3;

public class AglLoadingScene extends AglScene {

    public AglLoadingScene(Context context) {
        super(context);

        AglCamera camera = new AglPerspectiveCamera(
                new Vec3(0.0f, 0.0f, 3.5f),
                new Vec3(0.0f, 0.0f, 0.0f),
                new Vec3(0.0f, 1.0f, 0.0f),
                60.0f, 1.0f, 0.1f, 100.0f);

        setCamera(camera);
    }

    @Override protected void setupSceneBackgroundWork() {
        super.setupSceneBackgroundWork();

    }

    @Override protected void setupSceneGLWork() {
        super.setupSceneGLWork();

//        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
//        GLES20.glPolygonOffset(1.0f, 1.0f);
//
//        Vec3 spinVec = new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
//        spinVec.normalize();
//
//        float rotateSpeed = 3.0f;
//
//        for (int i = 0; i < numTestNodes; i++) {
//            AglBBMesh shapeMesh = bbMeshes[i];
//
//            if (shapeMesh != null) {
//                AglRenderable wireframeRenderable = shapeMesh.toWireframeRenderable();
//                AglRenderable coloredRenderable = shapeMesh.toColoredGeometryRenderable();
//
//                AglNode meshNode = new AglNode(new Vec3(0.0f, 0.0f, 0.0f), coloredRenderable);
//                meshNode.addModifier(new SpinModifier(meshNode, rotateSpeed, spinVec));
//                meshNode.setShouldRender(false);
//                meshNodes[i] = meshNode;
//                addNode(meshNode);
//
//                AglNode wireframeNode = new AglNode(new Vec3(0.0f, 0.0f, 0.0f),
//                        wireframeRenderable);
//                wireframeNode.addModifier(new SpinModifier(wireframeNode, rotateSpeed, spinVec));
//                wireframeNode.setShouldRender(false);
//                wireframeNodes[i] = wireframeNode;
//                addNode(wireframeNode);
//            }
//        }
//
//        meshNodes[activeNodeIndex].setShouldRender(true);
//        wireframeNodes[activeNodeIndex].setShouldRender(true);
//        bbMeshes = null; //don't need these anymore!
    }

    @Override
    public void destroyScene() {
        super.destroyScene();
    }
}
