package test;

import com.hatfat.agl.app.AglApplication;

public class TestApplication extends AglApplication {

    @Override
    public Object[] getInjectionModules() {
        return new Object[]{
                new TestModule(),
        };
    }
}
