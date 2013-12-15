package sif.IO.spreadsheet;

import java.io.File;

import org.apache.poi.ss.usermodel.Workbook;

import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.inspection.DynamicInspectionRequest;

/***
 * 
 * 
 * @author Sebastian Zitzelsberger, Manuel Lemcke
 * 
 */
public interface ISpreadsheetIO {

	/***
	 * Creates a spreadsheet instance from the given spreadsheet file.
	 * 
	 * @param spreadsheetFile
	 *            The given spreadsheet {@link File} from which to create a {@link Spreadsheet}
	 *            instance
	 * @return The {@link Spreadsheet} instance from the given spreadsheet {@link File}.
	 * @throws InvalidSpreadsheetFileException
	 *             Throws an exception if the spreadsheet file can't be
	 *             transformed to the internal model.
	 * @throws Exception 
	 */
	Spreadsheet createSpreadsheet(File spreadsheetFile)
			throws InvalidSpreadsheetFileException, Exception;
	
	/***
	 * Creates a spreadsheet instance from the given POI {@link Workbook} instance.
	 * 
	 * @param workbook
	 *            The given spreadsheet {@link Workbook} from which to create a {@link Spreadsheet}
	 *            instance
	 * @return The {@link Spreadsheet} instance from the given spreadsheet {@link File}.
	 * @throws InvalidSpreadsheetFileException
	 *             Throws an exception if the spreadsheet file can't be
	 *             transformed to the internal model.
	 * @throws Exception 
	 */
	Spreadsheet createSpreadsheet(Object workbook, String fileName)
			throws InvalidSpreadsheetFileException, Exception;
	
	/**
	 * 
	 * @param spreadsheetFile
	 *            The given spreadsheet {@link File} from which to create a {@link Spreadsheet}
	 *            instance
	 * @return The {@link Workbook} instance from the given spreadsheet file.
	 * @throws InvalidSpreadsheetFileException
	 *             Throws an exception if the spreadsheet file can't be
	 *             transformed to the internal model.
	 */
	Workbook createWorkbook(File spreadsheetFile) 
			throws InvalidSpreadsheetFileException;
	
	/***
	 * Creates a new {@link DynamicInspectionRequest} for the given spreadsheet file with the
	 * given request name which also stores the POI {@link Workbook} which is created during 
	 * creation of the {@link Spreadsheet}
	 * 
	 * @param requestName
	 *            The given name for the request.
	 * @param spreadsheetFile
	 *            The given spreadsheet file.
	 * @return The newly created inspection request.
	 * @throws Exception
	 *             Throws an exception if the given spreadsheet file is invalid.
	 */
	DynamicInspectionRequest<?> createInspectionRequest(String requestName,
			File spreadsheetFile) throws Exception;

}
