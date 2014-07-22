package sif.technicalDepartment.equipment.testing.facilities.implementations;

import java.util.ArrayList;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.NonConsideredValuesSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * The non considered Value pattern.
 * 
 * @author Sebastian Beck
 * 
 */

public class NonConsideredValuesTestFacility extends MonolithicTestFacility{

	private Cell[] ignoredCells = null;
	
	public Boolean isIngored(Cell cell) {
		Boolean isIgnored = false;
		for (Cell ignoredCell : ignoredCells) {
			if (cell.equals(ignoredCell)) {
				isIgnored = true;
				break;
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
			ArrayList<AbstractReference> incomingReferences = cell.getIncomingReferences();
			if (incomingReferences.isEmpty()){
					if (!isIngored(cell)) {
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

		return violations;
	}

}
