package sif.model.values;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import java.text.DecimalFormat;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class NumericValue extends AbstractValue {

    @SuppressWarnings("FieldCanBeLocal")
    private static final double epsilon = 0.00000001;
    private final Double doubleValue;

    @AssistedInject
    private NumericValue(@Assisted Double value) {
        super(ValueType.NUMERIC);
        doubleValue = value;
    }

    @AssistedInject
    private NumericValue(@Assisted Integer value) {
        super(ValueType.NUMERIC);
        doubleValue = value.doubleValue();
    }

    @Override
    public String getValueString() {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        return format.format(doubleValue);
    }

    @Override
    public Object getValueObject() {
        return doubleValue;
    }

    @Override
    public boolean valueEquals(Value value) {
        if (value != null && value.getType() == ValueType.NUMERIC) {
            Double otherDouble = (Double) value.getValueObject();
            return abs(doubleValue - otherDouble) < (epsilon * max(abs(doubleValue), abs(otherDouble)));
        }

        return false;
    }
}
