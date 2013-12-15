package sif.model.policy.policyrule.dynamicConditions;

import java.util.Enumeration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sif.IO.xml.TernaryRelationAdapter;

/**
 * 
 * A condition which by default represents intervals
 * 
 * @author Manuel Lemcke
 *
 */
public class TernaryCondition extends AbstractCondition {
    private ETernaryRelation relation = ETernaryRelation.open;
	private Double value = 0.0;
    private Double value2 = 0.0;
    private String target;
    private String elementType;
//    private String property;
    
    /**
     * Defines the kind of interval (open, closed, openleft, openright).
     * 
     * Default: open
     * 
	 * @return the relation
	 */
    @XmlElement
	@XmlJavaTypeAdapter(TernaryRelationAdapter.class)    
	public ETernaryRelation getRelation() {
		return relation;
	}

	/**
	 * @return the value2
	 */
	public Double getValue2() {
		return value2;
	}
	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(Double value2) {
		this.value2 = value2;
	}
	
	@Override
	public String getValue() {
		return value.toString();
	}
	
	public void setRelation(ETernaryRelation relation) {
		if (relation instanceof ETernaryRelation)
			this.relation = (ETernaryRelation) relation;
		else
			throw new IllegalArgumentException(
					"The provided Enumeration member is not part of EBinaryRelation.");
	}
	
	@XmlTransient
	@Override
	public void setRelation(Enumeration<String> relation) {
		if (relation instanceof ETernaryRelation)
			this.relation = (ETernaryRelation) relation;
		else
			throw new IllegalArgumentException(
					"The provided Enumeration member is not part of EBinaryRelation.");
	}
	
	@Override
	public void setValue(String valueParam) {
		Double doubleValue = Double.parseDouble(valueParam);
		this.value = doubleValue;
	}

	
	public void setValue(Double valueParam) {
		this.value = valueParam;		
	}

	@Override
	@XmlTransient
	public String getTarget() {
		return target;
	}

	@Override
	public void setTarget(String valueParam) {
		this.target = valueParam;		
	}

	@Override
	@XmlTransient
	public String getElementType() {
		return elementType;
	}

	@Override
	public void setElementType(String valueParam) {
		this.elementType = valueParam;		
	}

	@Override
	public String getProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProperty(String property) {
		// TODO Auto-generated method stub
		
	}
}