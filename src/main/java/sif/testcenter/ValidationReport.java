package sif.testcenter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/***
 * A violation list stores all violations of a specific policy rule.
 */

@SuppressWarnings("unused")
public class ValidationReport {

    private List<Violation> violations;
    private Policy policy;

    public ValidationReport() {
        this.violations = new ArrayList<>();
    }

    /***
     * Creates an empty violation list that stores violations of the given
     * policy rule.
     *
     * @param policy The given policy rule.
     */
    public ValidationReport(Policy policy) {
        this.policy = policy;
    }

    @XmlElement(name = "violation")
    @XmlElementWrapper(name = "violations")
    public List<Violation> getViolations() {
        return violations;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }

    public int getViolationCount() {
        if (violations != null)
            return violations.size();
        else
            return 0;
    }

    public void add(Violation violation) {
        if (violations == null) {
            violations = new ArrayList<>();
        }
        violations.add(violation);
    }

    public Policy getPolicy() { return policy; }
    public void setPolicy(Policy policy) { this.policy = policy; }
}