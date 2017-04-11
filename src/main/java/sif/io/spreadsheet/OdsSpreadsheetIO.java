package sif.io.spreadsheet;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.model.Spreadsheet;

import java.io.InputStream;

@RequestScoped
public class OdsSpreadsheetIO extends BaseSpreadsheetIO {

    private final Logger logger = LoggerFactory.getLogger(OdsSpreadsheetIO.class);

    @Inject
    OdsSpreadsheetIO(Spreadsheet spreadsheet) {
        super(spreadsheet);
    }

    @Override
    public Spreadsheet createSpreadsheet(InputStream spreadsheetInputStream) throws InvalidSpreadsheetException {
        logger.debug("calling createSpreadsheet() in OdsSpreadsheetIO");
        try {
            // @TODO:ods
        } catch (Exception e) {
            logger.error(e.getClass().getSimpleName());
            logger.debug(e.getClass().getSimpleName(), e);
            throw new InvalidSpreadsheetException();
        }
        return getSpreadsheet();
    }

    @Override
    public void updateSpreadsheetValues() {
        logger.debug("calling updateSpreadsheetValues() in OdsSpreadsheetIO");
        // @TODO:ods
    }

    @Override
    public void calculateFormulaValues() {
        logger.debug("calling calculateFormulaValues() in OdsSpreadsheetIO");
        // @TODO:ods
    }

    @Override
    public void updateModelValues() {
        logger.debug("calling updateModelValues() in OdsSpreadsheetIO");
        // @TODO:ods
    }
}
