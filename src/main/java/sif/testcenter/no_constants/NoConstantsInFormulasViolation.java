package sif.testcenter.no_constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.Cell;
import sif.model.tokens.ScalarConstant;
import sif.utility.Translator;

import java.util.Arrays;
import java.util.List;

/***
 * A custom single violation to record violations of the
 * {@link NoConstantsInFormulasPolicy}.
 * 
 */
public class NoConstantsInFormulasViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(NoConstantsInFormulasViolation.class);
    private ScalarConstant constant;

    public NoConstantsInFormulasViolation(Cell cell) {
        super(cell);
        logger.trace("New NoConstantsInFormulasViolation for Cell " + cell.getExcelNotation());
    }

    public void setConstant(ScalarConstant constant) {
        this.constant = constant;
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation() + "." + constant.getStringRepresentation();
    }

    @Override
    public String getDescription() {

        List<String> vars = Arrays.asList(
                getCausingCell().getWorksheet().getKey(),
                getCausingCell().getSimpleNotation(),
                getCausingCell().getFormula().getFormulaString(),
                constant.getValue().getType().toString(),
                constant.getValue().getValueString()
        );
        return Translator.tl("NoConstantsInFormulasPolicy.Violation", vars);
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }
}
