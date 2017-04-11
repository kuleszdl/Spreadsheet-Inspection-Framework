package sif.testcenter.sanity_checks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.model.Cell;
import sif.testcenter.Violation;

public class SanityChecksViolation extends Violation {

    private final Logger logger = LoggerFactory.getLogger(SanityChecksViolation.class);

    public SanityChecksViolation(Cell cell) {
        super(cell);
        logger.trace("New NonConsideredValuesViolation for Cell " + cell.getExcelNotation());
    }

    @Override
    public String getDescription() {
        return getCausingCell() + " causes sanity check violation.";
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation();
    }
}
