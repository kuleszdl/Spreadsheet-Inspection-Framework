package sif.model.policy.policyrule.dynamicConditions;

import java.util.Enumeration;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import sif.model.elements.containers.AbstractElementList;

@XmlSeeAlso({ TernaryCondition.class, BinaryCondition.class,
	ElementCountCondition.class })
public abstract class AbstractCondition {
	
	private String target;
	private String elementType;
	private String propertyName;
	private String value;

	/**
	 * Gets the relation between the expected and the actual value
	 * 
	 * @return
	 */
	@XmlTransient
    public abstract Enumeration<String> getRelation();

    /**
     * Sets the relation between the expected and the actual value
     * 
     * @param relation
     */
    public abstract void setRelation(Enumeration<String> relation);

    /**
     * Gets the value which is used to determine if the target or targets
     * fulfill the relation.
     * 
     * @param valueParam
     */
    public String getValue() {
    	return this.value;
    }

    /**
     * Sets the value which is used to determine if the target or targets
     * fulfill the relation.
     * 
     * @param valueParam
     */
    public void setValue(String valueParam) {
    	this.value = valueParam;
    }

    /**
     * Gets the address of the target in A1 notation or as name.
     * 
     * @param address
     *            the address to set
     */
    public String getTarget() {
    	return this.target;
    }

    /**
     * Sets the address of the target in A1 notation or as name.
     * 
     * @param address
     *            the address to set
     */
    public void setTarget(String valueParam) {
    	this.target = valueParam;
    }

    /**
     * Gets the class name of the SIF {@link AbstractElementClass} the target
     * {@link AbstractElementList} contains.
     * 
     * @return the element type
     */
    public String getElementType() {
    	return this.elementType;
    }

    /**
     * Sets the class name of the SIF {@link AbstractElementClass} the target
     * {@link AbstractElementList} contains.
     */
    public void setElementType(String valueParam) {
    	this.elementType = valueParam;
    }
    
    /**
     * Gets the name of the property of the checked element. If it is null and the element is an instance
     * of Cell or Range the value of the cell or range is used.
     * 
     * @return the property
     */
    public String getProperty() {
    	return this.propertyName;
    }

    /**
     * Sets the name of the property of the checked element. If it is null and the element is an instance
     * of Cell or Range the value of the cell or range is used.
     * 
     * @param property the property to set
     */
    public void setProperty(String property) {
    	this.propertyName = property;
    }
    
    
}
