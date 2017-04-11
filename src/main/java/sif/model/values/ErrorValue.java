package sif.model.values;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class ErrorValue extends AbstractValue {

    private final String errorMessage;
    private final int errorId;

    @AssistedInject
    private ErrorValue(@Assisted String errorMessage, @Assisted Integer errorId) {
        super(ValueType.ERROR);
        this.errorMessage = errorMessage;
        this.errorId = errorId;
    }

    @Override
    public String getValueString() {
        return errorMessage + " with Error ID " + errorId;
    }

    @Override
    public Object getValueObject() {
        return errorMessage;
    }

    @Override
    public boolean valueEquals(Value value) {
        if (value != null)
            return (value.getType() == ValueType.ERROR) && getValueString().equals(value.getValueString());
        else
            return false;
    }
}
