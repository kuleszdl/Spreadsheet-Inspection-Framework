package sif.testcenter.non_considered_values;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.model.Cell;
import sif.model.tokens.Reference;
import sif.model.values.ValueType;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;

import java.util.List;

/***
 * The non considered AbstractValue pattern.
 */
@RequestScoped
public class NonConsideredValuesFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(NonConsideredValuesFacility.class);
    private NonConsideredValuesPolicy policy;

    @Inject
    public NonConsideredValuesFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    @Override
    public void run() {

        for (Cell cell : spreadsheetInventory.getSpreadsheet().getCells()) {

            if (policy.isIgnored(cell) || cell.containsFormula()) {
                continue;
            }

            ValueType valueType = cell.getValue().getType();

            if (valueType == ValueType.BLANK) {
                // we don't want to check empty values
                continue;
            }

            if (!policy.doCheckStrings() && (valueType == ValueType.STRING)) {
                // we dont want to check text values
                continue;
            }

            List<Reference> incomingReferences = cell.getIncomingReferences();
            if (incomingReferences.isEmpty()){
                validationReport.add(new NonConsideredValuesViolation(cell));
            }
        }

        // addCell our ValidationReport to the InspectionResponse
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    @Override
    protected Class<?> getPolicyClass() {
        return NonConsideredValuesPolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (NonConsideredValuesPolicy) policy;
    }
}
