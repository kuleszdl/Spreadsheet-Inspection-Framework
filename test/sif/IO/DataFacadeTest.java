package sif.IO;

import java.io.File;

import sif.frontOffice.FrontDesk;
import sif.model.elements.basic.address.CellAddress;
import sif.model.elements.basic.address.RangeAddress;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.inspection.InspectionRequest;

public class DataFacadeTest {

	private static String filePath = "test/sif/testdata/input03_s1.xls";

	public static void main(String[] args) {

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

			CellAddress cellAddress = new CellAddress();
			cellAddress.setWorksheet(sheet2);
			cellAddress.setColumnIndex(6);
			cellAddress.setRowIndex(6);

			RangeAddress rangeAddress2 = new RangeAddress();
			rangeAddress2.setWorksheet(sheet);
			rangeAddress2.setLeftmostColumnIndex(7);
			rangeAddress2.setTopmostRowIndex(7);
			rangeAddress2.setRightmostColumnIndex(7);
			rangeAddress2.setBottommostRowIndex(7);

			System.out.println("Horizontal");
			System.out.println(rangeAddress.compareHorizontal(cellAddress));
			System.out.println("Vertical");
			System.out.println(rangeAddress.compareVertical(cellAddress));
			// PrintUtilities.printCellsFor(sheet);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
