/**
 * 
 */
package sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers;

import java.util.Iterator;

import sif.model.elements.containers.AbstractElementList;
import sif.model.inspection.SpreadsheetInventory;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.violations.ISingleViolation;
import sif.model.violations.single.ConditionSingleViolation;
import sif.model.violations.single.CountSingleViolation;

/**
 * @author Manuel Lemcke
 */
public class ElementCountChecker extends TargetlessConditionChecker implements
		IConditionChecker {

	/**
	 * @param policyRule
	 */
	public ElementCountChecker(AbstractPolicyRule policyRule) {
		super(policyRule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sif.technicalDepartment.equipment.testing.facilities.implementations.
	 * IConditionChecker
	 * #check(sif.model.policy.policyrule.dynamic.AbstractCondition,
	 * sif.model.elements.IElement)
	 */
	@Override
	public ISingleViolation check(AbstractCondition condition)
			throws IllegalArgumentException {
		CountSingleViolation violation = null;

		Iterator<AbstractElementList<?>> elementListIterator = inventory
				.getElementLists().iterator();
		AbstractElementList<?> currentElementList = null;
		String elementClassName = condition.getElementType();

		Integer expectedElementCount;
		if (condition.getValue() != null) {
			expectedElementCount = Integer.parseInt(condition.getValue());
		} else {
			throw new RuntimeException("The condition has no value");
		}

		int elementCount = 0;
		while (elementListIterator.hasNext()) {
			currentElementList = elementListIterator.next();

			if (currentElementList.getElementClass().getCanonicalName()
					.endsWith(elementClassName)) {
				elementCount = currentElementList.getNumberOfElements();
			}
		}

		// if element count doesn't match: create a violation
		if (!(elementCount == expectedElementCount)) {
			violation = new CountSingleViolation();
			violation.setCausingElement(null);
			violation.setPolicyRule(this.getPolicyRule());
			violation.setExpectedCount(expectedElementCount);
			violation.setActualCount(elementCount);
			violation.appendToDescription(elementCount
					+ "  occurrences of the element class " + elementClassName
					+ " were found but " + expectedElementCount + " were expected.");
			violation.setBaseSeverityValue(ConditionSingleViolation.SEVERITY_MEDIUM);
		}

		return violation;
	}

	public SpreadsheetInventory getInventory() {
		return inventory;
	}

	public void setInventory(SpreadsheetInventory inventory) {
		this.inventory = inventory;
	}
}
