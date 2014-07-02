package sif.model.policy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import sif.model.policy.policyrule.SanityPolicyRule;
import sif.model.policy.policyrule.implementations.FormulaComplexityPolicyRule;
import sif.model.policy.policyrule.implementations.NoConstantsInFormulasPolicyRule;
import sif.model.policy.policyrule.implementations.ReadingDirectionPolicyRule;
import sif.utilities.XML_Constants;

@XmlType(name = XML_Constants.NAME_POLICY_LIST 
	,propOrder = { 
		"dynamicPolicy", 
		"sanityPolicyRule", 
		"readingPolicyRule", 
		"noConstantsRule", 
		"formulaComplexityRule"}
	)
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PolicyList {

	private DynamicPolicy dynamicPolicy;
	private SanityPolicyRule sanityPolicyRule;
	private NoConstantsInFormulasPolicyRule noConstantsRule;
	private ReadingDirectionPolicyRule readingPolicyRule;
	private FormulaComplexityPolicyRule formulaComplexityRule;
	
	@XmlElement(name = XML_Constants.NAME_DYNAMIC_POLICY, type = DynamicPolicy.class, required = false)
	public DynamicPolicy getDynamicPolicy() {
		return dynamicPolicy;
	}
	public void setDynamicPolicy(DynamicPolicy dynamicPolicy) {
		this.dynamicPolicy = dynamicPolicy;
	}
	
	@XmlElement(name = XML_Constants.NAME_SANITY_POLICY_RULE, type = SanityPolicyRule.class, required = false) 
	public SanityPolicyRule getSanityPolicyRule() {
		return sanityPolicyRule;
	}
	public void setSanityPolicyRule(SanityPolicyRule sanityPolicyRule) {
		this.sanityPolicyRule = sanityPolicyRule;
	}
	
	@XmlElement(name = XML_Constants.NAME_NO_CONSTANTS_POLICY_RULE, type = NoConstantsInFormulasPolicyRule.class, required = false)
	public NoConstantsInFormulasPolicyRule getNoConstantsRule() {
		return noConstantsRule;
	}
	public void setNoConstantsRule(NoConstantsInFormulasPolicyRule noConstantsRule) {
		this.noConstantsRule = noConstantsRule;
	}
	
	@XmlElement(name = XML_Constants.NAME_READING_DIRECTION_POLICY_RULE, type = ReadingDirectionPolicyRule.class, required = false)
	public ReadingDirectionPolicyRule getReadingPolicyRule() {
		return readingPolicyRule;
	}

	public void setReadingPolicyRule(ReadingDirectionPolicyRule readingPolicyRule) {
		this.readingPolicyRule = readingPolicyRule;
	}
	
	@XmlElement(name = XML_Constants.NAME_FORMULA_COMPLEXITY_POLICY_RULE, type = FormulaComplexityPolicyRule.class, required = false)
	public FormulaComplexityPolicyRule getFormulaComplexityRule() {
		return formulaComplexityRule;
	}
	public void setFormulaComplexityRule(
			FormulaComplexityPolicyRule formulaComplexityRule) {
		this.formulaComplexityRule = formulaComplexityRule;
	}
	
	
}
