/**
 * 
 */
package sif.model.violations.single;

import sif.model.policy.policyrule.dynamicConditions.EBinaryRelation;

/**
 * @author Manuel Lemcke
 * 
 */
public class BinaryConditionSingleViolation extends ConditionSingleViolation {

	private EBinaryRelation relation;

	/**
	 * 
	 */
	public BinaryConditionSingleViolation() {
	}

	public void setRelation(EBinaryRelation relation) {
		this.relation = relation;
	}

	@Override
	public EBinaryRelation getRelation() {
		return relation;
	}
}
