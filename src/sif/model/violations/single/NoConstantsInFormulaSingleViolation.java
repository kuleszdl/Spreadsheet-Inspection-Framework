package sif.model.violations.single;

import sif.model.elements.basic.tokens.ScalarConstant;
import sif.model.policy.policyrule.implementations.NoConstantsInFormulasPolicyRule;
import sif.model.violations.IViolation;
import sif.utilities.Translator;

/***
 * A custom single violation to record violations of the
 * {@link NoConstantsInFormulasPolicyRule}.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class NoConstantsInFormulaSingleViolation extends GenericSingleViolation {

	private ScalarConstant constant;

	public NoConstantsInFormulaSingleViolation() {
	}

	/***
	 * Adds the given constant to the violation. If a constant of the same value
	 * has already been added, the counter for this constant will be increased
	 * by one.
	 * 
	 * @param constant
	 *            The given constant.
	 */
	public void setConstant(ScalarConstant constant) {
		this.constant = constant;
	}


	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append(Translator.instance.tl("PolicyConstants.0010", "The following constant was found:"));
		description.append(" ");
		description.append(constant.getStringRepresentation());

		return description.toString();
	}

	@Override
	public Double getWeightedSeverityValue() {
		return IViolation.SEVERITY_VERY_LOW * getPolicyRule().getSeverityWeight();
	}

}
