package sif.inspection;

import java.io.File;

import sif.frontOffice.FrontDesk;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.inspection.InspectionRequest;
import sif.model.policy.Policy;
import sif.model.policy.policyrule.AbstractPolicyRule;

public class SamplesTest {

	private static String filePathBase = "C:/Dokumente und Einstellungen/Administrator/Eigene Dateien/samples/spreadsheet_sample_";

	public static void main(String[] args) {
		for (int i = 1; i < 13; i++) {
			try {

				String filePath = filePathBase;
				if (i < 10) {
					filePath = filePath + "0";
				}
				if (i < 8) {
					filePath = filePath + i + ".xls";
				} else {
					filePath = filePath + i + ".xlsx";
				}

				System.out.println();
				System.out.println(filePath);
				File spreadsheetFile = new File(filePath);

				FrontDesk frontDesk = FrontDesk.getInstance();

				InspectionRequest inspection = FrontDesk.getInstance()
						.requestNewInspection(spreadsheetFile.getName(),
								spreadsheetFile);

				frontDesk.scan();

				System.out.println("Worksheets: "
						+ inspection.getInventory().getSpreadsheet()
								.getWorksheets().size());
				System.out.println("Cells: "
						+ inspection.getInventory().getListFor(Cell.class)
								.getNumberOfElements());
				System.out.println("Formulas: "
						+ inspection.getInventory().getListFor(Formula.class)
								.getNumberOfElements());

				Policy policy = FrontDesk.getInstance().getAvailablePolicies()
						.get("Basic Policy");

				AbstractPolicyRule rule = policy
						.getRuleByName("Policy Rule: No Constants In Formulas");
				Object[] ignoredConstants = { 0, 1 };
				rule.getConfiguration()
						.getParameterByFieldName("ignoredConstants")
						.setParameterValue(ignoredConstants);
				String[] ignoredFunctions = { "Index", "Indirect" };
				rule.getConfiguration()
						.getParameterByFieldName("ignoredFunctions")
						.setParameterValue(ignoredFunctions);

				AbstractPolicyRule ruleTwo = policy
						.getRuleByName("Policy Rule: Formula Complexity");
				Integer maxNumberOfOperations = 8;
				Integer maxNestingLevel = 3;
				ruleTwo.getConfiguration()
						.getParameterByFieldName("maxNumberOfOperations")
						.setParameterValue(maxNumberOfOperations);
				ruleTwo.getConfiguration()
						.getParameterByFieldName("maxNestingLevel")
						.setParameterValue(maxNestingLevel);

				frontDesk.setPolicy(policy);

				frontDesk.run();

				frontDesk.createInspectionReport("C://");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
