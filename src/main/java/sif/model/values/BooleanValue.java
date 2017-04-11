package sif.model.values;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class BooleanValue extends AbstractValue {

    private final Boolean booleanValue;

    @AssistedInject
    private BooleanValue(@Assisted Boolean value) {
        super(ValueType.BOOLEAN);
        booleanValue = value;
    }

    @Override
    public String getValueString() {
        return booleanValue.toString();
    }

    @Override
    public Object getValueObject() {
        return booleanValue;
    }

    @Override
    public boolean valueEquals(Value value) {
        if (value != null)
            return (value.getType() == ValueType.BOOLEAN) && booleanValue.equals(value.getValueObject());
        else
            return false;
    }
}
