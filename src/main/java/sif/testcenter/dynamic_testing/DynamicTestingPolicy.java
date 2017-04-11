package sif.testcenter.dynamic_testing;

import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@XmlRootElement(name = "dynamicTestingPolicy")
public class DynamicTestingPolicy extends Policy {

    private List<Scenario> scenarios = new ArrayList<>();
    
    public DynamicTestingPolicy() {
        super();
        setName(Translator.tl("DynamicTestingPolicy.Name"));
        setDescription(Translator.tl("DynamicTestingPolicy.Description"));
        setBackground(Translator.tl("DynamicTestingPolicy.Background"));
        setSolution(Translator.tl("DynamicTestingPolicy.Solution"));
        setPolicyType(PolicyType.DYNAMIC);
    }

    @XmlElementWrapper(name = "scenarios")
    @XmlElements({
            @XmlElement(name = "scenario", type = Scenario.class),
    })
    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }
}
