package sif.technicalDepartment.equipment.testing.facilities.implementations;

import java.util.ArrayList;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.cell.CellContentType;
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

	private String[] ignoredCells = null;

	private boolean isIgnored(Cell cell) {
		Boolean isIgnored = false;
		// getting the location as worksheet!address
		String location = cell.getCellAddress().getSpreadsheetAddress();
		for (String ignoredCell : ignoredCells) {
			// discarding existent $ and = chars from SIFEI
			if (location.equals(ignoredCell.replaceAll("[$=]", ""))) {
				isIgnored = true;
				break;
			}
		}
		return isIgnored;
	}

	@Override
	public ViolationList run() {
		ViolationList violations = new ViolationList(getTestedPolicyRule(), null);

		AbstractElementList<Cell> cells = this.inventory.getListFor(Cell.class);

		for (Cell cell : cells.getElements()) {
			if (isIgnored(cell)){
				continue;
			}
			if (cell.getCellContentType() != CellContentType.NUMERIC){
				// we only want to check constants
				continue;
			}
			ArrayList<AbstractReference> incomingReferences = cell.getIncomingReferences();
			if (incomingReferences.isEmpty()){
				NonConsideredValuesSingleViolation violation = new NonConsideredValuesSingleViolation();
				violation.setCausingElement(cell);
				violation.setPolicyRule(getTestedPolicyRule());
				violations.add(violation);
			}
		}

		return violations;
	}

}
