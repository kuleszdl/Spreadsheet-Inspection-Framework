package sif.testcenter.custom_rules;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import java.util.ArrayList;
import java.util.List;

public class Rule {
    private String name = "DEFAULT RULE_NAME";
    private List<RuleData> ruleData = new ArrayList<>();
    private List<Condition> conditions = new ArrayList<>();

    public Rule() {

    }

    public  Rule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElementWrapper(name="ruleData")
    @XmlElements({
            @XmlElement(name = "ruleData", type = RuleData.class),
    })
    public List<RuleData> getRuleData() {
        return ruleData;
    }
    public void setRuleData(List<RuleData> ruleData) {
        this.ruleData = ruleData;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

}
