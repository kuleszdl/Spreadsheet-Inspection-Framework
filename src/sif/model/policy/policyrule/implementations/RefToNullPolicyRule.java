package sif.model.policy.policyrule.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;

/***
 * A policy rule to check if a reference point to a null value. 
 * 
 * @author Sebastian Beck
 *
 */

public class RefToNullPolicyRule extends MonolithicPolicyRule{
	@ConfigurableParameter(parameterClass = Cell[].class, displayedName = "Ignored Cells.", description = "Defines the cells that should be ignored by the inspection.")
	private Cell[] ignoredCells = {};

	public RefToNullPolicyRule() {
		super();
		setAuthor("Sebastian Beck");
		setName("Policy Rule: Reference to null");
		setDescription("Checks if a referenced cells value is null.");
	}
}
