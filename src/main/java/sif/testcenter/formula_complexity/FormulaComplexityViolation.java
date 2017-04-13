package sif.testcenter.formula_complexity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.Cell;
import sif.utility.Translator;

import java.util.Arrays;
import java.util.List;

/***
 * A custom single violation to record a violation of the
 * {@link FormulaComplexityPolicy}.
 */
public class FormulaComplexityViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(FormulaComplexityViolation.class);

    private int operations = 0;
    private int nesting = 0;
    private int maxNesting = 0;
    private int maxOperations = 0;

    FormulaComplexityViolation(Cell cell) {
        super(cell);
        logger.trace("New FormulaComplexityViolation for Cell " + cell.getExcelNotation());
    }

    void setMaxNesting(int maxNesting) {
        this.maxNesting = maxNesting;
    }

    void setMaxOperations(int maxOperations) {
        this.maxOperations = maxOperations;
    }

    void setOperations(int operations) {
        this.operations = operations;
    }

    void setNesting(int nesting) {
        this.nesting = nesting;
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation();
    }

    @Override
    public String getDescription() {

        List<String> descriptionVars = Arrays.asList(
                getCausingCell().getWorksheet().getKey(),
                getCausingCell().getSimpleNotation()
        );
        String description = Translator.tl("FormulaComplexityPolicy.Violation", descriptionVars);

        if (nesting > maxNesting) {
            List<String> vars = Arrays.asList(
                    Integer.toString(nesting),
                    Integer.toString(maxNesting)
            );
            description += " " + Translator.tl("FormulaComplexityPolicy.Violation.Nesting", vars);
        }


        if (operations > maxOperations) {
            List<String> vars = Arrays.asList(
                    Integer.toString(operations),
                    Integer.toString(maxOperations)
            );
            description += " " + Translator.tl("FormulaComplexityPolicy.Violation.Operations", vars);
        }

        return description;
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }
}
