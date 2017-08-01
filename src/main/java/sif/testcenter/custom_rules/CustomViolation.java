package sif.testcenter.custom_rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.model.Cell;
import sif.testcenter.Violation;
import sif.utility.Translator;

import java.util.ArrayList;
import java.util.List;

/***
 * The custom_rules specific Violation
 * Adds the ConditionName, Location, ConditionType and the Values to the ViolationReport
 */
public class CustomViolation extends Violation{

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(CustomViolation.class);
    private final String ruleName;
    private final String actualValue;
    private final String expectedValue;
    private final RuleConditionType ruleConditionType;
    private final String conditionName;

    CustomViolation (Cell cell, String ruleName, String actualValue, String expectedValue, RuleConditionType ruleConditionType, String conditionName) {
        super(cell);
        this.ruleName = ruleName;
        this.actualValue = actualValue;
        this.expectedValue = expectedValue;
        this.ruleConditionType = ruleConditionType;
        this.conditionName = conditionName;
        logger.trace("New CustomViolation at" + cell.getExcelNotation());
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation() + "." + ruleName + "." +
                ruleConditionType + "[" + expectedValue + "]" + "[" +  actualValue + "]";
    }

    /**
     * returns the relevant information TODO: RuleDescription not returned yet
     * @return
     */
    @Override
    public String getDescription() {
        List<String> vars = new ArrayList<>();
        if (getCausingCell() != null) {
            vars.add(ruleName);
            vars.add(conditionName);
            vars.add(getCausingCell().getSimpleNotation());
            vars.add(getCausingCell().getWorksheet().getKey());
            vars.add(actualValue);
        }
        String start = Translator.tl("RulesPolicy.CustomViolation", vars);
        // 2. part unused
        String end = Translator.tl("RulesPolicy.CustomViolation."+ ruleConditionType);
        return start;
    }

    /*
    Currently unused
     */
    @Override
    public Double getSeverity() {
        return 0.0;
    }


}
