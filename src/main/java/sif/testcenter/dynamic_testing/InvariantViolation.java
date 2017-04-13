package sif.testcenter.dynamic_testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.Cell;
import sif.utility.Translator;

import java.util.ArrayList;
import java.util.List;

public class InvariantViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(InvariantViolation.class);
    private final String scenarioName;

    InvariantViolation(Cell cell, String scenarioName) {
        super(cell);
        this.scenarioName = scenarioName;
        logger.trace("New InvariantViolation for Cell " + cell.getExcelNotation());
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + scenarioName + "." + getCausingCell().getExcelNotation();
    }

    @Override
    public String getDescription() {
        List<String> vars = new ArrayList<>();
        if (getCausingCell() != null) {
            vars.add(scenarioName);
            vars.add(getCausingCell().getSimpleNotation());
            vars.add(getCausingCell().getWorksheet().getKey());
        }
        return Translator.tl("DynamicTestingPolicy.InvariantViolation", vars);
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }
}