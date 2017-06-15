package sif.testcenter.custom_rules;

import com.google.inject.Inject;
import sif.model.Cell;
import sif.model.values.Value;
import sif.model.values.ValueHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomChecker {
    @Inject
    private ValueHelper valueHelper;

    public boolean isFulfilled(RuleCondition ruleCondition, Cell cell) {
        Value value = valueHelper.importValue(ruleCondition.getValue(), ruleCondition.getType());

        // nicht sicher ob check nötig, sollte in SIFEI schon stattfinden
        if (value.getType() != cell.getValue().getType()) {
            return false;
        }

        // convert to Regex, or check regex pattern
        switch (ruleCondition.getConditionType()) {
            case REGEX:
                if (checkRegex(ruleCondition.getValue(), ruleCondition.getTarget())) {
                    return true;
                }
                return false;
            case CHARACTER_COUNT:
                String regexValue = characterCountToRegex(ruleCondition.getValue());
                if (checkRegex(regexValue, ruleCondition.getTarget())) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private String characterCountToRegex(String value) {
        String regexValue = "";
        // accepts arbitrary characters with the specified length
        for (int i = 0; i< Integer.parseInt(value); i++) {
            regexValue += regexValue + ".";
        }
        return regexValue;
    }

    public boolean checkRegex (String pattern, String input) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        if (m.find()) {
            return true;
        }
        else {
            return false;
        }


    }



}
