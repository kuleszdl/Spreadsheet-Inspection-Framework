/**
 * 
 */
package sif.IO;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import sif.frontOffice.FrontDesk;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.inspection.InspectionRequest;

/**
 * @author Manuel Lemcke
 * 
 */
public class SpreadsheetNumericReadTest {
	
	Double expectedValue = 4492.0;

	@Test
	public void test() throws Exception {
		InspectionRequest req = FrontDesk.getInstance().requestNewInspection("NumericTest",
				new File("test/sif/testdata/bafoeg-rueckzahlung4.xls"));		
		
		Worksheet rechnerSheet = req.getInventory().getSpreadsheet().getWorksheetFor("Rechner");
		Cell cell = rechnerSheet.getCellAt(2, 36);
		assertTrue(cell.getNumericContent().equals(expectedValue));
		System.out.println(cell.getLocation() + ": " + cell.getNumericContent());
	}

}
