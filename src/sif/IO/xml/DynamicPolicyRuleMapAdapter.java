/**
 * 
 */
package sif.IO.xml;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.DynamicPolicyRule;

/**
 * This adapter class tells JAXB how to convert List<DynamicPolicyRule> from the 
 * XML specification to TreeMap<String, AbstractPolicyRule> in the {@link Policy}
 * 
 * @author Manuel Lemcke
 *
 */
public class DynamicPolicyRuleMapAdapter 
	extends XmlAdapter<DynamicPolicyRuleListWrapper, TreeMap<String, AbstractPolicyRule>> {
	
	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public DynamicPolicyRuleListWrapper marshal(
			TreeMap<String, AbstractPolicyRule> v) throws Exception {
		DynamicPolicyRuleListWrapper listWrapper = new DynamicPolicyRuleListWrapper();
		
		// Since currently only DynamicPolicyRules are supported this is a List
		// of DynamicPolicyRules.
		ArrayList<DynamicPolicyRule> list = new ArrayList<DynamicPolicyRule>();
		for (AbstractPolicyRule rule : v.values()) {
			if (rule instanceof DynamicPolicyRule) {
				list.add((DynamicPolicyRule) rule);			
			}
		}
		
		listWrapper.setRules(list);
		return listWrapper;
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public TreeMap<String, AbstractPolicyRule> unmarshal(
			DynamicPolicyRuleListWrapper v) throws Exception {
		TreeMap<String, AbstractPolicyRule> map = new TreeMap<String, AbstractPolicyRule>();
		for (DynamicPolicyRule rule : v.getRules()) {
			map.put(rule.getName(), rule);
		}
		return map;
	}
}
