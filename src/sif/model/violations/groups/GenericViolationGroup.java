package sif.model.violations.groups;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import sif.model.elements.IElement;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.violations.ISingleViolation;
import sif.model.violations.IViolationGroup;
import sif.utilities.XML_Constants;

/***
 * A generic violation group for arbitrary violations.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlType(name = XML_Constants.NAME_GROUP_VIOLATION, propOrder = {
		"content",
		"location",
		"description",
		"weightedSeverityValue",
		"members"
})
@XmlAccessorType(XmlAccessType.NONE)
public class GenericViolationGroup implements IViolationGroup {
	@XmlTransient
	private AbstractPolicyRule policyRule;
	@XmlTransient
	private IElement causingElement;
	private ArrayList<ISingleViolation> groupMembers;
	@XmlTransient
	private StringBuilder descriptionBuilder = new StringBuilder();
	
	/**
	 * Only for jaxb
	 */
	public GenericViolationGroup(){
	}

	public GenericViolationGroup(AbstractPolicyRule policyRule) {
		this.policyRule = policyRule;
		descriptionBuilder.append("Violation Group");
		groupMembers = new ArrayList<ISingleViolation>();
	}

	@Override
	public void add(ISingleViolation singleViolation) {
		groupMembers.add(singleViolation);
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
	@XmlElement(name = XML_Constants.NAME_SINGLE_VIOLATION)
	public ArrayList<ISingleViolation> getMembers() {
		return groupMembers;
	}

	@Override
	public AbstractPolicyRule getPolicyRule() {
		return this.policyRule;
	}

	@Override
	@XmlAttribute(name = XML_Constants.NAME_SINGLE_VIOLATION_SEVERITY)
	public Double getWeightedSeverityValue() {
		Double severityValue = 0.0;
		for (ISingleViolation singleViolation : groupMembers) {
			severityValue = severityValue
					+ singleViolation.getWeightedSeverityValue();
		}
		return severityValue;
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
