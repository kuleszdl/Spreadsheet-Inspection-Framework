package sif.model.policy.policyrule.implementations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import sif.model.elements.basic.cell.Cell;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;
import sif.utilities.XML_Constants;

/***
 * A policy rule to find non considered constants.
 *
 * @author Sebastian Beck
 *
 */
public class NonConsideredValuesPolicyRule extends MonolithicPolicyRule {
	@ConfigurableParameter(parameterClass = Cell[].class, displayedName = "Ignored Cells.", description = "Defines the cells that should be ignored by the inspection.")
	private String[] ignoredCells = {};

	public NonConsideredValuesPolicyRule() {
		super();
		setAuthor("Sebastian Beck");
		setName("Policy Rule: Non considered values");
		setDescription("Checks if a constant value is nowhere considered.");
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