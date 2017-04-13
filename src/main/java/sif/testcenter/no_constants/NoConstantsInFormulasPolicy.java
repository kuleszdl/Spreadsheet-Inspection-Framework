package sif.testcenter.no_constants;

import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlRootElement;

/***
 * A policy rule that defines which constants value are not allowed in
 * formulas.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "noConstantsInFormulasPolicy")
public class NoConstantsInFormulasPolicy extends Policy {

    public NoConstantsInFormulasPolicy() {
        super();
        setName(Translator.tl("NoConstantsInFormulasPolicy.Name"));
        setDescription(Translator.tl("NoConstantsInFormulasPolicy.Description"));
        setBackground(Translator.tl("NoConstantsInFormulasPolicy.Background"));
        setSolution(Translator.tl("NoConstantsInFormulasPolicy.Solution"));
        setPolicyType(PolicyType.STATIC);
    }
}
