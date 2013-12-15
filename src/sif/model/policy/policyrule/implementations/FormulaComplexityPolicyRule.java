package sif.model.policy.policyrule.implementations;

import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokens.IOperationTokenElement;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;

/***
 * A policy rule that defines the allowed complexity of a {@link Formula}. The
 * complexity is measured twofold:<br>
 * - The Nesting level of a operation. <br>
 * - The number of operations {@link IOperationTokenElement}.<br>
 * Formulas that exceed the configured threshold for any of these two are
 * reported by a violation.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class FormulaComplexityPolicyRule extends MonolithicPolicyRule {

	@ConfigurableParameter(parameterClass = Integer.class, displayedName = "Maximum nesting level", description = "Configures the highest allowed nesting level for formulas.")
	private Integer maxNestingLevel = 2;

	@ConfigurableParameter(parameterClass = Integer.class, displayedName = "Maximum number of operations allowed", description = "Configures the highest allowed number of operations for formulas.")
	private Integer maxNumberOfOperations = 5;

	public FormulaComplexityPolicyRule() {
		super();
		setAuthor("Sebastian Zitzelsberger");
		setName("Policy Rule: Formula Complexity");
		setDescription("Checks whether formula complexity goes beyond a certain nesting level or contains more than a certain number of operations");
	}

}
