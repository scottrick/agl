package hatfat.com.agltestapp;

import com.hatfat.agl.app.AglModule;

import dagger.Module;

@Module
(
    library = true,

    includes = {
            AglModule.class,
    },

    injects = {
            TestActivity.class,
            TestApplication.class,
    }
)
public class TestModule {

}
