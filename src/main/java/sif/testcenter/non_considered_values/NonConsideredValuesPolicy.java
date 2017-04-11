package sif.testcenter.non_considered_values;

import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlRootElement;

/***
 * A policy rule to find non considered constants.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "nonConsideredValuesPolicy")
public class NonConsideredValuesPolicy extends Policy {

    private boolean checkStrings;

    public NonConsideredValuesPolicy() {
        super();
        setName(Translator.tl("NonConsideredValuesPolicy.Name"));
        setDescription(Translator.tl("NonConsideredValuesPolicy.Description"));
        setBackground(Translator.tl("NonConsideredValuesPolicy.Background"));
        setSolution(Translator.tl("NonConsideredValuesPolicy.Solution"));
        setPolicyType(PolicyType.STATIC);
        checkStrings = false;
    }


    public NonConsideredValuesPolicy(boolean checkStrings) {
        this();
        this.checkStrings = checkStrings;
    }

    public boolean doCheckStrings() {
        return checkStrings;
    }

    public void setCheckStrings(boolean checkStrings) {
        this.checkStrings = checkStrings;
    }
}