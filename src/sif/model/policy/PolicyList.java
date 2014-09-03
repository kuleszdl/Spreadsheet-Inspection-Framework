package sif.model.policy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import sif.model.policy.policyrule.SanityPolicyRule;
import sif.model.policy.policyrule.implementations.FormulaComplexityPolicyRule;
import sif.model.policy.policyrule.implementations.MultipleSameRefPolicyRule;
import sif.model.policy.policyrule.implementations.NoConstantsInFormulasPolicyRule;
import sif.model.policy.policyrule.implementations.NonConsideredValuesPolicyRule;
import sif.model.policy.policyrule.implementations.OneAmongOthersPolicyRule;
import sif.model.policy.policyrule.implementations.ReadingDirectionPolicyRule;
import sif.model.policy.policyrule.implementations.RefToNullPolicyRule;
import sif.model.policy.policyrule.implementations.StringDistancePolicyRule;
import sif.utilities.XML_Constants;

@XmlType(name = XML_Constants.NAME_POLICY_LIST 
,propOrder = { 
		"dynamicPolicy", 
		"sanityPolicyRule", 
		"readingPolicyRule", 
		"noConstantsInFormulasRule", 
		"formulaComplexityRule",
		"nonConsideredValuesRule",
		"oneAmongOthersRule",
		"refToNullRule",
		"stringDistanceRule",
		"multipleSameRefPolicyRule"
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class PolicyList {

	private DynamicPolicy dynamicPolicy;
	private SanityPolicyRule sanityPolicyRule;
	private ReadingDirectionPolicyRule readingPolicyRule;
	private FormulaComplexityPolicyRule formulaComplexityRule;
	private NoConstantsInFormulasPolicyRule noConstantsInFormulasRule;
	private NonConsideredValuesPolicyRule nonConsideredValuesRule;
	private OneAmongOthersPolicyRule oneAmongOthersRule;
	private RefToNullPolicyRule refToNullRule;
	private StringDistancePolicyRule stringDistanceRule;
	private MultipleSameRefPolicyRule multipleSameRefPolicyRule;

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

	@XmlElement(name = XML_Constants.NAME_NO_CONSTANTS_POLICY_RULE, type = NoConstantsInFormulasPolicyRule.class, required = false)
	public NoConstantsInFormulasPolicyRule getNoConstantsInFormulasRule() {
		return noConstantsInFormulasRule;
	}
	public void setNoConstantsInFormulasRule(NoConstantsInFormulasPolicyRule noConstantsInFormulasRule) {
		this.noConstantsInFormulasRule = noConstantsInFormulasRule;
	}

	@XmlElement(name = XML_Constants.NAME_NON_CONSIDERED_VALUES_POLICY, required = false)
	public NonConsideredValuesPolicyRule getNonConsideredValuesRule() {
		return nonConsideredValuesRule;
	}
	public void setNonConsideredValuesRule(
			NonConsideredValuesPolicyRule nonConsideredValuesRule) {
		this.nonConsideredValuesRule = nonConsideredValuesRule;
	}

	@XmlElement(name = XML_Constants.NAME_ONE_AMONG_OTHERS_POLICY, required = false)
	public OneAmongOthersPolicyRule getOneAmongOthersRule() {
		return oneAmongOthersRule;
	}
	public void setOneAmongOthersRule(OneAmongOthersPolicyRule oneAmongOthersRule) {
		this.oneAmongOthersRule = oneAmongOthersRule;
	}

	@XmlElement(name = XML_Constants.NAME_REF_TO_NULL_POLICY, required = false)
	public RefToNullPolicyRule getRefToNullRule() {
		return refToNullRule;
	}
	public void setRefToNullRule(RefToNullPolicyRule refToNullRule) {
		this.refToNullRule = refToNullRule;
	}

	@XmlElement(name = XML_Constants.NAME_STRING_DISTANCE_POLICY, required = false)
	public StringDistancePolicyRule getStringDistanceRule() {
		return stringDistanceRule;
	}
	public void setStringDistanceRule(StringDistancePolicyRule stringDistanceRule) {
		this.stringDistanceRule = stringDistanceRule;
	}

	@XmlElement(name = XML_Constants.NAME_MULTIPLE_SAME_REF_POLICY, required = false)
	public MultipleSameRefPolicyRule getMultipleSameRefPolicyRule() {
		return multipleSameRefPolicyRule;
	}
	public void setMultipleSameRefPolicyRule(MultipleSameRefPolicyRule multipleSameRefPolicyRule) {
		this.multipleSameRefPolicyRule = multipleSameRefPolicyRule;
	}
	public DynamicPolicy getCompletePolicy(){
		if (dynamicPolicy == null){
			dynamicPolicy = new DynamicPolicy();
		}
		if (sanityPolicyRule != null){
			dynamicPolicy.add(sanityPolicyRule);
		}
		if (readingPolicyRule != null){
			dynamicPolicy.add(readingPolicyRule);
		}
		if (formulaComplexityRule != null){
			dynamicPolicy.add(formulaComplexityRule);
		}
		if (noConstantsInFormulasRule != null){
			dynamicPolicy.add(noConstantsInFormulasRule);
		}
		if (nonConsideredValuesRule != null){
			dynamicPolicy.add(nonConsideredValuesRule);
		}
		if (oneAmongOthersRule != null){
			dynamicPolicy.add(oneAmongOthersRule);
		}
		if (refToNullRule != null){
			dynamicPolicy.add(refToNullRule);
		}
		if (stringDistanceRule != null){
			dynamicPolicy.add(stringDistanceRule);
		}
		if (multipleSameRefPolicyRule != null){
			dynamicPolicy.add(multipleSameRefPolicyRule);
		}
		return dynamicPolicy;
	}

}
