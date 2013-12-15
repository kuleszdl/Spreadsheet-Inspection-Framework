/**
 * 
 */
package sif.model.policy.policyrule;

/**
 * Represents information about a input or output cell saved in a policy.
 * 
 * @author Manuel Lemcke
 *
 */
public class IOCellInfo {
	
	private String name;
	private String a1Address;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the a1Address
	 */
	public String getA1Address() {
		return a1Address;
	}
	/**
	 * @param a1Address the a1Address to set
	 */
	public void setA1Address(String a1Address) {
		this.a1Address = a1Address;
	}
	
	

}
