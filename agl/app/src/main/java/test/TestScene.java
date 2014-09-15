package test;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.hatfat.agl.AglColoredGeometry;
import com.hatfat.agl.AglScene;

/**
 * Created by scottrick on 8/7/14.
 */
public class TestScene extends AglScene {

    private AglColoredGeometry cubeRenderable;
    private float testRotation = 0.0f;

    public TestScene() {

    }

    @Override
    public void prepareRender() {
        if (cubeRenderable == null) {
            cubeRenderable = TestRenderableFactory.createCube();
        }
    }

    @Override
    public void render() {
        GLES20.glUseProgram(cubeRenderable.getShaderProgram());

//        Matrix4f projectionMatrix = getCamera().getProjectionMatrix();
//        Matrix4f viewMatrix = new Matrix4f();

        testRotation += 2.0f;
        if (testRotation > 360.0f) {
            testRotation -= 360.0f;
        }

        float lookAtMatrix[] = new float[16];
        Matrix.setLookAtM(lookAtMatrix, 0, 10.0f, 10.0f, 10.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        float projMatrix[] = new float[16];
        Matrix.perspectiveM(projMatrix, 0, 60.0f, getCamera().getAspectRatio(), 0.5f, 100.0f);

        float modelMatrix[] = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, testRotation, 0.0f, 1.0f, 0.0f);

        int projectionUniformLocation = GLES20.glGetUniformLocation(cubeRenderable.getShaderProgram(), "proj");
        int viewUniformLocation = GLES20.glGetUniformLocation(cubeRenderable.getShaderProgram(), "view");
        int modelUniformLocation = GLES20.glGetUniformLocation(cubeRenderable.getShaderProgram(), "model");

        GLES20.glUniformMatrix4fv(projectionUniformLocation, 1, false, projMatrix, 0);
        GLES20.glUniformMatrix4fv(viewUniformLocation, 1, false, lookAtMatrix, 0);
        GLES20.glUniformMatrix4fv(modelUniformLocation, 1, false, modelMatrix, 0);

        cubeRenderable.render();

        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("TestScene", "glError: " + error);
        }
    }

    @Override
    public int getShaderProgram() {
        return 0;
    }
}
