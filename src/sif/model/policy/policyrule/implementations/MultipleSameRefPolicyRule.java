package sif.model.policy.policyrule.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;

/***
 * A police rule to find multiple same references inside one cell.
 * 
 * @author Sebastian Beck
 *
 */

public class MultipleSameRefPolicyRule extends MonolithicPolicyRule {
	@ConfigurableParameter(parameterClass = Cell[].class, displayedName = "Ignored Cells.", description = "Defines the cells that are allowed to reference against reading direction.")
	private Cell[] ignoredCells = {};

	public MultipleSameRefPolicyRule() {
		super();
		setAuthor("Sebastian Beck");
		setName("Policy Rule: Reference to null");
		setDescription("Checks if a referenced cells value is null.");
	}
}
