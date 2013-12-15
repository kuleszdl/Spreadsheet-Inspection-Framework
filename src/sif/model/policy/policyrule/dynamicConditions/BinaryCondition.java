package sif.model.policy.policyrule.dynamicConditions;

import java.util.Enumeration;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sif.IO.xml.BinaryRelationAdapter;

/**
 * A condition that provides means for comparing cell values to certain values.
 * 
 * @author Manuel Lemcke
 * 
 */
public class BinaryCondition extends AbstractCondition {
	private EBinaryRelation relation;

	/**
	 * @return the relation
	 */
	@Override
	@XmlJavaTypeAdapter(BinaryRelationAdapter.class)
	public EBinaryRelation getRelation() {
		return relation;
	}

	/**
	 * 
	 * 
	 * @param relation
	 *            the relation to set
	 * 
	 */
	public void setRelation(EBinaryRelation relation) {
		this.relation = relation;
	}

	/* (non-Javadoc)
	 * @see sif.model.policy.policyrule.dynamic.AbstractCondition#setRelation(java.util.Enumeration)
	 */
	@Override
	@XmlTransient
	public void setRelation(Enumeration<String> relation) {
		// TODO Auto-generated method stub
		
	}
}