package sif.model.policy;

import java.util.TreeMap;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sif.IO.xml.DynamicPolicyRuleMapAdapter;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.implementations.FormulaComplexityPolicyRule;
import sif.model.policy.policyrule.implementations.NoConstantsInFormulasPolicyRule;
import sif.model.policy.policyrule.implementations.NonConsideredValuesPolicyRule;
import sif.model.policy.policyrule.implementations.OneAmongOthersPolicyRule;
import sif.model.policy.policyrule.implementations.ReadingDirectionPolicyRule;
import sif.model.policy.policyrule.implementations.RefToNullPolicyRule;
import sif.model.policy.policyrule.implementations.StringDistancePolicyRule;
import sif.utilities.XML_Constants;

@XmlSeeAlso({ DynamicPolicyRule.class, FormulaComplexityPolicyRule.class,
		NoConstantsInFormulasPolicyRule.class, ReadingDirectionPolicyRule.class,
		NonConsideredValuesPolicyRule.class, OneAmongOthersPolicyRule.class,
		RefToNullPolicyRule.class, StringDistancePolicyRule.class})
@XmlAccessorType(XmlAccessType.FIELD)
public class Policy {
	@XmlAttribute(required = false)
	private String name;

	@XmlAttribute(required = false)
	private String description;

	@XmlAttribute(required = false)
	private String author;
	
	@XmlElement(name= XML_Constants.NAME_DYNAMIC_POLICY_RULE)
	@XmlJavaTypeAdapter(DynamicPolicyRuleMapAdapter.class)
	TreeMap<String, AbstractPolicyRule> abstractPolicyRules = new TreeMap<String, AbstractPolicyRule>();

	public void add(AbstractPolicyRule abstractPolicyRule) {
		this.abstractPolicyRules.put(abstractPolicyRule.getName(),
				abstractPolicyRule);
	}

	public String getAuthor() {
		return this.author;
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	public TreeMap<String, AbstractPolicyRule> getPolicyRules() {
		return abstractPolicyRules;
	}

	public AbstractPolicyRule getRuleByName(String name) {
		return abstractPolicyRules.get(name);
	}

	public void remove(AbstractPolicyRule abstractPolicyRule) {
		this.abstractPolicyRules.remove(abstractPolicyRule);
	}

	public void remove(UUID policyRuleId) {
		this.abstractPolicyRules.remove(policyRuleId);
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}
}