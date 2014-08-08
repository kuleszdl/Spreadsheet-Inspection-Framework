package sif.model.policy.policyrule.implementations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.PolicyRuleType;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;
import sif.utilities.XML_Constants;

/***
 * A policy rule to find typos with the help of the string distance
 * 
 * @author Sebastian Beck
 *
 */
@XmlType(name = XML_Constants.NAME_STRING_DISTANCE_POLICY, propOrder = {
	"maxDistance",
	"ignoredCells"
})
@XmlAccessorType(XmlAccessType.NONE)
public class StringDistancePolicyRule extends MonolithicPolicyRule{
	
	@ConfigurableParameter(parameterClass = String[].class, displayedName = "Ignored Cells.", description = "Defines the cells that should get ignored by the inspection.")
	private String[] ignoredCells = {};

	@XmlElement(name = XML_Constants.NAME_STRING_DISTANCE_DIFFERENCE)
	@ConfigurableParameter(parameterClass = Integer.class, displayedName = "Max. Levenshtein.", description = "Defines the max. number of changes  for which a failure should get reported.")
	private int maxDistance = 1;
	
	public StringDistancePolicyRule() {
		super();
		setAuthor("Sebastian Beck");
		setName("Policy Rule: String distance");
		setDescription("Checks with the help of the string distance for typos.");
		setType(PolicyRuleType.COMPOSITE);
	}
	
	@XmlElementWrapper(name = XML_Constants.NAME_IGNORED_CELLS_WRAPPER, required = false)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_IGNORED_CELLS_VALUE, type = String.class) })
	public String[] getIgnoredCells() {
		return ignoredCells;
	}

	public void setIgnoredCells(String[] ignoredCells) {
		this.ignoredCells = ignoredCells;
	}

}
