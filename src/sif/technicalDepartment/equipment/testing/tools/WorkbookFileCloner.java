/**
 * 
 */
package sif.technicalDepartment.equipment.testing.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author Manuel Lemcke
 *
 */
public class WorkbookFileCloner implements IWorkbookCloner {

	final String TEMP_FILE_NAME = "tempSheet";
	
	/* (non-Javadoc)
	 * @see sif.technicalDepartment.equipment.testing.tools.IWorkbookCloner#cloneWorkbook(org.apache.poi.ss.usermodel.Workbook)
	 */
	@Override
	public Workbook cloneWorkbook(Workbook workbook) { 
		Workbook clone = null;
		String fileExtension = null;
		String fileName = null;
		
		/*
		 * Create temporary filename
		 */
		int random = new java.util.Random().nextInt();
		
		if (workbook instanceof HSSFWorkbook) {
			fileExtension = ".xls";
		} else {
			fileExtension = ".xlsx";
		}
		
		fileName = TEMP_FILE_NAME + random + fileExtension;
		
		/*
		 * Write to that file
		 */
        try {
        	FileOutputStream fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        /*
         * Create a new Workbook instance from that file
         */
        try {
			clone = WorkbookFactory.create(new FileInputStream(fileName));
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        File delFile = new File(fileName);
        delFile.delete();
        
		return clone;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
