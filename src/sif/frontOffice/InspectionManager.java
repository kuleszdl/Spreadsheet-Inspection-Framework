package sif.frontOffice;

import java.io.File;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Workbook;

import sif.IO.DataFacade;
import sif.IO.ReportFormat;
import sif.IO.spreadsheet.ISpreadsheetIO;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.inspection.DynamicInspectionRequest;
import sif.model.inspection.InspectionRequest;
import sif.model.inspection.InspectionStateEnum;
import sif.model.inspection.SpreadsheetInventory;
import sif.model.policy.Policy;
import sif.technicalDepartment.management.TechnicalManager;

/***
 * The InspectionManager handles the management of spreadsheet inspections. It
 * accepts new inspection requests and coordinates the configuration and
 * execution of these requests. The methods must be called in the following
 * order: {@link #createNewInspectionRequest(String, File)} {@link #scan()}
 * {@link #setPolicy(Policy)} {@link #run()} (
 * {@link #createInspectionReport(String)}) optional.
 * 
 * @author Sebastian Zitzelsberger, Ehssan Doust
 * 
 */
public class InspectionManager {

	private TechnicalManager technicalManager;
	private InspectionRequest currentInspectionRequest;
	private InspectionStateEnum stateOfCurrenInspectionRequest;
	TreeMap<UUID, InspectionRequest> inspectionList;

	protected InspectionManager() {
		technicalManager = TechnicalManager.getInstance();
		inspectionList = new TreeMap<UUID, InspectionRequest>();
	}

	/***
	 * Creates an inspection report for the findings of the last created
	 * inspection request.
	 * 
	 * @throws Exception
	 */
	public String createInspectionReport(ReportFormat format) throws Exception {
		switch (stateOfCurrenInspectionRequest) {
		case VIOLATIONS_DETECTED:
			return DataFacade.getInstance().export(currentInspectionRequest,
					format);
		default:
			throw new Exception("Violations have not been detected yet.");
		}
	}

	/***
	 * Creates an inspection report at the given path for the findings of the
	 * last created inspection request.
	 * 
	 * @param path
	 *            The given path.
	 * @throws Exception
	 */
	public void createInspectionReport(String path, ReportFormat format)
			throws Exception {
		switch (stateOfCurrenInspectionRequest) {
		case VIOLATIONS_DETECTED:
			DataFacade.getInstance().export(currentInspectionRequest, path,
					format);
			break;
		default:
			throw new Exception("Violations have not been detected yet.");
		}
	}

	/***
	 * Creates a new inspection request for the given spreadsheet file with the
	 * given request name.
	 * 
	 * @param requestName
	 *            The given name for the request.
	 * @param spreadsheetFile
	 *            The given spreadsheet file.
	 * @return The newly created inspection request.
	 * @throws Exception
	 *             Throws an exception if the given spreadsheet file is invalid.
	 */
	protected InspectionRequest createNewInspectionRequest(String requestName,
			File spreadsheetFile) throws Exception {
		// Create request.
		InspectionRequest inspectionRequest = new InspectionRequest();
		stateOfCurrenInspectionRequest = InspectionStateEnum.INITIAL;
		inspectionRequest.setName(requestName);
		inspectionRequest.setSpreadsheetFile(spreadsheetFile);

		// Create initial inventory.
		SpreadsheetInventory inventory = new SpreadsheetInventory();
		inventory.setSpreadsheet(DataFacade.getInstance().createSpreadsheet(
				spreadsheetFile));
		inventory.setSpreadsheetFile(spreadsheetFile);
		inspectionRequest.setInventory(inventory);

		// Add to list and set as current request;
		inspectionList.put(inspectionRequest.getId(), inspectionRequest);
		currentInspectionRequest = inspectionRequest;
		return inspectionRequest;
	}

	/***
	 * Creates a new {@link DynamicInspectionRequest} for the given spreadsheet
	 * file with the given request name which also stores the POI
	 * {@link Workbook} which is created during creation of the
	 * {@link Spreadsheet}
	 * 
	 * @param requestName
	 *            The given name for the request.
	 * @param spreadsheetFile
	 *            The given spreadsheet file.
	 * @return The newly created inspection request.
	 * @throws Exception
	 *             Throws an exception if the given spreadsheet file is invalid.
	 */
	protected DynamicInspectionRequest<?> createNewDynamicInspectionRequest(
			String requestName, File spreadsheetFile,
			ISpreadsheetIO spreadsheetIO) throws Exception {
		// Create request
		stateOfCurrenInspectionRequest = InspectionStateEnum.INITIAL;

		DynamicInspectionRequest<?> inspectionRequest = spreadsheetIO
				.createInspectionRequest(requestName, spreadsheetFile);

		inspectionList.put(inspectionRequest.getId(), inspectionRequest);
		currentInspectionRequest = inspectionRequest;
		return inspectionRequest;
	}

	public InspectionRequest getCurrentInspectionRequest() {
		return this.currentInspectionRequest;
	}

	/***
	 * Executes the last created inspection request, based on the scanned
	 * elements and its configured policy. If the inspection request has already
	 * been executed the findings will be reset.
	 * 
	 * @throws Exception
	 *             Throws an exception if the inspection request has not been
	 *             prepared for execution yet.
	 */
	public void run() throws Exception {
		switch (stateOfCurrenInspectionRequest) {
		case POLICY_CHOSEN:
			this.technicalManager.run(this.currentInspectionRequest);
			stateOfCurrenInspectionRequest = InspectionStateEnum.VIOLATIONS_DETECTED;
			break;
		case VIOLATIONS_DETECTED:
			currentInspectionRequest.setFindings(null);
			this.technicalManager.run(this.currentInspectionRequest);
			stateOfCurrenInspectionRequest = InspectionStateEnum.VIOLATIONS_DETECTED;
		default:
			throw new Exception("Inspection request not ready for execution.");
		}

	}

	/***
	 * Scans the elements for the last created inspection request. If elements
	 * are scanned after the inspection request has already been configured or
	 * even executed, the configuration and the findings will be reset.
	 */
	public void scan() {
		switch (stateOfCurrenInspectionRequest) {
		case INITIAL:
			this.technicalManager.scanElementsFor(currentInspectionRequest);
			stateOfCurrenInspectionRequest = InspectionStateEnum.ELEMENTS_SCANNED;
			break;
		default:
			currentInspectionRequest.setPolicy(null);
			currentInspectionRequest.setFindings(null);
			this.technicalManager.scanElementsFor(currentInspectionRequest);
			stateOfCurrenInspectionRequest = InspectionStateEnum.ELEMENTS_SCANNED;
			break;
		}
	}

	/***
	 * Sets the given policy for the last created inspection request. If the
	 * inspection request has already been executed the findings will be reset.
	 * 
	 * @param policy
	 *            The given policy.
	 * @throws Exception
	 *             Throws an exception if the elements have not been scanned
	 *             yet.
	 */
	public void setPolicy(Policy policy) throws Exception {
		switch (stateOfCurrenInspectionRequest) {
		case ELEMENTS_SCANNED:
			this.currentInspectionRequest.setPolicy(policy);
			stateOfCurrenInspectionRequest = InspectionStateEnum.POLICY_CHOSEN;
			break;
		case POLICY_CHOSEN:
			this.currentInspectionRequest.setPolicy(policy);
			stateOfCurrenInspectionRequest = InspectionStateEnum.POLICY_CHOSEN;
			break;
		case VIOLATIONS_DETECTED:
			currentInspectionRequest.setFindings(null);
			this.currentInspectionRequest.setPolicy(policy);
			stateOfCurrenInspectionRequest = InspectionStateEnum.POLICY_CHOSEN;
		default:
			throw new Exception("Elements haven't been scanned yet.");
		}

	}

}