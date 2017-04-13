package sif.testcenter.dynamic_testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.Cell;
import sif.utility.Translator;

import java.util.ArrayList;
import java.util.List;

public class ConditionViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(ConditionViolation.class);
    private final String scenarioName;
    private final String actualValue;
    private final String expectedValue;
    private final Operator operator;

    ConditionViolation(Cell cell, String scenarioName, String actualValue, String expectedValue, Operator operator) {
        super(cell);
        this.scenarioName = scenarioName;
        this.actualValue = actualValue;
        this.expectedValue = expectedValue;
        this.operator = operator;
        logger.trace("New ConditionViolation for Cell " + cell.getExcelNotation());
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation() + "." + scenarioName + "." + operator + "[" + expectedValue + "]" + "[" + actualValue + "]";
    }

    @Override
    public String getDescription() {
        List<String> vars = new ArrayList<>();
        if (getCausingCell() != null) {
            vars.add(scenarioName);
            vars.add(getCausingCell().getSimpleNotation());
            vars.add(getCausingCell().getWorksheet().getKey());
            vars.add(actualValue);
        }
        String start = Translator.tl("DynamicTestingPolicy.ConditionViolation", vars);
        String end = Translator.tl("DynamicTestingPolicy.ConditionViolation." + operator, expectedValue);
        return start + " " + end;
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }
}