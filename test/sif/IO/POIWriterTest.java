/**
 * 
 */
package sif.IO;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.junit.Test;

import sif.IO.spreadsheet.ISpreadsheetIO;
import sif.IO.spreadsheet.poi.POIWriter;
import sif.IO.xml.SifMarshaller;
import sif.frontOffice.FrontDesk;
import sif.model.inspection.DynamicInspectionRequest;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.policyrule.DynamicPolicyRule;

/**
 * @author Manuel Lemcke
 * 
 */
public class POIWriterTest {

	String filepath = "test/sif/testdata/bafoeg-rueckzahlung.xls";
	String policyPath = "test/sif/testdata/bafoeg.xml";
	
	String inputCell1 = "Rechner!B8";
	String inputCell2 = "Rechner!B17";
	String resultCell = "Rechner!B34";
	
	Double expectedValue1 = 1000.0;
	Double expectedValue2 = 1025.0;
	
	/**
	 * Test method for
	 * {@link sif.IO.spreadsheet.poi.POIWriter#insertTestInput(sif.model.policy.policyrule.DynamicPolicyRule, sif.model.inspection.DynamicInspectionRequest)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInsertTestInput() throws Exception {
		FrontDesk desk = FrontDesk.getInstance();

		// read policy & rule
		File spreadsheetFile = new File(filepath);
		DynamicPolicy policy = SifMarshaller.unmarshal(new File(policyPath));
		@SuppressWarnings("unchecked")
		DynamicInspectionRequest<Workbook> req = (DynamicInspectionRequest<Workbook>) desk.requestNewDynamicInspection(
				"TestInputInsertionTest", spreadsheetFile);
		FrontDesk.getInstance().scan();
		FrontDesk.getInstance().register(policy);
		FrontDesk.getInstance().setPolicy(policy);
 
		DynamicPolicyRule rule = (DynamicPolicyRule) policy.getRuleByName("greaterThan0");
		assertTrue("No rule with name \"greaterThan0\" was found", rule != null);
		
		// write TestInput in PoiWorkbook
		POIWriter writer = new POIWriter();		
		writer.insertTestInput(rule, req.getExternalSpreadsheet());
		Workbook wb = req.getExternalSpreadsheet();
		
	    CellReference cellRef = new CellReference(inputCell1);
	    Sheet sheet2 = wb.getSheet(cellRef.getSheetName());
		Row row = sheet2.getRow(cellRef.getRow());
		Cell cell = row.getCell(cellRef.getCol());
		
		System.out.println(cell.toString());
		assertTrue(cell.toString().equals(expectedValue1.toString()));
		
		cellRef = new CellReference(inputCell2);
		sheet2 = wb.getSheet(cellRef.getSheetName());
		row = sheet2.getRow(cellRef.getRow());
		cell = row.getCell(cellRef.getCol());
		
		System.out.println(cell.toString());
		assertTrue(cell.toString().equals(expectedValue2.toString()));
		
		cellRef = new CellReference(resultCell);
		sheet2 = wb.getSheet(cellRef.getSheetName());
		row = sheet2.getRow(cellRef.getRow());
		cell = row.getCell(cellRef.getCol());
		
		// Just for information print result
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		if (cell!=null) {
		    switch (evaluator.evaluateFormulaCell(cell)) {
		        case Cell.CELL_TYPE_BOOLEAN:
		            System.out.println(cell.getBooleanCellValue());
		            break;
		        case Cell.CELL_TYPE_NUMERIC:
		            System.out.println(cell.getNumericCellValue());
		            break;
		        case Cell.CELL_TYPE_STRING:
		            System.out.println(cell.getStringCellValue());
		            break;
		        case Cell.CELL_TYPE_BLANK:
		            break;
		        case Cell.CELL_TYPE_ERROR:
		            System.out.println(cell.getErrorCellValue());
		            break;
		
		        // CELL_TYPE_FORMULA will never occur
		        case Cell.CELL_TYPE_FORMULA: 
		            break;
		    }
		}
	}
	
	public void printWorksheetContent() throws Exception {
		FrontDesk desk = FrontDesk.getInstance();

		// read policy & rule
		DynamicPolicy policy = SifMarshaller.unmarshal(new File(policyPath));
		
		@SuppressWarnings("unchecked")
		DynamicInspectionRequest<Workbook> req = (DynamicInspectionRequest<Workbook>) desk.requestNewDynamicInspection(
				"fubar", new File(filepath));
		FrontDesk.getInstance().scan();
		FrontDesk.getInstance().register(policy);
		FrontDesk.getInstance().setPolicy(policy);
 
		DynamicPolicyRule rule = (DynamicPolicyRule) policy.getRuleByName("greaterThan0");
		assertTrue("No rule with name \"greaterThan0\" was found", rule != null);
		
		// write TestInput in PoiWorkbook
		POIWriter writer = new POIWriter();		
		writer.insertTestInput(rule, req.getExternalSpreadsheet());
		Workbook wb = req.getExternalSpreadsheet();
		
	    Sheet sheet1 = wb.getSheetAt(0);
	    for (Row row : sheet1) {
	        for (Cell cell : row) {
	            CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
	            System.out.print(row.getRowNum() + ", " + cell.getColumnIndex());
	            System.out.print(" - ");	            
	            System.out.print(cellRef.formatAsString());
	            System.out.print(" - ");

	            switch (cell.getCellType()) {
	                case Cell.CELL_TYPE_STRING:
	                    System.out.println(cell.getRichStringCellValue().getString());
	                    break;
	                case Cell.CELL_TYPE_NUMERIC:
	                    if (DateUtil.isCellDateFormatted(cell)) {
	                        System.out.println(cell.getDateCellValue());
	                    } else {
	                        System.out.println(cell.getNumericCellValue());
	                    }
	                    break;
	                case Cell.CELL_TYPE_BOOLEAN:
	                    System.out.println(cell.getBooleanCellValue());
	                    break;
	                case Cell.CELL_TYPE_FORMULA:
	                    System.out.println(cell.getCellFormula());
	                    break;
	                default:
	                    System.out.println();
	            }
	        }
	    }
	}

}
