package sif.testcenter.formula_complexity;

import sif.model.Formula;
import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlRootElement;

/***
 * A policy rule that defines the allowed complexity of a {@link Formula}. The
 * complexity is measured twofold:<br>
 * - The Nesting level of a operation. <br>
 * - The number of operations <br>
 * Formulas that exceed the configured threshold for any of these two are
 * reported by a violation.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "formulaComplexityPolicy")
public class FormulaComplexityPolicy extends Policy {

    private int maxNesting;
    private int maxOperations;

    public FormulaComplexityPolicy() {
        setName(Translator.tl("FormulaComplexityPolicy.Name"));
        setDescription(Translator.tl("FormulaComplexityPolicy.Description"));
        setBackground(Translator.tl("FormulaComplexityPolicy.Background"));
        setSolution(Translator.tl("FormulaComplexityPolicy.Solution"));
        setPolicyType(PolicyType.STATIC);
        this.maxNesting = 2;
        this.maxOperations = 5;
    }

    public FormulaComplexityPolicy(int maxNesting, int maxOperations) {
        this();
        this.maxNesting = maxNesting;
        this.maxOperations = maxOperations;
    }

    public int getMaxNesting() {
        return maxNesting;
    }

    public void setMaxNesting(int maxNesting) {
        this.maxNesting = maxNesting;
    }

    public int getMaxOperations() {
        return maxOperations;
    }

    public void setMaxOperations(int maxOperations) {
        this.maxOperations = maxOperations;
    }

}
