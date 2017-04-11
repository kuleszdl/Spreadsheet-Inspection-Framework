package sif.testcenter.sanity_checks;

import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@XmlRootElement(name = "sanityChecksPolicy")
public class SanityChecksPolicy extends Policy {

    private List<String> sanityValueCells = new ArrayList<>();
    private List<String> sanityConstraintCells = new ArrayList<>();
    private List<String> sanityExplanationCells = new ArrayList<>();
    private List<String> sanityCheckingCells = new ArrayList<>();
    private boolean sanityWarnings;

    public SanityChecksPolicy() {
        super();
        setName(Translator.tl("SanityChecksPolicy.Name"));
        setDescription(Translator.tl("SanityChecksPolicy.Description"));
        setBackground(Translator.tl("SanityChecksPolicy.Background"));
        setSolution(Translator.tl("SanityChecksPolicy.Solution"));
        setPolicyType(PolicyType.STATIC);
    }

    public List<String> getSanityValueCells() {
        return sanityValueCells;
    }

    public void setSanityValueCells(List<String> sanityValueCells) {
        this.sanityValueCells = sanityValueCells;
    }

    public List<String> getSanityConstraintCells() {
        return sanityConstraintCells;
    }

    public void setSanityConstraintCells(List<String> sanityConstraintCells) {
        this.sanityConstraintCells = sanityConstraintCells;
    }

    public List<String> getSanityExplanationCells() {
        return sanityExplanationCells;
    }

    public void setSanityExplanationCells(List<String> sanityExplanationCells) {
        this.sanityExplanationCells = sanityExplanationCells;
    }

    public List<String> getSanityCheckingCells() {
        return sanityCheckingCells;
    }

    public void setSanityCheckingCells(List<String> sanityCheckingCells) {
        this.sanityCheckingCells = sanityCheckingCells;
    }

    public boolean getSanityWarnings() {
        return sanityWarnings;
    }

    public void setSanityWarnings(boolean sanityWarnings) {
        this.sanityWarnings = sanityWarnings;
    }
}
