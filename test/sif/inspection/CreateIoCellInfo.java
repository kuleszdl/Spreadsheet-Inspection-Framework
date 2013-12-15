/**
 * 
 */
package sif.inspection;

import java.io.File;

import org.junit.Test;

import sif.frontOffice.FrontDesk;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.policyrule.IOCellInfo;

/**
 * @author Manuel Lemcke
 *
 */
public class CreateIoCellInfo {
	
	private final static File inputFile = new File(
			"test/sif/testdata/bafoeg-rueckzahlung.xls");

	/**
	 * Test method for {@link sif.frontOffice.FrontDesk#scanAndCreatePolicy(java.lang.String, java.io.File)}.
	 * @throws Exception 
	 */
	@Test
	public void testScanAndCreatePolicy() throws Exception {
		FrontDesk desk = FrontDesk.getInstance();		
		
		DynamicPolicy policy;
		policy = desk.scanAndCreatePolicy("ioTestReq1", inputFile); 
		
//		List<IOCellInfo> inputInfos = policy.getInputCells();
//		List<IOCellInfo> outputInfos = policy.getInputCells();
		
		System.out.println("Input Cells " + policy.getInputCells().size());
		System.out.println("Output Cells " + policy.getOutputCells().size());
		
		for (IOCellInfo info : policy.getInputCells()) {
			System.out.println(info.getA1Address());
		}

	}

}
