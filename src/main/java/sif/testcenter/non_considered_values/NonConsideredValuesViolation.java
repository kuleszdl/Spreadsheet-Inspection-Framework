package sif.testcenter.non_considered_values;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.Cell;
import sif.utility.Translator;

import java.util.Arrays;
import java.util.List;

public class NonConsideredValuesViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(NonConsideredValuesViolation.class);

    public NonConsideredValuesViolation(Cell cell) {
        super(cell);
        logger.trace("New NonConsideredValuesViolation for Cell " + cell.getExcelNotation());
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation();
    }

    @Override
    public String getDescription() {
        List<String> vars = Arrays.asList(
                getCausingCell().getSimpleNotation(),
                getCausingCell().getExcelNotation()
        );
        return Translator.tl("NonConsideredValuesPolicy.Violation", vars);
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }

}
