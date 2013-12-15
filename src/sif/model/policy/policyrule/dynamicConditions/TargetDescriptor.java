package sif.model.policy.policyrule.dynamicConditions;

public class TargetDescriptor {
	
	private String address;
	private String type;
	/**
	 * The address of the target either in R1C1 notation or a name.
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * Sets the address of the target in R1C1 notation or as name.
	 * 
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * The name of the SIF {@link AbstractElementClass} the target {@link AbstractElementList} 
	 * contains.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
