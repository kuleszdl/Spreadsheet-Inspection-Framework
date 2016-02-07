package sif.model.policy.policyrule.implementations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokens.IOperationTokenElement;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.PolicyRuleType;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;
import sif.utilities.Translator;
import sif.utilities.XML_Constants;

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
@XmlType(name = XML_Constants.NAME_FORMULA_COMPLEXITY_POLICY_RULE, propOrder = { "maxNestingLevel",
		"maxNumberOfOperations" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class FormulaComplexityPolicyRule extends MonolithicPolicyRule {

	@ConfigurableParameter(parameterClass = Integer.class, displayedName = "Maximum nesting level", description = "Configures the highest allowed nesting level for formulas.")
	private Integer maxNestingLevel = 2;

	@ConfigurableParameter(parameterClass = Integer.class, displayedName = "Maximum number of operations allowed", description = "Configures the highest allowed number of operations for formulas.")
	private Integer maxNumberOfOperations = 5;

	public FormulaComplexityPolicyRule() {
		super();
		setAuthor("Sebastian Zitzelsberger");
		setName(Translator.instance.tl("PolicyFormulaComplexity.0001", "Formula Complexity"));
		setDescription(Translator.instance.tl("PolicyFormulaComplexity.0002",
				"Checks whether formula complexity goes beyond a certain nesting level or contains more than a certain number of operations"));
		setType(PolicyRuleType.STATIC);
	}

	@XmlElement(name = XML_Constants.NAME_FORMULA_MAX_NESTING, type = Integer.class, required = false)
	public Integer getMaxNestingLevel() {
		return maxNestingLevel;
	}

	public void setMaxNestingLevel(Integer maxNestingLevel) {
		this.maxNestingLevel = maxNestingLevel;
	}

	@XmlElement(name = XML_Constants.NAME_FORMULA_MAX_NR_OPERATIONS, type = Integer.class, required = false)
	public Integer getMaxNumberOfOperations() {
		return maxNumberOfOperations;
	}

	public void setMaxNumberOfOperations(Integer maxNumberOfOperations) {
		this.maxNumberOfOperations = maxNumberOfOperations;
	}

}
