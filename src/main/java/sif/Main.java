package sif;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.palominolabs.http.server.HttpServerConnectorConfig;
import com.palominolabs.http.server.HttpServerWrapper;
import com.palominolabs.http.server.HttpServerWrapperConfig;
import com.palominolabs.http.server.HttpServerWrapperFactory;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import sif.app.ApplicationModule;
import sif.app.GuiceServiceLocatorGenerator;

import java.util.logging.LogManager;

class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 8080;

    private Main() {
        // empty constructor, we don't need any instance of this class
    }

    public static void main(String[] args) throws Exception {
        // reset log handler and user slf4j bridge
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();

        // set jetty log level to info
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.JavaUtilLog");
        System.setProperty("org.eclipse.jetty.util.log.class.LEVEL", "INFO");

        // set system properties to  use utf-8 encoding
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("org.eclipse.jetty.util.UrlEncoding.charset", "utf-8");

        // start up an http server
        String ip = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        String configuredIp = System.getProperty("ip");
        String configuredPort = System.getProperty("port");
        if (configuredIp != null && !configuredIp.isEmpty())
            ip = configuredIp;
        if (configuredPort != null && !configuredPort.isEmpty())
            port = Integer.parseInt(configuredPort);

        HttpServerWrapperConfig config = new HttpServerWrapperConfig();
        config.withHttpServerConnectorConfig(HttpServerConnectorConfig.forHttp(ip, port));

        Injector injector = Guice.createInjector(new ApplicationModule());
        JerseyGuiceUtils.install(new GuiceServiceLocatorGenerator(injector));

        HttpServerWrapper server = injector.getInstance(HttpServerWrapperFactory.class).getHttpServerWrapper(config);
        server.start();
        logger.info("Started jetty http server");
    }
}
