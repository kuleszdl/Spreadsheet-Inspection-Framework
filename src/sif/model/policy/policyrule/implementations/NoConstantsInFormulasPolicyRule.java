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
 * A policy rule that defines which constants values are not allowed in
 * formulas.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlType(name = XML_Constants.NAME_NO_CONSTANTS_POLICY_RULE, propOrder = { 
		"ignoredConstants",
		"ignoredFunctions",
		"ignoredCells"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class NoConstantsInFormulasPolicyRule extends MonolithicPolicyRule {

	@ConfigurableParameter(parameterClass = Object[].class, displayedName = "Ignored contstants", description = "Configures constants that will be ignored when checking this policy rule.")
	private Object[] ignoredConstants = {};

	@ConfigurableParameter(parameterClass = String[].class, displayedName = "Ignored functions", description = "Spefizies the functions, in which constants are allowed.")
	private String[] ignoredFunctions = {};

	@ConfigurableParameter(parameterClass = String[].class, displayedName = "Ignored Cells.", description = "Defines the cells that are allowed to contain constants in their formulas.")
	private String[] ignoredCells = {};

	public NoConstantsInFormulasPolicyRule() {
		super();
		setAuthor("Sebastian Zitzelsberger");
		setName("No Constants In Formulas");
		setDescription("Checks whether formulas contain constant values.");
		setBackground("Constant values are not alyways as constant as they seem in the beginning."
				+ " In case their values change, its hard to adjust the constants consistently in the spreadsheet if they are not located in individual cells.");
		setPossibleSolution("Extract the constants into to separate cells and reference these cells");
		setType(PolicyRuleType.STATIC);
	}
	
	@XmlElementWrapper(name = XML_Constants.NAME_NO_CONSTANTS_IGNORED_CONSTANTS_WRAPPER)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_NO_CONSTANTS_IGNORED_CONSTANTS_VALUE, type = Object.class) })
	public Object[] getIgnoredConstants() {
		return ignoredConstants;
	}

	public void setIgnoredConstants(Object[] ignoredConstants) {
		this.ignoredConstants = ignoredConstants;
	}

	@XmlElementWrapper(name = XML_Constants.NAME_NO_CONSTANTS_IGNORED_FUNCTIONS_WRAPPER)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_NO_CONSTANTS_IGNORED_FUNCTION_NAME, type = String.class) })
	public String[] getIgnoredFunctions() {
		return ignoredFunctions;
	}

	public void setIgnoredFunctions(String[] ignoredFunctions) {
		this.ignoredFunctions = ignoredFunctions;
	}

	@XmlElementWrapper(name = XML_Constants.NAME_NO_CONSTANTS_IGNORED_CELLS_WRAPPER)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_NO_CONSTANTS_IGNORED_CELLS_VALUE, type = String.class) })
	public String[] getIgnoredCells() {
		return ignoredCells;
	}

	public void setIgnoredCells(String[] ignoredCells) {
		this.ignoredCells = ignoredCells;
	}


}
