package sif.io.spreadsheet;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.model.Cell;
import sif.model.ElementFactory;
import sif.model.Spreadsheet;
import sif.model.Worksheet;
import sif.model.values.ValueFactory;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Iterator;

@RequestScoped
public class OoxmlSpreadsheetIO extends BaseSpreadsheetIO {

    private final Logger logger = LoggerFactory.getLogger(OoxmlSpreadsheetIO.class);
    private final ElementFactory elementFactory;
    private final OoxmlFormulaTransformer formulaTransformer;
    private Workbook workbook;
    private boolean firstRun;
    private FormulaEvaluator evaluator;

    @Inject
    private ValueFactory valueFactory;

    @Inject
    OoxmlSpreadsheetIO(
            Spreadsheet spreadsheet,
            ElementFactory elementFactory,
            OoxmlFormulaTransformer formulaTransformer
    ) {
        super(spreadsheet);
        this.elementFactory = elementFactory;
        this.formulaTransformer = formulaTransformer;
        this.firstRun = true;
    }

    @Override
    public Spreadsheet createSpreadsheet(InputStream inputStream) throws InvalidSpreadsheetException {
        logger.debug("calling createPOIWorkbook() in OoxmlSpreadsheetIO");
        createPOIWorkbook(inputStream);
        logger.debug("calling createAllModelWorksheets() in OoxmlSpreadsheetIO");
        createAllModelWorksheets();
        logger.debug("calling updateAllModelWorksheets() in OoxmlSpreadsheetIO");
        updateAllModelWorksheets();
        return getSpreadsheet();
    }

    @Override
    public void updateSpreadsheetValues() {
        logger.debug("calling updateSpreadsheetValues() in OoxmlSpreadsheetIO");
        for (Cell cell : getSpreadsheet().getCells()) {
            // check if cell has backup value which marks a changed cell that needs to be written to the POI workbook
            if (cell.isDirty()) {
                org.apache.poi.ss.usermodel.Cell poiCell = getPOICell(cell);
                if (poiCell != null)
                    updatePOICell(poiCell, cell);
                cell.setDirty(false);
            }
        }
    }

    @Nullable
    private org.apache.poi.ss.usermodel.Cell getPOICell(Cell cell) {
        Sheet poiSheet = workbook.getSheet(cell.getCellAddress().getWorksheet().getKey());

        if (poiSheet != null) {
            Row poiRow = poiSheet.getRow(cell.getCellAddress().getRow().getIndex());
            if (poiRow != null) {
                return poiRow.getCell(cell.getCellAddress().getColumn().getIndex());
            } else {
                logger.error("Can not get POIRow with Index '{}' in OoxmlSpreadsheetIO.updatePOIWorkbook()", cell.getCellAddress().getRow().getIndex());
                return null;
            }
        } else {
            logger.error("Can not get POISheet with Index '{}' in OoxmlSpreadsheetIO.updatePOIWorkbook()", cell.getCellAddress().getWorksheet().getIndex());
            return null;
        }
    }

    @Override
    public void calculateFormulaValues() {
        logger.debug("calling calculateFormulaValues() in OoxmlSpreadsheetIO");
        if (evaluator != null)
            evaluator.evaluateAll();
    }

    @Override
    public void updateModelValues() {
        logger.debug("calling updateModelValues() in OoxmlSpreadsheetIO");
        this.firstRun = false;
        updateAllModelWorksheets();
    }

    /**
     *
     * @param inputStream input stream of model
     * @throws InvalidSpreadsheetException the provided model was not valid
     */
    private void createPOIWorkbook(InputStream inputStream) throws InvalidSpreadsheetException {
        try {
            workbook = WorkbookFactory.create(inputStream);
            formulaTransformer.initializeFormulaTransformer(workbook);
            evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        } catch (Exception e) {
            logger.error(e.getClass().getSimpleName());
            logger.debug(e.getClass().getSimpleName(), e);
            throw new InvalidSpreadsheetException();
        }
    }

    /***
     * Transforms the cell contents of the given {@link Sheet} from the
     * POI-model to the given {@link Worksheet} form the internal model.
     *
     * @param worksheet The given spreadsheet form the internal model.
     * @param poiSheet The given sheet from the POI-model.
     */
    private void updateModelWorksheet(Worksheet worksheet, Sheet poiSheet) {

        Iterator<Row> poiRowIterator = poiSheet.rowIterator();

        // Iterate over Rows.
        while (poiRowIterator.hasNext()) {
            Row poiRow = poiRowIterator.next();

            Iterator<org.apache.poi.ss.usermodel.Cell> poiCellIterator = poiRow.cellIterator();

            // Iterate over cells.
            while (poiCellIterator.hasNext()) {
                org.apache.poi.ss.usermodel.Cell poiCell = poiCellIterator.next();
                // Transform cell content.
                Cell cell = worksheet.getCell(poiCell.getAddress().formatAsString());
                updateModelCell(cell, poiCell);
            }
        }
    }

    private void updatePOICell(org.apache.poi.ss.usermodel.Cell poiCell, Cell cell) {
        switch (cell.getValue().getType()) {
            case BOOLEAN:
                boolean booleanValue = (Boolean) cell.getValue().getValueObject();
                poiCell.setCellValue(booleanValue);
                break;
            case NUMERIC:
                Double doubleValue = (Double) cell.getValue().getValueObject();
                poiCell.setCellValue(doubleValue);
                break;
            case STRING:
                String stringValue = (String) cell.getValue().getValueObject();
                poiCell.setCellValue(stringValue);
                break;
            default:
                break;
        }

        evaluator.notifyUpdateCell(poiCell);
    }


    /**
     * Updates the value of a {@link Cell} with the value of a given {@link org.apache.poi.ss.usermodel.Cell}
     *
     * @param cell {@link Cell}
     * @param poiCell {@link org.apache.poi.ss.usermodel.Cell}
     */
    private void updateModelCell(Cell cell, org.apache.poi.ss.usermodel.Cell poiCell) {

        boolean isFormula = false;

        int poiType = poiCell.getCellType();
        if (poiType == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA) {
            isFormula = true;

            if (!firstRun && (cell.getBackupValue() == null)) {
                cell.setBackupValue(cell.getValue());
            }

            poiType = poiCell.getCachedFormulaResultType();
        }

        // Set cell value.
        switch (poiType) {
            // Boolean cell.
            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
                cell.setValue(valueFactory.createBooleanValue(poiCell.getBooleanCellValue()));
                break;
            // Numeric cell.
            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
                Double number = poiCell.getNumericCellValue();
                // @TODO:schuelfr check for date?
                cell.setValue(valueFactory.createNumericValue(number));
                break;
            // String cell.
            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
                cell.setValue(valueFactory.createStringValue(poiCell.getStringCellValue()));
                break;
            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR:
                Byte myByte = poiCell.getErrorCellValue();
                cell.setValue(valueFactory.createErrorValue(ErrorEval.getText(myByte), myByte.intValue()));
                break;
            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK:
                cell.setValue(valueFactory.createNullValue());
                break;
            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
                break;
            // Other cell.
            default:
                logger.error("not implemented cell type with ID '{}' found", poiCell.getCellType());
                break;
        }

        if (isFormula && firstRun) {
            // this is needed to disable formulaEvaluation for scenarios
            // we only need to parse the formulas at the first spreadsheet import
            formulaTransformer.transformFormulaContent(cell, poiCell);
        }

    }

    private void createAllModelWorksheets() {
        // Transform attributes and structure.
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet poiSheet = workbook.getSheetAt(i);
            // Create new spreadsheet.
            Worksheet worksheet = elementFactory.createWorksheet(poiSheet.getSheetName(), i);
            worksheet.setSpreadsheet(getSpreadsheet());
            worksheet.setHidden(workbook.isSheetHidden(workbook.getSheetIndex(poiSheet)));
            getSpreadsheet().add(worksheet);
        }
    }

    private void updateAllModelWorksheets() {

        // Transform cell contents. Needs to be done after worksheets are created.
        // rows, columns and cells in the worksheet are created on the fly when accessed
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet poiSheet = workbook.getSheetAt(i);
            Worksheet worksheet = getSpreadsheet().getWorksheet(poiSheet.getSheetName());
            // Transform cell contents.
            updateModelWorksheet(worksheet, poiSheet);
        }
    }
}
