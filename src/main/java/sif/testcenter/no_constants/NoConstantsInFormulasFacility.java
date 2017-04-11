package sif.testcenter.no_constants;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;
import sif.model.Formula;
import sif.model.tokens.ScalarConstant;
import sif.model.tokens.Token;
import sif.model.tokens.TokenType;

@RequestScoped
public class NoConstantsInFormulasFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(NoConstantsInFormulasFacility.class);
    private NoConstantsInFormulasPolicy policy;

    @Inject
    public NoConstantsInFormulasFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    public void run() {

        for (Formula formula : spreadsheetInventory.getSpreadsheet().getFormulas()) {
            if (policy.isIgnored(formula.getCell())) {
                continue;
            }

            for (Token formulaToken : formula.getAllTokens()) {
                analyzeToken(formulaToken);
            }
        }

        // addCell our ValidationReport to the InspectionResponse
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    private void analyzeToken(Token token) {
        if (token.getTokenType() == TokenType.SCALAR_CONSTANT) {
            NoConstantsInFormulasViolation violation = new NoConstantsInFormulasViolation(token.getContainer().getCell());
            violation.setConstant((ScalarConstant) token);
            validationReport.add(violation);
        }
    }

    @Override
    protected Class<?> getPolicyClass() {
        return NoConstantsInFormulasPolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (NoConstantsInFormulasPolicy) policy;
    }
}