package sif.evaluation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import sif.IO.ReportFormat;
import sif.frontOffice.FrontDesk;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.inspection.InspectionRequest;
import sif.model.policy.Policy;
import sif.model.violations.Findings;
import sif.model.violations.lists.ViolationList;

public class Evaluation extends Task {
	private String filePathBase = "test/sif";
	private FileWriter outFile;

	public String getFilePathBase() {
		return filePathBase;
	}

	public void setFilePathBase(String filePathBase) {
		this.filePathBase = filePathBase;
	}

	private PrintWriter out;

	private void writeSingleFindingNumbers(Findings findings) {
		out.write(",");
		for (ViolationList list : findings.getViolationLists()) {
			out.write(list.getNumberOfSingleViolations() + ",");
		}
	}

	private void writeGroupFindingNumbers(Findings findings) {
		out.write(",");
		for (ViolationList list : findings.getViolationLists()) {
			out.write(list.getNumberOfViolations() + ",");
		}
	}

	public void execute() throws BuildException {
		File folder = new File(filePathBase);
		try {
			outFile = new FileWriter(filePathBase + "results.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		out = new PrintWriter(outFile);

		out.write("spreadsheet, number of worksheets, number of cells, number of formulas, "
				+ "single violations, formula complexity, constants, reading direction, "
				+ "group violations, formula complexity, constants, reading direction");

		for (File spreadsheet : folder.listFiles()) {
			if (spreadsheet.getName().endsWith(".xls")
					| spreadsheet.getName().endsWith(".xlsx")) {

				FrontDesk frontDesk = FrontDesk.getInstance();
				InspectionRequest inspection;
				try {
					inspection = FrontDesk.getInstance().requestNewInspection(
							spreadsheet.getName(), spreadsheet);

					frontDesk.scan();
					Policy policy = FrontDesk.getInstance()
							.getAvailablePolicies().get("Basic Policy");

					frontDesk.setPolicy(policy);
					frontDesk.run();

					frontDesk.createInspectionReport(filePathBase + "/reports", ReportFormat.HTML);
					out.println();
					out.write(spreadsheet.getName() + ",");
					out.write(inspection.getInventory().getSpreadsheet()
							.getWorksheets().size()
							+ ",");

					out.write(inspection.getInventory().getListFor(Cell.class)
							.getNumberOfElements()
							+ ",");
					out.write(inspection.getInventory()
							.getListFor(Formula.class).getNumberOfElements()
							+ ",");

					writeSingleFindingNumbers(inspection.getFindings());
					writeGroupFindingNumbers(inspection.getFindings());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		out.close();

	}

	public static void main(String[] args) throws Exception {
		Evaluation evaluator = new Evaluation();
		// if (args.length > 0) {
		// evaluator.filePathBase = args[0];
		// }
		evaluator.execute();
	}
}
