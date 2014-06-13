package sif.model.policy.policyrule;

/**
 * Enum for the types of the policy rules
 * 
 * @author Fabian Toth
 */
public enum PolicyRuleType {
	
	/**
	 * A static policy
	 */
	STATIC,
	/**
	 * A dynamic policy
	 */
	DYNAMIC,
	/**
	 * A plausibility policy
	 */
	SANITY,
	/**
	 * A composite policy
	 */
	COMPOSITE;
	
}
