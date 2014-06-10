package sif.model.policy.policyrule.sanityModel;

import sif.model.elements.IElement;

public class SanityTuple {
	/**
	 * The value looked up in the referencing Range
	 */
	public String value;
	/**
	 * The target worksheet name
	 */
	public String referencing;
	/**
	 * The containing Formula
	 */
	public IElement owner;
}
