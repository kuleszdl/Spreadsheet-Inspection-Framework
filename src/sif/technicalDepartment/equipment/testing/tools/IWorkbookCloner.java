/**
 * 
 */
package sif.technicalDepartment.equipment.testing.tools;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Manuel Lemcke
 *
 */
public interface IWorkbookCloner {
	
	/**
	 * 
	 * @param workbook
	 * @return
	 */
	public Workbook cloneWorkbook(Workbook workbook);

}
