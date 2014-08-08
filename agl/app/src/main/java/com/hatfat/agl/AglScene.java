package com.hatfat.agl;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by scottrick on 7/31/14.
 */
public abstract class AglScene implements AglRenderable {

    private List<AglRenderable> renderables;

    public AglScene() {
        renderables = new LinkedList<AglRenderable>();
    }

    public void addRenderable(AglRenderable renderable) {
        if (!renderables.contains(renderable)) {
            renderables.add(renderable);
        }
    }

    public void removeRenderable(AglRenderable renderable) {
        renderables.remove(renderable);
    }

    public void removeAllRenderables() {
        renderables.clear();
    }
}
