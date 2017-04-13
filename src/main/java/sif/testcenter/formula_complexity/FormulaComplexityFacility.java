package sif.testcenter.formula_complexity;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;
import sif.model.Formula;
import sif.model.TokenContainer;
import sif.model.tokens.Token;
import sif.model.tokens.TokenType;

@RequestScoped
public class FormulaComplexityFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(FormulaComplexityFacility.class);
    private FormulaComplexityPolicy policy;

    @Inject
    public FormulaComplexityFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    @Override
    public void run() {

        for (Formula formula : spreadsheetInventory.getSpreadsheet().getFormulas()) {

            if (policy.isIgnored(formula.getCell())) {
                continue;
            }

            checkFormula(formula);

        }
        // addCell our ValidationReport to the InspectionResponse
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    private void checkFormula(Formula formula) {

        int operations = getNumberOfOperations(formula);
        int nesting = formula.getDepth();

        if ((operations > policy.getMaxOperations()) || (nesting > policy.getMaxNesting())) {
            FormulaComplexityViolation violation = new FormulaComplexityViolation(formula.getCell());
            violation.setNesting(nesting);
            violation.setMaxNesting(policy.getMaxNesting());
            violation.setOperations(operations);
            violation.setMaxOperations(policy.getMaxOperations());
            validationReport.add(violation);
        }
    }

    private int getNumberOfOperations(TokenContainer container) {
        int numberOfOperations = 0;
        for (Token token : container.getTokens()) {
            if (token.getTokenType() == TokenType.OPERATOR) {
                numberOfOperations++;
            }

            if (token instanceof TokenContainer) {
                numberOfOperations = numberOfOperations + getNumberOfOperations((TokenContainer) token);
            }
        }
        return numberOfOperations;
    }

    @Override
    protected Class<?> getPolicyClass() {
        return FormulaComplexityPolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (FormulaComplexityPolicy) policy;
    }
}
