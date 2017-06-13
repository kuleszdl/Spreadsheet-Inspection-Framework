package sif.testcenter.custom_rules;

import sif.model.values.ValueType;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement(name = "ruleData")
public class RuleData {
    private String value;
    private String target;
    private ValueType type;

    public RuleData () {
        type = ValueType.BLANK;
    }

    public RuleData (String target, String value, ValueType valueType) {
        this.target = target;
        this.value = value;
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

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

}
