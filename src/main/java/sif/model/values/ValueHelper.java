package sif.model.values;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ValueHelper {

    private final Logger logger = LoggerFactory.getLogger(ValueHelper.class);

    @Inject
    private ValueFactory valueFactory;

    public Value importValue(String stringValue, ValueType type) {
        switch (type) {
            case BOOLEAN:
                return valueFactory.createBooleanValue(parseBooleanValue(stringValue));
            case NUMERIC:
                return valueFactory.createNumericValue(parseNumericValue(stringValue));
            case STRING:
                return valueFactory.createStringValue(stringValue);
            default:
                // create null value
                return valueFactory.createNullValue();
        }
    }

    private boolean parseBooleanValue(String value) {
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException e2)  {
            return false;
        }
    }

    private double parseNumericValue(String value) {
        Locale theLocale = Locale.getDefault();
        NumberFormat numberFormat = DecimalFormat.getInstance(theLocale);
        Number theNumber;
        try {
            theNumber = numberFormat.parse(value);
            return theNumber.doubleValue();
        } catch (ParseException e) {
            logger.error("Can not parse {} as number. Do you use the right locale? Retrying with Double.valueOf()", value);
            try {
                return Double.valueOf(value);
            } catch (NumberFormatException e2)  {
                logger.error("Can not parse {} as number with Double.valueOf()", value);
                return 0.0;
            }
        }
    }
}
