package siftest.zzz_integration_test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.palominolabs.http.server.HttpServerConnectorConfig;
import com.palominolabs.http.server.HttpServerWrapper;
import com.palominolabs.http.server.HttpServerWrapperConfig;
import com.palominolabs.http.server.HttpServerWrapperFactory;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;
import sif.app.ApplicationModule;
import sif.app.GuiceServiceLocatorGenerator;

public class EmbeddedJetty {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private HttpServerWrapper server;

    public void start() throws Exception {
        HttpServerWrapperConfig config = new HttpServerWrapperConfig();
        config.withHttpServerConnectorConfig(HttpServerConnectorConfig.forHttp(HOST, PORT));

        Injector injector = Guice.createInjector(new ApplicationModule());
        JerseyGuiceUtils.install(new GuiceServiceLocatorGenerator(injector));

        server = injector.getInstance(HttpServerWrapperFactory.class).getHttpServerWrapper(config);
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public String getBaseUri() {
        return "http://" + HOST + ":" + PORT + "/";
    }
}