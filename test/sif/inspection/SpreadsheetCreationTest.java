/**
 * 
 */
package sif.inspection;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

import sif.frontOffice.FrontDesk;
import sif.model.elements.basic.cell.Cell;
import sif.model.inspection.InspectionRequest;

/**
 * @author Manuel Lemcke
 *
 */
public class SpreadsheetCreationTest {
	
	/**
	 * Tests if the correct number of cells in the worksheet "Rechner" match the
	 * pattern for input cells. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIncCellRefsRechnerSheet1() throws Exception {
		File inputFile = new File("test/sif/testdata/bafoeg-rueckzahlung_mod.xls");
		InspectionRequest request = FrontDesk.getInstance().requestNewInspection(
				"Input and output cell Scanning test", inputFile);
		
		FrontDesk.getInstance().scan();
		
		ArrayList<Cell> cellList = request.getInventory().getListFor(Cell.class).getElements();
		
		int occurrencesSheet1 = 0;
		for (Cell cell : cellList) {
			if (!cell.getIncomingReferences().isEmpty() && cell.getOutgoingReferences().isEmpty() && cell.getWorksheet().getName().equals("Rechner")) {
//				System.out.println(cell.getLocation());
				occurrencesSheet1++;
			}
		}
		
		assertFalse(occurrencesSheet1 > 8);
	}
	
	/**
	 * Tests if the correct number of cells in the worksheet "Rechner" match the
	 * pattern for input cells. 
	 * 
	 * Currently this fails due to Bug #971
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIncCellRefsRechnerSheet2() throws Exception {
		File inputFile = new File("test/sif/testdata/bafoeg-rueckzahlung.xls");
		InspectionRequest request = FrontDesk.getInstance().requestNewInspection(
				"Input and output cell Scanning test", inputFile);
		
		FrontDesk.getInstance().scan();
		
		ArrayList<Cell> cellList = request.getInventory().getListFor(Cell.class).getElements();
		
		int occurrencesSheet1 = 0;
		for (Cell cell : cellList) {
			if (!cell.getIncomingReferences().isEmpty() && cell.getOutgoingReferences().isEmpty() && cell.getWorksheet().getName().equals("Rechner")) {
				System.out.println(cell.getLocation());
				occurrencesSheet1++;
			}
		}
		
		assertFalse(occurrencesSheet1 > 8);
	}

}
