package sif.testcenter.ref_to_null;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.Cell;
import sif.utility.Translator;

import java.util.Arrays;
import java.util.List;

public class RefToNullViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(RefToNullViolation.class);

    RefToNullViolation(Cell cell) {
        super(cell);
        logger.trace("New RefToNullViolation for Cell " + cell.getExcelNotation());
    }

    @Override
    public String getDescription() {
        List<String> vars = Arrays.asList(
                getCausingCell().getWorksheet().getKey(),
                getCausingCell().getSimpleNotation(),
                getCausingCell().getFormula().getFormulaString()
        );
        return Translator.tl("RefToNullPolicy.Violation", vars);
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation() + "." + getCausingCell().getFormula().getFormulaString();
    }

}
