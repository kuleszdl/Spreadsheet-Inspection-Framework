package sif.testcenter.dynamic_testing;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.model.AddressFactory;
import sif.model.Cell;
import sif.model.CellAddress;
import sif.model.values.ValueHelper;
import sif.model.values.ValueType;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;

import java.util.List;

/**
 * A Facility which can realize the {@link Input}s of a {@link DynamicTestingPolicy}
 * and check for it's {@link Condition} instances
 */
@RequestScoped
public class DynamicTestingFacility extends AbstractFacility {

    private final Logger logger = LoggerFactory.getLogger(DynamicTestingFacility.class);
    private DynamicTestingPolicy policy;
    private Scenario currentScenario;

    @Inject
    private ConditionChecker conditionChecker;
    @Inject
    private ValueHelper valueHelper;
    @Inject
    private AddressFactory addressFactory;

    @Inject
    public DynamicTestingFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    public void run() {

        for (Scenario scenario : policy.getScenarios()) {
            this.currentScenario = scenario;
            evaluateScenario(scenario);
        }

        // addCell our violation to the inspection report
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    private void evaluateScenario(Scenario scenario) {
        // parse input values and update the model
        parseInputs(scenario.getInputs());
        // call the spreadsheetIO and fill in all the dirty model values
        spreadsheetInventory.getSpreadsheetIO().updateSpreadsheetValues();
        // call the spreadsheetIO and recalculate all the formulas
        spreadsheetInventory.getSpreadsheetIO().calculateFormulaValues();
        // call the spreadsheetIo and update the model
        spreadsheetInventory.getSpreadsheetIO().updateModelValues();
        // parse invariants and and check them in the model
        parseInvariants(scenario.getInvariants());
        // parse the conditions and check them in the model
        parseConditions(scenario.getConditions());
        // restore all changed cells to the values before evaluating the scenario
        restoreCellBackupValues();
        // call the spreadsheetIO and fill in all the dirty model values
        spreadsheetInventory.getSpreadsheetIO().updateSpreadsheetValues();
    }

    private void parseConditions(List<Condition> conditions) {
        for (Condition condition : conditions) {
            parseCondition(condition);
        }
    }

    private void parseCondition(Condition condition) {
        CellAddress ca = addressFactory.createCellAddress(condition.getTarget());
        if (ca.isValidAddress() && (condition.getType() != ValueType.BLANK)) {
            checkCondition(ca.getCell(), condition);
        }
    }

    private void checkCondition(Cell cell, Condition condition) {
        if (!conditionChecker.isFulfilled(condition, cell)) {
            validationReport.add(new ConditionViolation(
                    cell,
                    currentScenario.getName(),
                    cell.getValue().getValueString(),
                    condition.getValue(),
                    condition.getOperator()
            ));
        }
    }

    private void parseInvariants(List<String> invariants) {
        for (String invariant : invariants) {
            parseInvariant(invariant);
        }
    }

    private void parseInvariant(String invariant) {
        CellAddress ca = addressFactory.createCellAddress(invariant);
        if (ca.isValidAddress()) {
            Cell cell = ca.getCell();
            checkInvariant(cell);
        }
    }

    private void checkInvariant(Cell cell) {
        if (cell.getBackupValue() != null) {
            if (!cell.getValue().valueEquals(cell.getBackupValue())) {
                validationReport.add(new InvariantViolation(cell, currentScenario.getName()));
            }
        }
    }

    private void restoreCellBackupValues() {
        for (Cell cell : spreadsheetInventory.getSpreadsheet().getCells()) {
            if (cell.getBackupValue() != null) {
                // restore the backup value
                cell.setValue(cell.getBackupValue());
                // clean backup value
                cell.setBackupValue(null);
                // mark cell for spreadsheet update
                cell.setDirty(true);
            }
        }
    }

    private void parseInputs(List<Input> inputs) {
        for (Input input : inputs) {
            parseInput(input);
        }
    }

    private void parseInput(Input input) {
        CellAddress ca = addressFactory.createCellAddress(input.getTarget());
        if (ca.isValidAddress() && (input.getType() != ValueType.BLANK)) {
            Cell cell = ca.getCell();
            // create the backup value
            cell.setBackupValue(cell.getValue());
            // update the value
            cell.setValue(valueHelper.importValue(input.getValue(), input.getType()));
            // mark cell for spreadsheet update
            cell.setDirty(true);
        }
    }

    @Override
    protected Class<?> getPolicyClass() {
        return DynamicTestingPolicy.class;
    }

    @Override
    protected void setPolicy(Policy policy) {
        this.policy = (DynamicTestingPolicy) policy;
    }
}
