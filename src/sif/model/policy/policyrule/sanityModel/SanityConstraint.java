package sif.model.policy.policyrule.sanityModel;

/**
 * Defines a SanityConstraint
 * @author Wolfgang Kraus
 *
 */
public class SanityConstraint {
	/**
	 * The Value to be looked up
	 */
	public String value;

	/**
	 * The target of the restriction
	 */
	public String definedFor;
	/**
	 * The sane values for the definedFor references
	 */
	public String[] allowedValues;
	/**
	 * The source for the restriction, usually a worksheet
	 */
	public String definedFrom;
	
	/**
	 * An optional explanation for the constraint, useful if the restriction is a regular expression 
	 */
	public String explanation;
}
