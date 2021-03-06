package com.hatfat.agl.component.transform;

import com.hatfat.agl.base.AglScene;
import com.hatfat.agl.component.AglComponent;
import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.util.Matrix;
import com.hatfat.agl.util.PosQuat;
import com.hatfat.agl.util.Quat;
import com.hatfat.agl.util.Vec3;

public class Transform extends AglComponent {

    public PosQuat posQuat;

    public boolean isBillboard;

    protected Vec3   scale;
    protected Matrix scaleMatrix;

    public Transform() {
        super(ComponentType.TRANSFORM);

        this.posQuat = new PosQuat(new Vec3(), new Quat());
    }

    public Transform(Vec3 position) {
        super(ComponentType.TRANSFORM);

        this.posQuat = new PosQuat(position, new Quat());
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
        if (scaleMatrix != null) {
            //need to apply scaling, do some extra work
            posQuat.quat.toMatrix(matrix);
            Matrix newMatrix = Matrix.multiplyBy(scaleMatrix, matrix);
            newMatrix.translate(posQuat.pos);
            matrix.set(newMatrix);
        }
        else {
            //just rotation and translation
            posQuat.quat.toMatrix(matrix);
            matrix.translate(posQuat.pos);
        }
    }

    public Vec3 getAbsolutePos(AglScene scene) {
        return posQuat.pos;
    }
}
