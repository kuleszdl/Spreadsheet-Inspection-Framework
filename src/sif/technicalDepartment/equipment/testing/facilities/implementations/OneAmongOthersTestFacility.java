package sif.technicalDepartment.equipment.testing.facilities.implementations;

import java.util.ArrayList;

import sif.model.elements.basic.address.CellAddress;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.cell.CellContentType;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.groupors.SameCausingCellGroupor;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.OneAmongOthersSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * The one among others pattern.
 *
 * @author Sebastian Beck
 *
 */

public class OneAmongOthersTestFacility extends MonolithicTestFacility{

	private String[] ignoredCells = null;
	//1=horizontal, 2=vertical, 3=cross
	private Integer enviromentStyle = 1;
	private Integer enviromentLength = 1;
	public static final int ONEAMONGOTHERS_HORIZONTAL = 1;
	public static final int ONEAMONGOTHERS_VERTICAL = 2;
	public static final int ONEAMONGOTHERS_CROSS = 3;

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

	private ArrayList<Cell> getHorizontal(Cell c){
		ArrayList<Cell> toReturn = new ArrayList<>(enviromentLength * 2);
		CellAddress addr = c.getCellAddress();
		for (int i = 0; i < enviromentLength; i++){
			Cell adding = addr.getWorksheet()
					.getCellAt(addr.getColumnIndex() - i, addr.getRowIndex());

			if (adding != null){
				toReturn.add(adding);					
			}

			adding = addr.getWorksheet()
					.getCellAt(addr.getColumnIndex() + i, addr.getRowIndex());

			if (adding != null){
				toReturn.add(adding);					
			}
		}
		return toReturn;
	}

	private ArrayList<Cell> getVertical(Cell c){
		ArrayList<Cell> toReturn = new ArrayList<>(enviromentLength * 2);
		CellAddress addr = c.getCellAddress();
		for (int i = 0; i < enviromentLength; i++){
			Cell adding = addr.getWorksheet()
					.getCellAt(addr.getColumnIndex(), addr.getRowIndex() - i);

			if (adding != null){
				toReturn.add(adding);					
			}

			adding = addr.getWorksheet()
					.getCellAt(addr.getColumnIndex(), addr.getRowIndex() + i);

			if (adding != null){
				toReturn.add(adding);					
			}
		}

		return toReturn;
	}

	private ArrayList<Cell> getCross(Cell c){
		ArrayList<Cell> toReturn = getHorizontal(c);
		toReturn.addAll(getVertical(c));
		return toReturn;
	}


	private ArrayList<Cell> getEnvironment(Cell c){
		switch (enviromentStyle){
		case ONEAMONGOTHERS_HORIZONTAL:
			return getHorizontal(c);
		case ONEAMONGOTHERS_VERTICAL:
			return getVertical(c);
		case ONEAMONGOTHERS_CROSS:
			return getCross(c);
		default:
			return null;
		}
	}

	private boolean hasSameAdjactantType(Cell c){
		CellContentType type = c.getCellContentType();
		boolean hasAdjactant = false;
		for (Cell neighbor : getEnvironment(c)){
			if (neighbor.getCellContentType().equals(type)){
				hasAdjactant = true;
				break;
			}
		}

		return hasAdjactant;
	}

	@Override
	public ViolationList run(){
		ViolationList violations = new ViolationList(getTestedPolicyRule(), new SameCausingCellGroupor());

		AbstractElementList<Cell> cells = getInventory().getListFor(Cell.class);
		for (Cell c : cells.getElements()){
			if (isIgnored(c)){
				continue;
			}
			if (!hasSameAdjactantType(c)){
				OneAmongOthersSingleViolation violation = new OneAmongOthersSingleViolation();
				violation.setCausingElement(c);
				violation.setPolicyRule(getTestedPolicyRule());
				violations.add(violation);
			}
		}
		return violations;
	}
}
