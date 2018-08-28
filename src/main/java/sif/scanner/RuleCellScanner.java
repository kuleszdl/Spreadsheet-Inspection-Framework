package sif.scanner;


import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import sif.testcenter.SpreadsheetInventory;
import sif.model.Cell;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class RuleCellScanner implements Scanner{
    private final Logger logger = LoggerFactory.getLogger(RuleCellScanner.class);

    @Inject
    private SpreadsheetInventory spreadsheetInventory;

    @Override
    public void scan() {
        List<Cell> ruleCells = new ArrayList<>();
        for (Cell cell : spreadsheetInventory.getSpreadsheet().getCells()) {
            if (true) {
                ruleCells.add(cell);
            }
        }
        spreadsheetInventory.addElementList("RuleCell", ruleCells);
        logger.debug("finished scan() with " + ruleCells.size() + " new elements");
    }
}
