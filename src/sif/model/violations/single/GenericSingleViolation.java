package sif.model.violations.single;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import sif.model.elements.IElement;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.violations.ISingleViolation;
import sif.utilities.XML_Constants;

/***
 * A generic single violation to record the violation of any policy rule.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlType(name = XML_Constants.NAME_SINGLE_VIOLATION, propOrder = {
		"content",
		"location",
		"description",
		"weightedSeverityValue"
})
@XmlAccessorType(XmlAccessType.NONE)
public class GenericSingleViolation implements ISingleViolation {
	@XmlTransient
	private Double severityValue = 0.0;
	@XmlTransient
	private AbstractPolicyRule policyRule;
	@XmlTransient
	private IElement causingElement;
	@XmlTransient
	private StringBuilder descriptionBuilder = new StringBuilder();

	/**
	 * Appends the given decription part to the description.
	 * 
	 * @param descriptionPart
	 */
	public void appendToDescription(String descriptionPart) {
		this.descriptionBuilder.append(descriptionPart);
	}

	@Override
	public IElement getCausingElement() {
		return causingElement;
	}

	@Override
	@XmlAttribute(name = XML_Constants.NAME_SINGLE_VIOLATION_DESCRIPTION)
	public String getDescription() {
		return descriptionBuilder.toString();
	}

	@Override
	public AbstractPolicyRule getPolicyRule() {
		return policyRule;
	}

	@Override
	@XmlAttribute(name = XML_Constants.NAME_SINGLE_VIOLATION_SEVERITY)
	public Double getWeightedSeverityValue() {
		return severityValue;
	}

	public void setBaseSeverityValue(Double severityValue) {
		this.severityValue = severityValue * policyRule.getSeverityWeight();
	}

	@Override
	public void setCausingElement(IElement element) {
		this.causingElement = element;
	}

	@Override
	public void setPolicyRule(AbstractPolicyRule policyRule) {
		this.policyRule = policyRule;
	}
	
	@XmlAttribute(name = XML_Constants.NAME_SINGLE_VIOLATION_CAUSING)
	public String getContent(){
		return getCausingElement().getStringRepresentation();
	}
	
	@XmlAttribute(name = XML_Constants.NAME_SINGLE_VIOLATION_LOCATION)
	public String getLocation(){
		return getCausingElement().getLocation();
	}

}
