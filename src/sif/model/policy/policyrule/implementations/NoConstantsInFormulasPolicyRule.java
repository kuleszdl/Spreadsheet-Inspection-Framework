package sif.model.policy.policyrule.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.PolicyRuleType;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;

/***
 * A policy rule that defines which constants values are not allowed in
 * formulas.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class NoConstantsInFormulasPolicyRule extends MonolithicPolicyRule {

	@ConfigurableParameter(parameterClass = Object[].class, displayedName = "Ignored contstants", description = "Configures constants that will be ignored when checking this policy rule.")
	private Object[] ignoredConstants = {};

	@ConfigurableParameter(parameterClass = String[].class, displayedName = "Ignored functions", description = "Spefizies the functions, in which constants are allowed.")
	private String[] ignoredFunctions = {};

	@ConfigurableParameter(parameterClass = Cell[].class, displayedName = "Ignored Cells.", description = "Defines the cells that are allowed to contain constants in their formulas.")
	private Cell[] ignoredCells = {};

	public NoConstantsInFormulasPolicyRule() {
		super();
		setAuthor("Sebastian Zitzelsberger");
		setName("No Constants In Formulas");
		setDescription("Checks whether formulas contain constant values.");
		setBackground("Constant values are not alyways as constant as they seem in the beginning."
				+ " In case their values change, its hard to adjust the constants consistently in the spreadsheet if they are not located in individual cells.");
		setPossibleSolution("Extract the constants into to separate cells and reference these cells");
	}

	@Override
	public PolicyRuleType getType() {
		return PolicyRuleType.STATIC;
	}

}
