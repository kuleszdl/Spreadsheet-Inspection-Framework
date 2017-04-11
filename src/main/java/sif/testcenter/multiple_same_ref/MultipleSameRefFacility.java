package sif.testcenter.multiple_same_ref;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;
import sif.model.Cell;
import sif.model.Formula;
import sif.model.tokens.Reference;

import java.util.Iterator;
import java.util.List;

@RequestScoped
public class MultipleSameRefFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(MultipleSameRefFacility.class);
    private MultipleSameRefPolicy policy;

    @Inject
    public MultipleSameRefFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    @Override
    public void run() {

        for (Formula formula : spreadsheetInventory.getSpreadsheet().getFormulas()) {

            if (policy.isIgnored(formula.getCell())) {
                continue;
            }

            checkReferences(formula.getCell());
        }

        // addCell our ValidationReport to the InspectionResponse
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    private void checkReferences(Cell cell) {

        int count = 1;
        List<Reference> references = cell.getOutgoingReferences();
        Iterator<Reference> it = references.iterator();
        Reference current = null, next;
        while (it.hasNext()) {
            next = it.next();
            if (current != null) {
                count = getCount(cell, count, current, next);
            }
            current = next;
        }

        // if this was the end of the formula
        if ((count > 1) && (!it.hasNext())) {
            validationReport.add(new MultipleSameRefViolation(cell, current, count));
        }
    }

    private int getCount(Cell cell, int count, Reference current, Reference next) {
        if (next.getTargetElement().equals(current.getTargetElement())) {
            count++;
        } else {
            if (count > 1) {
                validationReport.add(new MultipleSameRefViolation(cell, current, count));
                count = 1;
            }
        }

        return count;
    }

    @Override
    protected Class<?> getPolicyClass() {
        return MultipleSameRefPolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (MultipleSameRefPolicy) policy;
    }
}
