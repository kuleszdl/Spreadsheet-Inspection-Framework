/**
 * 
 */
package sif.inspection;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sif.frontOffice.FrontDesk;
import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.containers.AbstractElementList;
import sif.model.elements.custom.InputCell;
import sif.model.elements.custom.OutputCell;
import sif.model.inspection.InspectionRequest;

/**
 * @author Manuel Lemcke
 *
 */
public class InputOutputCellScannerTest {
	private static File inputFile = null;
	private static InspectionRequest request = null;;
	
	@Before
	public void setUp() throws Exception {
		inputFile = new File("test/sif/testdata/input03_s1.xls");
		request = FrontDesk.getInstance().requestNewInspection(
				"Input and output cell Scanning test", inputFile);
		FrontDesk.getInstance().scan();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/*
	 * A unreliable test that only checks if the number of input cells is in a certain interval 
	 */
	@Test
	public void inputCellTest() throws Exception {
		AbstractElementList<InputCell> inputCellList = request.getInventory().getListFor(InputCell.class);
		Assert.assertTrue(inputCellList != null && inputCellList.getNumberOfElements() > 0);
		
		ArrayList<String> a1List = new ArrayList<String>();
		
		for(InputCell cell : inputCellList.getElements()) {
			String location = cell.getLocation();
			String a1String = location.substring(location.indexOf("!") + 1);
			a1List.add(a1String);
			
			if (cell.getValueAsString().equals("BLANK")) {
				AbstractReference incomingRef = cell.getCell().getIncomingReferences().get(0);				
				System.out.println(cell.getCell().getLocation() + ": " + cell.getValueAsString());
				System.out.println(cell.getCell().getIncomingReferences().size() + " Incoming refs. First: " + incomingRef.getLocation());
				System.out.println("---------------------------------------------------------------");
			} else {
				System.out.println(cell.getCell().getLocation() + ": " + cell.getValueAsString());	
			}
		}
		
		Assert.assertTrue(a1List.contains("C5"));
		Assert.assertTrue(a1List.contains("C12"));
		Assert.assertFalse(a1List.contains("E6"));
		
		System.out.println(inputCellList.getNumberOfElements());
		
		Assert.assertTrue(inputCellList.getNumberOfElements() == 351);
	}

	/*
	 *	Tests the scanning of output cells by checking if all expected cells 
	 *  are included in the list and only them.
	 *  
	 */
	@Test
	public void outputCellTest() throws Exception {
		boolean[] containsCells = new boolean[5];
		String[] addresses = {"C54", "C55", "C56", "C57", "C58"};
		
		AbstractElementList<OutputCell> outputCellList = request.getInventory().getListFor(OutputCell.class);
		
		Assert.assertTrue(outputCellList != null && outputCellList.getNumberOfElements() > 0);
		
//		System.out.println("Output Cells: ");		
		for(OutputCell cell : outputCellList.getElements()) {
			for (int i = 0; i < addresses.length; i++) {
				if (cell.getLocation().endsWith(addresses[i])) {
					containsCells[i] = true;
				}
			}
//			System.out.println(cell.getLocation() + ": " + cell.getValueAsString());
		}
		
		boolean result = true;
		for (boolean containsCell : containsCells) {
			// nur wenn alle bisherigen Zellen enthalten waren und die aktuelle auch enthalten ist
			// ist der Test "ok" also negativ
			result = (result && containsCell);
		}
		Assert.assertTrue(result && outputCellList.getNumberOfElements() == 5);
	}
}
