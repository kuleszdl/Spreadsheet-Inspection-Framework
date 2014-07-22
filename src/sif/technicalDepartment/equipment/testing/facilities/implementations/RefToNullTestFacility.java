package sif.technicalDepartment.equipment.testing.facilities.implementations;

import sif.model.elements.basic.cell.Cell;
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

	private Cell[] ignoredCells = null;
	
	public Boolean isIngored(AbstractReference reference) {
		Boolean isIgnored = false;
		if (reference.getReferencingElement() instanceof Cell) {
			Cell cell = (Cell) reference.getReferencingElement();
			for (Cell ignoredCell : ignoredCells) {
				if (cell.equals(ignoredCell)) {
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
		// Referenced Elements Value is null
		if (referencedElement.getValueAsString().equalsIgnoreCase(null)) {
			result = true;
		}

		return result;
	}
	
	@Override
	public ViolationList run() {
		ViolationList violations = new ViolationList(getTestedPolicyRule(), null);
		
		AbstractElementList<Formula> formulas = this.inventory
				.getListFor(Formula.class);
		for (Formula formula : formulas.getElements()) {
			RefToNulllSingleViolation violation = null;
			for (ITokenElement token : formula.getAllTokens()) {
				if (token instanceof AbstractReference) {
					AbstractReference reference = (AbstractReference) token;
					if (!isIngored(reference)) {
						if (isRefNull(reference)) {
							if (violation == null) {
								violation = new RefToNulllSingleViolation();
								violation.setCausingElement(reference);
								violation.setPolicyRule(getTestedPolicyRule());
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
