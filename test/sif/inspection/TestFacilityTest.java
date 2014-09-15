package sif.inspection;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import sif.IO.ReportFormat;
import sif.frontOffice.FrontDesk;
import sif.model.inspection.InspectionRequest;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.Policy;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.BinaryCondition;
import sif.model.policy.policyrule.dynamicConditions.EBinaryRelation;

public class TestFacilityTest {

	private static File inputFile;

	@Before
	public void setUp() throws Exception {
		inputFile = new File("test/sif/testdata/input03_s1.xls");
	}

	@Test
	public void testTestFacility() throws Exception {
		InspectionRequest request = null;

		Policy policy = new DynamicPolicy();
		policy.setName("fubar");

		DynamicPolicyRule rule = new DynamicPolicyRule();
		BinaryCondition binCond = new BinaryCondition();

		binCond.setElementType("Cell");
		binCond.setRelation(EBinaryRelation.greaterThan);
		binCond.setValue("0");

		binCond.setTarget("region1");
		binCond.setRelation(EBinaryRelation.lessThan);
		binCond.setValue("100");

		rule.getPostconditions().add(binCond);

		policy.add(rule);
		
		policy.getPolicyRules().values();

		// DynamicTestFacility testFacility = new DynamicTestFacility(checkers);
		// TestBay testBay = new TestBay(null);
		// testFacility.setTestedPolicyRule(rule);

		request = FrontDesk.getInstance().createAndRunDynamicInspectionRequest(
				"TestRequest 1", inputFile, policy);

		// TODO Erwartete Resultate genauer bestimmen
		assertTrue(request.getFindings() != null);

		FrontDesk.getInstance().createInspectionReport("test/sif/", ReportFormat.HTML);
	}

}
