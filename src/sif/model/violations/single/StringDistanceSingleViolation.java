package sif.model.violations.single;

import sif.model.elements.IElement;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.violations.ISingleViolation;
import sif.model.violations.IViolation;

public class StringDistanceSingleViolation implements ISingleViolation{

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
		description.append("maybe has a typo");
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
