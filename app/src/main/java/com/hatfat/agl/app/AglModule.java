package com.hatfat.agl.app;

import android.os.Handler;
import android.os.Looper;

import com.hatfat.agl.AglLoadingScene;
import com.hatfat.agl.util.AglRandom;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
(
        complete = false,
        library = true,

        injects = {
                AglApplication.class,
                AglRenderer.class,
                AglActivity.class,
                AglLoadingScene.class,
        }
)
public class AglModule {
    @Provides
    @Singleton Bus provideBus() {
        return new Bus();
    }

    @Provides
    @Singleton Handler provideMainHandler() {
        return new Handler(Looper.getMainLooper());
    }

    @Provides
    @Singleton
    AglRandom provideAglRandom() {
        return new AglRandom();
    }
}
