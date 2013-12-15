/**
 * 
 */
package sif.model.violations.single;

import sif.model.policy.policyrule.dynamicConditions.ETernaryRelation;

/**
 * @author Manuel Lemcke
 * 
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
		String ternaryDescription = "";
		String interval = this.getExpectedValue() + ";"
				+ this.getExpectedHigherValue();

		ternaryDescription = "The found value was not in the interval ";

		switch (this.getRelation()) {
		case closed:
			ternaryDescription += "[" + interval + "]";
			break;
		case open:
			ternaryDescription += "]" + interval + "[";
			break;
		case openLeft:
			ternaryDescription += "]" + interval + "]";
			break;
		case openRight:
			ternaryDescription += "[" + interval + "[";
			break;
		}
		
		return ternaryDescription;

	}

}
