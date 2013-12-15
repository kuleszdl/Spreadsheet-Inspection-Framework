/**
 * 
 */
package sif.model.violations.single;

import java.util.Enumeration;

/**
 * @author Manuel Lemcke
 *
 */
public class CountSingleViolation extends ConditionSingleViolation {

	private int expectedCount;
	private int actualCount;
	
	/**
	 * 
	 */
	public CountSingleViolation() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see sif.model.violations.single.ConditionSingleViolation#getRelation()
	 */
	@Override
	public Enumeration<String> getRelation() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the expectedCount
	 */
	public int getExpectedCount() {
		return expectedCount;
	}

	/**
	 * @param expectedCount the expectedCount to set
	 */
	public void setExpectedCount(int expectedCount) {
		this.expectedCount = expectedCount;
	}

	/**
	 * @return the actualCount
	 */
	public int getActualCount() {
		return actualCount;
	}

	/**
	 * @param actualCount the actualCount to set
	 */
	public void setActualCount(int actualCount) {
		this.actualCount = actualCount;
	}

}
