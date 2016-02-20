package sif.model.elements.basic.address;

import sif.model.elements.basic.cell.Cell;

/***
 * The address of a {@link Cell} within a spreadsheet.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class CellAddress extends AbstractAddress {

	private Integer columnIndex;
	private Integer rowIndex;

	@Override
	public int compareHorizontalIntrasheet(AbstractAddress address) {
		int comparision = 0;

		// Cell address.
		if (address instanceof CellAddress) {
			CellAddress cellAddress = (CellAddress) address;

			// Given cell address is left of this cell address.
			if (cellAddress.getColumnIndex() < this.columnIndex) {
				comparision = -1;
			}
			// Given cell address is right of this cell address.
			if (cellAddress.getColumnIndex() > this.columnIndex) {
				comparision = 1;
			}

			// Range address.
		} else {
			RangeAddress rangeAddress = (RangeAddress) address;
			// Given range address is left of this cell address.
			if (rangeAddress.getRightmostColumnIndex() < this.columnIndex) {
				comparision = -1;
			}
			// // Given range address is right of this cell address.
			if (rangeAddress.getLeftmostColumnIndex() > this.columnIndex) {
				comparision = 1;
			}

		}

		// Given address is in a worksheet left of the worksheet of this
		// address - TODO: Move this code to a separate policy
		{
			// if (address.getWorksheet().getIndex() <
			// this.worksheet.getIndex()) {
			// comparision = -1;
			// }
			// // Given address is in a worksheet right of the worksheet of this
			// // address.
			// if (address.getWorksheet().getIndex() >
			// this.worksheet.getIndex()) {
			// comparision = 1;
			// }
		}
		

		return comparision;
	}

	@Override
	public int compareVertical(AbstractAddress address) {
		int comparision = 0;
		// Cell address.
		if (address instanceof CellAddress) {
			CellAddress cellAddress = (CellAddress) address;

			// Given cell address is below this cell address.
			if (cellAddress.getRowIndex() > this.rowIndex) {
				comparision = -1;
			}
			// Given cell address is above this cell address.
			if (cellAddress.getRowIndex() < this.rowIndex) {
				comparision = 1;
			}

			// Range address.
		} else {
			RangeAddress rangeAddress = (RangeAddress) address;
			// Given range address is below this cell address.
			if (rangeAddress.getTopmostRowIndex() > this.rowIndex) {
				comparision = -1;
			}
			// Given range address is above this cell address.
			if (rangeAddress.getBottommostRowIndex() < this.rowIndex) {
				comparision = 1;
			}

		}

		return comparision;
	}

	@Override
	public String getAddress() {
		return getSpreadsheetAddress();
	}

	public Integer getColumnIndex() {
		return columnIndex;
	}

	@Override
	public String getFullAddress() {
		return "[" + worksheet.getSpreadsheet().getName() + "]" + getSpreadsheetAddress();
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	@Override
	public String getSpreadsheetAddress() {
		return worksheet.getName() + "!" + getWorksheetAddress();
	}

	@Override
	public String getWorksheetAddress() {
		return AbstractAddress.getCharacterFor(columnIndex) + rowIndex.toString();
	}

	@Override
	public Boolean hasIntersection(AbstractAddress address) {
		int h = compareHorizontalIntrasheet(address);
		int v = compareVertical(address);
		// Vertical difference doesn't check for the worksheet
		v = address.worksheet.equals(worksheet) ? v : 1;
		return h == 0 || v == 0;
	}

	@Override
	public Boolean isAdjacent(AbstractAddress address) {
		Boolean isAdjacent = false;
		if (address instanceof CellAddress) {
			CellAddress otherAdress = (CellAddress) address;
			if ((Math.abs(this.columnIndex - otherAdress.getColumnIndex()) <= 1)
					& (Math.abs(this.rowIndex - otherAdress.getRowIndex()) <= 1)) {
				isAdjacent = true;
			}
		}

		return isAdjacent;
	}

	@Override
	public Boolean isWithin(AbstractAddress address) {
		int h = compareHorizontalIntrasheet(address);
		int v = compareVertical(address);
		return h == 0 && v == 0;
	}

	public void setColumnIndex(Integer columnIndex) {
		this.columnIndex = columnIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}
}
