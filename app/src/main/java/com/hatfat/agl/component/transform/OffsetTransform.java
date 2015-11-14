package com.hatfat.agl.component.transform;

import com.hatfat.agl.base.AglScene;
import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.util.Matrix;
import com.hatfat.agl.util.PosQuat;
import com.hatfat.agl.util.Quat;
import com.hatfat.agl.util.Vec3;

public class OffsetTransform extends Transform {

    private final int offsetEntityId;

    public OffsetTransform(int offsetEntityId) {
        this.offsetEntityId = offsetEntityId;
        this.posQuat = new PosQuat(new Vec3(1.0f, 1.0f, 1.0f), new Quat());
    }

    public void setScale(Vec3 newScale) {
        this.scale = newScale;

        if (this.scale != null) {
            this.scaleMatrix = new Matrix();
            scaleMatrix.setScale(this.scale);
        }
        else {
            scaleMatrix = null;
        }
    }

    //will set the model matrix to the passed in matrix
    public void getModelMatrix(AglScene scene, Matrix matrix) {
        Matrix offsetMatrix = new Matrix();

        Transform transform = scene.getEntityById(offsetEntityId).getComponentByType(ComponentType.TRANSFORM);
        transform.getModelMatrix(scene, matrix);

        if (scaleMatrix != null) {
            //need to apply scaling, do some extra work
            posQuat.quat.toMatrix(offsetMatrix);
            Matrix newMatrix = Matrix.multiplyBy(scaleMatrix, offsetMatrix);
            newMatrix.translate(posQuat.pos);
            offsetMatrix.set(newMatrix);
        }
        else {
            //just rotation and translation
            posQuat.quat.toMatrix(offsetMatrix);
            offsetMatrix.translate(posQuat.pos);
        }

        Matrix result = Matrix.multiplyBy(offsetMatrix, matrix);

        if (isBillboard) {
            Vec3 absPos = result.getPositionOffset();
            matrix.setIdentity();
            matrix.setScale(scale);
            matrix.translate(absPos);
        }
        else {
            matrix.set(result);
        }
    }

    @Override public Vec3 getAbsolutePos(AglScene scene) {
        Matrix matrix = new Matrix();
        getModelMatrix(scene, matrix);
        return matrix.getPositionOffset();
    }
}
