package sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import sif.model.elements.IElement;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.cell.ICellElement;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.violations.ISingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.PropertyAccessException;

/**
 * The abstract base class for condition checker classes.
 * 
 * Implements a modified Template Method pattern. That is, the abstract methods
 * checkProperty and checkCellValue are called in the method check. The
 * modification of the pattern is that the method check is not final - so
 * subclasses can change it if that's necessary.
 * 
 * @author Manuel Lemcke
 * 
 */
public abstract class AbstractConditionChecker implements IConditionChecker {

	private AbstractPolicyRule policyRule = null;
	private IElement element = null;

	@SuppressWarnings("unused")
	private AbstractConditionChecker() {

	}

	public AbstractConditionChecker(AbstractPolicyRule policyRule) {
		this.policyRule = policyRule;
	}

	public AbstractConditionChecker(AbstractPolicyRule policyRule,
			IElement element) {
		this.policyRule = policyRule;
		this.element = element;
	}

	/**
	 * Checks a single Element against the condition.
	 * 
	 * @param condition
	 * @return A implementation of {@link ISingleViolation} if there have been
	 *         violations during the check.
	 * @throws PropertyAccessException
	 * @throws IncompleteConditionException 
	 *             Thrown, if the condition does not contain all information
	 *             needed to check it properly.
	 */
	public ISingleViolation check(AbstractCondition condition)
			throws PropertyAccessException, IncompleteConditionException {
		ISingleViolation violation = null;

		if (condition.getProperty() != null && condition.getProperty() != "") {
			Object property = null;
			String propertyName = condition.getProperty();
			try {
				property = retrieveProperty(propertyName, element);
				violation = checkProperty(property, condition);
				// because checkProperty doesn't know the causing element, add
				// element as causing element to the violation
				violation.setCausingElement(element);
			} catch (Exception e) {
				throw new PropertyAccessException(e);
			}
		} else if (element instanceof ICellElement) {
			// A Cell instance will return itself here, others will return the
			// referencing cell
			Cell cell = ((ICellElement) element).getCell();
			violation = checkCellValue(cell, condition);
		}

		return violation;
	}

	/**
	 * Checks a single Element against the condition.
	 * 
	 * @param condition
	 * @param element
	 * @return A implementation of {@link ISingleViolation} if there have been
	 *         violations during the check.
	 * @throws PropertyAccessException
	 *             Thrown, if the property specified by a condition could not be
	 *             accessed
	 * @throws IncompleteConditionException 
	 */
	public ISingleViolation check(AbstractCondition condition, IElement element)
			throws PropertyAccessException, IncompleteConditionException {
		this.setElement(element);
		return check(condition);
	}

	/**
	 * Checks if the property fulfills the condition
	 * 
	 * @param property
	 * @param condition
	 * @return A implementation of {@link ISingleViolation} if there have been
	 *         violations during the check.
	 */
	protected abstract ISingleViolation checkProperty(Object property,
			AbstractCondition condition);

	/**
	 * Checks if a cell value fulfills the condition
	 * 
	 * @param cell
	 * @param condition
	 * @return A implementation of {@link ISingleViolation} if there have been
	 *         violations during the check.
	 * @throws IncompleteConditionException
	 *             Thrown, if the condition does not contain all information
	 *             needed to check it properly.
	 */
	protected abstract ISingleViolation checkCellValue(Cell cell,
			AbstractCondition condition) throws IncompleteConditionException;

	/**
	 * @return the policyRule
	 */
	public AbstractPolicyRule getPolicyRule() {
		return policyRule;
	}

	/**
	 * @param policyRule
	 *            the policyRule to set
	 */
	public void setPolicyRule(AbstractPolicyRule policyRule) {
		this.policyRule = policyRule;
	}

	/**
	 * Calls the getter of a property and returns the value retrieved that way.
	 * 
	 * @param propertyName
	 *            The name of the property
	 * @param element
	 *            The element which's property shall be retrieved
	 * @return The value of the property
	 * @throws IllegalAccessException
	 *             If the getter of the property could not be accessed
	 * @throws InvocationTargetException
	 *             If the property getter could not be invoked
	 * @throws IntrospectionException
	 *             If no {@link BeanInfo} could be generated for the element
	 * @throws IllegalArgumentException
	 *             If the property getter could not be invoked due to a illegal
	 *             property name argument
	 */
	public static Object retrieveProperty(String propertyName, IElement element)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, IntrospectionException {
		Object returnValue = null;

		for (PropertyDescriptor pd : Introspector.getBeanInfo(
				element.getClass()).getPropertyDescriptors()) {
			if (pd.getReadMethod() != null && propertyName.equals(pd.getName())) {
				returnValue = pd.getReadMethod().invoke(element);
			}
		}
		return returnValue;
	}

	/**
	 * @param element
	 *            the element to set
	 */
	public void setElement(IElement element) {
		this.element = element;
	}

	/**
	 * @return the element
	 */
	public IElement getElement() {
		return element;
	}

}
