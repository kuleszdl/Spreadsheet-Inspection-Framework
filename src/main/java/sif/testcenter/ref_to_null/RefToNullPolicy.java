package sif.testcenter.ref_to_null;

import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlRootElement;

/***
 * A policy rule to check if a reference point to a null value.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "refToNullPolicy")
public class RefToNullPolicy extends Policy {

    public RefToNullPolicy() {
        super();
        setName(Translator.tl("RefToNullPolicy.Name"));
        setDescription(Translator.tl("RefToNullPolicy.Description"));
        setBackground(Translator.tl("RefToNullPolicy.Background"));
        setSolution(Translator.tl("RefToNullPolicy.Solution"));
        setPolicyType(PolicyType.STATIC);
    }
}
