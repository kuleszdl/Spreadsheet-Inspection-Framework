/**
 * 
 */
package sif.IO.spreadsheet.poi;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.TestInput;

/**
 * Provides means to write data into a POI Workbook
 * 
 * @author Manuel Lemcke
 * 
 */
public class POIWriter implements ISpreadsheetWriter {

	/**
	 * Creates a named cell in a workbook
	 * 
	 * @param workbook
	 * @param cellName
	 * @param sheetName
	 */
	public void createName(Workbook workbook, String cellName, String sheetName) {
		Name namedCel2 = workbook.createName();
		namedCel2.setNameName(cellName);
		String reference = sheetName + "!A1"; // cell reference
		namedCel2.setRefersToFormula(reference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sif.technicalDepartment.equipment.testing.tools.IDynamicSpreadsheetWriter
	 * #realizePreconditions(sif.model.inspection.DynamicInspectionRequest)
	 */
	public Workbook insertTestInput(DynamicPolicyRule rule, Object spreadsheet)
			throws Exception {
		// Workbook workbook = request.getPoiWorkbook();

		if (spreadsheet instanceof Workbook) {
			Workbook workbook = (Workbook) spreadsheet;
			DynamicPolicyRule dynamicRule = rule;
			ArrayList<TestInput> testInputs = dynamicRule.getTestInputs();

			for (TestInput testInput : testInputs) {
				try {
					CellReference address = new CellReference(
							testInput.getTarget());
					Sheet sheet;

					// Always use sheet 0 hardcoded, since switching sheets seems to be currently broken:
					sheet =workbook.getSheetAt(0);
					
					// FIXME: Make this work again
//					if (address.getSheetName() != null) {
//						sheet = workbook.getSheet(address.getSheetName());
//					} else {
//						int activeIndex = workbook.getActiveSheetIndex();
//						sheet = workbook.getSheetAt(activeIndex);
//					}

					Row row = sheet.getRow(address.getRow());
					Cell cell = row.getCell(address.getCol());

					switch (testInput.getType()) {
					case BOOLEAN:
						boolean booleanValue = Boolean.parseBoolean(testInput
								.getValue());
						cell.setCellValue(booleanValue);
						break;
					case NUMERIC:
						double doubleValue = Double.parseDouble(testInput
								.getValue().replace(',', '.'));
						cell.setCellValue(doubleValue);
						break;
					case TEXT:
						cell.setCellValue(testInput.getValue());
						break;
					case ERROR:
						// TODO Finale Loesung finden
						cell.setCellValue(testInput.getValue());
						break;
					case BLANK:
						// TODO Darf/Soll eine Zelle vom Typ BLANK einen Wert
						// erhalten?
						break;
					default:
						break;
					}
				} catch (Exception e) {
					throw e;
				}
			}

			return workbook;
		} else {
			throw new Exception("The spreadsheet object is not of the type Workbook!");
		}
	}
}
