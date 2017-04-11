package sif.testcenter.error_containing_cell;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;
import sif.model.Cell;
import sif.model.values.ValueType;

@RequestScoped
public class ErrorContainingCellFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(ErrorContainingCellFacility.class);
    private ErrorContainingCellPolicy policy;

    @Inject
    public ErrorContainingCellFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    @Override
    public void run() {

        for (Cell cell : spreadsheetInventory.getSpreadsheet().getCells()) {
            if (!policy.isIgnored(cell)) {
                if (cell.getValue().getType() == ValueType.ERROR) {
                    validationReport.add(new ErrorContainingCellViolation(cell));
                }
            }
        }

        // addCell our ValidationReport to the InspectionResponse
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    @Override
    protected Class<?> getPolicyClass() {
        return ErrorContainingCellPolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (ErrorContainingCellPolicy) policy;
    }

}
