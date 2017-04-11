package sif.model;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import sif.model.tokens.Reference;
import sif.model.values.Value;
import sif.model.values.ValueFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a cell.
 */
public class Cell extends Element implements Referenceable, Safeable {

    private final CellAddress address;
    private final List<Reference> incomingReferences = new ArrayList<>();
    private final List<Reference> outgoingReferences = new ArrayList<>();
    private Formula formula = null;
    private boolean isDirty = false;
    private Value value;
    private Value backupValue = null;


    @AssistedInject
    Cell(ValueFactory valueFactory, @Assisted CellAddress address) {
        this.address = address;
        this.value = valueFactory.createNullValue();
    }

    /**
     * Checks if this cell contains a formula
     *
     * @return true if this cell contains a formula (and is a FormulaCell)
     */
    public boolean containsFormula() {
        return formula != null;
    }

    /**
     * Implements Referenceable.getIncomingReferences()
     *
     * @return returns a list of references pointing to this cell
     */
    @Override
    public List<Reference> getIncomingReferences() {
        return incomingReferences;
    }

    @Override
    public void addIncomingReference(Reference reference) {
        incomingReferences.add(reference);
    }

    /**
     * Implements Referenceable.getOutgoingReferences()
     *
     * @return returns a list of references pointing to other cells from this cell
     */
    @Override
    public List<Reference> getOutgoingReferences() {
        return outgoingReferences;
    }

    @Override
    public void addOutgoingReference(Reference reference) {
        outgoingReferences.add(reference);
    }

    /**
     * Implements Addressable.getAddress()
     *
     * @return returns Address of this cell
     */
    @Override
    public Address getAddress() {
        return address;
    }

    /**
     * Implements Addressable.getAddress()
     *
     * @return returns address of this cell in excel notation eg. "MyWorksheet!A25"
     */
    @Override
    public String getExcelNotation() {
        return getAddress().getExcelNotation();
    }

    @Override
    public String getSimpleNotation() {
        return getAddress().getSimpleNotation();
    }

    @Override
    public boolean isSingleCell() {
        return true;
    }

    /**
     * Implements Safeable.getKey()
     *
     * @return key as String
     */
    @Override
    public String getKey() {
        return address.getCellKey();
    }

    @Override
    public String getStringRepresentation() {
        return getClass().getSimpleName() + " [" + getKey() + "]";
    }

    public String toString() {
        return getKey() + ":" + getValue().getValueString();
    }

    //region getters & setters
    public Worksheet getWorksheet() {
        return address.getWorksheet();
    }

    public CellAddress getCellAddress() {
        return address;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Value getBackupValue() {
        return backupValue;
    }

    public void setBackupValue(Value backupValue) {
        this.backupValue = backupValue;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }
    //endregion
}