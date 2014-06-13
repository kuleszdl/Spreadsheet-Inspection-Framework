package sif.model.policy.policyrule.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.PolicyRuleType;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;

/**
 * A policy rule that defines that references must be readable in configurable
 * directions.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class ReadingDirectionPolicyRule extends MonolithicPolicyRule {

	@ConfigurableParameter(parameterClass = Boolean.class, displayedName = "Readable from left to right.", description = "Defines if the spreadsheet must be readable from left to right.")
	private Boolean mustBeLeftToRightReadable = true;

	@ConfigurableParameter(parameterClass = Boolean.class, displayedName = "Readable from top to bottom.", description = "Defines if the spreadsheet must be readable from top to bottom.")
	private Boolean mustBeTopToBottomReadable = true;

	@ConfigurableParameter(parameterClass = Cell[].class, displayedName = "Ignored Cells.", description = "Defines the cells that are allowed to reference against reading direction.")
	private Cell[] ignoredCells = {};

	public ReadingDirectionPolicyRule() {
		super();
		setAuthor("Sebastian Zitzelsberger");
		setName("Reading direction - References");
		setDescription("Checks whether the spreadsheet can be read in configurable directions.");
	}

	@Override
	public PolicyRuleType getType() {
		return PolicyRuleType.STATIC;
	}

}
