package sif.model.policy.policyrule.dynamicConditions;

import sif.model.elements.basic.cell.CellContentType;

/**
 * The input which is used to set the spreadsheet in the desired state.
 * 
 * @author Manuel Lemcke
 *
 */
public class TestInput {
	private String value;
	private String target;
	private CellContentType type;
	
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the type
	 */
	public CellContentType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(CellContentType type) {
		this.type = type;
	}
}
