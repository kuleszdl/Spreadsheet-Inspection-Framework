/**
 * 
 */
package sif.IO.spreadsheet.poi;

import org.apache.poi.ss.usermodel.Workbook;

import sif.model.inspection.SpreadsheetInventory;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.TestInput;

/**
 * 
 * 
 * @author Manuel Lemcke
 *
 */
public interface ISpreadsheetWriter {

	/**
	 * Writes {@link TestInput}s of a {@link DynamicPolicyRule} to a representation of the 
	 * spreadsheet which is stored internally.
	 * 
	 * @param rule The rule whose test inputs are written to the spreadsheet representation 
	 * @param request The request which contains further information like {@link Workbook}, 
	 * 		  {@link SpreadsheetInventory} etc.
	 * @throws Exception 
	 */
	public Workbook insertTestInput(DynamicPolicyRule rule, Object spreadsheet) 
			throws Exception;


}
