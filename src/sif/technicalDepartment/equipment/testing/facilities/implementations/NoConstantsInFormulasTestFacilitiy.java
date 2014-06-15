package sif.technicalDepartment.equipment.testing.facilities.implementations;

import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokencontainers.Function;
import sif.model.elements.basic.tokencontainers.ITokenContainer;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.elements.basic.tokens.ScalarConstant;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.groupors.FormulaBlockGroupor;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.NoConstantsInFormulaSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

public class NoConstantsInFormulasTestFacilitiy extends MonolithicTestFacility {

	public String[] ignoredFunctions;

	private Object[] ignoredConstants;

	private String[] ignoredCells;

	private void checkForViolation(
			NoConstantsInFormulaSingleViolation violation, ITokenElement token) {

		if (token instanceof ScalarConstant) {
			ScalarConstant constant = (ScalarConstant) token;
			if (!isIgnoredConstant(constant.getValue())) {
				violation.add(constant);
			}
		}
		if (token instanceof ITokenContainer) {

			if (token instanceof Function) {
				Function function = (Function) token;
				// Iterate over function tokens.
				for (ITokenElement functionToken : function.getTokens()) {
					if (!isFunctionWithConstantsAllowed(function)) {
						checkForViolation(violation, functionToken);
					}

				}
			} else {
				ITokenContainer container = (ITokenContainer) token;
				for (ITokenElement containerToken : container.getTokens()) {
					checkForViolation(violation, containerToken);

				}
			}
		}
	}

	private Boolean isFunctionWithConstantsAllowed(Function function) {
		Boolean constantsAllowed = false;
		for (String ignoredFunction : ignoredFunctions) {
			if (ignoredFunction.equalsIgnoreCase(function.getName())) {
				constantsAllowed = true;
				break;
			}
		}

		return constantsAllowed;
	}

	private Boolean isIgnored(Formula formula) {
		Boolean isIgnored = false;
		// getting the cell address into worksheet!Address
		String location = formula.getCell().getLocation().replace("[", "").replace("]", "!");
		for (String cell : ignoredCells) {
			// discarding existent "$" chars from SIFEI
			if (location.equals(cell.replace("$", ""))) {
				isIgnored = true;
				break;
			}
		}

		return isIgnored;
	}

	private Boolean isIgnoredConstant(Object constant) {
		Boolean isIgnored = false;
		if (ignoredConstants != null) {
			for (Object ignoredConstant : ignoredConstants) {
				if (ignoredConstant.equals(constant)) {
					isIgnored = true;
				}
			}
		}
		return isIgnored;
	}

	public ViolationList run() {
		ViolationList violations = new ViolationList(getTestedPolicyRule(),
				new FormulaBlockGroupor());
		// Get formulas from inventory.
		AbstractElementList<Formula> formulas = this.inventory
				.getListFor(Formula.class);

		// Iterate over formulas.
		for (Formula formula : formulas.getElements()) {
			if (!isIgnored(formula)) {
				// Violation for the formula.
				NoConstantsInFormulaSingleViolation violation = new NoConstantsInFormulaSingleViolation();
				violation.setCausingElement(formula);
				violation.setPolicyRule(getTestedPolicyRule());

				// Iterate over formula tokens.
				for (ITokenElement formulaToken : formula.getTokens()) {
					checkForViolation(violation, formulaToken);
				}
				if (violation.getNumberOfConstantsFound() > 0) {
					violations.add(violation);
				}
			}
		}

		return violations;
	}
}