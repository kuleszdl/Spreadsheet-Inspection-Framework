package sif.model.policy.policyrule.implementations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;
import sif.utilities.XML_Constants;

/***
 * A policy rule to check if a cell contains something else then it should be in respect to the enviroment of the cell.
 *
 * @author Sebastian Beck
 *
 */
public class OneAmongOthersPolicyRule extends MonolithicPolicyRule{
	@ConfigurableParameter(parameterClass = String[].class, displayedName = "Ignored Cells.", description = "Defines the cells that should be ignored by the inspection.")
	private String[] ignoredCells = {};

	//1=horizontal, 2=vertical, 3=cross
	@ConfigurableParameter(parameterClass = Integer.class, displayedName = "Style of the enviroment to test", description = "Defines the style of the enviroment to test against.(1=horizontal, 2=vertical, 3=cross)")
	private Integer enviromentStyle = 3;

	@ConfigurableParameter(parameterClass = Integer.class, displayedName = "Checking length.", description = "Defines the radius of the enviroment to test against.")
	private Integer enviromentLength = 2;

	public OneAmongOthersPolicyRule() {
		super();
		setAuthor("Sebastian Beck");
		setName("Smell: One among others");
		setDescription("Checks if a cell contains something else then it should be in respect to the enviroment of the cell.");
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
