package sif.model.inspection;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * A {@linkplain InspectionRequest} adds the possibility to execute {@link DynamicPolicyRule} instances
 * instead of only (static) {@link PolicyRule} instances {@link InspectionRequest}
 * 
 * @author Manuel Lemcke
 *
 */
public class DynamicInspectionRequest<T> extends InspectionRequest {
	private Class<?> sheetClass;
	
	private T spreadsheet;
	
	@SuppressWarnings("unused")
	private DynamicInspectionRequest() {
		
	}
	
	public DynamicInspectionRequest(Class<?> spreadsheetClass) {
		this.sheetClass = spreadsheetClass;
	}


	/**
	 * @return the poiWorkbook
	 */
	public T getExternalSpreadsheet() {
		return spreadsheet;
	}

	/**
	 * @param poiWorkbook the poiWorkbook to set
	 */
	public void setExternalSpreadsheet(T spreadsheet) {		
		if (sheetClass.isInstance(spreadsheet)) {
			this.spreadsheet = spreadsheet;			
		}
	}
	
}
