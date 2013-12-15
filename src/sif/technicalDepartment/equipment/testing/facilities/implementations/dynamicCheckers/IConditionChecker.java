/**
 * 
 */
package sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers;

import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.violations.ISingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.PropertyAccessException;

/**
 * @author Manuel Lemcke
 * 
 */
public interface IConditionChecker {

	/**
	 * Checks if a condition is met.
	 * 
	 * @param condition
	 * @param element
	 * @return
	 * @throws IncompleteConditionException
	 *             Thrown, if the condition does not contain all information
	 *             needed to check it properly.
	 * @throws Exception
	 *             It's possible that exceptions are thrown by implementations
	 */
	ISingleViolation check(AbstractCondition condition)
			throws PropertyAccessException, IncompleteConditionException;

}
