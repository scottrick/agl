package com.hatfat.agl;

/**
 * Created by scottrick on 7/31/14.
 *
 * Something that can be rendered by OpenGL by a AglScene
 */
public interface AglRenderable {
    public void prepareRender(int shaderProgram);
    public void render();

    public String getShaderProgramName();
}
