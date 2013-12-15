/**
 * 
 */
package sif.evaluation;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import sif.IO.xml.SifMarshaller;
import sif.frontOffice.FrontDesk;
import sif.model.inspection.InspectionRequest;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.policy.policyrule.dynamicConditions.TestInput;

/**
 * 
 * 
 * @author Manuel Lemcke
 * 
 */
public class DynamicScenario {

	private static String specificationPath1 = "test/sif/testdata/test1.xml";
	private static String specificationPath2 = "test/sif/testdata/bafoeg.xml";

	// private static File inputFile = new
	// File("test/sif/testdata/input03_s1.xls");

	/*
	 * Deserializes a spreadsheet specification, then
	 */
	@Test
	public void scenario1() throws Exception {
		File specFile = new File(specificationPath1);
		DynamicPolicy policy = SifMarshaller.unmarshal(specFile);

		// Check if a policy has been created
		assertTrue(policy != null);

		DynamicPolicyRule rule = (DynamicPolicyRule) policy
				.getRuleByName("EqualAndInterval1");

		// Check if it contains rules
		assertTrue(rule != null);

		File spreadsheetFile = new File(specFile.getParentFile(),
				policy.getSpreadsheetFileName());

		AbstractCondition condition1 = rule.getPostconditions().get(0);
		AbstractCondition condition2 = rule.getPostconditions().get(1);
		TestInput input1 = rule.getTestInputs().get(0);

		// Check if contains at least 2 conditions
		assertTrue("NULL-Check Condition1", condition1 != null);
		assertTrue("NULL-Check Condition2", condition2 != null);

		// Check if there is a TestInput with the value 1
		assertTrue("Wert check TestInput1", input1.getValue().equals("1"));

		/*
		 * Execute the inspection
		 */
		InspectionRequest request = FrontDesk.getInstance()
				.createAndRunDynamicInspectionRequest("TestRequest 1",
						spreadsheetFile, policy);

		// Check if there were findings (scenario is set up in a way there
		// should be 1 finding)
		assertTrue("Findings count check", request.getFindings()
				.getNumberOfViolations() > 0);

		// Check if violation has the correct causing element
//		assertTrue("Finding location check", request.getFindings()
//				.getViolationLists().get(0).getViolations().get(0)
//				.getCausingElement().getLocation().endsWith("D5"));

		// Create a report to see for yourself
		FrontDesk.getInstance().createInspectionReport("test/sif/");
	}
	
	@Test
	public void scenario2() throws Exception {
		File specFile = new File(specificationPath2);
		DynamicPolicy policy = SifMarshaller.unmarshal(specFile);

		// Check if a policy has been created
		assertTrue(policy != null);

		DynamicPolicyRule rule = (DynamicPolicyRule) policy
				.getRuleByName("intermediate2");

		// Check if it contains rules
		assertTrue(rule != null);

		File spreadsheetFile = new File(specFile.getParentFile(),
				policy.getSpreadsheetFileName());

		AbstractCondition condition1 = rule.getInvariants().get(0);

		// Check if rule contains an invariant
		assertTrue("NULL-Check Condition1", condition1 != null);

		/*
		 * Execute the inspection
		 */
		InspectionRequest request = FrontDesk.getInstance()
				.createAndRunDynamicInspectionRequest("TestRequest 2",
						spreadsheetFile, policy);

		// Check if there were findings (scenario is set up in a way there
		// should be 1 finding)
		assertTrue("Findings count check", request.getFindings()
				.getNumberOfViolations() > 0);

		// Check if violation has the correct causing element
//		assertTrue("Finding location check", request.getFindings()
//				.getViolationLists().get(0).getViolations().get(0)
//				.getCausingElement().getLocation().endsWith("D5"));

		// Create a report to see for yourself
		FrontDesk.getInstance().createInspectionReport("test/sif/");
	}

}
