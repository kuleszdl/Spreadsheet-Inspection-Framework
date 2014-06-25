package sif.model.violations.single;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import sif.model.elements.IElement;
import sif.model.elements.basic.tokens.ScalarConstant;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.implementations.NoConstantsInFormulasPolicyRule;
import sif.model.violations.ISingleViolation;
import sif.model.violations.IViolation;

/***
 * A custom single violation to record violations of the
 * {@link NoConstantsInFormulasPolicyRule}.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class NoConstantsInFormulaSingleViolation implements ISingleViolation {

	private IElement causingFormula;
	private HashMap<ScalarConstant, Integer> constants;
	private AbstractPolicyRule policyRule;

	public NoConstantsInFormulaSingleViolation() {
		constants = new HashMap<ScalarConstant, Integer>();
	}

	/***
	 * Adds the given constant to the violation. If a constant of the same value
	 * has already been added, the counter for this constant will be increased
	 * by one.
	 * 
	 * @param constant
	 *            The given constant.
	 */
	public void add(ScalarConstant constant) {

		Boolean added = false;
		for (ScalarConstant scalar : constants.keySet()) {
			if (scalar.isSameAs(constant)) {
				Integer occurences = constants.get(scalar) + 1;
				constants.put(scalar, occurences);

				added = true;
				break;
			}
		}

		if (!added) {
			constants.put(constant, 1);
		}

	}

	@Override
	public IElement getCausingElement() {
		return causingFormula;
	}
	
	private String getScalarDescription(ScalarConstant constant){
		if (constants.get(constant) > 1){
			return constant.getStringRepresentation() + " (" + constants.get(constant) + "x)";
		}
		return constant.getStringRepresentation();
	}

	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append("The following constants were found: ");
		ArrayList<ScalarConstant> sorted = new ArrayList<ScalarConstant>(constants.keySet());
		Collections.sort(sorted, createComparator());
		Iterator<ScalarConstant> constantIterator = sorted
				.iterator();

		while (constantIterator.hasNext()) {
			ScalarConstant constant = constantIterator.next();

			description.append(getScalarDescription(constant));
			if (constantIterator.hasNext()) {
				description.append(", ");
			}
		}

		return description.toString();
	}

	public Integer getNumberOfConstantsFound() {
		return constants.size();
	}

	@Override
	public AbstractPolicyRule getPolicyRule() {
		return policyRule;
	}

	@Override
	public Double getWeightedSeverityValue() {
		Integer numberOfConstants = 0;
		Double severtityValue = 0.0;
		for (ScalarConstant constant : constants.keySet()) {
			numberOfConstants = numberOfConstants + constants.get(constant);
		}
		switch (numberOfConstants) {
		case 1:
			severtityValue = IViolation.SEVERITY_VERY_LOW;
			break;
		case 2:
			severtityValue = IViolation.SEVERITY_LOW;
			break;
		case 3:
			severtityValue = IViolation.SEVERITY_MEDIUM;
			break;
		case 4:
			severtityValue = IViolation.SEVERITY_HIGH;
			break;
		case 5:
			severtityValue = IViolation.SEVERITY_VERY_HIGH;
			break;
		default:
			severtityValue = IViolation.SEVERITY_VERY_HIGH;
			break;
		}

		return severtityValue * getPolicyRule().getSeverityWeight();
	}

	@Override
	public void setCausingElement(IElement element) {
		this.causingFormula = element;
	}

	@Override
	public void setPolicyRule(AbstractPolicyRule policyRule) {
		this.policyRule = policyRule;
	}

	private Comparator<ScalarConstant> createComparator(){
		return new Comparator<ScalarConstant>() {
			@Override
			public int compare(ScalarConstant o1, ScalarConstant o2) {
				return getScalarDescription(o1).compareTo(getScalarDescription(o2));
			}
		};
	}
}
