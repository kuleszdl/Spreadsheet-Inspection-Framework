package sif.testcenter.custom_rules;

import sif.model.values.ValueType;

public class RuleCondition {
    private String target;
    private String value;
    private RuleConditionType ruleConditionType;
    private ValueType type;

    public RuleCondition() {
        type = ValueType.BLANK;
    }

    public RuleCondition(String target, String value, RuleConditionType conditionType, ValueType valueType) {
        this.target = target;
        this.value = value;
        this.ruleConditionType = conditionType;
        this.type = valueType;
    }


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public RuleConditionType getConditionType() {
        return ruleConditionType;
    }

    public void setConditionType(RuleConditionType conditionType) {
        this.ruleConditionType = conditionType;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }
}
