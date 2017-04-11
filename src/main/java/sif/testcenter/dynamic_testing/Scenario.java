package sif.testcenter.dynamic_testing;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@XmlRootElement(name = "scenario")
public class Scenario {

    private String name = "DEFAULT_SCENARIO_NAME";
    private List<Input> inputs = new ArrayList<>();
    private List<Condition> conditions = new ArrayList<>();
    private List<String> invariants = new ArrayList<>();

    public Scenario() {
    }

    public Scenario(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElementWrapper(name = "inputs")
    @XmlElements({
            @XmlElement(name = "input", type = Input.class),
    })
    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    @XmlElementWrapper(name = "conditions")
    @XmlElements({
            @XmlElement(name = "condition", type = Condition.class),
    })
    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @XmlElementWrapper(name = "invariants")
    @XmlElement(name = "invariant")
    public List<String> getInvariants() {
        return invariants;
    }

    public void setInvariants(List<String> invariants) {
        this.invariants = invariants;
    }

}
