package sif.model.policy.policyrule.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;

/***
 * A policy rule to check if a cell contains something else then it should be in respect to the enviroment of the cell.
 * 
 * @author Sebastian Beck
 *
 */
public class OneAmongOthersPolicyRule extends MonolithicPolicyRule{
	@ConfigurableParameter(parameterClass = Cell[].class, displayedName = "Ignored Cells.", description = "Defines the cells that should be ignored by the inspection.")
	private Cell[] ignoredCells = {};

	//1=horizontal, 2=vertical, 3=cross
	@ConfigurableParameter(parameterClass = Integer.class, displayedName = "Style of the enviroment to test", description = "Defines the style of the enviroment to test against.(1=horizontal, 2=vertical, 3=cross)")
	private Cell[] enviromentStyle = {};
	
	@ConfigurableParameter(parameterClass = Integer.class, displayedName = "Ignored Cells.", description = "Defines the radius of the enviroment to test against.")
	private Cell[] enviromentLength = {};
	
	public OneAmongOthersPolicyRule() {
		super();
		setAuthor("Sebastian Beck");
		setName("Policy Rule: One among others");
		setDescription("Checks if a cell contains something else then it should be in respect to the enviroment of the cell.");
	}
}
