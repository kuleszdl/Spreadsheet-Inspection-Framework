package sif.IO;

import java.io.File;

import org.junit.Assert;

import sif.frontOffice.FrontDesk;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.inspection.InspectionRequest;
import sif.utilities.PrintUtilities;

public class PrintFirstWorksheet {

	private static String filePath = "test/sif/testdata/input03_s1.xls";

	public static void main(String[] args) {

		try {

			File spreadsheetFile = new File(filePath);

			InspectionRequest inspetion = FrontDesk.getInstance()
					.requestNewInspection("testInspection", spreadsheetFile);
			FrontDesk.getInstance().scan();
			Worksheet sheet = inspetion.getInventory().getSpreadsheet()
					.getWorksheetAt(1);
			PrintUtilities.print(sheet);

			System.out.println("----");
			System.out.println("----");
			for (Cell cell : inspetion.getInventory().getListFor(Cell.class)
					.getElements()) {
				if (cell.containsFormula()) {
					System.out.println(cell.getFormula().getValueAsString());
					System.out.println(cell.getFormula().isCalculating());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}
}