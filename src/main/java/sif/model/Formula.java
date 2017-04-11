package sif.model;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Representation of a formula.
 */
public class Formula extends TokenContainer {

    private String formulaString;

    @AssistedInject
    public Formula(@Assisted Cell cell) {
        super(cell);
    }

    @Override
    public String getStringRepresentation() {
        return getClass().getSimpleName() + " [" + formulaString + "]";
    }

    public String getFormulaString() {
        return formulaString;
    }

    public void setFormulaString(String formulaString) {
        this.formulaString = "=" + formulaString;
    }

    @Override
    public String toString() {
        return "Formula: " + formulaString;
    }
}