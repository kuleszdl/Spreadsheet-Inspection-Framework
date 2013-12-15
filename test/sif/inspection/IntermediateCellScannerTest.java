/**
 * 
 */
package sif.inspection;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import sif.frontOffice.FrontDesk;
import sif.model.elements.containers.AbstractElementList;
import sif.model.elements.custom.IntermediateCell;
import sif.model.inspection.InspectionRequest;

/**
 * @author Manuel Lemcke
 *
 */
public class IntermediateCellScannerTest {
	private static File inputFile = null;
	private static String inputPath = "test/sif/testdata/bafoeg-rueckzahlung_mod.xls";
	private static InspectionRequest request = null;
	
	@Before
	public void setUp() throws Exception {
		inputFile = new File(inputPath);
		request = FrontDesk.getInstance().requestNewInspection(
				"Input and output cell Scanning test", inputFile);
		FrontDesk.getInstance().scan();
	}
	
	@Test
	public void test() {
		AbstractElementList<IntermediateCell> icList = request.getInventory().getListFor(IntermediateCell.class);
		System.out.println(icList.getNumberOfElements());
		int occurs = 0;
		System.out.println(icList.getElements().get(0).getCell().getWorksheet().getName());
		for (IntermediateCell iCell : icList.getElements()) {
			System.out.println(iCell.getLocation());
			if (iCell.getCell().getWorksheet().getName().equals("Rechner")) {
				occurs++;
			}
		}
		System.out.println(occurs);
	}

}
