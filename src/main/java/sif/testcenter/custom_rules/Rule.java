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
    private List<RuleCells> ruleCells = new ArrayList<>();
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

    @XmlElementWrapper(name="ruleCells")
    @XmlElements({
            @XmlElement(name = "ruleCell", type = RuleCells.class),
    })
    public List<RuleCells> getRuleCells() {
        return ruleCells;
    }
    public void setRuleCells(List<RuleCells> ruleCells) {
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
