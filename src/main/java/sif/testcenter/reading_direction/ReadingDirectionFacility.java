package sif.testcenter.reading_direction;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;
import sif.model.Formula;
import sif.model.tokens.Reference;
import sif.model.tokens.Token;
import sif.model.tokens.TokenType;

@RequestScoped
public class ReadingDirectionFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(ReadingDirectionFacility.class);
    private ReadingDirectionPolicy policy;

    @Inject
    public ReadingDirectionFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    @Override
    public void run() {

        for (Formula formula : spreadsheetInventory.getSpreadsheet().getFormulas()) {

            if (policy.isIgnored(formula.getCell())) {
                continue;
            }

            for (Token token : formula.getAllTokens()) {
                if (token.getTokenType() == TokenType.REFERENCE) {
                    analyzeReference((Reference) token);
                }
            }
        }
        // addCell our ValidationReport to the InspectionResponse
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    private void analyzeReference(Reference reference) {

        boolean leftToRight = false;
        boolean topToBottom = false;

        if (policy.getLeftToRight()) {
            if (reference.getTargetElement().getAddress().isRightOf(reference.getSourceElement().getAddress())) {
                leftToRight = true;
            }
        }

        if (policy.getTopToBottom()) {
            if (reference.getTargetElement().getAddress().isBelow(reference.getSourceElement().getAddress())) {
                topToBottom = true;
            }
        }

        // if one of both is true add the violation
        if (leftToRight || topToBottom) {
            ReadingDirectionViolation violation = new ReadingDirectionViolation(reference);
            violation.setLeftToRight(leftToRight);
            violation.setTopToBottom(topToBottom);
            validationReport.add(violation);
        }

    }

    @Override
    protected Class<?> getPolicyClass() {
        return ReadingDirectionPolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (ReadingDirectionPolicy) policy;
    }

}
