package sif.testcenter.error_containing_cell;

import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement(name = "errorContainingCellPolicy")
public class ErrorContainingCellPolicy extends Policy {

    public ErrorContainingCellPolicy() {
        setName(Translator.tl("ErrorContainingCellPolicy.Name"));
        setDescription(Translator.tl("ErrorContainingCellPolicy.Description"));
        setBackground(Translator.tl("ErrorContainingCellPolicy.Background"));
        setSolution(Translator.tl("ErrorContainingCellPolicy.Solution"));
        setPolicyType(PolicyType.STATIC);
    }
}
