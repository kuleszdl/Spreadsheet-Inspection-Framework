package sif.app;

import com.google.inject.Inject;
import org.glassfish.jersey.servlet.ServletContainer;

class ServletContainerProvider implements com.google.inject.Provider<ServletContainer> {

    private final ApplicationResourceConfig app;

    @Inject
    ServletContainerProvider(ApplicationResourceConfig app) {
        this.app = app;
    }

    @Override
    public ServletContainer get() {
        return new ServletContainer(app);
    }
}