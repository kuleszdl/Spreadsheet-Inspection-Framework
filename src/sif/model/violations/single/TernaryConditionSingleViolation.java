/**
 * 
 */
package sif.model.violations.single;

import sif.model.policy.policyrule.dynamicConditions.ETernaryRelation;

/**
 * @author Manuel Lemcke
 * 
 *         TODO: Strange naming: The term "ExpectedHigherValue" refers to the upper
 *         interval, while the "ExpectedValue" refers the lower interval, NOT
 *         the actually expected value entered in the result cell.
 */
public class TernaryConditionSingleViolation extends ConditionSingleViolation {

	private String expectedMaximum;
	private ETernaryRelation relation;

	/**
	 * 
	 */
	public TernaryConditionSingleViolation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the expectedHigherValue
	 */
	public String getExpectedHigherValue() {
		return expectedMaximum;
	}

	/**
	 * @param expectedHigherValue
	 *            the expectedHigherValue to set
	 */
	public void setExpectedHigherValue(String expectedHigherValue) {
		this.expectedMaximum = expectedHigherValue;
	}

	public void setRelation(ETernaryRelation relation) {
		this.relation = relation;
	}

	@Override
	public ETernaryRelation getRelation() {
		return relation;
	}

	@Override
	public String getDescription() {

		ETernaryRelation actualRelation = this.getRelation();

		StringBuilder ternaryDescription = new StringBuilder();
		ternaryDescription.append("When the scenario, the actual value for this cell");
		ternaryDescription.append(" ");
		ternaryDescription.append("(");
		ternaryDescription.append(this.getActualValue());
		ternaryDescription.append(")");
		ternaryDescription.append(" ");

		if (this.getExpectedValue().equals(this.getExpectedHigherValue())) {
			ternaryDescription.append("differs from the expected value");
			ternaryDescription.append(" ");
			ternaryDescription.append("(");
			ternaryDescription.append(this.getExpectedValue());
			ternaryDescription.append(")");
			ternaryDescription.append(".");
		} else {
			ternaryDescription.append("was outside of the expected interval which is:");

			ternaryDescription.append(" ");
			ternaryDescription.append("greater");
			if (actualRelation == ETernaryRelation.open || actualRelation == ETernaryRelation.openLeft) {
				ternaryDescription.append(" ");
				ternaryDescription.append("or equals");
			}
			ternaryDescription.append(" ");
			ternaryDescription.append(getExpectedValue());

			ternaryDescription.append(" ");
			ternaryDescription.append("and");
			ternaryDescription.append(" ");

			ternaryDescription.append("smaller");
			if (actualRelation == ETernaryRelation.open || actualRelation == ETernaryRelation.openRight) {
				ternaryDescription.append(" ");
				ternaryDescription.append("or equals");
			}
			ternaryDescription.append(" ");
			ternaryDescription.append(getExpectedHigherValue());

		}

		return ternaryDescription.toString();

	}

}
