package sif.testcenter.custom_rules;


import org.apache.poi.ss.usermodel.ConditionType;
import sif.model.values.ValueType;

public class Condition {
    private String target;
    private String value;
    private ConditionType conditionType;
    private ValueType type;

    public Condition () {
        type = ValueType.BLANK;
    }

    public  Condition(String target, String value, ConditionType conditionType, ValueType valueType) {
        this.target = target;
        this.value = value;
        this.conditionType = conditionType;
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



    public ConditionType getConditionType() {
        return conditionType;
    }
    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }
    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }
}
