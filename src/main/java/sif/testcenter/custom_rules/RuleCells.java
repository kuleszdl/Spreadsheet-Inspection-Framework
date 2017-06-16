package sif.testcenter.custom_rules;

import sif.model.values.ValueType;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement(name = "ruleCell")
public class RuleCells {
    private String value;
    private String target;
    private ValueType type;

    public RuleCells() {
        type = ValueType.BLANK;
    }

    public RuleCells(String target, String value, ValueType valueType) {
        this.target = target;
        this.type = valueType;
        this.value = value;
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

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

}
