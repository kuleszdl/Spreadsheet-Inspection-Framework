package sif.technicalDepartment.equipment.testing.facilities.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.basic.reference.IReferencedElement;
import sif.model.elements.basic.reference.IReferencingElement;
import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.groupors.FormulaBlockGroupor;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.ReadingDirectionSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

public class ReadingDirectionTestFacility extends MonolithicTestFacility {

	private Boolean mustBeLeftToRightReadable = true;

	private Boolean mustBeTopToBottomReadable = true;

	private String[] ignoredCells = null;

	private boolean isIgnored(AbstractReference reference) {
		boolean isIgnored = false;
		if (reference.getReferencingElement() instanceof Cell) {
			Cell cell = (Cell) reference.getReferencingElement();
			// getting the location as worksheet!address
			String location = cell.getCellAddress().getSpreadsheetAddress();
			for (String ignoredCell : ignoredCells) {
				// discarding existent $ and = chars from SIFEI
				if (location.equals(ignoredCell.replaceAll("[$=]", ""))) {
					isIgnored = true;
					break;
				}
			}
		}
		return isIgnored;
	}

	public Boolean isLeftToRightFullfilled(AbstractReference reference) {
		Boolean result = true;
		IReferencedElement referencedElement = reference.getReferencedElement();
		IReferencingElement referencingElement = reference
				.getReferencingElement();

		// Referenced element is right of referencing element.
		if (referencingElement.getAbstractAddress().compareHorizontal(
				referencedElement.getAbstractAddress()) == 1) {
			result = false;
		}

		return result;
	}

	public Boolean isTopToBottomFullfilled(AbstractReference reference) {
		Boolean result = true;
		IReferencedElement referencedElement = reference.getReferencedElement();
		IReferencingElement referencingElement = reference
				.getReferencingElement();

		// Referenced element is below referencing element.
		if (referencingElement.getAbstractAddress().compareVertical(
				referencedElement.getAbstractAddress()) == -1) {
			result = false;
		}

		return result;
	}

	@Override
	public ViolationList run() {

		ViolationList violations = new ViolationList(getTestedPolicyRule(),
				new FormulaBlockGroupor());

		AbstractElementList<Formula> formulas = this.inventory
				.getListFor(Formula.class);

		for (Formula formula : formulas.getElements()) {
			ReadingDirectionSingleViolation violation = null;

			for (ITokenElement token : formula.getAllTokens()) {
				if (token instanceof AbstractReference) {
					AbstractReference reference = (AbstractReference) token;
					if (!isIgnored(reference)) {

						if (mustBeLeftToRightReadable) {
							if (!isLeftToRightFullfilled(reference)) {
								if (violation == null) {
									violation = new ReadingDirectionSingleViolation();
									violation.setCausingElement(formula);
									violation
											.setPolicyRule(getTestedPolicyRule());
								}

								violation.addNonLeftToRight(reference);

							}
						}

						if (mustBeTopToBottomReadable) {
							if (!isTopToBottomFullfilled(reference)) {
								if (violation == null) {
									violation = new ReadingDirectionSingleViolation();
									violation.setCausingElement(formula);
									violation
											.setPolicyRule(getTestedPolicyRule());
								}

								violation.addNonTopToBottom(reference);

							}
						}
					}
				}
			}
			if (violation != null) {
				violations.add(violation);
			}
		}

		return violations;
	}

}
