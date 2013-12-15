package sif.technicalDepartment.equipment.testing.facilities.types;

import java.lang.reflect.Field;

import sif.model.inspection.SpreadsheetInventory;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.configuration.ParameterConfiguration;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.violations.lists.ViolationList;
import sif.technicalDepartment.equipment.TestBay;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.CheckerCreationException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.NoConditionTargetException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.PropertyAccessException;

public abstract class AbstractTestFacility {

	private TestBay testBay;

	private AbstractPolicyRule testedPolicyRule;

	protected SpreadsheetInventory inventory;

	private void configure() throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		for (ParameterConfiguration<?> pconfg : getTestedPolicyRule()
				.getConfiguration().getParameters()) {

			Field f = this.getClass().getDeclaredField(pconfg.getFieldName());
			f.setAccessible(true);
			f.set(this, pconfg.getParameterValue());
			f.setAccessible(false);
		}
	}

	protected SpreadsheetInventory getInventory() {
		return inventory;
	}

	public TestBay getTestBay() {
		return this.testBay;
	}

	public AbstractPolicyRule getTestedPolicyRule() {
		return testedPolicyRule;
	}

	/**
	 * Tests the {@link AbstractPolicyRule}.
	 * 
	 * @return {@link ViolationList} containing the violations found during the
	 *         run
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
	public abstract ViolationList run() throws NoConditionTargetException,
			PropertyAccessException, CheckerCreationException,
			IncompleteConditionException;

	public void setTestBay(TestBay testBay) {
		this.testBay = testBay;
		this.inventory = testBay.getInspection().getInventory();
	}

	public void setTestedPolicyRule(AbstractPolicyRule testedPolicyRule)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		this.testedPolicyRule = testedPolicyRule;
		this.configure();
	}
}