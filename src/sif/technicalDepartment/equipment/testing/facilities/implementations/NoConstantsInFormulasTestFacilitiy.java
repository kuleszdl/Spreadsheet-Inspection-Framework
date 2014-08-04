package sif.technicalDepartment.equipment.testing.facilities.implementations;

import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokencontainers.Function;
import sif.model.elements.basic.tokencontainers.ITokenContainer;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.elements.basic.tokens.ScalarConstant;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.groupors.SameCausingCellGroupor;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.NoConstantsInFormulaSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

public class NoConstantsInFormulasTestFacilitiy extends MonolithicTestFacility {

	public String[] ignoredFunctions;

	private Object[] ignoredConstants;

	private String[] ignoredCells;

	private void checkForViolation(
			ViolationList violations, ITokenElement token) {

		if (token instanceof ScalarConstant) {
			ScalarConstant constant = (ScalarConstant) token;
			if (!isIgnoredConstant(constant.getValue())) {
				NoConstantsInFormulaSingleViolation violation = new NoConstantsInFormulaSingleViolation();
				violation.setCausingElement(constant);
				violation.setPolicyRule(getTestedPolicyRule());
				violation.setConstant(constant);
				violations.add(violation);
			}
		}
		if (token instanceof ITokenContainer) {

			if (token instanceof Function) {
				Function function = (Function) token;
				// Iterate over function tokens.
				for (ITokenElement functionToken : function.getTokens()) {
					if (!isFunctionWithConstantsAllowed(function)) {
						checkForViolation(violations, functionToken);
					}

				}
			} else {
				ITokenContainer container = (ITokenContainer) token;
				for (ITokenElement containerToken : container.getTokens()) {
					checkForViolation(violations, containerToken);

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
			// discarding not needed chars from SIFEI
			if (location.equalsIgnoreCase(cell.replaceAll("[$=]", ""))) {
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
				new SameCausingCellGroupor());
		// Get formulas from inventory.
		AbstractElementList<Formula> formulas = this.inventory
				.getListFor(Formula.class);

		// Iterate over formulas.
		for (Formula formula : formulas.getElements()) {
			if (!isIgnored(formula)) {

				// Iterate over formula tokens.
				for (ITokenElement formulaToken : formula.getTokens()) {
					checkForViolation(violations, formulaToken);
				}
			}
		}

		return violations;
	}
}