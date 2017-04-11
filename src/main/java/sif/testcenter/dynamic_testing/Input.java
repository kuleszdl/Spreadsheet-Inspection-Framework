package sif.testcenter.dynamic_testing;

import sif.model.values.ValueType;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The input which is used to set the model in the desired state.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "input")
public class Input {

    private String value;
    private String target;
    private ValueType type;

    public Input() {
        type = ValueType.BLANK;
    }

    public Input(String target, String value, ValueType valueType) {
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
