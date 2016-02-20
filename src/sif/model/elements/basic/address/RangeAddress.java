package sif.model.elements.basic.address;

import sif.model.elements.basic.range.Range;

/***
 * The address of a {@link Range} within a spreadsheet.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class RangeAddress extends AbstractAddress {

	private Integer leftmostColumnIndex;
	private Integer rightmostColumnIndex;
	private Integer bottommostRowIndex;
	private Integer topmostRowIndex;

	public RangeAddress() {
	}

	/**
	 * Creates a range address from the given cell address.
	 * 
	 * @param cellAddress
	 *            The given cell address.
	 */
	public RangeAddress(CellAddress cellAddress) {
		setWorksheet(cellAddress.getWorksheet());
		setLeftmostColumnIndex(cellAddress.getColumnIndex());
		setTopmostRowIndex(cellAddress.getRowIndex());
		setRightmostColumnIndex(cellAddress.getColumnIndex());
		setBottommostRowIndex(cellAddress.getRowIndex());
	}

	@Override
	public int compareHorizontalIntrasheet(AbstractAddress address) {
		int comparision = 0;

		// Cell address.
		if (address instanceof CellAddress) {
			CellAddress cellAddress = (CellAddress) address;

			// Given cell address is left of this address.
			if (cellAddress.getColumnIndex() < this.leftmostColumnIndex) {
				comparision = -1;
			}
			// Given cell address is right of this address.
			if (cellAddress.getColumnIndex() > this.rightmostColumnIndex) {
				comparision = 1;
			}

			// Range address.
		} else {
			RangeAddress rangeAddress = (RangeAddress) address;
			// Given range address is left of this address.
			if (rangeAddress.rightmostColumnIndex < this.leftmostColumnIndex) {
				comparision = -1;
			}
			// Given cell address is right of this address.
			if (rangeAddress.leftmostColumnIndex > this.rightmostColumnIndex) {
				comparision = 1;
			}

		}

		// Given address is in a worksheet left of the worksheet of this
				// address - TODO: Move this code to a separate policy
		{

			// // Given address is in a worksheet left of the worksheet of this
			// // address.
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
			if (cellAddress.getRowIndex() > this.bottommostRowIndex) {
				comparision = -1;
			}
			// Given cell address is above this cell address.
			if (cellAddress.getRowIndex() < this.topmostRowIndex) {
				comparision = 1;
			}

			// Range address.
		} else {
			RangeAddress rangeAddress = (RangeAddress) address;
			// Given range address is below this cell address.
			if (rangeAddress.topmostRowIndex > this.bottommostRowIndex) {
				comparision = -1;
			}
			// Given cell address is above this cell address.
			if (rangeAddress.bottommostRowIndex < this.topmostRowIndex) {
				comparision = 1;
			}
		}
		return comparision;
	}

	@Override
	public String getAddress() {
		return getWorksheetAddress();
	}

	public Integer getBottommostRowIndex() {
		return this.bottommostRowIndex;
	}

	@Override
	public String getFullAddress() {
		return "[" + worksheet.getSpreadsheet().getName() + "]" + getSpreadsheetAddress();
	}

	public Integer getLeftmostColumnIndex() {
		return leftmostColumnIndex;
	}

	public Integer getRightmostColumnIndex() {
		return this.rightmostColumnIndex;
	}

	@Override
	public String getSpreadsheetAddress() {
		return worksheet.getName() + "!" + getWorksheetAddress();
	}

	public Integer getTopmostRowIndex() {
		return this.topmostRowIndex;
	}

	@Override
	public String getWorksheetAddress() {
		return AbstractAddress.getCharacterFor(leftmostColumnIndex) + topmostRowIndex.toString() + ":"
				+ AbstractAddress.getCharacterFor(rightmostColumnIndex) + bottommostRowIndex.toString();
	}

	@Override
	public Boolean hasIntersection(AbstractAddress address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isAdjacent(AbstractAddress address) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean isSingleCell() {
		return ((leftmostColumnIndex == rightmostColumnIndex) & (topmostRowIndex == bottommostRowIndex));
	}

	@Override
	public Boolean isWithin(AbstractAddress address) {
		Boolean isWithin = false;
		if (address instanceof CellAddress) {
			CellAddress cellAddress = (CellAddress) address;
			if (cellAddress.getWorksheet().equals(worksheet) & cellAddress.getColumnIndex() >= getLeftmostColumnIndex()
					& cellAddress.getColumnIndex() <= getRightmostColumnIndex()
					& cellAddress.getRowIndex() >= getTopmostRowIndex()
					& cellAddress.getRowIndex() <= getBottommostRowIndex()) {
				isWithin = true;
			}
		}
		if (address instanceof RangeAddress) {
			// TODO: Implement.
		}

		return isWithin;

	}

	public void setBottommostRowIndex(int bottommostRowIndex) {
		this.bottommostRowIndex = bottommostRowIndex;
	}

	public void setLeftmostColumnIndex(int leftmostColumnIndex) {
		this.leftmostColumnIndex = leftmostColumnIndex;
	}

	public void setRightmostColumnIndex(int rightmostColumnIndex) {
		this.rightmostColumnIndex = rightmostColumnIndex;
	}

	public void setTopmostRowIndex(int topmostRowIndex) {
		this.topmostRowIndex = topmostRowIndex;
	}

}
