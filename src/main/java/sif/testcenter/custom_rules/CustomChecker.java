package sif.testcenter.custom_rules;

import com.google.inject.Inject;
import sif.model.Cell;
import sif.model.values.Value;
import sif.model.values.ValueHelper;


public class CustomChecker {
    @Inject
    private ValueHelper valueHelper;
    @Inject
    private RegexChecker regexChecker;

    public boolean isFulfilled(RuleCondition ruleCondition, Cell cell) {
        Value value = valueHelper.importValue(ruleCondition.getValue(), ruleCondition.getType());

        // nicht sicher ob check nötig, sollte in SIFEI schon stattfinden
        if (value.getType() != cell.getValue().getType()) {
            return false;
        }

        // convert to Regex, or check regex pattern
        switch (ruleCondition.getConditionType()) {
            case REGEX:
                if (regexChecker.isFulfilled(ruleCondition.getValue(), ruleCondition.getTarget())) {
                    return true;
                }
                return false;
            case CHARACTER_COUNT:
                String regexValue = characterCountToRegex(ruleCondition.getValue());
                if (regexChecker.isFulfilled(regexValue, ruleCondition.getTarget())) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private String characterCountToRegex(String value) {
        String regexValue = "";
        // TODO Conver CharacterCount to Regex
        return regexValue;
    }

}
