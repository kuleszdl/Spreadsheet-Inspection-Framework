package sif.inspection;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import sif.IO.ReportFormat;
import sif.frontOffice.FrontDesk;
import sif.model.inspection.InspectionRequest;
import sif.model.policy.Policy;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.configuration.ParameterConfiguration;
import sif.model.policy.policyrule.configuration.PolicyRuleConfiguration;
//import sif.model.inspection.InspectionRequest;

/**
 * Basic Test, checks whether a full inspection works
 * 
 * @author Sebastian Zitzelsberger
 * @author Daniel Kulesz
 *
 */
public class PolicyTest {

	private static String filePath = "test/sif/testdata/bafoeg-rueckzahlung.xls";
	private static String outpath = "test/sif/";
	
	private static String outFile = "bafoeg-rueckzahlung.xls.html";
	  
	  @AfterClass 
	  public static void testCleanup(){
	    File f = new File(outFile);
	    f.delete();
	  }
	  
	

	@Test
	public void TestPolicy() {
		
		try {
			File spreadsheetFile = new File(filePath);

			FrontDesk frontDesk = FrontDesk.getInstance();

			;
			// Create inspection request.
			InspectionRequest inspection = FrontDesk.getInstance()
					.requestNewInspection(spreadsheetFile.getName(),
							spreadsheetFile);

			// SpreadsheetInventory inventory = inspection.getInventory();
			// ElementList<Cell> cellList = inventory.getListFor(Cell.class);
			// ElementList<OutputCell> outputCellList = inventory
			// .getListFor(OutputCell.class);
			// ElementList<Formula> formulaList = inventory
			// .getListFor(Formula.class);
			// ElementList<CalculationNode> treeList =
			// inventory.getListFor(CalculationNode.class);

			// PrintUtilities.print(treeList);
			// PrintUtilities.print(inventory.getSpreadsheet());
			// PrintUtilities.print(inspection.getInventory().getSpreadsheet());

			// PrintUtilities.print(cellList);
			// System.builder.append();
			// System.builder.append();
			// Formula f = formulaList.getElements().get(0);
			// PrintUtilities.print(formulaList);
			// PrintUtilities.print(outputCellList);
			// //
			// inspectionManager.getAvailablePolicies();
			//
			// ScanningConfiguration configuration =
			// InspectionManager.getInstance().getDefaultScanningConfiguration();
			//
			// InspectionManager.getInstance().setScanningConfiguration(configuration);

			frontDesk.scan();

			Policy policy = FrontDesk.getInstance().getAvailablePolicies()
					.get("Basic Policy");

			AbstractPolicyRule rule1 = policy.getPolicyRules().get(
					"Constants In Formulas");
			Object[] ignoredConstants = { 1 };

			PolicyRuleConfiguration configuration = rule1.getConfiguration();
			ArrayList<ParameterConfiguration<?>> paramconf = configuration
					.getParameters();
			 //.setParameterValue(ignoredConstants);

			String[] ignoredFunctions = { "INDEX", "SUMME" };
			rule1.getConfiguration()
					.getParameterByFieldName("ignoredFunctions")
					.setParameterValue(ignoredFunctions);
			frontDesk.register(policy);
			frontDesk.setPolicy(policy);
			frontDesk.run();

			frontDesk.createInspectionReport(outpath, ReportFormat.HTML);
			
//			FileInputStream fis = new FileInputStream(new File(outpath + outFile));
//			String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
//			fis.close();
//			Assert.assertEquals("9787664cf2effd6d00814ef8368b2f1c", md5);
			

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		
	}

}
