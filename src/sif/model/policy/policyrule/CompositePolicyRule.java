package sif.model.policy.policyrule;

import sif.model.policy.expression.policyrule.PolicyRuleExpression;

/**
 * A composite policy rule defines its regulations via a
 * {@link PolicyRuleExpression}. It can be used to generate generic policy
 * rules. NOTE: Not used yet.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class CompositePolicyRule extends AbstractPolicyRule {
	private PolicyRuleExpression pExp;

	/**
	 * Gets the policy rule expression of the policy rule.
	 * 
	 * @return The policy expression.
	 */
	public PolicyRuleExpression getPolicyRuleExpression() {
		return this.pExp;
	}

	/**
	 * Gets the policy expression as a String.
	 * 
	 * @return The string representation of the policy rule expression.
	 */
	public String getRuleRepresentation() {
		return this.pExp.getStringRepresentation();
	}

	@Override
	public PolicyRuleType getType() {
		return PolicyRuleType.COMPOSITE;
	}
}