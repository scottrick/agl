package com.hatfat.agl.component;

import com.hatfat.agl.render.AglRenderable;

public class RenderableComponent extends AglComponent {

    protected AglRenderable renderable;

    protected boolean shouldRender = true;

    public RenderableComponent(AglRenderable renderable) {
        super(ComponentType.RENDERABLE);

        this.renderable = renderable;
    }

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }

    public boolean shouldRender() {
        return shouldRender;
    }

    public AglRenderable getRenderable() {
        return renderable;
    }
}
