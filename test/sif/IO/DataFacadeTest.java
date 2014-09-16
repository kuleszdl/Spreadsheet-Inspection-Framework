package sif.IO;

import java.io.File;

import static junit.framework.Assert.*;

import org.junit.Test;

import sif.frontOffice.FrontDesk;
import sif.model.elements.basic.address.CellAddress;
import sif.model.elements.basic.address.RangeAddress;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.inspection.InspectionRequest;

public class DataFacadeTest {

	private static String filePath = "test/sif/testdata/input03_s1.xls";

	@Test
	public void testDataFacade() {

		try {

			File spreadsheetFile = new File(filePath);

			InspectionRequest inspection = FrontDesk.getInstance()
					.requestNewInspection("testInspection", spreadsheetFile);
			Worksheet sheet = inspection.getInventory().getSpreadsheet()
					.getWorksheetAt(1);
			Worksheet sheet2 = inspection.getInventory().getSpreadsheet()
					.getWorksheetAt(2);

			RangeAddress rangeAddress = new RangeAddress();
			rangeAddress.setWorksheet(sheet);
			rangeAddress.setLeftmostColumnIndex(3);
			rangeAddress.setTopmostRowIndex(3);
			rangeAddress.setRightmostColumnIndex(6);
			rangeAddress.setBottommostRowIndex(6);

			RangeAddress rangeAddress2 = new RangeAddress();
			rangeAddress2.setWorksheet(sheet2);
			rangeAddress2.setLeftmostColumnIndex(4);
			rangeAddress2.setTopmostRowIndex(4);
			rangeAddress2.setRightmostColumnIndex(7);
			rangeAddress2.setBottommostRowIndex(7);
			
			CellAddress cellAddress = new CellAddress();
			cellAddress.setWorksheet(sheet2);
			cellAddress.setColumnIndex(6);
			cellAddress.setRowIndex(6);

			CellAddress cellAddress2 = new CellAddress();
			cellAddress2.setWorksheet(sheet2);
			cellAddress2.setColumnIndex(1);
			cellAddress2.setRowIndex(3);

			CellAddress cellAddress3 = new CellAddress();
			cellAddress3.setWorksheet(sheet2);
			cellAddress3.setColumnIndex(8);
			cellAddress3.setRowIndex(8);


			assertEquals(1, rangeAddress.compareHorizontal(cellAddress));
			assertEquals(0, rangeAddress.compareVertical(cellAddress));

			assertEquals(0, rangeAddress2.compareHorizontal(cellAddress));
			assertEquals(0, rangeAddress2.compareVertical(cellAddress));

			assertEquals(1, rangeAddress.compareHorizontal(cellAddress2));
			assertEquals(0, rangeAddress.compareVertical(cellAddress2));

			assertEquals(-1, rangeAddress2.compareHorizontal(cellAddress2));
			assertEquals(1, rangeAddress2.compareVertical(cellAddress2));

			assertEquals(1, rangeAddress2.compareHorizontal(cellAddress3));
			assertEquals(-1, rangeAddress2.compareVertical(cellAddress3));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
