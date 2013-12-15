package sif.model.elements.basic.address;

import sif.model.elements.basic.worksheet.Worksheet;

/***
 * An abstract address represents the location of a spreadsheet element within a
 * spreadsheet.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public abstract class AbstractAddress {

	
	/**
	 * Calculates the columnindex from a A1 notion string
	 * 
	 * @param character
	 * @return
	 */
	public static int getColumnIndexFor(char character) {
		return Character.getNumericValue(character)
				- Character.getNumericValue('A');
	}

	/**
	 * Converts the given column index to its responding character.
	 * 
	 * @param columnIndex
	 *            the given column index.
	 * @return The given column index as a character.
	 */
	public static String getCharacterFor(Integer columnIndex) {
		String converted = "";
		// Repeatedly divide the number by 26 and convert the
		// remainder into the appropriate letter.
		do {
			int remainder = (columnIndex % 26);
			if (remainder != 0) {
				remainder = remainder - 1;
			} else {
				remainder = 25;
			}
			converted = (char) (remainder + 'A') + converted;
			columnIndex = (columnIndex - remainder) / 26;
		} while (columnIndex > 0);

		return converted;
	}

	protected Worksheet worksheet;

	/**
	 * Compares this address with the given address horizontally.
	 * 
	 * @param address
	 *            The given address.
	 * @return Returns -1 if the given address is left of this address, 0 if the
	 *         addresses overlap and 1 if the given address is right of this
	 *         address.
	 */
	public abstract int compareHorizontal(AbstractAddress address);

	/**
	 * Compares this address with the given address vertically.
	 * 
	 * @param address
	 *            The given address.
	 * @return Returns -1 if the given address is below this address, 0 if the
	 *         addresses overlap and 1 if the given address is above this
	 *         address.
	 */
	public abstract int compareVertical(AbstractAddress address);

	/***
	 * Gets the String representation of this address when referenced from the
	 * same worksheet.
	 * 
	 * @return The string representation of this address.
	 */
	public abstract String getAddress();

	/**
	 * Gets the String representation of this address when referenced from an
	 * external spreadsheet.
	 * 
	 * @return The string representation of this address.
	 */
	public abstract String getFullAddress();

	/**
	 * Gets the String representation of this address when referenced within a
	 * spreadsheet but from a different worksheet.
	 * 
	 * @return The string representation of this address.
	 */
	public abstract String getSpreadsheetAddress();

	/**
	 * Gets the worksheet of the address.
	 * 
	 * @return The worksheet.
	 */
	public Worksheet getWorksheet() {
		return this.worksheet;
	}

	/**
	 * Gets the String representation of this address when referenced within a
	 * worksheet.
	 * 
	 * @return The string representation of this address.
	 */
	public abstract String getWorksheetAddress();

	/**
	 * Checks whether this address physically overlaps with the given address
	 * within a worksheet.
	 * 
	 * @param address
	 *            the given address.
	 * @return Whether the given address overlaps with this address.
	 */
	public abstract Boolean hasIntersection(AbstractAddress address);

	/**
	 * Checks whether this address is physically adjacent to the given address
	 * within a worksheet.
	 * 
	 * @param address
	 *            The given address.
	 * @return Whether the given address is adjacent with this address.
	 */
	public abstract Boolean isAdjacent(AbstractAddress address);

	/**
	 * Checks if this address physically contains given address within a
	 * worksheet.
	 * 
	 * @param address
	 *            The given address.
	 * @return Whether the given address is contained within this address.
	 */
	public abstract Boolean isWithin(AbstractAddress address);

	/**
	 * Sets the given worksheet as the worksheet of the address.
	 * 
	 * @param worksheet
	 *            The given worksheet.
	 */
	public void setWorksheet(Worksheet worksheet) {
		this.worksheet = worksheet;
	}

}
