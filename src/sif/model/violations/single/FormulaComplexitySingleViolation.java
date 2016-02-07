package sif.model.violations.single;

import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.implementations.FormulaComplexityPolicyRule;
import sif.model.violations.IViolation;
import sif.utilities.Translator;

/***
 * A custom single violation to record a violation of the
 * {@link FormulaComplexityPolicyRule}.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class FormulaComplexitySingleViolation extends GenericSingleViolation {

	private Integer numberOfOperations;
	private Integer nestingLevel;

	public void setMaxNestingLevel(Integer maxNestingLevel) {
		this.maxNestingLevel = maxNestingLevel;
	}

	public void setMaxNumberOfOperations(Integer maxNumberOfOperations) {
		this.maxNumberOfOperations = maxNumberOfOperations;
	}

	private Integer maxNestingLevel;
	private Integer maxNumberOfOperations;
	private AbstractPolicyRule policyRule;

	public void setNumberOfOperations(Integer numberOfOperations) {
		this.numberOfOperations = numberOfOperations;
	}

	public void setNestingLevel(Integer nestingLevel) {
		this.nestingLevel = nestingLevel;
	}

	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();

		if (numberOfOperations != null) {
			description.append(Translator.instance.tl("PolicyFormulaComplexity.0010", "Number of operations"));
			description.append(" [");
			description.append(numberOfOperations.toString());
			description.append("] ");
			description.append(Translator.instance.tl("PolicyFormulaComplexity.0011",
					"exceeds maximum allowed number of operations"));
			description.append(" [");
			description.append(maxNumberOfOperations.toString());
			description.append("]");
		}

		if (nestingLevel != null) {
			if (description.length() > 0) {
				description.append(" ");
				description.append(Translator.instance.tl("PolicyFormulaComplexity.0012", "and nesting level"));
				description.append(" [");
			} else {
				description.append(Translator.instance.tl("PolicyFormulaComplexity.0013","Nesting level"));
				description.append(" [");
			}
			description.append(nestingLevel.toString());
			description.append("] ");
			description.append(Translator.instance.tl("PolicyFormulaComplexity.0014", "exceeds maximum allowed nesting level"));
			description.append(" [");
			description.append(maxNestingLevel.toString());
			description.append("]");
		}

		return description.toString();
	}

	@Override
	public AbstractPolicyRule getPolicyRule() {
		return policyRule;
	}

	@Override
	public Double getWeightedSeverityValue() {
		Double severtityValue = 0.0;
		double exceedingPercentage = 0.0;

		if (nestingLevel != null & numberOfOperations != null) {
			exceedingPercentage = (((numberOfOperations / maxNumberOfOperations) - 1.0)
					+ ((nestingLevel / maxNestingLevel) - 1.0)) / 2;
		} else if (nestingLevel == null) {
			exceedingPercentage = (numberOfOperations / maxNumberOfOperations) - 1.0;
		} else {
			exceedingPercentage = (nestingLevel / maxNestingLevel) - 1.0;
		}

		if (exceedingPercentage < 0.50) {
			severtityValue = IViolation.SEVERITY_VERY_LOW;
		} else if (exceedingPercentage < 1.0) {
			severtityValue = IViolation.SEVERITY_LOW;
		} else if (exceedingPercentage < 1.5) {
			severtityValue = IViolation.SEVERITY_MEDIUM;
		} else if (exceedingPercentage < 2.0) {
			severtityValue = IViolation.SEVERITY_HIGH;
		} else {
			severtityValue = IViolation.SEVERITY_VERY_HIGH;
		}
		return severtityValue * getPolicyRule().getSeverityWeight();
	}

	@Override
	public void setPolicyRule(AbstractPolicyRule policyRule) {
		this.policyRule = policyRule;
	}

}
