package sif.inspection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import sif.IO.xml.SifMarshaller;
import sif.frontOffice.FrontDesk;
import sif.model.inspection.InspectionRequest;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.PolicyList;
import sif.model.violations.Findings;

public class TestErrorContainingCellTestFacility {

	private static final String filePath = "test/sif/testdata/CellsWithErrors.xls",
			policyPath = "test/sif/testdata/CellsWithErrorsPolicy.xml";

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
			assertTrue("Config is missing", list.getErrorContainingPolicyRule() != null);
			DynamicPolicy policy = list.getCompletePolicy();
			assertTrue("Policy failed to be created", policy != null);

			// Create inspection request.
			InspectionRequest inspection = FrontDesk.getInstance().
					createAndRunDynamicInspectionRequest(
							"TestErrorContainingCellsPolicyTestFacility", spreadsheetFile, policy);
			Findings f = inspection.getFindings();
			assertEquals("Wrong amount of findings", 3, (int) f.getNumberOfViolations()); 


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
