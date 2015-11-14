package com.hatfat.agl.textures;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class AglTexture {

    private int[] textures = new int[1];
    private String filename;

    public AglTexture(String filename, Context context) {
        this.filename = filename;

        create(context);
    }

    void destroy() {
        GLES20.glDeleteTextures(1, textures, 0);
        textures[0] = 0;
    }

    void create(final Context context) {
        if (getTexture() != 0) {
            Log.e("AglTexture", "Texture error: texture is already created.");
            return;
        }

        //generate our texture id
        GLES20.glGenTextures(1, textures, 0);

        final BitmapFactory.Options options = new BitmapFactory.Options();

        //no pre-scaling
        options.inScaled = false;

        int textureResourceId = context.getResources().getIdentifier(filename, "drawable",
                context.getPackageName());

        final Bitmap sourceBitmap = BitmapFactory.decodeResource(context.getResources(),
                textureResourceId, options);

        /* need to flip the bitmap vertically so it matches how opengl handles texture coordinates */
        Matrix flip = new Matrix();
        flip.postScale(1f, -1f);
        final Bitmap rotatedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
                sourceBitmap.getHeight(), flip, true);

        //bind the texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getTexture());

        //setup filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        //load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, rotatedBitmap, 0);

        //generate mipmaps
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        //and clean up
        sourceBitmap.recycle();
        rotatedBitmap.recycle();
    }

    public int getTexture() {
        return textures[0];
    }

    public String getFilename() {
        return filename;
    }
}
