package sif.testcenter.custom_rules;

import com.google.inject.Inject;
import sif.model.Cell;
import sif.model.values.ValueHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomChecker {
    @Inject
    private ValueHelper valueHelper;

    public boolean isFulfilled(RuleCondition ruleCondition, Cell cell) {

        // convert to Regex, or check regex pattern
        switch (ruleCondition.getConditionType()) {
            case Regex:
                if (checkRegex(ruleCondition.getConditionValue(), cell.getValue().getValueString())) {
                    return true;
                }
                return false;
            case CharacterCount:
                try {
                    if (cell.getValue().getValueString().length() <= Integer.parseInt(ruleCondition.getConditionValue())+1) {
                        return true;
                    }
                } catch (Exception e) {

                }

                return false;
            default:
                return false;
        }
    }


    public boolean checkRegex (String pattern, String input) {
        Pattern p = Pattern.compile("(^|\\W)" + pattern + "($|\\W)");
        Matcher m = p.matcher(input);
        if (m.find()) {
            return true;
        }
        else {
            return false;
        }


    }



}
