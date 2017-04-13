package sif.model;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/***
 * The representation of a worksheet
 */
public class Worksheet extends Element implements Safeable {

    private final Map<String, Cell> cells = new HashMap<>();
    private final Map<String, Row> rows = new HashMap<>();
    private final Map<String, Column> columns = new HashMap<>();
    private Spreadsheet spreadsheet;
    private final String worksheetKey;
    private final int worksheetIndex;
    private boolean isHidden;

    @Inject
    private ElementFactory elementFactory;
    @Inject
    private AddressFactory addressFactory;

    @Inject
    Worksheet(@Assisted String worksheetKey, @Assisted Integer index) {
        this.worksheetKey = worksheetKey;
        this.worksheetIndex = index;
    }

    /**
     * Adds the given cell to the spreadsheet. The cell must have a proper cell
     * address.
     *
     * @param cell The given cell.
     */
    private void addCell(Cell cell) {
        cells.put(cell.getKey(), cell);

        Row row;
        String rowKey = cell.getCellAddress().getRowKey();
        if (rows.containsKey(rowKey)) {
            row = rows.get(rowKey);
        } else {
            row = elementFactory.createRow(rowKey);
            rows.put(rowKey, row);
        }

        cell.getCellAddress().setRow(row);
        row.add(cell);

        Column column;
        String columnKey = cell.getCellAddress().getColumnKey();
        if (columns.containsKey(columnKey)) {
            column = columns.get(columnKey);
        } else {
            column = elementFactory.createColumn(columnKey);
            columns.put(columnKey, column);
        }

        cell.getCellAddress().setColumn(column);
        column.add(cell);
    }

    @Override
    public String toString() {
        return "Worksheet [" + worksheetKey + "]";

    }

    public int getIndex() {
        return worksheetIndex;
    }

    @Override
    public String getKey() {
        return worksheetKey;
    }

    @Override
    public String getStringRepresentation() {
        return getClass().getSimpleName() + " [" + getKey() + "]";
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Spreadsheet getSpreadsheet() {
        return spreadsheet;
    }

    public void setSpreadsheet(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
    }


    public Collection<Cell> getCells() {
        return cells.values();
    }

    public Cell getCell(String cellKey) {
        Cell cell = cells.get(cellKey);
        if (cell == null) {
            CellAddress cellAddress = addressFactory.createCellAddress(this, cellKey);
            cell = elementFactory.createCell(cellAddress);
            // add cell to worksheet
            addCell(cell);
            // add cell to spreadsheet
            spreadsheet.add(cell);
        }
        return cell;
    }

    public Collection<Row> getRows() {
        return rows.values();
    }

    public Row getRow(String rowKey) {
        return rows.computeIfAbsent(rowKey, k -> elementFactory.createRow(rowKey));
    }

    public Collection<Column> getColumns() {
        return columns.values();
    }

    public Column getColumn(String columnKey) {
        return columns.computeIfAbsent(columnKey, k -> elementFactory.createColumn(columnKey));
    }
}