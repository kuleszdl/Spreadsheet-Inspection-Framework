package sif.model.elements.basic.worksheet;

import java.util.Iterator;
import java.util.TreeMap;

import sif.model.elements.BasicAbstractElement;
import sif.model.elements.basic.address.AbstractAddress;
import sif.model.elements.basic.cell.Cell;

/***
 * Representation of a column.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class Column extends BasicAbstractElement {
	Integer index;
	TreeMap<Integer, Cell> cells;
	private Worksheet worksheet;

	public Column() {
		cells = new TreeMap<Integer, Cell>();
	}

	/**
	 * Adds the given cell to the column.
	 * 
	 * @param cell
	 *            The given cell.
	 */
	public void add(Cell cell) {
		this.cells.put(cell.getRowIndex(), cell);
	}

	/**
	 * Gets the cell at the given index. May return null.
	 * 
	 * @param rowIndex
	 *            The given index.
	 * @return The cell at the given index.
	 */
	public Cell getCellAt(Integer rowIndex) {
		Cell cell = null;
		if (cells.containsKey(rowIndex)) {
			cell = cells.get(rowIndex);
		}
		return cell;
	}

	public Iterator<Cell> getCellIterator() {
		return cells.values().iterator();
	}

	public Integer getHighestRowIndex() {
		return (Integer) this.cells.lastKey();
	}

	public Integer getIndex() {
		return this.index;
	}

	@Override
	public String getLocation() {
		return null;
	}

	public Integer getLowestCellIndex() {
		return (Integer) this.cells.firstKey();
	}

	public Integer getNumberOfCells() {
		return Integer.valueOf(this.cells.size());
	}

	@Override
	public String getStringRepresentation() {
		return "Column " + AbstractAddress.getCharacterFor(index);
	}

	@Override
	public String getValueAsString() {
		StringBuilder builder = new StringBuilder();

		for (Cell cell : cells.values()) {
			builder.append(cell.getStringRepresentation());
			builder.append("  ");
		}
		return builder.toString();
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Worksheet getWorksheet() {
		return worksheet;
	}

	public void setWorksheet(Worksheet worksheet) {
		this.worksheet = worksheet;
	}

}