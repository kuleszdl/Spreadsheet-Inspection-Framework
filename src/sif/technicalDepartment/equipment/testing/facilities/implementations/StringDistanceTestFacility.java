package sif.technicalDepartment.equipment.testing.facilities.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.policy.policyrule.implementations.FormulaComplexityPolicyRule;
import sif.model.violations.groupors.FormulaBlockGroupor;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.StringDistanceSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * the string distance pattern.
 * 
 * @author Sebastian Beck
 * 
 */

public class StringDistanceTestFacility extends MonolithicTestFacility{
	private Integer maxDistance = 0;
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
	
	private Integer getDistance(String sourceString, String testString) {
		//Levenshtein algortihm
		
		//length of strings	
		int sourceLength = sourceString.length();
	    int testLength = testString.length();

	    if (sourceLength ==0) {
	        return testLength;
	    } else if (testLength == 0) {
	        return sourceLength;
	    }
	    // if sourceString is longer than testString then swap
	    if (sourceLength > testLength) {
	    	String swapString = sourceString;
	       	sourceString = testString;
	        testString = swapString;
	        //swap lengths also
	        sourceLength = testLength;
	        testLength = testString.length();
	    }

	    int prevCost[] = new int[sourceLength+1];
	    int actCost[] = new int[sourceLength+1];
	    int swapCost[];
	    // indizes
	    int sourceIndex;
	    int testIndex;
	    // char in testString at position testIndex-1
	    char testIndexChar;

	    int cost;

	    for (sourceIndex = 0; sourceIndex<=sourceLength; sourceIndex++) {
	    	prevCost[sourceIndex] = sourceIndex;
	    }

	    for (testIndex = 1; testIndex<=testLength; testIndex++) {
	    	testIndexChar = testString.charAt(testIndex-1);
	        actCost[0] = testIndex;

	        for (sourceIndex=1; sourceIndex<=sourceLength; sourceIndex++) {
	        	// cost = 0|1
	        	cost = sourceString.charAt(sourceIndex-1)==testIndexChar ? 0 : 1;
	            // minimum of cell to the left + 1, to the top + 1, 
	        	//diagonally left and up + cost
	        	actCost[sourceIndex] = Math.min(Math.min(actCost[sourceIndex-1]+1, prevCost[sourceIndex]+1),  prevCost[sourceIndex-1]+cost);
	        }
	        // actual distance counts to previous distance counts
	        swapCost = prevCost;
	        prevCost = actCost;
	        actCost = swapCost;
	    }
	    return prevCost[sourceLength];
	}

	

	@Override
	public ViolationList run() {
		ViolationList violations = new ViolationList(
				new FormulaComplexityPolicyRule(), new FormulaBlockGroupor());

		for (Cell cell : this.inventory.getListFor(Cell.class)
				.getElements()) {
			String sourceString = cell.getTextContent();
			StringDistanceSingleViolation violation = null;
			for (Cell cellTest : this.inventory.getListFor(Cell.class)
					.getElements()){
				String testString = cellTest.getTextContent();
					Integer distance = getDistance(sourceString, testString);
					if (!isIngored(cell)) {
						if (0<distance && distance<maxDistance){
							if (violation == null) {
								violation = new StringDistanceSingleViolation();
								violation.setCausingElement(cell);
								violation.setPolicyRule(getTestedPolicyRule());
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
	
