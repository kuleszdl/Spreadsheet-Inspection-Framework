package sif.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("heartbeat")
public class HeartbeatResource {

    public static final String MSG = "I'm still alive";

    private final Logger logger = LoggerFactory.getLogger(sif.api.OdsResource.class);

    /**
     * Webservice entry point for heartbeat
     *
     * @return will always return "I'm still alive"
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHeartbeat() {
        logger.debug("called HeartbeatResource");
        return MSG;
    }

}
