package sif.testcenter.multiple_same_ref;

import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlRootElement;

/***
 * A police rule to find multiple same references inside one cell.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "multipleSameRefPolicy")
public class MultipleSameRefPolicy extends Policy {

    public MultipleSameRefPolicy() {
        super();
        setName(Translator.tl("MultipleSameRefPolicy.Name"));
        setDescription(Translator.tl("MultipleSameRefPolicy.Description"));
        setBackground(Translator.tl("MultipleSameRefPolicy.Background"));
        setSolution(Translator.tl("MultipleSameRefPolicy.Solution"));
        setPolicyType(PolicyType.STATIC);
    }

}
