package sif.model.policy.policyrule.implementations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;
import sif.utilities.XML_Constants;

/***
 * A police rule to find multiple same references inside one cell.
 *
 * @author Sebastian Beck
 *
 */
@XmlType(name = XML_Constants.NAME_MULTIPLE_SAME_REF_POLICY, propOrder = {
		"ignoredCells"
})
public class MultipleSameRefPolicyRule extends MonolithicPolicyRule {
	@ConfigurableParameter(parameterClass = String[].class, displayedName = "Ignored Cells.", description = "Defines the cells that are allowed to reference against reading direction.")
	private String[] ignoredCells = {};

	public MultipleSameRefPolicyRule() {
		super();
		setAuthor("Sebastian Beck");
		setName("Policy Rule: Reference to null");
		setDescription("Checks if a referenced cells value is null.");
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
