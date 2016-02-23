package sif.technicalDepartment.equipment.testing.tools;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;

import sif.IO.DataFacade;
import sif.IO.spreadsheet.InvalidSpreadsheetFileException;
import sif.IO.spreadsheet.poi.ISpreadsheetWriter;
import sif.IO.spreadsheet.poi.POIWriter;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.inspection.DynamicInspectionRequest;
import sif.model.policy.policyrule.DynamicPolicyRule;

/**
 * Evaluates the formulae of the POI spreadsheet contained in the
 * DynamicInspectionRequest. This implementation requires a
 * {@link DynamicInspectionRequest}.
 * 
 * @author Manuel Lemcke
 * 
 */
public class POISpreadsheetPreparator implements IDynamicSpreadsheetRunner {

	private DynamicInspectionRequest request;
	private IWorkbookCloner cloner;
	private Workbook workbook;
	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unused")
	private POISpreadsheetPreparator() {

	}

	public Workbook writeTestInput(DynamicPolicyRule rule, Workbook workbook) {
		Workbook wb = workbook;
		ISpreadsheetWriter writer = new POIWriter();
		Workbook testWorkbook = cloner.cloneWorkbook(wb);

		try {
			writer.insertTestInput(rule, testWorkbook);
		} catch (Exception e) {
			logger.warn("", e);
		}
		return testWorkbook;
	}

	public POISpreadsheetPreparator(DynamicInspectionRequest inspectionRequest) {
		this.request = inspectionRequest;
		this.cloner = new PersistentWorkbookCloner();
	}

	public POISpreadsheetPreparator(
			DynamicInspectionRequest inspectionRequest,
			IWorkbookCloner workbookCloner) {
		this.request = inspectionRequest;
		this.cloner = workbookCloner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sif.technicalDepartment.equipment.testing.tools.IDynamicSpreadsheetRunner
	 * (sif.model.inspection.DynamicInspectionRequest, org.apache.poi.ss.usermodel.Workbook)
	 */
	@Override
	public synchronized Spreadsheet prepare(DynamicPolicyRule rule, Object poiSpreadsheet)
			throws Exception {
		if (poiSpreadsheet instanceof Workbook) {
			Workbook workbook = (Workbook) poiSpreadsheet;
			String name = this.request.getSpreadsheetFile().getName();
			
			Spreadsheet spreadsheet = null;
			this.workbook = writeTestInput(rule, workbook);

			spreadsheet = DataFacade.getInstance().createSpreadsheet(this.workbook,
					name);

			return spreadsheet;
		} else {
			throw new InvalidSpreadsheetFileException("The parameter spreadsheet is not of the Type Workbook.");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sif.technicalDepartment.equipment.testing.tools.IDynamicSpreadsheetRunner
	 * (sif.model.inspection.DynamicInspectionRequest, org.apache.poi.ss.usermodel.Workbook)
	 */
	@Override
	public synchronized Spreadsheet evaluate() {
		Spreadsheet spreadsheet = null;
		String name = this.request.getSpreadsheetFile().getName();
		if (this.workbook != null) {
			FormulaEvaluator fe = this.workbook.getCreationHelper().createFormulaEvaluator();
			fe.clearAllCachedResultValues();
			fe.evaluateAll();
		}

		try {
			spreadsheet = DataFacade.getInstance().createSpreadsheet(this.workbook,
					name);
		} catch (Exception e) {
			logger.warn("", e);
		}

		return spreadsheet;
	}

}
