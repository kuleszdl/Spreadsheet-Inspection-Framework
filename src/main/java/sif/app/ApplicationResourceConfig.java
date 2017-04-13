package sif.app;

import com.google.inject.Inject;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.MessageProperties;
import org.glassfish.jersey.server.ResourceConfig;

class ApplicationResourceConfig extends ResourceConfig {

    @Inject
    ApplicationResourceConfig() {
        // include all resources in sif.api
        packages("sif.api");
        // enable multipart feature
        register(MultiPartFeature.class);

        property(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
        property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
        property(CommonProperties.JSON_PROCESSING_FEATURE_DISABLE, true);
        property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);
        property(MessageProperties.XML_FORMAT_OUTPUT, true);
    }
}
