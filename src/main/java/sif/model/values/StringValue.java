package sif.model.values;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class StringValue extends AbstractValue {

    private final String stringValue;

    @AssistedInject
    private StringValue(@Assisted String value) {
        super(ValueType.STRING);
        stringValue = value;
    }

    @Override
    public String getValueString() {
        return stringValue;
    }

    @Override
    public Object getValueObject() {
        return stringValue;
    }

    @Override
    public boolean valueEquals(Value value) {
        if (value != null)
            return (value.getType() == ValueType.STRING) && getValueString().equals(value.getValueString());
        else
            return false;
    }
}
