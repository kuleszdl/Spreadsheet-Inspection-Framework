package sif.model.violations.single;

import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.policy.policyrule.implementations.ReadingDirectionPolicyRule;
import sif.utilities.Translator;

/***
 * A custom single violation to record violations of the
 * {@link ReadingDirectionPolicyRule}.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class ReadingDirectionSingleViolation extends GenericSingleViolation {

	private AbstractReference nonLeftToRightreference = null;
	private AbstractReference nonTopToBottomreference = null;

	public ReadingDirectionSingleViolation() {
	}

	/**
	 * Adds the given reference as a reference that cannot be read from left to
	 * right. If the reference is equal to
	 * {@link ITokenElement#isSameAs(ITokenElement)} a reference that has
	 * already been added, a counter will be increased.
	 * 
	 * @param violatingReference
	 *            The given reference.
	 */
	public void setNonLeftToRight(AbstractReference violatingReference) {
		nonLeftToRightreference = violatingReference;
	}

	/**
	 * Adds the given reference as a reference that cannot be read from top to
	 * bottom. If the reference is equal to
	 * {@link ITokenElement#isSameAs(ITokenElement)} a reference that has
	 * already been added, a counter will be increased.
	 * 
	 * @param violatingReference
	 *            The given reference.
	 */
	public void setNonTopToBottom(AbstractReference violatingReference) {
		nonTopToBottomreference = violatingReference;
	}



	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();

		if (nonLeftToRightreference != null) {
			description.append(Translator.instance.tl("PolicyReadingDirection.0010", "The following reference cannot be read from left to right: "));
			description.append(" ");
			description.append(nonLeftToRightreference.getValueAsString());
		}

		if (nonTopToBottomreference != null) {
			description.append(Translator.instance.tl("PolicyReadingDirection.0011", "The following references cannot be read from top to bottom:"));
			description.append(" ");
			description.append(nonTopToBottomreference.getValueAsString());
		}

		return description.toString();
	}




	@Override
	public Double getWeightedSeverityValue() {
		// Integer numberOfConstants = 0;
		Double severtityValue = 0.0;

		// TODO:Implement severity calculation

		// for (ScalarConstant constant : constants.keySet()) {
		// numberOfConstants = numberOfConstants + constants.get(constant);
		// }
		// switch (numberOfConstants) {
		// case 1:
		// severtityValue = IViolation.SEVERITY_VERY_LOW
		// * getPolicyRule().getSeverityWeight();
		// break;
		// case 2:
		// severtityValue = IViolation.SEVERITY_LOW
		// * getPolicyRule().getSeverityWeight();
		// break;
		// case 3:
		// severtityValue = IViolation.SEVERITY_MEDIUM
		// * getPolicyRule().getSeverityWeight();
		// break;
		// case 4:
		// severtityValue = IViolation.SEVERITY_HIGH
		// * getPolicyRule().getSeverityWeight();
		// break;
		// case 5:
		// severtityValue = IViolation.SEVERITY_VERY_HIGH
		// * getPolicyRule().getSeverityWeight();
		// break;
		// default:
		// severtityValue = IViolation.SEVERITY_VERY_HIGH
		// * getPolicyRule().getSeverityWeight();
		// break;
		// }

		return severtityValue;
	}


}
