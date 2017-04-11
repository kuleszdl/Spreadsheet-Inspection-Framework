package sif.model.tokens;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import sif.model.Cell;

/**
 * Representation of a function.
 */
public class Function extends TokenContainerToken {

    private final String functionString;

    @AssistedInject
    private Function(@Assisted Cell cell, @Assisted String functionString) {
        super(cell, TokenType.FUNCTION);
        this.functionString = functionString;
    }

    public String getFunctionString() {
        return functionString;
    }

    @Override
    public String getTokenString() {
        return functionString;
    }

    @Override
    public String getStringRepresentation() {
        return getClass().getSimpleName() + " [" + getTokenString() + "]";
    }
}