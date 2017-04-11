package sif.model.values;

public abstract class AbstractValue implements Value {

    private final ValueType valueType;

    AbstractValue(ValueType valueType) {
        this.valueType = valueType;
    }

    @Override
    public ValueType getType() {
        return valueType;
    }

    @Override
    public String toString() {
        return getType() + ": " + getValueString();
    }
}
