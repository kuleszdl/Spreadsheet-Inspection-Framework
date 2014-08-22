package sif.technicalDepartment.equipment.testing.facilities.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.cell.CellContentType;
import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.basic.reference.IReferencedElement;
import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.RefToNulllSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * The reference to null pattern
 *
 * @author Sebastian Beck
 *
 */
public class RefToNullTestFacility extends MonolithicTestFacility{

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

	public Boolean isRefNull(AbstractReference reference) {
		Boolean result = false;
		IReferencedElement referencedElement = reference.getReferencedElement();

		if (referencedElement == null){
			return true;
		}
		
		if (referencedElement instanceof Cell) {
			Cell c = (Cell) referencedElement;
			if (c.getCellContentType() == CellContentType.BLANK){
				result = true;
			} else if (c.getValueAsString().trim().isEmpty()){
				result = true;
			}
		}

		
		return result;
	}

	@Override
	public ViolationList run() {
		ViolationList violations = new ViolationList(getTestedPolicyRule(), null);

		AbstractElementList<Formula> formulas = this.inventory
				.getListFor(Formula.class);
		for (Formula formula : formulas.getElements()) {
			for (ITokenElement token : formula.getAllTokens()) {
				if (token instanceof AbstractReference) {
					AbstractReference reference = (AbstractReference) token;
					if (!isIgnored(reference)) {
						if (isRefNull(reference)) {
							RefToNulllSingleViolation violation = new RefToNulllSingleViolation();
							violation.setCausingElement(reference);
							violation.setPolicyRule(getTestedPolicyRule());
							violations.add(violation);
						}
					}
				}
			}
		}

		return violations;
	}

}
