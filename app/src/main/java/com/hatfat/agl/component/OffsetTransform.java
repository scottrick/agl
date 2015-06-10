package com.hatfat.agl.component;

import com.hatfat.agl.entity.AglEntity;
import com.hatfat.agl.util.Matrix;
import com.hatfat.agl.util.PosQuat;
import com.hatfat.agl.util.Quat;
import com.hatfat.agl.util.Vec3;

public class OffsetTransform extends Transform {

    private final AglEntity offsetEntity;

    public OffsetTransform(AglEntity offsetEntity) {
        this.offsetEntity = offsetEntity;
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
    public void getModelMatrix(Matrix matrix) {
        Matrix offsetMatrix = new Matrix();

        Transform transform = offsetEntity.getComponentByType(ComponentType.TRANSFORM);
        transform.getModelMatrix(matrix);

        if (scaleMatrix != null) {
            //need to apply scaling, do some extra work
            posQuat.quat.toMatrix(offsetMatrix);
            Matrix newMatrix = Matrix.multiplyBy(offsetMatrix, scaleMatrix);
            newMatrix.translate(posQuat.pos);
            offsetMatrix.set(newMatrix);
        }
        else {
            //just rotation and translation
            posQuat.quat.toMatrix(offsetMatrix);
            offsetMatrix.translate(posQuat.pos);
        }

        matrix.set(Matrix.multiplyBy(offsetMatrix, matrix));
    }

    @Override public Vec3 getAbsolutePos() {
        Matrix matrix = new Matrix();
        getModelMatrix(matrix);
        return matrix.getPositionOffset();
    }
}
