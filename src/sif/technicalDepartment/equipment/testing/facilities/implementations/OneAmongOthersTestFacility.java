package sif.technicalDepartment.equipment.testing.facilities.implementations;

import java.util.ArrayList;

import sif.model.elements.basic.address.CellAddress;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.OneAmongOthersSingleViolation;
import sif.model.violations.single.RefToNulllSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.CheckerCreationException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.NoConditionTargetException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.PropertyAccessException;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * The one among others pattern.
 *
 * @author Sebastian Beck
 *
 */

@SuppressWarnings("unused")
public class OneAmongOthersTestFacility extends MonolithicTestFacility{

	private String[] ignoredCells = null;
	//1=horizontal, 2=vertical, 3=cross
	private Integer enviromentStyle = 1;
	private Integer enviromentLength = 2;
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
			if (addr.getColumnIndex() > 1){
				addr.setColumnIndex(addr.getColumnIndex() - 1);
				toReturn.add(getInventory().getSpreadsheet().getCellFor(addr));
			}
		}
		addr = c.getCellAddress();
		for (int i = 0; i < enviromentLength; i++){
			addr.setColumnIndex(addr.getColumnIndex() + 1);
			toReturn.add(getInventory().getSpreadsheet().getCellFor(addr));
		}
		return toReturn;
	}
	
	private ArrayList<Cell> getVertical(Cell c){
		ArrayList<Cell> toReturn = new ArrayList<>(enviromentLength * 2);
		CellAddress addr = c.getCellAddress();
		for (int i = 0; i < enviromentLength; i++){
			if (addr.getRowIndex() > 1){
				addr.setRowIndex(addr.getRowIndex() - 1);
				toReturn.add(getInventory().getSpreadsheet().getCellFor(addr));
			}
		}
		addr = c.getCellAddress();
		for (int i = 0; i < enviromentLength; i++){
			addr.setRowIndex(addr.getRowIndex() + 1);
			toReturn.add(getInventory().getSpreadsheet().getCellFor(addr));
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

	@Override
	public ViolationList run(){
		ViolationList violations = new ViolationList(getTestedPolicyRule(), null);

		//TODO implementation of OneAmongOthers + needed functions
		return violations;
	}
		}
