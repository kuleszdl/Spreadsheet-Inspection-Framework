/**
 * 
 */
package sif.model.policy.policyrule.dynamicConditions;

import java.util.Enumeration;

import javax.xml.bind.annotation.XmlTransient;

/**
 * A condition which checks how many elements of a certain type are in the spreadsheet inventory
 * 
 * @author Manuel Lemcke
 *
 */
public class ElementCountCondition extends AbstractCondition {

	/* (non-Javadoc)
	 * @see sif.model.policy.policyrule.dynamic.AbstractCondition#getRelation()
	 */
//	@Override
	@XmlTransient
	public Enumeration<String> getRelation() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sif.model.policy.policyrule.dynamic.AbstractCondition#setRelation(java.util.Enumeration)
	 */
//	@Override
	public void setRelation(Enumeration<String> relation) {
		// TODO Auto-generated method stub

	}

}
