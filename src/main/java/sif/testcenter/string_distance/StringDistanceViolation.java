package sif.testcenter.string_distance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.Cell;
import sif.utility.Translator;

import java.util.Arrays;
import java.util.List;

public class StringDistanceViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(StringDistanceViolation.class);

    private final Cell nearestMatch;

    StringDistanceViolation(Cell cell, Cell nearestMatch) {
        super(cell);
        this.nearestMatch = nearestMatch;
        logger.trace("New StringDistanceViolation for Cell " + cell.getExcelNotation());
    }

    @Override
    public String getDescription() {
        List<String> vars = Arrays.asList(
                getCausingCell().getWorksheet().getKey(),
                getCausingCell().getSimpleNotation(),
                nearestMatch.getSimpleNotation()
        );
        return Translator.tl("StringDistancePolicy.Violation", vars);
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation() + "." + nearestMatch.getExcelNotation();
    }


}
