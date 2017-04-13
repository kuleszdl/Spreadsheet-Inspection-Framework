package sif.testcenter.string_distance;

import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A policy rule to find typos with the help of the string distance
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "stringDistancePolicy")
public class StringDistancePolicy extends Policy {

    private int minDistance;
    
    public StringDistancePolicy() {
        super();
        setName(Translator.tl("StringDistancePolicy.Name"));
        setDescription(Translator.tl("StringDistancePolicy.Description"));
        setBackground(Translator.tl("StringDistancePolicy.Background"));
        setSolution(Translator.tl("StringDistancePolicy.Solution"));
        setPolicyType(PolicyType.STATIC);
        this.minDistance = 2;
    }

    public StringDistancePolicy(int minDistance) {
        this();
        this.minDistance = minDistance;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

}
