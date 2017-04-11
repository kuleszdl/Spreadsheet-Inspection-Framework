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
public class InputCellScanner implements Scanner {

    private final Logger logger = LoggerFactory.getLogger(InputCellScanner.class);

    @Inject
    private SpreadsheetInventory spreadsheetInventory;

    @Override
    public void scan() {
        List<Cell> inputCells = new ArrayList<>();
        for (Cell cell : spreadsheetInventory.getSpreadsheet().getCells()) {
            if (cell.getOutgoingReferences().isEmpty() & !cell.getIncomingReferences().isEmpty()) {
                inputCells.add(cell);
            }
        }
        spreadsheetInventory.addElementList("InputCell", inputCells);
        logger.debug("finished scan() with " + inputCells.size() + " new elements");
    }
}
