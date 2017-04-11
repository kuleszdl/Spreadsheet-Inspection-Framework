package sif.testcenter;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractFacility implements Facility {

    protected final SpreadsheetInventory spreadsheetInventory;
    protected ValidationReport validationReport;

    @Inject
    public AbstractFacility(
            SpreadsheetInventory spreadsheetInventory
    ) {
        this.spreadsheetInventory = spreadsheetInventory;
    }

    public boolean runCheck() {
        for (Policy policy : spreadsheetInventory.getInspectionRequest().getPolicies()) {
            if (getPolicyClass().isInstance(policy)) {
                mergeIgnoredCells(policy);
                mergeIgnoredWorksheets(policy);
                setPolicy(policy);
                validationReport = new ValidationReport(policy);
                return true;
            }
        }
        return false;
    }

    private void mergeIgnoredCells(Policy policy) {
        List<String> total = new ArrayList<>();
        total.addAll(spreadsheetInventory.getInspectionRequest().getIgnoredCells());
        total.addAll(policy.getIgnoredCells());
        policy.setIgnoredCells(total);
    }

    private void mergeIgnoredWorksheets(Policy policy) {
        List<String> total = new ArrayList<>();
        total.addAll(spreadsheetInventory.getInspectionRequest().getIgnoredWorksheets());
        total.addAll(policy.getIgnoredWorksheets());
        policy.setIgnoredCells(total);
    }

    protected abstract Class<?> getPolicyClass();

    protected abstract void setPolicy(Policy policy);

}