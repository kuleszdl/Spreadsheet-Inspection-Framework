package sif.testcenter.custom_rules;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@XmlRootElement(name = "rule")
public class Rule {
    private String name = "DEFAULT_RULE_NAME";
    private String description = "DEFAULT_RULE_DESCRIPTION";
    private List<RuleCell> ruleCells = new ArrayList<>();
    private List<RuleCondition> ruleConditions = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElementWrapper(name="ruleCells")
    @XmlElements({
            @XmlElement(name = "ruleCell", type = RuleCell.class),
    })
    public List<RuleCell> getRuleCells() {
        return ruleCells;
    }
    public void setRuleCells(List<RuleCell> ruleCells) {
        this.ruleCells = ruleCells;
    }

    @XmlElementWrapper(name ="ruleConditions")
    @XmlElements({
            @XmlElement(name="ruleCondition", type= RuleCondition.class),
    })
    public List<RuleCondition> getRuleConditions() {
        return ruleConditions;
    }

    public void setRuleConditions(List<RuleCondition> ruleConditions) {
        this.ruleConditions = ruleConditions;
    }

}
