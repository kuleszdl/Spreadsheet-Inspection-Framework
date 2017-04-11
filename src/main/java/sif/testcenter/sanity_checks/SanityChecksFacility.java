package sif.testcenter.sanity_checks;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;
import sif.model.Worksheet;

import java.util.Collection;

@RequestScoped
public class SanityChecksFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(SanityChecksFacility.class);

    @SuppressWarnings("FieldCanBeLocal")
    private SanityChecksPolicy policy;

    @Inject
    public SanityChecksFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    @Override
    public void run() {
        Collection<Worksheet> worksheets = spreadsheetInventory.getSpreadsheet().getWorksheets();
        for (Worksheet worksheet : worksheets) {
            // @TODO do something...
            logger.debug("Worksheet Key is: " + worksheet.getKey());
        }
    }


    @Override
    protected Class<?> getPolicyClass() {
        return SanityChecksPolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (SanityChecksPolicy) policy;
    }
}
