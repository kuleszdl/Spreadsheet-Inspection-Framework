package sif.IO.spreadsheet.poi;

import org.apache.poi.ss.formula.FormulaParseException;

public class FormulaConversionException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1303885645254053302L;

	private static final String CELL_INFORMATION = "ParseException in Cell (";

	FormulaParseException innerException;
	Integer RowIndex = null;
	Integer ColumnIndex = null;
	
	FormulaConversionException(FormulaParseException parseException) {
		this.innerException = parseException;
	}
	
	FormulaConversionException(FormulaParseException parseException, Integer RowIndex, Integer ColumnIndex) {
		this.innerException = parseException;
	}
	
	public String getMessage() {
		String newMessage;
		if (RowIndex != null && ColumnIndex != null) {
			newMessage = CELL_INFORMATION + RowIndex + "," + ColumnIndex + super.getMessage();
		}
		else {
			newMessage = super.getMessage();
		}
		return newMessage;
	}
}
