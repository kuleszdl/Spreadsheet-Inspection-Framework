package sif.model.policy.policyrule.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;

/***
 * A policy rule to find non considered constants.
 *
 * @author Sebastian Beck
 *
 */
public class NonConsideredValuesPolicyRule extends MonolithicPolicyRule {
	@ConfigurableParameter(parameterClass = Cell[].class, displayedName = "Ignored Cells.", description = "Defines the cells that should be ignored by the inspection.")
	private Cell[] ignoredCells = {};

	public NonConsideredValuesPolicyRule() {
		super();
		setAuthor("Sebastian Beck");
		setName("Policy Rule: Non considered values");
		setDescription("Checks if a constant value is nowhere considered.");
	}
}