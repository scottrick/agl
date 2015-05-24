package com.hatfat.agl.app;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;

public class AglApplication extends Application {
    private ObjectGraph objectGraph;

    public static AglApplication get(Context context) {
        if (context == null) {
            throw new RuntimeException("null context");
        }

        return (AglApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        setupObjectGraph();
    }

    private void setupObjectGraph() {
        objectGraph = ObjectGraph.create(getInjectionModules());
        objectGraph.inject(this);
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public Object[] getInjectionModules() {
        return new Object[]{
                new AglModule(),
        };
    }
}
