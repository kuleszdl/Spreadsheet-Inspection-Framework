/**
 * 
 */
package sif.model.violations.single;

import java.util.Enumeration;

import sif.model.elements.IElement;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.EBinaryRelation;
import sif.model.policy.policyrule.dynamicConditions.ETernaryRelation;
import sif.model.violations.ISingleViolation;

/**
 * @author Manuel Lemcke
 *
 */
public class ConditionSingleViolation implements ISingleViolation {
	
	private Enumeration<String> relation = null;
	private String expectedValue = null;
	private String actualValue = null;
	
	private Double severityValue = 0.0;
	private AbstractPolicyRule policyRule;
	private IElement causingElement;
	private StringBuilder descriptionBuilder = new StringBuilder();

	/**
	 * 
	 */
	public ConditionSingleViolation() {
	}
	
	/**
	 * Appends the given decription part to the description.
	 * 
	 * @param descriptionPart
	 */
	public void appendToDescription(String descriptionPart) {
		this.descriptionBuilder.append(descriptionPart);
	}

	/* (non-Javadoc)
	 * @see sif.model.violations.IViolation#getCausingElement()
	 */
	@Override
	public IElement getCausingElement() {
		return causingElement;
	}

	/* (non-Javadoc)
	 * @see sif.model.violations.IViolation#getDescription()
	 */
	@Override
	public String getDescription() {
		return descriptionBuilder.toString();
	}

	/* (non-Javadoc)
	 * @see sif.model.violations.IViolation#getPolicyRule()
	 */
	@Override
	public AbstractPolicyRule getPolicyRule() {
		return policyRule;
	}

	public Double getBaseSeverityValue() {
		return severityValue;
	}
	
	/**
	 * @return the relation
	 */
	public Enumeration<String> getRelation() {
		return this.relation;
	}
	
	/**
	 * @return the expectedValue
	 */
	public String getExpectedValue() {
		return expectedValue;
	}

	/**
	 * @return the actualValue
	 */
	public String getActualValue() {
		return actualValue;
	}

	/* (non-Javadoc)
	 * @see sif.model.violations.IViolation#setCausingElement(sif.model.elements.IElement)
	 */
	@Override
	public void setCausingElement(IElement element) {
		causingElement = element;
		
	}

	/* (non-Javadoc)
	 * @see sif.model.violations.IViolation#setPolicyRule(sif.model.policy.policyrule.AbstractPolicyRule)
	 */
	@Override
	public void setPolicyRule(AbstractPolicyRule policyRule) {
		this.policyRule = policyRule;		
	}

	/**
	 * @param relation the relation to set
	 */
	public void setRelation(Enumeration<String> relation) {
		this.relation = relation;
	}

	/**
	 * @param expectedValue the expectedValue to set
	 */
	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}

	/**
	 * @param actualValue the actualValue to set
	 */
	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	/* (non-Javadoc)
	 * @see sif.model.violations.IViolation#getWeightedSeverityValue()
	 */
	@Override
	public Double getWeightedSeverityValue() {
		// TODO Auto-generated method stub
		return this.severityValue;
	}
	
	/**
	 * 
	 * @param severityValue
	 */
	public void setBaseSeverityValue(Double severityValue) {
		this.severityValue = severityValue * policyRule.getSeverityWeight();
	}

}
