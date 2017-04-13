package sif.testcenter.ref_to_null;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;
import sif.model.Cell;
import sif.model.Formula;
import sif.model.Referenceable;
import sif.model.tokens.Reference;
import sif.model.tokens.Token;
import sif.model.tokens.TokenType;
import sif.model.values.ValueType;

/***
 * The reference to null pattern
 */
@RequestScoped
public class RefToNullFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(RefToNullFacility.class);
    private RefToNullPolicy policy;

    @Inject
    public RefToNullFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    @Override
    public void run() {

        for (Formula formula : spreadsheetInventory.getSpreadsheet().getFormulas()) {

            if (policy.isIgnored(formula.getCell())) {
                continue;
            }

            boolean hasViolation = false;
            for (Token token : formula.getAllTokens()) {
                if (token.getTokenType() == TokenType.REFERENCE) {
                    Reference reference = (Reference) token;
                    if (isReferenceToNullValue(reference)) {
                        hasViolation = true;
                        break;
                    }
                }
            }

            if (hasViolation) {
                RefToNullViolation violation = new RefToNullViolation(formula.getCell());
                validationReport.add(violation);
            }
        }

        // addCell our ValidationReport to the InspectionResponse
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    private boolean isReferenceToNullValue(Reference reference) {
        boolean result = false;
        Referenceable referencedElement = reference.getTargetElement();

        if (referencedElement == null){
            return true;
        }

        if (referencedElement instanceof Cell) {
            Cell c = (Cell) referencedElement;
            if (c.getValue().getType() == ValueType.BLANK) {
                result = true;
            } else if (c.getValue().toString().isEmpty()) {
                result = true;
            }
        }

        return result;
    }

    @Override
    protected Class<?> getPolicyClass() {
        return RefToNullPolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (RefToNullPolicy) policy;
    }
}
