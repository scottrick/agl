package com.hatfat.agl;

import com.hatfat.agl.modifiers.Modifier;
import com.hatfat.agl.render.AglRenderable;
import com.hatfat.agl.util.Matrix;
import com.hatfat.agl.util.PosQuat;
import com.hatfat.agl.util.Quat;
import com.hatfat.agl.util.Vec3;

public class AglNode implements AglUpdateable {

    public PosQuat posQuat;

    private Vec3   scale;
    private Matrix scaleMatrix;

    protected AglRenderable renderable;

    protected boolean shouldRender;

    protected int numModifiers    = 0;
    protected int maxNumModifiers = 8;
    protected Modifier[] modifiers;
    protected int updateInt = 0;

    public AglNode(Vec3 position, AglRenderable renderable) {
        this.shouldRender = true;
        this.posQuat = new PosQuat(position, new Quat());
        this.renderable = renderable;
        this.modifiers = new Modifier[maxNumModifiers];
    }

    public boolean shouldRender() {
        return shouldRender;
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

    public void setShouldRender(boolean newValue) {
        this.shouldRender = newValue;
    }

    //will set the model matrix to the passed in matrix
    public void getModelMatrix(Matrix matrix) {
        if (scaleMatrix != null) {
            //need to apply scaling, do some extra work
            posQuat.quat.toMatrix(matrix);
            Matrix newMatrix = Matrix.multiplyBy(matrix, scaleMatrix);
            newMatrix.translate(posQuat.pos);
            matrix.set(newMatrix);
        }
        else {
            //just rotation and translation
            posQuat.quat.toMatrix(matrix);
            matrix.translate(posQuat.pos);
        }
    }

    public AglRenderable getRenderable() {
        return renderable;
    }

    public void addModifier(Modifier modifier) {
        if (numModifiers + 1 >= maxNumModifiers) {
            throw new RuntimeException("Can't add more modifiers!");
        }

        modifiers[numModifiers] = modifier;
        numModifiers++;
    }

    public void removeModifier(Modifier modifier) {
        int modifierIndex = -1;

        for (int i = 0; i < numModifiers; i++) {
            if (modifiers[i] == modifier) {
                modifierIndex = i;
                break;
            }
        }

        if (modifierIndex < 0) {
            //modifier was not on this node!
            return;
        }

        modifiers[modifierIndex] = modifiers[numModifiers - 1]; //swap the last modifier with the modifier we are removing

        numModifiers--;

        modifiers[numModifiers] = null;
    }

    @Override
    public void update(float time, float deltaTime) {
        for (updateInt = 0; updateInt < numModifiers; updateInt++) {
            modifiers[updateInt].update(time, deltaTime, this);
        }
    }
}
