package sif.testcenter.reading_direction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Violation;
import sif.model.tokens.Reference;
import sif.utility.Translator;

import java.util.Arrays;
import java.util.List;

/***
 * A custom single violation to record violations of the
 * {@link ReadingDirectionPolicy}.
 */
public class ReadingDirectionViolation extends Violation {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(ReadingDirectionViolation.class);

    private final Reference reference;
    private boolean leftToRight;
    private boolean topToBottom;

    public ReadingDirectionViolation(Reference reference) {
        super(reference.getContainer().getCell());
        this.reference = reference;
        this.leftToRight = false;
        this.topToBottom = false;
        logger.trace("New ReadingDirectionViolation for Cell " + getCausingCell().getExcelNotation());
    }

    public void setLeftToRight(boolean leftToRight) {
        this.leftToRight = leftToRight;
    }

    public void setTopToBottom(boolean topToBottom) {
        this.topToBottom = topToBottom;
    }

    @Override
    public String getUid() {
        return getClass().getSimpleName() + "." + getCausingCell().getExcelNotation() + "." + reference.getStringRepresentation();
    }

    @Override
    public String getDescription() {

        List<String> descriptionVars = Arrays.asList(
                getCausingCell().getWorksheet().getKey(),
                getCausingCell().getSimpleNotation()
        );
        String description = Translator.tl("ReadingDirectionPolicy.Violation", descriptionVars);

        if (leftToRight) {
            description += " " + Translator.tl("ReadingDirectionPolicy.Violation.leftRight", reference.getStringRepresentation());
        }

        if (topToBottom) {
            description += " " + Translator.tl("ReadingDirectionPolicy.Violation.topBottom", reference.getStringRepresentation());
        }

        return description;
    }

    @Override
    public Double getSeverity() {
        return 0.0;
    }
}
