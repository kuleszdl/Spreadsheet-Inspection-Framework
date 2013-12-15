package sif.technicalDepartment.management;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.UUID;

import sif.model.inspection.InspectionRequest;
import sif.model.policy.expression.policyrule.PolicyRuleExpression;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.CompositePolicyRule;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.policy.policyrule.implementations.FormulaComplexityPolicyRule;
import sif.model.policy.policyrule.implementations.NoConstantsInFormulasPolicyRule;
import sif.model.policy.policyrule.implementations.ReadingDirectionPolicyRule;
import sif.technicalDepartment.equipment.TestBay;
import sif.technicalDepartment.equipment.testing.facilities.implementations.DynamicTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.implementations.FormulaComplexityTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.implementations.NoConstantsInFormulasTestFacilitiy;
import sif.technicalDepartment.equipment.testing.facilities.implementations.ReadingDirectionTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.IConditionChecker;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.CheckerCreationException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.NoConditionTargetException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.PropertyAccessException;
import sif.technicalDepartment.equipment.testing.facilities.types.AbstractTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.types.CompositeTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * The TestBayManager is in charge of managing the test equipment and for
 * creating {@link TestBay}s for {@link InspectionRequest}s.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
class TestBayManager {

	private TreeMap<UUID, TestBay> testBays;
	private TreeMap<String, Class<? extends MonolithicTestFacility>> monolithicTestFacilites;
	private TreeMap<String, Class<? extends IConditionChecker>> conditionCheckers;

	protected TestBayManager() {
		monolithicTestFacilites = new TreeMap<String, Class<? extends MonolithicTestFacility>>();
		monolithicTestFacilites.put(
				NoConstantsInFormulasPolicyRule.class.getCanonicalName(),
				NoConstantsInFormulasTestFacilitiy.class);
		monolithicTestFacilites.put(
				ReadingDirectionPolicyRule.class.getCanonicalName(),
				ReadingDirectionTestFacility.class);
		monolithicTestFacilites.put(
				FormulaComplexityPolicyRule.class.getCanonicalName(),
				FormulaComplexityTestFacility.class);
		testBays = new TreeMap<UUID, TestBay>();
		conditionCheckers = new TreeMap<String, Class<? extends IConditionChecker>>();
	}

	private CompositeTestFacility composeTestFacility(
			PolicyRuleExpression policyExpr) {
		CompositeTestFacility testFacility = policyExpr.interpret();
		return testFacility;
	}

	private AbstractTestFacility getTestFacilityFor(
			AbstractPolicyRule abstractPolicyRule)
			throws InstantiationException, IllegalAccessException {

		AbstractTestFacility abstractTestFacility;
		if ((abstractPolicyRule instanceof CompositePolicyRule))
			abstractTestFacility = composeTestFacility(((CompositePolicyRule) abstractPolicyRule)
					.getPolicyRuleExpression());
		else if ((abstractPolicyRule instanceof DynamicPolicyRule)) {
			abstractTestFacility = createDynamicTestFacility();
		} else {
			abstractTestFacility = this.monolithicTestFacilites.get(
					abstractPolicyRule.getClass().getCanonicalName())
					.newInstance();
		}

		return abstractTestFacility;
	}

	private AbstractTestFacility createDynamicTestFacility() {
		DynamicTestFacility dynFacility = new DynamicTestFacility(
				this.conditionCheckers);
		return dynFacility;
	}

	/***
	 * 
	 * @param inspection
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws Exception
	 */
	protected void prepareTestBayFor(InspectionRequest inspection)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException {
		TestBay testBay = null;
		if (!this.testBays.containsKey(inspection.getId())) {
			testBay = new TestBay(inspection);
			this.testBays.put(inspection.getId(), testBay);
		} else {
			testBay = testBays.get(inspection.getId());
		}

		Iterator<AbstractPolicyRule> policyRuleIterator = inspection
				.getPolicy().getPolicyRules().values().iterator();

		while (policyRuleIterator.hasNext()) {
			AbstractPolicyRule abstractPolicyRule = policyRuleIterator.next();
			try {
				AbstractTestFacility testFacility = getTestFacilityFor(abstractPolicyRule);
				testFacility.setTestBay(testBay);
				testFacility.setTestedPolicyRule(abstractPolicyRule);
				testBay.add(testFacility);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	protected void register(Class<? extends MonolithicPolicyRule> ruleClass,
			Class<? extends MonolithicTestFacility> testFacilityClass) {
		this.monolithicTestFacilites.put(ruleClass.getCanonicalName(),
				testFacilityClass);
	}

	protected void registerCondition(
			Class<? extends AbstractCondition> conditionClass,
			Class<? extends IConditionChecker> checkerClass) {
		this.conditionCheckers.put(conditionClass.getCanonicalName(),
				checkerClass);
	}

	/**
	 * Runs all registered {@link TestFacility} instances of the {@link TestBay}
	 * registered for the inspection
	 * 
	 * @param inspection
	 *            The inspection request to be tested
	 * 
	 * @throws CheckerCreationException
	 *             Thrown, if a checker for a {@link AbstractCondition} subclass
	 *             couldn't be created. This might happen, if a
	 *             {@link AbstractCondition} has not been registered.
	 * 
	 * @throws PropertyAccessException
	 *             Thrown, if a property of a SIF Element could not be access.
	 *             Most likely because the element doesn't have that property.
	 * 
	 * @throws NoConditionTargetException
	 *             Thrown, if a {@link AbstractCondition} object has no target
	 *             but is supposed to.
	 * 
	 * @throws IncompleteConditionException
	 *             Thrown, if the one of the conditions contained in one of the
	 *             policy rules does not contain all information necessary to
	 *             check it properly.
	 */
	protected void runTestFacilities(InspectionRequest inspection)
			throws NoConditionTargetException, PropertyAccessException,
			CheckerCreationException, IncompleteConditionException {
		this.testBays.get(inspection.getId()).runTestFacilities();
	}

	/**
	 * Creates a new TestBay if needed and runs all {@link TestFacility}
	 * instances registered for rules in the {@link InspectionRequest}.
	 * 
	 * @param inspection
	 * @throws Exception
	 */
	public void run(InspectionRequest inspection) throws Exception {
		prepareTestBayFor(inspection);
		runTestFacilities(inspection);
	}
}
