package sif.testcenter.dynamic_testing;

import com.google.inject.Inject;
import sif.model.Cell;
import sif.model.values.Value;
import sif.model.values.ValueHelper;
import sif.model.values.ValueType;

public class ConditionChecker {

    @Inject
    private ValueHelper valueHelper;

    public boolean isFulfilled(Condition condition, Cell cell) {
        Value value = valueHelper.importValue(condition.getValue(), condition.getType());

        if (value.getType() != cell.getValue().getType()) {
            return false;
        }

        if (condition.getType() == ValueType.NUMERIC) {
            switch (condition.getOperator()) {
                case GREATER_OR_EQUAL:
                    return checkNumericGreaterOr(value, cell);
                case GREATER:
                    return checkNumericGreater(value, cell);
                case LESS:
                    return checkNumericLess(value, cell);
                case LESS_OR_EQUAL:
                    return checkNumericLessOr(value, cell);
                default:
                    return checkCommonEquals(value, cell);
            }
        } else {
            return checkCommonEquals(value, cell);
        }
    }

    private boolean checkNumericLessOr(Value value, Cell cell) {
        double a = (double) cell.getValue().getValueObject();
        double b = (double) value.getValueObject();
        return a <= b;
    }

    private boolean checkNumericLess(Value value, Cell cell) {
        double a = (double) cell.getValue().getValueObject();
        double b = (double) value.getValueObject();
        return a < b;
    }

    private boolean checkNumericGreater(Value value, Cell cell) {
        double a = (double) cell.getValue().getValueObject();
        double b = (double) value.getValueObject();
        return a > b;
    }

    private boolean checkNumericGreaterOr(Value value, Cell cell) {
        double a = (double) cell.getValue().getValueObject();
        double b = (double) value.getValueObject();
        return a >= b;
    }

    private boolean checkCommonEquals(Value value, Cell cell) {
        return value.valueEquals(cell.getValue());
    }
}
