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
public class IntermediateCellScanner implements Scanner {

    private final Logger logger = LoggerFactory.getLogger(IntermediateCellScanner.class);

    @Inject
    private SpreadsheetInventory spreadsheetInventory;

    @Override
    public void scan() {
        List<Cell> intermediateCellList = new ArrayList<>();
        for (Cell cell : spreadsheetInventory.getSpreadsheet().getCells()) {
            if (!cell.getOutgoingReferences().isEmpty() & !cell.getIncomingReferences().isEmpty()) {
                intermediateCellList.add(cell);
            }
        }

        spreadsheetInventory.addElementList("IntermediateCell", intermediateCellList);
        logger.debug("finished scan() with " + intermediateCellList.size() + " new elements");
    }

}
