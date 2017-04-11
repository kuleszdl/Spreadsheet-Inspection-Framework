package sif.model;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The representation of a row.
 */
public class Row extends Element implements Safeable {

    private final Map<String, Cell> cells = new HashMap<>();
    private final String rowKey;

    @AssistedInject
    Row(@Assisted String rowKey) {
        this.rowKey = rowKey;
    }

    public void add(Cell cell) {
        cells.put(cell.getKey(), cell);
    }

    public Collection<Cell> getCells() {
        return cells.values();
    }

    @Override
    public String toString() {
        return "Row [" + rowKey + "]";
    }

    @Override
    public String getKey() {
        return rowKey;
    }

    public int getIndex() {
        return Integer.parseInt(rowKey) - 1;
    }

    @Override
    public String getStringRepresentation() {
        return getClass().getSimpleName() + " [" + getKey() + "]";
    }
}