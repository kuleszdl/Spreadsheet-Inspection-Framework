package sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers;

import sif.model.elements.IElement;
import sif.model.elements.basic.cell.Cell;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.policy.policyrule.dynamicConditions.BinaryCondition;
import sif.model.policy.policyrule.dynamicConditions.EBinaryRelation;
import sif.model.violations.ISingleViolation;
import sif.model.violations.single.BinaryConditionSingleViolation;
import sif.model.violations.single.ConditionSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;

/**
 * Implementation of {@link AbstractConditionChecker} for
 * {@link BinaryCondition} objects.
 * 
 * @author Manuel Lemcke
 * 
 */
public class BinaryConditionChecker extends AbstractConditionChecker {

	public BinaryConditionChecker(AbstractPolicyRule policyRule,
			IElement element) {
		super(policyRule, element);
	}
	
	public BinaryConditionChecker(AbstractPolicyRule policyRule) {
		super(policyRule);
	}

	@Override
	protected ISingleViolation checkProperty(Object property,
			AbstractCondition condition) {
		String expectedString = condition.getValue().toString();
		String actualString = property.toString();

		ConditionSingleViolation violation = null;

		if (actualString.equals(expectedString)) {
			// do nothing
		} else {
			violation = new BinaryConditionSingleViolation();
			violation.setPolicyRule(this.getPolicyRule());
			violation.setBaseSeverityValue(ISingleViolation.SEVERITY_MEDIUM);
		}

		return violation;
	}

	@Override
	protected ISingleViolation checkCellValue(Cell cell,
			AbstractCondition condition) throws IncompleteConditionException {
		ConditionSingleViolation violation = null;
		boolean conditionIsOk = false;
		EBinaryRelation relation = (EBinaryRelation) condition.getRelation();
		Object expectedValue = condition.getValue();
		String message = "";

		switch (cell.getCellContentType()) {
		default:
		case ERROR:
		case BLANK:
		case TEXT:
			// In case it's text
			if (relation.equals(EBinaryRelation.equal)) {
				conditionIsOk = expectedValue.toString().equals(
						cell.getValueAsString());
			} else {
				conditionIsOk = false;
			}
			break;
		case NUMERIC:
			// In case it's a number (Dates are also (formatted)
			// numbers)
			try {
				Double numericConditionValue = Double.parseDouble(expectedValue
						.toString());
				Double numericElementValue = Double.parseDouble(cell
						.getValueAsString());
				
				int comparison = numericElementValue
						.compareTo(numericConditionValue);

				if (relation != null) {
				switch (relation) {
				case equal:
					conditionIsOk = (comparison == 0);
					break;
				case greaterOrEqual:
					conditionIsOk = (comparison == 0 || comparison == 1);
					break;
				case greaterThan:
					conditionIsOk = (comparison == 1);
					break;
				case lessOrEqual:
					conditionIsOk = (comparison == -1 || comparison == 0);
					break;
				case lessThan:
					conditionIsOk = (comparison == -1);
					break;
				default:
					break;
				}				
				message = "The expected value was " + relation.toString() + " " + expectedValue 
						+ " but the found value was " + numericElementValue;
				} else {
					throw new IncompleteConditionException("The relation is undefined.");
				}
			} catch (NumberFormatException nfe) {
				conditionIsOk = false;
				message = "The expected value of the condition couldn't be parsed.";
			}
			break;
		case BOOLEAN:
			break;
		}

		if (!conditionIsOk) {
			violation = new BinaryConditionSingleViolation();
			violation.setCausingElement(cell);
			violation.setPolicyRule(this.getPolicyRule());
			violation.setBaseSeverityValue(ISingleViolation.SEVERITY_MEDIUM);
			violation.appendToDescription(message);
		}

		return violation;
	}
}
