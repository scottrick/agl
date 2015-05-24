package com.hatfat.agl.textures;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

public class AglTextureManager {

    private HashMap<String, AglTexture> textureMap;
    private Context                     context;

    public final static String defaultTextureName = "default_texture";

    public AglTextureManager(Context context) {
        this.context = context;
        this.textureMap = new HashMap<>();
    }

    public AglTexture getTexture(String textureName) {
        AglTexture texture = textureMap.get(textureName);

        if (texture == null) {
            texture = new AglTexture(textureName, context);

            if (texture.getTexture() == 0) {
                //did not create successfully, so just use the default texture!
                Log.e("AglTextureManager", "Failed to create texture '" + textureName + "'");
                texture = getDefaultTexture();
            }

            textureMap.put(textureName, texture);
        }

        return texture;
    }

    public AglTexture getDefaultTexture() {
        return getTexture(defaultTextureName);
    }

    /*
     * Removes all textures.
     * Call this when the surfaceView has been paused, so it doesn't need destroy them.
     */
    public void clearTextures() {
        textureMap.clear();
    }
}
