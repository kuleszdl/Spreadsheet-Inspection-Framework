package sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers;

import sif.model.inspection.SpreadsheetInventory;
import sif.model.policy.policyrule.AbstractPolicyRule;

public abstract class TargetlessConditionChecker implements IConditionChecker {
	
	protected AbstractPolicyRule policyRule;
	protected SpreadsheetInventory inventory;
	
	@SuppressWarnings("unused")
	private TargetlessConditionChecker() {
		
	}
	
	public TargetlessConditionChecker(AbstractPolicyRule rule) {
		this.policyRule = rule;
	}

	public AbstractPolicyRule getPolicyRule() {
		return policyRule;
	}

	public void setPolicyRule(AbstractPolicyRule policyRule) {
		this.policyRule = policyRule;
	}

	public SpreadsheetInventory getInventory() {
		return inventory;
	}

	public void setInventory(SpreadsheetInventory inventory) {
		this.inventory = inventory;
	}

}
