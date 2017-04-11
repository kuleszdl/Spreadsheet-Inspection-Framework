package sif.model.values;

public interface Value {
    String getValueString();

    Object getValueObject();

    ValueType getType();

    boolean valueEquals(Value value);
}
