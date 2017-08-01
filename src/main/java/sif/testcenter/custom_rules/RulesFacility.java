package sif.testcenter.custom_rules;


import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.model.AddressFactory;
import sif.model.Cell;
import sif.model.CellAddress;
import sif.model.values.ValueHelper;
import sif.testcenter.AbstractFacility;
import sif.testcenter.Policy;
import sif.testcenter.SpreadsheetInventory;

import java.util.Iterator;
import java.util.List;


/***
 * The Facility run the custom_rules tests
 */
@RequestScoped
public class RulesFacility extends AbstractFacility{
    private final Logger logger = LoggerFactory.getLogger(RulesFacility.class);
    private RulesPolicy policy;
    private Rule currentRule;

    @Inject
    private CustomChecker customChecker;
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
        logger.debug("finished run() with " + validationReport.getViolationCount() + " new violations.");
    }

    /**
     * Parses RuleCells and gets the Spreadsheet Information, then parses the conditions
     * @param rule
     */
    private void evaluateRule(Rule rule) {
        // parse cells
        parseRuleCells(rule.getRuleCells());

        spreadsheetInventory.getSpreadsheetIO().updateSpreadsheetValues();
        spreadsheetInventory.getSpreadsheetIO().calculateFormulaValues();
        spreadsheetInventory.getSpreadsheetIO().updateModelValues();
        parseConditions(rule.getRuleConditions());

        restoreCellBackupValues();
        spreadsheetInventory.getSpreadsheetIO().updateSpreadsheetValues();

    }

    /**
     * Parses all Conditions for a Rule
     * @param ruleConditions
     */
    private void parseConditions(List<RuleCondition> ruleConditions) {
        for (RuleCondition ruleCondition : ruleConditions) {
            parseCondition(ruleCondition);
        }
    }

    /**
     * Parses through every RuleCell of a Rule for each Condition
     * @param ruleCondition
     */
    private void parseCondition (RuleCondition ruleCondition){
        for (Iterator<RuleCell> iter = this.currentRule.getRuleCells().iterator(); iter.hasNext();) {
            RuleCell ruleCell = iter.next();
            CellAddress ca = addressFactory.createCellAddress(ruleCell.getTarget());
            if (ca.isValidAddress() && (ruleCondition.getConditionType() != RuleConditionType.Blank)) {
                checkCondition(ca.getCell(), ruleCondition);
            }
        }
    }

    /**
     * Checks a Cell against a Condition and adds a CustomViolation to the ValidationReport if it isn't
     * @param cell
     * @param ruleCondition
     */
    private void checkCondition (Cell cell, RuleCondition ruleCondition) {
        //for (int i = 0; i < currentRule.getRuleConditions().lastIndexOf(ruleCondition); i++) {}

        if (!customChecker.isFulfilled(ruleCondition, cell)) {
            validationReport.add(new CustomViolation(
                    cell,
                    currentRule.getName(),
                    cell.getValue().getValueString(),
                    ruleCondition.getConditionValue(),
                    ruleCondition.getConditionType(),
                    ruleCondition.getConditionName()
            ));
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

    /**
     * Parses all RuleCells for a Rule
     * @param ruleData
     */
    private void parseRuleCells(List<RuleCell> ruleData) {
        for (RuleCell ruleCell : ruleData) {
            parseRuleCell(ruleCell);
        }
    }

    /**
     * Checks CellAddress and sets Values
     * @param ruleCell
     */
    private void parseRuleCell(RuleCell ruleCell) {
        CellAddress ca = addressFactory.createCellAddress(ruleCell.getTarget());
        if (ca.isValidAddress()) {
          //  if ((ruleData.getType() == ValueType.BLANK)) {
                Cell cell = ca.getCell();
                cell.setBackupValue(cell.getValue());
                cell.setValue(valueHelper.importValue(ruleCell.getValue(), ruleCell.getType()));
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
