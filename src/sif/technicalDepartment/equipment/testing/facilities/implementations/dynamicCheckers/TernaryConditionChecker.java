package sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers;

import sif.model.elements.IElement;
import sif.model.elements.basic.cell.Cell;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.policy.policyrule.dynamicConditions.ETernaryRelation;
import sif.model.policy.policyrule.dynamicConditions.TernaryCondition;
import sif.model.violations.ISingleViolation;
import sif.model.violations.single.TernaryConditionSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;

/**
 * A implementation of IConditionChecker which can determine if the condition
 * represented by a {@link TernaryCondition} instance is met.
 * 
 * @author Manuel Lemcke
 * 
 */
public class TernaryConditionChecker extends AbstractConditionChecker {

	public TernaryConditionChecker(AbstractPolicyRule policyRule,
			IElement element) {
		super(policyRule, element);
	}
	
	public TernaryConditionChecker(AbstractPolicyRule policyRule) {
		super(policyRule);
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// * sif.technicalDepartment.equipment.testing.facilities.implementations.
	// * AbstractConditionChecker
	// * #checkSingle(sif.model.policy.policyrule.dynamic.AbstractCondition,
	// * sif.model.elements.IElement)
	// */
	// @Override
	// public ISingleViolation check(AbstractCondition condition) {
	// GenericSingleViolation violation = null;
	// boolean conditionIsOk = false;
	// ETernaryRelation relation = null;
	// Object expectedValue = null;
	// IElement element = this.getElement();
	//
	// if (condition instanceof TernaryCondition) {
	// relation = ((TernaryCondition) condition).getRelation();
	// expectedValue = condition.getValue();
	// } else {
	// throw new IllegalArgumentException(
	// "The condition is not of the type TernaryCondition.");
	// }
	//
	// if (element instanceof Cell) {
	// Cell cell = (Cell) element;
	//
	// switch (cell.getCellContentType()) {
	// default:
	// case ERROR:
	// case BLANK:
	// case TEXT:
	// break;
	// case NUMERIC:
	// // In case it's a number (Dates are also (formatted) numbers)
	// try {
	// Double numericConditionValue1 = Double
	// .parseDouble(expectedValue.toString());
	// Double numericElementValue = Double.parseDouble(element
	// .getValueAsString());
	//
	// // TODO
	// if (relation.equals(ETernaryRelation.closed)) {
	//
	// } else if (relation.equals(ETernaryRelation.open)) {
	//
	// } else if (relation.equals(ETernaryRelation.openLeft)) {
	//
	// } else if (relation.equals(ETernaryRelation.openRight)) {
	//
	// }
	// } catch (NumberFormatException nfe) {
	// // Double couldn't be parsed -> do nothing
	// conditionIsOk = false;
	// }
	// break;
	// case BOOLEAN:
	// break;
	// }
	// }
	//
	// if (!conditionIsOk) {
	// violation = new GenericSingleViolation();
	// violation.setCausingElement(element);
	// violation.setPolicyRule(this.getPolicyRule());
	// violation
	// .setBaseSeverityValue(GenericSingleViolation.SEVERITY_MEDIUM);
	// }
	//
	// return violation;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sif.technicalDepartment.equipment.testing.facilities.implementations.
	 * AbstractConditionChecker#checkProperty(java.lang.Object,
	 * sif.model.policy.policyrule.dynamic.AbstractCondition)
	 */
	@Override
	protected ISingleViolation checkProperty(Object property,
			AbstractCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sif.technicalDepartment.equipment.testing.facilities.implementations.
	 * AbstractConditionChecker
	 * #checkCellValue(sif.model.elements.basic.cell.Cell,
	 * sif.model.policy.policyrule.dynamic.AbstractCondition)
	 */
	@Override
	protected ISingleViolation checkCellValue(Cell cell,
			AbstractCondition condition) throws IncompleteConditionException {
		TernaryConditionSingleViolation violation = null;
		boolean conditionIsOk = false;

		ETernaryRelation relation = ((TernaryCondition) condition)
				.getRelation();
		Object expectedValue = condition.getValue();
		Object expectedValue2 = ((TernaryCondition) condition).getValue2();
		String message = "";
		Double numericConditionValue1 = null;
		Double numericConditionValue2 = null;
		Double numericElementValue = null;
		
		if (condition.getRelation() == null) {
			throw new IncompleteConditionException();
		}
		
		switch (cell.getCellContentType()) {
		default:
		case ERROR:
		case BLANK:
		case TEXT:
			break;
		case NUMERIC:
			// In case it's a number (Dates are also (formatted)
			// numbers)
			try {
				numericConditionValue1 = Double
						.parseDouble(expectedValue.toString());
				numericConditionValue2 = Double
						.parseDouble(expectedValue2.toString());
				numericElementValue = Double.parseDouble(cell
						.getValueAsString());

				switch (relation) {
				case closed:
					if (numericElementValue.compareTo(numericConditionValue1) == 1
							&& numericElementValue
									.compareTo(numericConditionValue2) == -1) {
						conditionIsOk = true;
					}
					break;
				case open:
					if (numericElementValue.compareTo(numericConditionValue1) > -1
							&& numericElementValue
									.compareTo(numericConditionValue2) < 1) {
						// The result of comparison 1 is 0 or 1 (element >=
						// condition)
						// The result of comparison 2 is -1 or 0 (element <=
						// condition)
						conditionIsOk = true;
					}
					break;
				case openLeft:
					if (numericElementValue.compareTo(numericConditionValue1) > -1
							&& numericElementValue
									.compareTo(numericConditionValue2) == -1) {
						// The result of comparison 1 is 0 or 1 (element >=
						// condition)
						// The result of comparison 2 is -1 (element <
						// condition)
						conditionIsOk = true;
					}
					break;
				case openRight:
					if (numericElementValue.compareTo(numericConditionValue1) > -1
							&& numericElementValue
									.compareTo(numericConditionValue2) == -1) {
						// The result of comparison 1 is 1 (element > condition)
						// The result of comparison 2 is -1 or 0 (element <=
						// condition)
						conditionIsOk = true;
					}
					break;
				default:
					break;
				}
			} catch (NumberFormatException nfe) {
				conditionIsOk = false;
				message = "One of the  expected values of the condition "
						+ "couldn't be parsed.";
			}
			break;
		case BOOLEAN:
			break;
		}

		if (!conditionIsOk) {
			violation = new TernaryConditionSingleViolation();
			violation.setExpectedValue(numericConditionValue1.toString());
			violation.setExpectedHigherValue(numericConditionValue2.toString());
			violation.setActualValue(numericElementValue.toString());
			violation.setCausingElement(cell);
			violation.setPolicyRule(this.getPolicyRule());
			violation.setBaseSeverityValue(ISingleViolation.SEVERITY_MEDIUM);
			violation.appendToDescription(message);
			((TernaryConditionSingleViolation)violation).setRelation(((TernaryCondition)condition).getRelation());
		} else {
			violation = null;
		}

		return violation;
	}
}
