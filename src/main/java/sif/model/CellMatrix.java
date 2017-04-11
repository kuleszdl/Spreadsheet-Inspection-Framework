package sif.model;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import sif.model.tokens.Reference;

import java.util.ArrayList;
import java.util.List;

public class CellMatrix extends Element implements Referenceable {

    private List<Cell> cells = null;
    private List<Column> columns = null;
    private List<Row> rows = null;
    private final CellMatrixAddress address;

    @AssistedInject
    CellMatrix(@Assisted CellMatrixAddress address) {
        this.address = address;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public String getExcelNotation() {
        return address.getExcelNotation();
    }

    @Override
    public String getSimpleNotation() {
        return address.getSimpleNotation();
    }

    @Override
    public boolean isSingleCell() {
        return false;
    }

    @Override
    public List<Reference> getIncomingReferences() {
        List<Reference> list = new ArrayList<>();
        for (Cell cell : getCells()) {
            for (Reference ref : cell.getIncomingReferences()) {
                list.add(ref);
            }
        }
        return list;
    }

    @Override
    public void addIncomingReference(Reference reference) {
        // add incoming reference to all cells
        for (Cell cell : getCells()) {
            cell.addIncomingReference(reference);
        }
    }

    @Override
    public List<Reference> getOutgoingReferences() {
        List<Reference> list = new ArrayList<>();
        for (Cell cell : getCells()) {
            for (Reference ref : cell.getOutgoingReferences()) {
                list.add(ref);
            }
        }
        return list;
    }

    @Override
    public void addOutgoingReference(Reference reference) {
        for (Cell cell : getCells()) {
            cell.addOutgoingReference(reference);
        }
    }

    @Override
    public Worksheet getWorksheet() {
        return address.getWorksheet();
    }

    @Override
    public String getStringRepresentation() {
        return "CellMatrix [" + address + "]";
    }

    public List<Cell> getCells() {
        if (cells == null) {
            cells = new ArrayList<>();
            for (String cellKey : address.getCellKeys()) {
                Cell cell = getWorksheet().getCell(cellKey);
                cells.add(cell);
            }
        }
        return cells;
    }

    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
            for (String columnKey : address.getColumnKeys()) {
                Column column = getWorksheet().getColumn(columnKey);
                columns.add(column);
            }
        }
        return columns;
    }

    public List<Row> getRows() {
        if (rows == null) {
            rows = new ArrayList<>();
            for (String rowKey : address.getRowKeys()) {
                Row row = getWorksheet().getRow(rowKey);
                rows.add(row);
            }
        }
        return rows;
    }
}