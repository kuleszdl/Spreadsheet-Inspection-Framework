package sif.api;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.InspectionResponse;
import sif.io.policy.PolicyIO;
import sif.io.spreadsheet.SpreadsheetIO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("ooxml")
public class OoxmlResource extends BaseResource {

    private final Logger logger = LoggerFactory.getLogger(OoxmlResource.class);

    /**
     * OoxmlResource Constructor
     *
     * @param inspectionService inspectionService
     * @param spreadsheetIO spreadsheetIO
     * @param policyIO policyIO
     */
    @Inject
    OoxmlResource(
            InspectionService inspectionService,
            @Named("ooxml") SpreadsheetIO spreadsheetIO,
            @Named("xml") PolicyIO policyIO
    ) {
        super(inspectionService, spreadsheetIO, policyIO);
    }

    /**
     * Webservice entry point for ooxml testing
     *
     * @param policyBodyPart policy file
     * @param spreadsheetBodyPart model file
     * @return xml report for inspection
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_XML + "; charset=UTF-8")
    public InspectionResponse getInspectionReport(
        @FormDataParam("policy") FormDataBodyPart policyBodyPart,
        @FormDataParam("spreadsheet") FormDataBodyPart spreadsheetBodyPart
    ) throws Exception {
        logger.info("SIF: receiving ooxml inspection request");
        PolicyAction(policyBodyPart);
        SpreadsheetAction(spreadsheetBodyPart);
        InspectionResponse response = getInspectionService().generateResponse();
        logger.info("SIF: sending inspection response");
        return response;
    }
}