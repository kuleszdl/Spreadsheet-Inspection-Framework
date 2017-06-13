package sif.testcenter.custom_rules;


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


@RequestScoped
public class RulesFacility extends AbstractFacility{
    private final Logger logger = LoggerFactory.getLogger(RulesFacility.class);
    private RulesPolicy policy;
    private Rule currentRule;

    // Inject Conditionchecker
    @Inject
    private ValueHelper valueHelper;
    @Inject
    private AddressFactory addressFactory;



    @Inject
    public RulesFacility(SpreadsheetInventory spreadsheetInventory) {
        super(spreadsheetInventory);
    }

    public void run() {
        for (Rule rule : policy.getRules()) {
            this.currentRule = rule;
            evaluateRule(rule);
        }
        spreadsheetInventory.getInspectionResponse().add(validationReport);
        logger.debug("finished run() with " + validationReport.getViolationCount() + "new violations.");
    }

    private void evaluateRule(Rule rule) {
        // parse cells
        parseRuleData(rule.getRuleData());

        spreadsheetInventory.getSpreadsheetIO().updateSpreadsheetValues();
        spreadsheetInventory.getSpreadsheetIO().calculateFormulaValues();
        spreadsheetInventory.getSpreadsheetIO().updateModelValues();
        parseConditions(rule.getConditions());

        restoreCellBackupValues();
        spreadsheetInventory.getSpreadsheetIO().updateSpreadsheetValues();

    }

    private void parseConditions(List<Condition> conditions) {
        for (Condition condition: conditions) {
            parseCondition(condition);
        }
    }
    private void parseCondition (Condition condition){
        CellAddress ca = addressFactory.createCellAddress(condition.getTarget());
        if (ca.isValidAddress() && (condition.getType() != ValueType.BLANK)) {
            checkCondition(ca.getCell(), condition);
        }
    }

    private void checkCondition (Cell cell, Condition condition) {
        if (!conditionChecker.ifFulfulled(condition, cell)) {
            validationReport.add(new CustomViolation(
                    cell,
                    currentRule.getName(),
                    cell.getValue().getValueString(),
                    condition.getValue(),
                    condition.getConditionType()
            ))
        }
    }

    private void restoreCellBackupValues() {
        for (Cell cell : spreadsheetInventory.getSpreadsheet().getCells())
            if (cell.getBackupValue() != null) {
                cell.setValue(cell.getBackupValue());
                cell.setBackupValue(null);
                cell.setDirty(true);
            }
    }

    private void parseRuleData(List<RuleData> ruleData) {
        for (RuleData ruleCells: ruleData) {
            parseRuleData(ruleCells);
        }
    }

    private void parseRuleData(RuleData ruleData) {
        CellAddress ca = addressFactory.createCellAddress(ruleData.getTarget());
        if (ca.isValidAddress())
            if ((ruleData.getType() == ValueType.BLANK)) {
                Cell cell = ca.getCell();
                cell.setBackupValue(cell.getValue());
                cell.setValue(valueHelper.importValue(ruleData.getValue(), ruleData.getType()));
                cell.setDirty(true);
            }

    }

    @Override
    protected Class<?> getPolicyClass() {
        return  RulesPolicy.class;
    }

    @Override
    protected void setPolicy (Policy policy) {
        this.policy = (RulesPolicy) policy;
    }



















}
