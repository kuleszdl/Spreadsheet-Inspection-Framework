package sif.inspection;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;

import sif.frontOffice.FrontDesk;
import sif.model.inspection.InspectionRequest;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.policyrule.implementations.FormulaComplexityPolicyRule;
import sif.model.policy.policyrule.implementations.MultipleSameRefPolicyRule;
import sif.model.policy.policyrule.implementations.NoConstantsInFormulasPolicyRule;
import sif.model.policy.policyrule.implementations.NonConsideredValuesPolicyRule;
import sif.model.policy.policyrule.implementations.OneAmongOthersPolicyRule;
import sif.model.policy.policyrule.implementations.ReadingDirectionPolicyRule;
import sif.model.policy.policyrule.implementations.RefToNullPolicyRule;
import sif.model.policy.policyrule.implementations.StringDistancePolicyRule;
import sif.model.violations.Findings;

public class TestAllStaticPolicies {

	private static final String filePath = "test/sif/testdata/WallOfViolations.xls";
	
	@Test
	public void testFormulaComplexity(){
		DynamicPolicy pol = new DynamicPolicy();
		pol.add(new FormulaComplexityPolicyRule());
		InspectionRequest req = null;
		try {
			req = FrontDesk.getInstance().createAndRunDynamicInspectionRequest(
					"Static policies", new File(filePath), pol);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("An exception occured, " + e.getLocalizedMessage());
		}
		Findings violations = req.getFindings();
		Assert.assertEquals(3, (int) violations.getNumberOfTopLevelViolations()); 
	}
	
	@Test
	public void testMultipleSameRef(){
		DynamicPolicy pol = new DynamicPolicy();
		pol.add(new MultipleSameRefPolicyRule());
		InspectionRequest req = null;
		try {
			req = FrontDesk.getInstance().createAndRunDynamicInspectionRequest(
					"Static policies", new File(filePath), pol);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("An exception occured, " + e.getLocalizedMessage());
		}
		Findings violations = req.getFindings();
		Assert.assertEquals(2, (int) violations.getNumberOfTopLevelViolations()); 
	}

	@Test
	public void testNoConstantsInFormulas(){
		DynamicPolicy pol = new DynamicPolicy();
		pol.add(new NoConstantsInFormulasPolicyRule());
		InspectionRequest req = null;
		try {
			req = FrontDesk.getInstance().createAndRunDynamicInspectionRequest(
					"Static policies", new File(filePath), pol);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("An exception occured, " + e.getLocalizedMessage());
		}
		Findings violations = req.getFindings();
		Assert.assertEquals(8, (int) violations.getNumberOfTopLevelViolations()); 
	}
	
	@Test
	public void testNonConsideredValues(){
		DynamicPolicy pol = new DynamicPolicy();
		pol.add(new NonConsideredValuesPolicyRule());
		InspectionRequest req = null;
		try {
			req = FrontDesk.getInstance().createAndRunDynamicInspectionRequest(
					"Static policies", new File(filePath), pol);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("An exception occured, " + e.getLocalizedMessage());
		}
		Findings violations = req.getFindings();
		Assert.assertEquals(355, (int) violations.getNumberOfTopLevelViolations()); 
	}
	
	@Test
	public void testOneAmongOthers(){
		DynamicPolicy pol = new DynamicPolicy();
		pol.add(new OneAmongOthersPolicyRule());
		InspectionRequest req = null;
		try {
			req = FrontDesk.getInstance().createAndRunDynamicInspectionRequest(
					"Static policies", new File(filePath), pol);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("An exception occured, " + e.getLocalizedMessage());
		}
		Findings violations = req.getFindings();
		Assert.assertEquals(22, (int) violations.getNumberOfTopLevelViolations()); 
	}
	
	@Test
	public void testReadingDirection(){
		DynamicPolicy pol = new DynamicPolicy();
		pol.add(new ReadingDirectionPolicyRule());
		InspectionRequest req = null;
		try {
			req = FrontDesk.getInstance().createAndRunDynamicInspectionRequest(
					"Static policies", new File(filePath), pol);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("An exception occured, " + e.getLocalizedMessage());
		}
		Findings violations = req.getFindings();
		Assert.assertEquals(3, (int) violations.getNumberOfTopLevelViolations()); 
	}
	
	@Test
	public void testRefToNull(){
		DynamicPolicy pol = new DynamicPolicy();
		pol.add(new RefToNullPolicyRule());
		InspectionRequest req = null;
		try {
			req = FrontDesk.getInstance().createAndRunDynamicInspectionRequest(
					"Static policies", new File(filePath), pol);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("An exception occured, " + e.getLocalizedMessage());
		}
		Findings violations = req.getFindings();
		Assert.assertEquals(12, (int) violations.getNumberOfTopLevelViolations()); 
	}
	
	@Test
	public void testStringDistance(){
		DynamicPolicy pol = new DynamicPolicy();
		pol.add(new StringDistancePolicyRule());
		InspectionRequest req = null;
		try {
			req = FrontDesk.getInstance().createAndRunDynamicInspectionRequest(
					"Static policies", new File(filePath), pol);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("An exception occured, " + e.getLocalizedMessage());
		}
		Findings violations = req.getFindings();
		Assert.assertEquals(2, (int) violations.getNumberOfTopLevelViolations()); 
	}


}
