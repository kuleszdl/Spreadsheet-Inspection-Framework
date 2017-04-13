package sif.testcenter.one_among_others;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.Cell;
import sif.utility.Translator;

import java.util.Arrays;
import java.util.List;

public class OneAmongOthersViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(OneAmongOthersViolation.class);

    public OneAmongOthersViolation(Cell cell) {
        super(cell);
        logger.trace("New OneAmongOthersViolation for Cell " + cell.getExcelNotation());
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation();
    }

    @Override
    public String getDescription() {
        List<String> descriptionVars = Arrays.asList(
                getCausingCell().getWorksheet().getKey(),
                getCausingCell().getSimpleNotation(),
                getCausingCell().getValue().getType().toString()
        );
        return Translator.tl("OneAmongOthersPolicy.Violation", descriptionVars);
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }

}
