package sif.technicalDepartment.equipment.testing.facilities.implementations;

import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokencontainers.ITokenContainer;
import sif.model.elements.basic.tokens.IOperationTokenElement;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.policy.policyrule.implementations.FormulaComplexityPolicyRule;
import sif.model.violations.groupors.SameCausingCellGroupor;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.FormulaComplexitySingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

public class FormulaComplexityTestFacility extends MonolithicTestFacility {

	private Integer maxNestingLevel = 0;
	private Integer maxNumberOfOperations = 0;


	private Integer getNumberOfOperations(ITokenContainer container) {
		Integer numberOfOperations = 0;
		for (ITokenElement token : container.getTokens()) {
			if (token instanceof IOperationTokenElement) {
				numberOfOperations++;
			}

			if (token instanceof ITokenContainer) {
				numberOfOperations = numberOfOperations
						+ getNumberOfOperations((ITokenContainer) token);
			}
		}

		return numberOfOperations;
	}

	@Override
	public ViolationList run() {
		ViolationList violations = new ViolationList(
				new FormulaComplexityPolicyRule(), new SameCausingCellGroupor());

		for (Formula formula : this.inventory.getListFor(Formula.class)
				.getElements()) {

			FormulaComplexitySingleViolation violation = null;

			Integer nestingLevel = formula.getDepth();
			if (nestingLevel > maxNestingLevel) {
				if (violation == null) {
					violation = new FormulaComplexitySingleViolation();
					violation.setPolicyRule(getTestedPolicyRule());
					violation.setCausingElement(formula);
				}

				violation.setNestingLevel(nestingLevel);
				violation.setMaxNestingLevel(maxNestingLevel);

			}

			Integer numberOfOperations = getNumberOfOperations(formula);
			if (numberOfOperations > maxNumberOfOperations) {

				if (violation == null) {
					violation = new FormulaComplexitySingleViolation();
					violation.setPolicyRule(getTestedPolicyRule());
					violation.setCausingElement(formula);
				}

				violation.setNumberOfOperations(numberOfOperations);
				violation.setMaxNumberOfOperations(maxNumberOfOperations);

			}
			if (violation != null) {
				violations.add(violation);
			}

		}

		return violations;
	}
}
