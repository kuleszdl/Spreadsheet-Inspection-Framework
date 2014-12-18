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

@XmlType(name = XML_Constants.NAME_ERROR_CONTAINING_CELL_POLICY, propOrder = {
		"ignoredCells"
	})
@XmlAccessorType(XmlAccessType.NONE)
public class ErrorContainingCellPolicyRule extends MonolithicPolicyRule{
	@ConfigurableParameter(parameterClass = String[].class, displayedName = "Ignored Cells.", description = "Defines the cells that should get ignored by the inspection.")
	private String[] ignoredCells = {};
	
	public ErrorContainingCellPolicyRule() {
		super();
		setAuthor("Sebastian Beck");
		setName("Error in formulas");
		setDescription("Checks for cells being marked as \"#ERROR\".");
		setType(PolicyRuleType.STATIC);
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
