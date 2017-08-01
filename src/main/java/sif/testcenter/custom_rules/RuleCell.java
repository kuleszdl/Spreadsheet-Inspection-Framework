package sif.testcenter.custom_rules;

import sif.model.values.ValueType;

import javax.xml.bind.annotation.XmlRootElement;

/***
 * The RuleCells belong to a Rule.
 *
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "ruleCell")
public class RuleCell {
    private String value;
    private String target;
    private ValueType type;

    public RuleCell() {
        type = ValueType.BLANK;
    }

    public RuleCell(String target, String value, ValueType valueType) {
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
