package sif.testcenter.dynamic_testing;

import sif.model.values.ValueType;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A condition that provides means for comparing cell value to certain value.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "binaryCondition")
public class Condition {

    private String target;
    private String value;
    private ValueType type;
    private Operator operator;

    public Condition() {
        type = ValueType.BLANK;
    }

    public Condition(String target, Operator operator, String value, ValueType valueType) {
        this.target = target;
        this.operator = operator;
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

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
}