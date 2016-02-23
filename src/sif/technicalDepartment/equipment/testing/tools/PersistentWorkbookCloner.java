package sif.technicalDepartment.equipment.testing.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.xmlbeans.impl.values.XmlValueDisconnectedException;

/**
 * Provides a means to clone XSSF workbooks repeatedly for multiple dynamic inspections.
 * @author Wolfgang Kraus
 */
public class PersistentWorkbookCloner implements IWorkbookCloner{

	private static File workbookFile = null;

	/**
	 * Creates a new temporary File to store the workbook in
	 * @param workbook to clone
	 * @return the cloned workbook
	 */
	private synchronized Workbook cloneIntoNewFile(Workbook workbook){
		File currentFile = null;

		try {
			currentFile = File.createTempFile(workbook.getSheetName(0), "");
			currentFile.deleteOnExit();
			FileOutputStream fileOut = new FileOutputStream(currentFile);
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cloneFromFile(currentFile);
	}

	/**
	 * Creates a workbook from the given file
	 * @param currentFile to read
	 * @return the workbook
	 */
	private synchronized Workbook cloneFromFile(File currentFile){
		Workbook clone = null;

		try {
			clone = WorkbookFactory.create(new FileInputStream(currentFile));
			workbookFile = currentFile;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return clone;
	}

	@Override
	public synchronized Workbook cloneWorkbook(Workbook workbook) {
		Workbook clone = null;
		try {
			clone = cloneIntoNewFile(workbook);
		} catch (XmlValueDisconnectedException ex) {
			/*
			 * Use the old file again, due to 
			 * https://issues.apache.org/bugzilla/show_bug.cgi?id=49940
			 */
			clone = cloneFromFile(workbookFile);
		}

		return clone;
	}

}
