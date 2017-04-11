package sif.api;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.io.policy.PolicyIO;
import sif.io.spreadsheet.SpreadsheetIO;
import sif.testcenter.InspectionResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("ods")
public class OdsResource extends BaseResource {

    private final Logger logger = LoggerFactory.getLogger(OdsResource.class);

    /**
     * @param inspectionService main services class
     */
    @Inject
    OdsResource(
            InspectionService inspectionService,
            @Named("ods") SpreadsheetIO spreadsheetIO,
            @Named("xml") PolicyIO policyIO)
    {
        super(inspectionService, spreadsheetIO, policyIO);
    }

    /**
     * Webservice entry point for ods testing
     *
     * @param policyBodyPart policy file
     * @param spreadsheetBodyPart model file
     * @return xml report for inspection
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_XML)
    public InspectionResponse getInspectionReport(
            @FormDataParam("policy") FormDataBodyPart policyBodyPart,
            @FormDataParam("spreadsheet") FormDataBodyPart spreadsheetBodyPart
    )throws Exception {
        logger.info("incoming ods inspection request");
        PolicyAction(policyBodyPart);
        SpreadsheetAction(spreadsheetBodyPart);
        return getInspectionService().generateResponse();
    }
}