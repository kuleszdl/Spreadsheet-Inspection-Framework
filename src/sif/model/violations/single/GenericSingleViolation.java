package sif.model.violations.single;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import sif.model.elements.IElement;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.violations.AbstractViolation;
import sif.model.violations.ISingleViolation;
import sif.utilities.XML_Constants;

/***
 * A generic single violation to record the violation of any policy rule.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlType(name = XML_Constants.NAME_SINGLE_VIOLATION)
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericSingleViolation extends AbstractViolation implements ISingleViolation {
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
	public String getDescription() {
		return descriptionBuilder.toString();
	}

	@Override
	public AbstractPolicyRule getPolicyRule() {
		return policyRule;
	}

	@Override
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


}
