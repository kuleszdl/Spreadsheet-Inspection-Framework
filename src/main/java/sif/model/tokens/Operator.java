package sif.model.tokens;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import sif.model.Cell;

/**
 * Represents an operator, such as + or -.
 */
public class Operator extends TokenContainerToken {

    private OperatorType operatorType;

    @AssistedInject
    private Operator(@Assisted Cell cell) {
        super(cell, TokenType.OPERATOR);
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(OperatorType operatorType) {
        this.operatorType = operatorType;
    }

    @Override
    public String getTokenString() {
        return getOperatorType().toString();
    }

    @Override
    public String getStringRepresentation() {
        return getClass().getSimpleName() + " [" + getTokenString() + "]";
    }
}
