package sif.IO.spreadsheet.poi;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.ss.formula.FormulaParsingWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import sif.IO.spreadsheet.ISpreadsheetIO;
import sif.IO.spreadsheet.InvalidSpreadsheetFileException;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.elements.basic.spreadsheet.SpreadsheetProperties;
import sif.model.elements.basic.worksheet.Row;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.inspection.DynamicInspectionRequest;
import sif.model.inspection.SpreadsheetInventory;

/***
 * Common {@link ISpreadsheetIO} for .xls and .xlx spreadsheet files that uses
 * the <a href="http://poi.apache.org/spreadsheet/index.html"> Apache POI-SS
 * library</a> to access the spreadsheet files. Transformations that require
 * special treatment for .xls and .xlsx files are done in
 * {@link POISpreadsheetIO}.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public abstract class AbstractPOISpreadsheetIO implements ISpreadsheetIO {

	private POIFormulaTransformer poiFormulaTransformer = new POIFormulaTransformer(
			this);

	/***
	 * The {@link Workbook} of the POI-model created for the spreadsheetFile
	 * given in {@link #createInitialInventoryFor}.
	 */
	protected Workbook poiWorkbook;

	/***
	 * The {@link FormulaParsingWorkbook} for the {@link #poiWorkbook} to parse
	 * its formulas.
	 */
	protected FormulaParsingWorkbook formulaParsingWorkbook;

	/***
	 * The {@link Spreadsheet} in the internal model created from
	 * {@link #poiWorkbook}.
	 */
	protected Spreadsheet spreadsheet;

	/***
	 * Transforms the {@link CoreProperties} of the {@link #poiWorkbook} to the
	 * {@link SpreadsheetProperties} of the {@link #spreadsheet}.
	 */
	protected abstract void createCoreProperties();

	@Override
	public Spreadsheet createSpreadsheet(File spreadsheetFile)
			throws Exception {

		Workbook workbook = createWorkbook(spreadsheetFile);		
		this.createSpreadsheet(workbook, spreadsheetFile.getName());
		
		return this.spreadsheet;
	}

	/* (non-Javadoc)
	 * @see sif.IO.spreadsheet.ISpreadsheetIO#createSpreadsheet(org.apache.poi.ss.usermodel Workbook, java.lang.String)
	 */
	@Override
	public Spreadsheet createSpreadsheet(Object workbook, String fileName)
			throws Exception {
		if (workbook instanceof Workbook) {
			this.poiWorkbook = (Workbook) workbook;	
			
			// Create instances of SpreadsheetInventory and Spreadsheet.
			spreadsheet = new Spreadsheet();
			spreadsheet.setName(fileName);

			// Transform POI-workbook to internal model.
			this.transformWorkbook();
			
			return this.spreadsheet;
		} else {
			throw new Exception("Wrong spreadsheet class");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see sif.IO.spreadsheet.ISpreadsheetIO#createWorkbook(java.io.File)
	 */
	@Override
	public Workbook createWorkbook(File spreadsheetFile) 
			throws InvalidSpreadsheetFileException {		
		
		// Create a POI-workbook from the spreadsheet file.
		try {
			FileInputStream inp = new FileInputStream(spreadsheetFile);
			this.poiWorkbook = WorkbookFactory.create(inp);
		} catch (Exception e) {
			// Throw an exception if the spreadsheet can't be read by the
			// POI-library.
			InvalidSpreadsheetFileException exception = new InvalidSpreadsheetFileException(
					"Invalid spreadsheet file.");
			exception.setStackTrace(e.getStackTrace());
			exception.addAdditional(e);
			throw exception;
		}
		
		return this.poiWorkbook;
	}

	/***
	 * Creates the specific {@link #formulaParsingWorkbook} for the
	 * {@link POISpreadsheetIO}.
	 */
	protected abstract void initialize();

	/***
	 * Transforms the attributes and the cell structure of a given {@link Sheet}
	 * from the POI-model to the given {@link Worksheet} from internal model.
	 * 
	 * @param worksheet
	 *            The given worksheet form the internal model.
	 * 
	 * @param poiSheet
	 *            The given sheet from the POI-model.
	 */
	private void transformAttributesAndStructure(Worksheet worksheet,
			Sheet poiSheet) {
		worksheet.setName(poiSheet.getSheetName());
		worksheet.setHidden(poiWorkbook.isSheetHidden(poiWorkbook
				.getSheetIndex(poiSheet)));
		// Transform cell structure.
		transformCellStructure(worksheet, poiSheet);
	}

	/***
	 * Transforms the cell contents of the given {@link Sheet} from the
	 * POI-model to the given {@link Worksheet} form the internal model.
	 * 
	 * @param worksheet
	 *            The given worksheet form the internal model.
	 * 
	 * @param poiSheet
	 *            The given sheet from the POI-model.
	 */
	private void transformCellContents(Worksheet worksheet, Sheet poiSheet) {

		Iterator<org.apache.poi.ss.usermodel.Row> poiRowIterator = poiSheet
				.rowIterator();

		// Iterate over Rows.
		while (poiRowIterator.hasNext()) {
			org.apache.poi.ss.usermodel.Row poiRow = poiRowIterator.next();
			Row row = worksheet.getRowAt(poiRow.getRowNum() + 1);

			Iterator<org.apache.poi.ss.usermodel.Cell> poiCellIterator = poiRow
					.cellIterator();

			// Iterate over cells.
			while (poiCellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell poiCell = poiCellIterator
						.next();
				Cell cell = row.getCellAt(poiCell.getColumnIndex() + 1);

				// Transform cell content.
				transformContent(cell, poiCell);
			}
		}
	}

	/***
	 * Transforms the cell structure of the given
	 * {@link org.apache.poi.ss.usermodel.Cell} from the POI-model to the given
	 * {@link Cell} from the internal model.
	 * 
	 * @param cell
	 *            The given cell from the internal model.
	 * @param poiCell
	 *            The given cell from the POI-model.
	 */
	public void transformCellStructure(Cell cell,
			org.apache.poi.ss.usermodel.Cell poiCell) {

		// Switch to 1-based index.
		cell.setColumnIndex(poiCell.getColumnIndex() + 1);
		cell.setRowIndex(poiCell.getRowIndex() + 1);

	}

	/***
	 * Transforms the cell structure of the given sheet from the POI-model to
	 * the given worksheet from the internal model.
	 * 
	 * @param worksheet
	 *            The given worksheet form the internal model.
	 * 
	 * @param poiSheet
	 *            The given sheet from the POI-model.
	 */
	private void transformCellStructure(Worksheet worksheet, Sheet poiSheet) {

		Iterator<org.apache.poi.ss.usermodel.Row> poiRowIterator = poiSheet
				.iterator();

		// Iterate over rows.
		while (poiRowIterator.hasNext()) {
			org.apache.poi.ss.usermodel.Row poiRow = poiRowIterator.next();

			// Iterate over POI-cells.
			Iterator<org.apache.poi.ss.usermodel.Cell> poiCellIterator = poiRow
					.cellIterator();
			while (poiCellIterator.hasNext()) {
				org.apache.poi.ss.usermodel.Cell poiCell = poiCellIterator
						.next();
				// Transform cell-structure and add cell to worksheet.
				Cell cell = new Cell();
				cell.setWorksheet(worksheet);
				transformCellStructure(cell, poiCell);
				worksheet.add(cell);

			}
		}
	}

	/***
	 * Transforms the content of the given
	 * {@link org.apache.poi.ss.usermodel.Cell} to the given {@link Cell} from
	 * the internal model.
	 * 
	 * @param cell
	 *            The given cell from the internal model.
	 * @param poiCell
	 *            The given cell from the POI-model.
	 */
	public void transformContent(Cell cell,
			org.apache.poi.ss.usermodel.Cell poiCell) {

		// Set cell value.
		switch (poiCell.getCellType()) {
		// Boolean cell.
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
			cell.setBooleanContent(poiCell.getBooleanCellValue());
			break;
		// Error cell.
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR:
			// TODO: implement ERROR content.
			break;

		// Formula / Reference cell.
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:

			// Transform formula content.
			poiFormulaTransformer.transformFormulaContent(cell, poiCell);
			break;
		// Numeric cell.
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
			cell.setNumericContent(poiCell.getNumericCellValue());
			break;
		// String cell.
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
			cell.setTextContent(poiCell.getStringCellValue());
			break;

		// Blank cell.
		default:
			// Nothing to do;
			break;
		}
	}

	/***
	 * Transforms the {@link #poiWorkbook} to the {@link #spreadsheet}.
	 */
	private void transformWorkbook() {
		// Initialize SpreadsheetIO;
		initialize();

		// Create coreProperties
		createCoreProperties();

		// Create worksheets.
		transformWorksheets();
	}

	/***
	 * Creates the worksheets for the internal model.
	 */
	private void transformWorksheets() {

		// Transform attributes and structure.
		for (int i = 0; i < poiWorkbook.getNumberOfSheets(); i++) {
			// Create new worksheet.
			Worksheet worksheet = new Worksheet();
			worksheet.setSpreadsheet(spreadsheet);
			worksheet.setIndex(i + 1);
			Sheet poiSheet = poiWorkbook.getSheetAt(i);
			// Transform attributes and structure.
			transformAttributesAndStructure(worksheet, poiSheet);
			spreadsheet.add(worksheet);
		}

		// Transform cell contents. Needs to be done after structure is created
		// to establish cell references correctly.(No references to non-existing
		// Cell-Objects)
		for (int i = 0; i < poiWorkbook.getNumberOfSheets(); i++) {
			Sheet poiSheet = poiWorkbook.getSheetAt(i);
			Worksheet worksheet = spreadsheet.getWorksheetAt(i + 1);
			// Transform cell contents.
			transformCellContents(worksheet, poiSheet);
		}

	}
	
	/***
	 * Creates a new {@link DynamicInspectionRequest} for the given spreadsheet file with the
	 * given request name which also stores the POI {@link Workbook} which is created during 
	 * creation of the {@link Spreadsheet}
	 * 
	 * @param requestName
	 *            The given name for the request.
	 * @param spreadsheetFile
	 *            The given spreadsheet file.
	 * @return The newly created inspection request.
	 * @throws Exception
	 *             Throws an exception if the given spreadsheet file is invalid.
	 */
	public DynamicInspectionRequest<?> createInspectionRequest(String requestName,
			File spreadsheetFile) throws Exception {
		// Create request		
		Spreadsheet spreadsheet = null;
		
		Workbook workbook = this.createWorkbook(spreadsheetFile);
		
		DynamicInspectionRequest<Workbook> inspectionRequest = new DynamicInspectionRequest<Workbook>(workbook.getClass());
		inspectionRequest.setName(requestName);
		inspectionRequest.setSpreadsheetFile(spreadsheetFile);
		inspectionRequest.setExternalSpreadsheet(workbook);
		
		spreadsheet = this.createSpreadsheet(workbook, spreadsheetFile.getName());		
		
		// Create initial inventory.
		SpreadsheetInventory inventory = new SpreadsheetInventory();
		inventory.setSpreadsheet(spreadsheet);
		inventory.setSpreadsheetFile(spreadsheetFile);
		inspectionRequest.setInventory(inventory);

		// Add to list and set as current request;
		return inspectionRequest;
	}
}
