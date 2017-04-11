package sif.model;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import sif.utility.Converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/***
 * Representation of a column.
 */
public class Column extends Element implements Safeable {

    private final Map<String, Cell> cells = new HashMap<>();
    private final String columnKey;

    @AssistedInject
    Column(@Assisted String columnKey) {
        this.columnKey = columnKey;
    }

    public void add(Cell cell) {
        cells.put(cell.getKey(), cell);
    }

    public Collection<Cell> getCells() {
        return cells.values();
    }

    @Override
    public String getKey() {
        return columnKey;
    }

    public int getIndex() {
        return Converter.columnToInteger(columnKey) - 1;
    }

    @Override
    public String getStringRepresentation() {
        return getClass().getSimpleName() + " [" + getKey() + "]";
    }
}