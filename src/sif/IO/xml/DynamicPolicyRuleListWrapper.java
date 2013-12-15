/**
 * 
 */
package sif.IO.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import sif.model.policy.policyrule.DynamicPolicyRule;

/**
 * This class helps JAXB to convert List<DynamicPolicyRule> from the XML 
 * specification to TreeMap<String, AbstractPolicyRule> in the {@link Policy}
 * 
 * @author Manuel Lemcke
 *
 */
public class DynamicPolicyRuleListWrapper {

	private List<DynamicPolicyRule> rules = new ArrayList<DynamicPolicyRule>();

	/**
	 * @return the rules
	 */
	@XmlElement(name="rule")
	public List<DynamicPolicyRule> getRules() {
		return rules;
	}

	/**
	 * @param rules the rules to set
	 */
	public void setRules(List<DynamicPolicyRule> rules) {
		this.rules = rules;
	}
}
