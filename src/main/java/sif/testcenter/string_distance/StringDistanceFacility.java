package sif.testcenter.string_distance;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;
import sif.model.Cell;
import sif.model.Worksheet;
import sif.model.values.ValueType;

import java.util.ArrayList;
import java.util.Collection;

/***
 * the string distance pattern.
 */
@RequestScoped
public class StringDistanceFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(StringDistanceFacility.class);
    private StringDistancePolicy policy;
    private Collection<Cell> cells;

    @Inject
    public StringDistanceFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    @Override
    public void run() {

        Collection<Worksheet> worksheets = spreadsheetInventory.getSpreadsheet().getWorksheets();
        for (Worksheet worksheet : worksheets) {

            cells = new ArrayList<>();
            for (Cell cell : worksheet.getCells()) {

                if (!policy.isIgnored(cell) && (cell.getValue().getType() == ValueType.STRING)) {
                    cells.add(cell);
                }
            }

            for (Cell cell : cells) {
                checkCell(cell);
            }
        }


        // addCell our ValidationReport to the InspectionResponse
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    private void checkCell(Cell cell) {
        String sourceString = cell.getValue().getValueString();
        Cell lowestCell = null;
        int lowestDist = Integer.MAX_VALUE;

        for (Cell cellTest : cells) {
            if (cellTest == cell) {
                continue;
            }
            String testString = cellTest.getValue().getValueString();
            int distance = getDistance(sourceString, testString);
            if (distance < lowestDist) {
                lowestCell = cellTest;
                lowestDist = distance;
                if (lowestDist == 0) {
                    break;
                }
            }
        }

        if (lowestCell == null) {
            lowestCell = cell;
        }

        if ((0 < lowestDist) && (lowestDist < policy.getMinDistance())) {
            StringDistanceViolation violation = new StringDistanceViolation(cell, lowestCell);
            validationReport.add(violation);
        }
    }

    private int getDistance(String sourceString, String testString) {
        //Levenshtein algorithm

        //length of strings
        int sourceLength = (sourceString == null) ? 0 : sourceString.length();
        int testLength = (testString == null) ? 0 : testString.length();

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

        int prevCost[] = new int[sourceLength + 1];
        int actCost[] = new int[sourceLength + 1];
        int swapCost[];
        // indices
        int sourceIndex;
        int testIndex;
        // char in testString at position testIndex-1
        char testIndexChar;

        int cost;

        for (sourceIndex = 0; sourceIndex <= sourceLength; sourceIndex++) {
            prevCost[sourceIndex] = sourceIndex;
        }

        for (testIndex = 1; testIndex <= testLength; testIndex++) {
            testIndexChar = testString.charAt(testIndex - 1);
            actCost[0] = testIndex;

            for (sourceIndex = 1; sourceIndex <= sourceLength; sourceIndex++) {
                // cost = 0|1
                cost = (sourceString.charAt(sourceIndex - 1) == testIndexChar) ? 0 : 1;
                // minimum of cell to the left + 1, to the top + 1,
                //diagonally left and up + cost
                actCost[sourceIndex] = Math.min(Math.min(actCost[sourceIndex - 1] + 1, prevCost[sourceIndex] + 1), prevCost[sourceIndex - 1] + cost);
            }
            // actual distance counts to previous distance counts
            swapCost = prevCost;
            prevCost = actCost;
            actCost = swapCost;
        }
        return prevCost[sourceLength];
    }

    @Override
    protected Class<?> getPolicyClass() {
        return StringDistancePolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (StringDistancePolicy) policy;
    }
}

