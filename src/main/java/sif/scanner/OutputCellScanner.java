package sif.scanner;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.SpreadsheetInventory;
import sif.model.Cell;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class OutputCellScanner implements Scanner {

    private final Logger logger = LoggerFactory.getLogger(OutputCellScanner.class);

    @Inject
    private SpreadsheetInventory spreadsheetInventory;

    @Override
    public void scan() {
        List<Cell> outputCells = new ArrayList<>();
        for (Cell cell : spreadsheetInventory.getSpreadsheet().getCells()) {
            if ((cell.containsFormula()) & cell.getIncomingReferences().isEmpty()) {
                outputCells.add(cell);
            }
        }
        spreadsheetInventory.addElementList("OutputCell", outputCells);
        logger.debug("finished scan() with " + outputCells.size() + " new elements");
    }

}
