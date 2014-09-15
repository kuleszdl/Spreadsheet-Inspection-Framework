/**
 * 
 */
package sif.inspection;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import sif.IO.xml.SifMarshaller;
import sif.frontOffice.FrontDesk;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.inspection.DynamicInspectionRequest;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.PolicyList;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.technicalDepartment.equipment.testing.tools.POISpreadsheetPreparator;

/**
 * @author Manuel Lemcke
 *
 */
public class PreparatorTest {

	String filepath = "test/sif/testdata/bafoeg-rueckzahlung.xls";
	String policyPath = "test/sif/testdata/bafoeg.xml";
	
	String inputCell1 = "Rechner!B8";
	String inputCell2 = "Rechner!B17";
	
	Double expectedValue1 = 1000.0;
	Double expectedValue2 = 1025.0;
	
	/**
	 * Test method for
	 * {@link sif.IO.spreadsheet.poi.POIWriter#insertTestInput(sif.model.policy.policyrule.DynamicPolicyRule, sif.model.inspection.DynamicInspectionRequest)}
	 * .
	 * 
	 * Expects the policy bafoeg.xml to define two TestInputs which set
	 * B8 to 1000 and B17 to 1025 to work.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInsertTestInput() throws Exception {
		FrontDesk desk = FrontDesk.getInstance();

		// read policy & rule
		PolicyList polList = SifMarshaller.unmarshal(new File(policyPath));

		assertTrue(polList != null);
		
		DynamicPolicy policy = polList.getCompletePolicy();

		
		@SuppressWarnings("unchecked")
		DynamicInspectionRequest<Workbook> req = (DynamicInspectionRequest<Workbook>) desk.requestNewDynamicInspection(
				"TestInputInsertionTest", new File(filepath));
		FrontDesk.getInstance().scan();
		FrontDesk.getInstance().register(policy);
		FrontDesk.getInstance().setPolicy(policy);
		
		POISpreadsheetPreparator prep = new POISpreadsheetPreparator(req);
		
		// prepare spreadsheet
		Spreadsheet ss = prep.prepare((DynamicPolicyRule) policy.getRuleByName("greaterThan0"), req.getExternalSpreadsheet());
		
		// read prepared spreadsheet
		Worksheet sheet = ss.getWorksheetFor("Rechner");
		
		Cell cell = sheet.getCellAt(2, 8);		
//		System.out.println(cell.getLocation() + ": " + cell.getStringRepresentation());
		assertTrue(cell.getNumericContent() == expectedValue1.doubleValue());
		
		cell = sheet.getCellAt(2, 17);
//		System.out.println(cell.getLocation() + ": " + cell.getStringRepresentation());
		assertTrue(cell.getNumericContent() == expectedValue2.doubleValue());				
	}

}
