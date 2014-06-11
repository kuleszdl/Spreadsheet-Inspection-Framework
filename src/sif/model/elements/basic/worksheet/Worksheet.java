package sif.model.elements.basic.worksheet;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import sif.model.elements.BasicAbstractElement;
import sif.model.elements.basic.address.RangeAddress;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.range.Range;
import sif.model.elements.basic.spreadsheet.Spreadsheet;

/***
 * The representation of a worksheet.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class Worksheet extends BasicAbstractElement {

	private Integer index;
	private Spreadsheet spreadsheet;
	private String name;
	private Boolean isHidden;
	private LinkedList<Cell> cells;
	private TreeMap<Integer, Row> rows;
	private TreeMap<Integer, Column> columns;

	public Worksheet() {
		cells = new LinkedList<Cell>();
		rows = new TreeMap<Integer, Row>();
		columns = new TreeMap<Integer, Column>();
	}

	/**
	 * Adds the given cell to the worksheet. The cell must have a proper cell
	 * address.
	 * 
	 * @param cell
	 *            The given cell.
	 */
	public void add(Cell cell) {
		this.cells.add(cell);

		Row row = getRowAt(cell.getRowIndex());
		if (row == null) {
			row = new Row();
			row.setIndex(cell.getRowIndex());
			rows.put(row.getIndex(), row);
		}
		row.add(cell);

		Column column = getColumnAt(cell.getColumnIndex());
		if (column == null) {
			column = new Column();
			column.setWorksheet(this);
			column.setIndex(cell.getColumnIndex());
			columns.put(column.getIndex(), column);
		}
		column.add(cell);
	}

	/**
	 * Adds the given column to the worksheet.
	 * 
	 * @param column
	 *            The given column.
	 */
	public void add(Column column) {
		this.columns.put(column.getIndex(), column);
	}

	/**
	 * Adds the given row to the worksheet.
	 * 
	 * @param row
	 *            The given worksheet.
	 */
	public void add(Row row) {
		this.rows.put(row.getIndex(), row);
	}

	/**
	 * Creates a range form the given range address.
	 * 
	 * @param address
	 *            The given address.
	 * @return A range with all cells in the given range.
	 */
	public Range createRangeFor(RangeAddress address) {
		Range range = new Range(address);

		for (int row = address.getTopmostRowIndex(); row <= address
				.getBottommostRowIndex(); row++) {
			for (int column = address.getLeftmostColumnIndex(); column <= address
					.getRightmostColumnIndex(); column++) {
				range.add(getCellAt(column, row));
			}
		}
		return range;
	}

	/**
	 * Gets the cell at the location specified by the given column and row
	 * index.
	 * 
	 * @param columnIndex
	 *            The given column index.
	 * @param rowIndex
	 *            The given row index.
	 * @return The cell at the given location.
	 */
	public Cell getCellAt(Integer columnIndex, Integer rowIndex) {
		Cell cell = null;
		Row row = getRowAt(rowIndex);
		if (row != null) {
			cell = row.getCellAt(columnIndex);
		}

		return cell;
	}

	public Iterator<Cell> getCellIterator() {
		return cells.iterator();
	}

	public Column getColumnAt(Integer columnIndex) {
		Column column = null;
		if (columns.containsKey(columnIndex)) {
			column = columns.get(columnIndex);
		}

		return column;
	}

	public Iterator<Column> getColumnIterator() {
		return columns.values().iterator();
	}

	public Collection<Column> getColumns() {
		return this.columns.values();
	}

	public String getDescription() {
		return null;
	}

	public Integer getHighestColumnIndex() {
		return this.columns.lastEntry().getKey();
	}

	public Integer getHighestRowIndex() {
		return this.rows.lastEntry().getKey();
	}

	public Integer getIndex() {
		return index;
	}

	@Override
	public String getLocation() {
		return spreadsheet.getName() + "[" + index + "]";
	}

	public String getName() {
		return this.name;
	}

	public Integer getNumberOfCells() {
		return this.cells.size();
	}

	public Integer getNumberOfColumns() {
		return this.columns.size();
	}

	public Integer getNumberOfRows() {
		return Integer.valueOf(this.rows.size());
	}

	public String getPluralName() {
		return null;
	}

	public Row getRowAt(Integer rowIndex) {
		Row row = null;
		if (rows.containsKey(rowIndex)) {
			row = this.rows.get(rowIndex);
		}

		return row;
	}

	public Iterator<Row> getRowIterator() {
		return rows.values().iterator();
	}

	public Collection<Row> getRows() {
		return this.rows.values();
	}

	public Spreadsheet getSpreadsheet() {
		return this.spreadsheet;
	}

	@Override
	public String getStringRepresentation() {
		return "Worksheet (" + index + ") " + name;

	}

	@Override
	public String getValueAsString() {
		StringBuilder builder = new StringBuilder();
		for (Row row : rows.values()) {
			builder.append(row.getStringRepresentation());
			builder.append("\n");
		}
		return builder.toString();
	}

	public Boolean isHidden() {
		return this.isHidden;
	}

	public void setHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSpreadsheet(Spreadsheet spreadsheet) {
		this.spreadsheet = spreadsheet;
	}

}