package sif.technicalDepartment.equipment.testing.facilities.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.cell.CellContentType;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.groupors.SameCausingCellGroupor;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.GenericSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

public class ErrorContainingCellTestFacility extends MonolithicTestFacility {
	private String[] ignoredCells = null;

	private boolean isIgnored(Cell cell) {
		boolean isIgnored = false;
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

	private GenericSingleViolation createViolation(Cell cell){
		GenericSingleViolation violation = new GenericSingleViolation();
		violation.appendToDescription("The following cell contained an error in the fomula:");
		violation.appendToDescription(cell.getAbstractAddress().getSpreadsheetAddress());
		violation.setCausingElement(cell);

		return violation;
	}


	@Override
	public ViolationList run() {

		ViolationList violations = new ViolationList(getTestedPolicyRule(),
				new SameCausingCellGroupor());

		AbstractElementList<Cell> cells = this.inventory.getListFor(Cell.class);

		for (Cell checking : cells.getElements()) {
			if (isIgnored(checking)){
				continue;
			}
			if (checking.getCellContentType() == CellContentType.ERROR){
				violations.add(createViolation(checking));
			}
		}

		return violations;
	}
}
