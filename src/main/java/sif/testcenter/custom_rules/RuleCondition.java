package sif.testcenter.custom_rules;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement(name = "ruleCondition")
public class RuleCondition {
    private String conditionValue;
    private RuleConditionType conditionType;

    public RuleCondition() {
        conditionType = RuleConditionType.BLANK;
    }

    public RuleCondition(RuleConditionType conditionType, String conditionValue) {
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
    }


public String getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }


    public RuleConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(RuleConditionType conditionType) {
        this.conditionType = conditionType;
    }

}
