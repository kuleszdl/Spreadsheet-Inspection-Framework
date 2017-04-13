package sif.testcenter.one_among_others;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;
import sif.model.AddressFactory;
import sif.model.Cell;
import sif.model.CellMatrixAddress;
import sif.model.values.ValueType;

import java.util.ArrayList;
import java.util.List;

/***
 * The one among others pattern.
 */
@RequestScoped
public class OneAmongOthersFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(OneAmongOthersFacility.class);
    private OneAmongOthersPolicy policy;

    @Inject
    private AddressFactory addressFactory;

    @Inject
    public OneAmongOthersFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    @Override
    public void run(){

        /*
         * CAREFUL we create a new list, cause cell count can change while performing this test
         * this will happen because we will check for cells around existing cells
         * all cells that are not initialized yet will be created on the fly and added to the spreadsheet
         */
        List<Cell> cells = new ArrayList<>(spreadsheetInventory.getSpreadsheet().getCells());
        for (Cell cell : cells) {

            if (policy.isIgnored(cell)) {
                continue;
            }

            if (!hasSameAdjacentType(cell, policy)) {
                OneAmongOthersViolation violation = new OneAmongOthersViolation(cell);
                validationReport.add(violation);
            }
        }

        // addCell our ValidationReport to the InspectionResponse
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    private List<Cell> getNeighborCells(Cell c, OneAmongOthersPolicy policy) {
        int d = policy.getEnvironmentLength();
        CellMatrixAddress cma;
        switch (policy.getEnvironmentStyle()) {
            case OneAmongOthersPolicy.HORIZONTAL:
                cma = addressFactory.createCMA(c, d, 0);
                break;
            case OneAmongOthersPolicy.VERTICAL:
                cma = addressFactory.createCMA(c, 0, d);
                break;
            default:
                cma = addressFactory.createCMA(c, d, d);
                break;
        }
        return new ArrayList<>(cma.getCellMatrix().getCells());
    }

    private boolean hasSameAdjacentType(Cell cell, OneAmongOthersPolicy policy) {
        ValueType type = cell.getValue().getType();
        boolean hasAdjacent = false;
        List<Cell> neighbors = getNeighborCells(cell, policy);
        for (Cell neighbor : neighbors) {
            if (neighbor.equals(cell))
                continue;
            if (neighbor.getValue().getType().equals(type)) {
                hasAdjacent = true;
                break;
            }
        }

        return hasAdjacent;
    }

    @Override
    protected Class<?> getPolicyClass() {
        return OneAmongOthersPolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (OneAmongOthersPolicy) policy;
    }
}
