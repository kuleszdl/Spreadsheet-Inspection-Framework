package sif.IO.spreadsheet;

import java.util.ArrayList;

/***
 * Indicates that a spreadsheet instance could not be created from a spreadsheet
 * file, because the file was invalid.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class InvalidSpreadsheetFileException extends Exception {

	private static final long serialVersionUID = -4386440206999821735L;
	private ArrayList<Throwable> suppressed;
	public InvalidSpreadsheetFileException(String message) {
		super(message);
		suppressed = new ArrayList<Throwable>();
	}

	public void addSuppressed(Throwable t){
		if (t != null){
			suppressed.add(t);
		}
	}
	
	public ArrayList<Throwable> getSuppressed(){
		return suppressed;
	}
}
