package sif.technicalDepartment.equipment.testing.facilities.implementations;

import java.util.ArrayList;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.NonConsideredValuesSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * the multiple same references pattern.
 *
 * @author Sebastian Beck
 *
 */

public class MultipleSameRefTestFacility extends MonolithicTestFacility{

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

	@Override
	public ViolationList run() {
		ViolationList violations = new ViolationList(getTestedPolicyRule());

		AbstractElementList<Cell> cells = this.inventory.getListFor(Cell.class);
		for (Cell cell : cells.getElements()) {
			NonConsideredValuesSingleViolation violation = null;
			ArrayList<AbstractReference> outgoingReferences = cell.getOutgoingReferences();
			for (int index = 0; index < outgoingReferences.size(); index++){
			AbstractReference reference = outgoingReferences.get(index);
				for (int innerIndex = index; innerIndex < outgoingReferences.size(); innerIndex++)
					if (outgoingReferences.get(innerIndex).equals(reference)){
					if (!isIgnored(reference)) {
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
