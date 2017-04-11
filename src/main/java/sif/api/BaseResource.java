package sif.api;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.InspectionRequest;
import sif.io.policy.NoPolicyException;
import sif.io.policy.PolicyIO;
import sif.io.spreadsheet.NoSpreadsheetException;
import sif.io.spreadsheet.SpreadsheetIO;
import sif.model.Spreadsheet;

import java.io.InputStream;

abstract class BaseResource {

    private final Logger logger = LoggerFactory.getLogger(BaseResource.class);

    private final InspectionService inspectionService;
    private final SpreadsheetIO spreadsheetIO;
    private final PolicyIO policyIO;

    BaseResource(InspectionService inspectionService, SpreadsheetIO spreadsheetIO, PolicyIO policyIO) {
        this.inspectionService = inspectionService;
        this.spreadsheetIO = spreadsheetIO;
        this.policyIO = policyIO;
    }

    InspectionService getInspectionService() {
        return inspectionService;
    }

    void PolicyAction(FormDataBodyPart filePart) {
        try {
            if (filePart != null) {
                InspectionRequest inspectionRequest = policyIO.createInspectionRequest(filePart.getEntityAs(InputStream.class));
                inspectionService.getSpreadsheetInventory().setInspectionRequest(inspectionRequest);
            } else {
                throw new NoPolicyException();
            }
        } catch (Exception e) {
            logger.error(e.getClass().getSimpleName());
            logger.debug(e.getClass().getSimpleName(), e);
            inspectionService.getSpreadsheetInventory().getInspectionResponse().addError(e.getMessage());
        }
    }

    void SpreadsheetAction(FormDataBodyPart filePart) {
        try {
            if (filePart != null) {
                Spreadsheet spreadsheet = spreadsheetIO.createSpreadsheet(filePart.getEntityAs(InputStream.class));
                spreadsheet.setName(filePart.getContentDisposition().getFileName());
                inspectionService.getSpreadsheetInventory().setSpreadsheet(spreadsheet);
                inspectionService.getSpreadsheetInventory().setSpreadsheetIO(spreadsheetIO);
            } else {
                throw new NoSpreadsheetException();
            }
        } catch (Exception e) {
            logger.error(e.getClass().getSimpleName());
            logger.debug(e.getClass().getSimpleName(), e);
            inspectionService.getSpreadsheetInventory().getInspectionResponse().addError(e.getMessage());
        }
    }
}
