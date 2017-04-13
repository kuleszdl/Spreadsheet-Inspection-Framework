package sif.testcenter.error_containing_cell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.Cell;
import sif.utility.Translator;

import java.util.ArrayList;
import java.util.List;

public class ErrorContainingCellViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(ErrorContainingCellViolation.class);

    ErrorContainingCellViolation(Cell cell) {
        super(cell);
        logger.trace("New ErrorContainingCellViolation for Cell " + cell.getExcelNotation());
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation();
    }

    @Override
    public String getDescription() {
        List<String> vars = new ArrayList<>();
        if (getCausingCell() != null) {
            vars.add(getCausingCell().getWorksheet().getKey());
            vars.add(getCausingCell().getSimpleNotation());
        }
        return Translator.tl("ErrorContainingCellPolicy.Violation", vars);
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }
}
