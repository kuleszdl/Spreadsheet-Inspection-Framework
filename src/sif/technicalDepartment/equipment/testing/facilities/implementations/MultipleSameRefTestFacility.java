package sif.technicalDepartment.equipment.testing.facilities.implementations;

import java.util.ArrayList;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.MultipleSameRefSingleViolation;
import sif.model.violations.single.NonConsideredValuesSingleViolation;
import sif.model.violations.single.RefToNulllSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.CheckerCreationException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.NoConditionTargetException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.PropertyAccessException;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * the multiple same references pattern.
 * 
 * @author Sebastian Beck
 * 
 */

public class MultipleSameRefTestFacility extends MonolithicTestFacility{

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
	
	@Override
	public ViolationList run() {
ViolationList violations = new ViolationList(getTestedPolicyRule(), null);
		
		AbstractElementList<Cell> cells = this.inventory
				.getListFor(Cell.class);
		for (Cell cell : cells.getElements()) {
			NonConsideredValuesSingleViolation violation = null;
			ArrayList<AbstractReference> outgoingReferences = cell.getOutgoingReferences();
			for (int index = 0; index < outgoingReferences.size(); index++){
			AbstractReference reference = outgoingReferences.get(index);
				for (int innerIndex = index; innerIndex < outgoingReferences.size(); innerIndex++)
					if (outgoingReferences.get(innerIndex).equals(reference)){
					if (!isIngored(reference)) {
						if (violation == null) {
							violation = new NonConsideredValuesSingleViolation();
							violation.setCausingElement(cell);
							violation.setPolicyRule(getTestedPolicyRule());
						}							
					}
				}
			if (violation != null) {
				violations.add(violation);
			}
		}
		}
		return violations;
	}

}
