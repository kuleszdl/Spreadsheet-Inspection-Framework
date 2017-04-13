package sif.model.tokens;

import com.google.inject.assistedinject.AssistedInject;
import sif.model.values.Value;
import sif.model.values.ValueFactory;

/**
 * Representation of a scalar constant.
 */
public class ScalarConstant extends UnspecifiedToken {

    private Value value;

    @AssistedInject
    public ScalarConstant(ValueFactory valueFactory) {
        super(TokenType.SCALAR_CONSTANT);
        value = valueFactory.createNullValue();
    }

    public String getTokenString() {
        return getValue().getValueString();
    }

    public String getStringRepresentation() {
        return getClass().getSimpleName() + " [" + getTokenString() + "]";
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}