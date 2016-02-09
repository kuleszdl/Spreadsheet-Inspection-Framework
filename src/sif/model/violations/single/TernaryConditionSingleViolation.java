/**
 * 
 */
package sif.model.violations.single;

import sif.model.policy.policyrule.dynamicConditions.ETernaryRelation;
import sif.utilities.Translator;

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
		ternaryDescription.append(Translator.instance.tl("PolicyScenarios.0010", "After running the scenario"));
		ternaryDescription.append(" \"");
		ternaryDescription.append(this.getPolicyRule().getName());
		ternaryDescription.append(" \"");
		ternaryDescription.append(" ");
		ternaryDescription.append(Translator.instance.tl("PolicyScenarios.0010b", ",the actual value for this cell"));
		ternaryDescription.append(" ");
		ternaryDescription.append("(");
		ternaryDescription.append(this.getActualValue());
		ternaryDescription.append(")");
		ternaryDescription.append(" ");

		if (this.getExpectedValue().equals(this.getExpectedHigherValue())) {
			ternaryDescription.append(Translator.instance.tl("PolicyScenarios.0011","differs from the expected value"));
			ternaryDescription.append(" ");
			ternaryDescription.append("(");
			ternaryDescription.append(this.getExpectedValue());
			ternaryDescription.append(")");
			ternaryDescription.append(".");
		} else {
			ternaryDescription.append(Translator.instance.tl("PolicyScenarios.0012","was outside of the expected interval which is"));
			ternaryDescription.append(":");
			ternaryDescription.append(" ");
			ternaryDescription.append(Translator.instance.tl("PolicyScenarios.0013","greater"));
			if (actualRelation == ETernaryRelation.open || actualRelation == ETernaryRelation.openLeft) {
				ternaryDescription.append(" ");
				ternaryDescription.append(Translator.instance.tl("PolicyScenarios.0014","or equals"));
			}
			ternaryDescription.append(" ");
			ternaryDescription.append(getExpectedValue());

			ternaryDescription.append(" ");
			ternaryDescription.append(Translator.instance.tl("PolicyScenarios.0015","and"));
			ternaryDescription.append(" ");

			ternaryDescription.append(Translator.instance.tl("PolicyScenarios.0016","smaller"));
			if (actualRelation == ETernaryRelation.open || actualRelation == ETernaryRelation.openRight) {
				ternaryDescription.append(" ");
				ternaryDescription.append(Translator.instance.tl("PolicyScenarios.0014","or equals"));
			}
			ternaryDescription.append(" ");
			ternaryDescription.append(getExpectedHigherValue());

		}

		return ternaryDescription.toString();

	}

}
