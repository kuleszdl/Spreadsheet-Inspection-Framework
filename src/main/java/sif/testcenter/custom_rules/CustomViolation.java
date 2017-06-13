package sif.testcenter.custom_rules;

import org.apache.poi.ss.usermodel.ConditionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.utility.Translator;
import sif.model.Cell;

import java.util.ArrayList;
import java.util.List;

public class CustomViolation extends Violation{

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(CustomViolation.class);
    private final String ruleName;
    private final String actualValue;
    private final String expectedValue;
    private final ConditionType conditionType;

    CustomViolation (Cell cell, String ruleName, String actualValue, String expectedValue, ConditionType conditionType) {
        super(cell);
        this.ruleName = ruleName;
        this.actualValue = actualValue;
        this.expectedValue = expectedValue;
        this.conditionType = conditionType;
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation() + "." + ruleName + "." +
                conditionType + "[" + expectedValue + "]" + "[" +  actualValue + "]";
    }

    @Override
    public String getDescription() {
        List<String> vars = new ArrayList<>();
        if (getCausingCell() != null) {
            vars.add(ruleName);
            vars.add(getCausingCell().getSimpleNotation());
            vars.add(getCausingCell().getWorksheet().getKey());
            vars.add(actualValue);
        }
        String start = Translator.tl("CustomRulePolicy.CustomViolation", vars);
        String end = Translator.tl("CustomRulePolicy.CustomViolation." + conditionType, expectedValue);
        return start + " " + end;
    }

    /*
    Currently unused
     */
    @Override
    public Double getSeverity() {
        return 0.0;
    }


}
