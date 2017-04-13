package sif.testcenter.multiple_same_ref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.Cell;
import sif.model.tokens.Reference;
import sif.utility.Translator;

import java.util.Arrays;
import java.util.List;

public class MultipleSameRefViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(MultipleSameRefViolation.class);

    private final int count;
    private final Reference causingReference;

    MultipleSameRefViolation(Cell cell, Reference causingReference, int count) {
        super(cell);
        this.count = count;
        this.causingReference = causingReference;
        logger.trace("New MultipleSameRefViolation for Cell " + cell.getExcelNotation());
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation() + "." + causingReference.getStringRepresentation();
    }

    @Override
    public String getDescription() {
        List<String> vars = Arrays.asList(
                getCausingCell().getWorksheet().getKey(),
                getCausingCell().getSimpleNotation(),
                Integer.toString(count),
                causingReference.getTargetElement().getSimpleNotation(),
                causingReference.getTargetElement().getWorksheet().getKey()
        );
        return Translator.tl("MultipleSameRefPolicy.Violation", vars);
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }
}
