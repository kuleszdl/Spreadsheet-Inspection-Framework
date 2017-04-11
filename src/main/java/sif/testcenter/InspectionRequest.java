package sif.testcenter;

import sif.testcenter.dynamic_testing.DynamicTestingPolicy;
import sif.testcenter.error_containing_cell.ErrorContainingCellPolicy;
import sif.testcenter.formula_complexity.FormulaComplexityPolicy;
import sif.testcenter.multiple_same_ref.MultipleSameRefPolicy;
import sif.testcenter.no_constants.NoConstantsInFormulasPolicy;
import sif.testcenter.non_considered_values.NonConsideredValuesPolicy;
import sif.testcenter.one_among_others.OneAmongOthersPolicy;
import sif.testcenter.reading_direction.ReadingDirectionPolicy;
import sif.testcenter.ref_to_null.RefToNullPolicy;
import sif.testcenter.sanity_checks.SanityChecksPolicy;
import sif.testcenter.string_distance.StringDistancePolicy;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request from a user of the Spreadsheet Inspection Framework to
 * inspect a model.
 */
@SuppressWarnings("unused")
@XmlRootElement
public class InspectionRequest {

    private List<Policy> policies;
    private List<String> ignoredCells;
    private List<String> ignoredWorksheets;

    public InspectionRequest() {
        this.policies = new ArrayList<>();
        this.ignoredCells = new ArrayList<>();
        this.ignoredWorksheets = new ArrayList<>();
    }

    @XmlElementWrapper(name = "policies")
    @XmlElements({
            @XmlElement(name = "errorContainingCellPolicy", type = ErrorContainingCellPolicy.class),
            @XmlElement(name = "formulaComplexityPolicy", type = FormulaComplexityPolicy.class),
            @XmlElement(name = "multipleSameRefPolicy", type = MultipleSameRefPolicy.class),
            @XmlElement(name = "noConstantsInFormulasPolicy", type = NoConstantsInFormulasPolicy.class),
            @XmlElement(name = "nonConsideredValuesPolicy", type = NonConsideredValuesPolicy.class),
            @XmlElement(name = "oneAmongOthersPolicy", type = OneAmongOthersPolicy.class),
            @XmlElement(name = "readingDirectionPolicy", type = ReadingDirectionPolicy.class),
            @XmlElement(name = "refToNullPolicy", type = RefToNullPolicy.class),
            @XmlElement(name = "stringDistancePolicy", type = StringDistancePolicy.class),
            @XmlElement(name = "sanityChecksPolicy", type = SanityChecksPolicy.class),
            @XmlElement(name = "dynamicTestingPolicy", type = DynamicTestingPolicy.class)
    })
    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    @XmlElementWrapper(name = "ignoredCells")
    @XmlElement(name = "cell")
    public List<String> getIgnoredCells() {
        return ignoredCells;
    }

    public void setIgnoredCells(List<String> ignoredCells) {
        this.ignoredCells = ignoredCells;
    }

    @XmlElementWrapper(name = "ignoredWorksheets")
    @XmlElement(name = "worksheet")
    public List<String> getIgnoredWorksheets() {
        return ignoredWorksheets;
    }

    public void setIgnoredWorksheets(List<String> ignoredWorksheets) {
        this.ignoredWorksheets = ignoredWorksheets;
    }
}