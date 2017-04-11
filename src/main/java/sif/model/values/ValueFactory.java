package sif.model.values;

import com.google.inject.assistedinject.Assisted;

public interface ValueFactory {

    NullValue createNullValue();

    BooleanValue createBooleanValue(@Assisted Boolean value);

    ErrorValue createErrorValue(@Assisted String message, @Assisted Integer id);

    NumericValue createNumericValue(@Assisted Integer value);

    NumericValue createNumericValue(@Assisted Double value);

    StringValue createStringValue(@Assisted String value);
}
