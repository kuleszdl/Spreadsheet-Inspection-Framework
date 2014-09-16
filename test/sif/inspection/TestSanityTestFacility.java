package sif.inspection;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import sif.IO.ReportFormat;
import sif.IO.xml.SifMarshaller;
import sif.frontOffice.FrontDesk;
import sif.main.Application;
import sif.model.inspection.InspectionRequest;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.PolicyList;
import sif.model.violations.Findings;
import sif.model.violations.IViolation;

public class TestSanityTestFacility {
	private static final String filePath = "test/sif/testdata/TestSanity.xls",
			policyPath = "test/sif/testdata/TestSanityPolicy.xml";

	@Test
	public void testSanityMainMethod(){
		Application.main(new String[] { "file", "xml", policyPath, filePath });
	}

	@Test
	public void testSanityFindingsWarnings(){
		try {
			// Checking the files
			File spreadsheetFile = new File(filePath);
			File policyFile = new File(policyPath);
			assertTrue("Spreadsheet file was not found or coudn't be read", 
					spreadsheetFile.exists() && spreadsheetFile.canRead());
			assertTrue("Policy file was not found or coudn't be read", 
					policyFile.exists() && policyFile.canRead());
			
			// reading the policy
			PolicyList list = SifMarshaller.unmarshal(policyFile);
			assertTrue("Failed to deserialize the policy list", list != null);
			assertTrue("Sanity config is missing", list.getSanityPolicyRule() != null);
			list.getSanityPolicyRule().setSanityWarnings(true);
			DynamicPolicy policy = list.getCompletePolicy();
			assertTrue("Policy failed to be created", policy != null);
			
			// Create inspection request.
			InspectionRequest inspection = FrontDesk.getInstance().
					createAndRunDynamicInspectionRequest(
							"TestSanityTestFacility", spreadsheetFile, policy);
			Findings f = inspection.getFindings();
			// 1 violation, 2 warnings for an invalid regex, 2 empty cells, 1 unknown value
			assertEquals("Wrong amount of findings", 7, (int) f.getNumberOfViolations()); 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testSanityFindingsNoWarnings(){
		try {
			// Checking the files
			File spreadsheetFile = new File(filePath);
			File policyFile = new File(policyPath);
			assertTrue("Spreadsheet file was not found or coudn't be read", 
					spreadsheetFile.exists() && spreadsheetFile.canRead());
			assertTrue("Policy file was not found or coudn't be read", 
					policyFile.exists() && policyFile.canRead());
			
			// reading the policy
			PolicyList list = SifMarshaller.unmarshal(policyFile);
			assertTrue("Failed to deserialize the policy list", list != null);
			assertTrue("Sanity config is missing", list.getSanityPolicyRule() != null);
			list.getSanityPolicyRule().setSanityWarnings(false);
			DynamicPolicy policy = list.getCompletePolicy();
			assertTrue("Policy failed to be created", policy != null);
			
			// Create inspection request.
			InspectionRequest inspection = FrontDesk.getInstance().
					createAndRunDynamicInspectionRequest(
							"TestSanityTestFacility", spreadsheetFile, policy);
			Findings f = inspection.getFindings();

			assertEquals("Wrong amount of findings", 2, (int) f.getNumberOfViolations()); 
			IViolation vio = f.getViolationLists().get(0).getViolations().get(0);
			assertTrue("Failed to retrive the violation", vio != null);
			assertTrue("Violation is not at the correct position", 
					vio.getCausingElement().getLocation().matches(".*Bestellung.*[bB]4.*"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
