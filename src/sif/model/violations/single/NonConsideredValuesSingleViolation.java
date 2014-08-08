package sif.model.violations.single;

import sif.model.elements.IElement;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.violations.IViolation;

public class NonConsideredValuesSingleViolation extends GenericSingleViolation{

	private IElement causingRef;

	private AbstractPolicyRule policyRule;

	@Override
	public IElement getCausingElement() {
		return causingRef;
	}

	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append(causingRef.getStringRepresentation());
		description.append("is not referenced");
		return description.toString();
	}

	@Override
	public AbstractPolicyRule getPolicyRule() {
		return policyRule;
	}

	@Override
	public Double getWeightedSeverityValue() {
		Double severtityValue = IViolation.SEVERITY_HIGH;
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

}
