package sif.model.violations.single;

import sif.model.elements.IElement;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.violations.IViolation;

public class StringDistanceSingleViolation extends GenericSingleViolation{

	private IElement causingRef;
	private IElement nearestMatch;
	private AbstractPolicyRule policyRule;

	@Override
	public IElement getCausingElement() {
		return causingRef;
	}

	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();

		description.append(causingRef.getValueAsString());
		description.append(" appears only once, nearest match would be: "
				+ nearestMatch.getValueAsString());
		return description.toString();
	}

	@Override
	public AbstractPolicyRule getPolicyRule() {
		return policyRule;
	}

	@Override
	public Double getWeightedSeverityValue() {
		Double severtityValue = IViolation.SEVERITY_MEDIUM;
		return severtityValue;
	}

	@Override
	public void setCausingElement(IElement element) {
		this.causingRef = element;

	}

	@Override
	public void setPolicyRule(AbstractPolicyRule policyRule) {
		this.policyRule = policyRule;

	}

	public IElement getNearestMatch() {
		return nearestMatch;
	}

	public void setNearestMatch(IElement nearestMatch) {
		this.nearestMatch = nearestMatch;
	}

}
