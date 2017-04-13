package siftest;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.RequestScoped;
import sif.app.ApiModule;
import sif.app.TestcenterModule;
import sif.app.ModelModule;
import sif.app.ScannerModule;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        FakeRequestScope fakeRequestScope = new FakeRequestScope();
        fakeRequestScope.enter();
        binder().requireExplicitBindings();
        install(new ApiModule());
        install(new ScannerModule());
        install(new TestcenterModule());
        install(new ModelModule());
        bind(FakeRequestScope.class).toInstance(fakeRequestScope);
        bindScope(RequestScoped.class, fakeRequestScope);
    }
}
