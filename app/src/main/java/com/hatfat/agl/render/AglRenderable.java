package com.hatfat.agl.render;

/**
 * Created by scottrick on 7/31/14.
 *
 * Something that can be rendered by OpenGL by a AglScene
 */
public interface AglRenderable {
    void prepareRender(int shaderProgram);
    void render();
    void cleanupRender();

    String getShaderProgramName();
}
