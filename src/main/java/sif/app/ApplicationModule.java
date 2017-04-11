package sif.app;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.palominolabs.http.server.HttpServerWrapperModule;
import org.glassfish.jersey.servlet.ServletContainer;

@SuppressWarnings("PointlessBinding")
public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().requireExplicitBindings();
        install(new HttpServerWrapperModule());
        install(new ApiModule());
        install(new ScannerModule());
        install(new TestcenterModule());
        install(new ModelModule());

        bind(ServletContainer.class).toProvider(ServletContainerProvider.class).in(Scopes.SINGLETON);
        bind(ApplicationResourceConfig.class);

        install(new ServletModule() {
            @Override
            protected void configureServlets() {
                serve("/*").with(ServletContainer.class);
            }
        });
    }
}
