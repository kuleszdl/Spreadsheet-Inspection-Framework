package sif.model.values;

import com.google.inject.assistedinject.AssistedInject;

public class NullValue extends AbstractValue {

    @AssistedInject
    private NullValue() {
        super(ValueType.BLANK);
    }

    @Override
    public String getValueString() {
        return "[null]";
    }

    @Override
    public Object getValueObject() {
        return null;
    }

    @Override
    public boolean valueEquals(Value value) {
        if (value != null)
            return value.getType() == ValueType.BLANK;
        else
            return false;
    }
}
