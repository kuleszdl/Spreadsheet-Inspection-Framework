/**
 * 
 */
package sif.model.inspection;

/**
 * A generic Wrapper for spreadsheet representations
 * 
 * @author Manuel Lemcke
 *
 */
public class ExternalSpreadsheetWrapper<T> {
	
	private T spreadsheet = null;

	@SuppressWarnings("unused")
	private ExternalSpreadsheetWrapper() {
		
	}
	
	public ExternalSpreadsheetWrapper(T spreadsheet) {
		this.spreadsheet = spreadsheet;
	}
	
	public T getSpreadsheet() {
		return spreadsheet;
	}
}
