package sif.technicalDepartment.equipment.testing.facilities.implementations;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.cell.CellContentType;
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

	private int getDistance(String sourceString, String testString) {
		//Levenshtein algortihm

		//length of strings
		int sourceLength = sourceString == null ? 0 : sourceString.length();
	    int testLength = testString == null ? 0 : testString.length();

	    if (sourceLength == 0) {
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
		ViolationList violations = new ViolationList(getTestedPolicyRule());

		for (Cell cell : this.inventory.getListFor(Cell.class).getElements()) {
			if (isIgnored(cell) || cell.getCellContentType() != CellContentType.TEXT){
				continue;
			}

			String sourceString = cell.getTextContent();

			Cell lowestCell = null;
			int lowestDist = Integer.MAX_VALUE;
			for (Cell cellTest : this.inventory.getListFor(Cell.class).getElements()){
				if (cellTest == cell){
					continue;
				}
				String testString = cellTest.getTextContent();
				int distance = getDistance(sourceString, testString);
				if (distance < lowestDist){
					lowestCell = cellTest;
					lowestDist = distance;
					if (lowestDist == 0){
						break;
					}
				}
			}

			if (0<lowestDist && lowestDist <= maxDistance){
				StringDistanceSingleViolation violation = new StringDistanceSingleViolation();
				violation.setCausingElement(cell);
				violation.setNearestMatch(lowestCell);
				violation.setPolicyRule(getTestedPolicyRule());
				violations.add(violation);
			}

		}

		return violations;
		}
	}

