package sif.technicalDepartment.equipment.testing.facilities.implementations;

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

public class OneAmongOthersTestFacility extends MonolithicTestFacility{

	private Cell[] ignoredCells = null;
	//1=horizontal, 2=vertical, 3=cross
	private Integer enviromentStyle = 1;
	private Integer enviromentLength = 2;
	
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
	public ViolationList run(){
		ViolationList violations = new ViolationList(getTestedPolicyRule(), null);
		
		//TODO implementation of OneAmongOthers + needed functions
		return violations; 
	}
		}
