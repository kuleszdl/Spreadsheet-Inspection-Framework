package sif.model.policy.policyrule.implementations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;
import sif.utilities.XML_Constants;

/***
 * A policy rule to check if a reference point to a null value.
 *
 * @author Sebastian Beck
 *
 */

public class RefToNullPolicyRule extends MonolithicPolicyRule{
	@ConfigurableParameter(parameterClass = String[].class, displayedName = "Ignored Cells.", description = "Defines the cells that should be ignored by the inspection.")
	private String[] ignoredCells = {};

	public RefToNullPolicyRule() {
		super();
		setAuthor("Sebastian Beck");
		setName("Smell: Reference to null");
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
