package sif.frontOffice;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import sif.IO.DataFacade;
import sif.IO.ReportFormat;
import sif.IO.spreadsheet.ISpreadsheetIO;
import sif.model.elements.basic.address.AbstractAddress;
import sif.model.elements.basic.cell.ICellElement;
import sif.model.elements.containers.AbstractElementList;
import sif.model.elements.custom.InputCell;
import sif.model.elements.custom.OutputCell;
import sif.model.inspection.DynamicInspectionRequest;
import sif.model.inspection.InspectionRequest;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.Policy;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.CompositePolicyRule;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.IOCellInfo;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.technicalDepartment.equipment.scanning.ElementScanner;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.AbstractConditionChecker;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;
import sif.technicalDepartment.management.TechnicalManager;

/***
 * The FrontDesk is the access point to use the functionality of SIF. However it
 * does not perform any work, but simply forwards calls to the
 * {@link PolicyManager}, {@link TechnicalManager} or {@link InspectionManager}.
 * Implements the Facade and Singleton pattern.
 * 
 * @author Sebastian Zitzelsberger, Manuel Lemcke, Ehssan Doust
 * 
 */
public class FrontDesk {

	private final static String inputCellName = "SIF_InputCell_%s";
	private final static String outputCellName = "SIF_OutputCell_%s";

	/***
	 * Gets the instance of the front desk.
	 * 
	 * @return The instance.
	 */
	public static FrontDesk getInstance() {
		return theInstance;

	}

	private PolicyManager policyManager;
	private InspectionManager inspectionManager;
	private TechnicalManager technicalManager;

	private static final FrontDesk theInstance = new FrontDesk();

	private FrontDesk() {
		policyManager = new PolicyManager();
		inspectionManager = new InspectionManager();
		technicalManager = TechnicalManager.getInstance();
	}

	/**
	 * Scans a spreadsheet and creates a policy with information about the input
	 * cells, output cells and spreadsheet file name.
	 * 
	 * @param name
	 * @param spreadsheet
	 * @return
	 * @throws Exception
	 *             Throws an exception if the given spreadsheet file is invalid.
	 */
	public DynamicPolicy scanAndCreatePolicy(String name, File spreadsheet)
			throws Exception {
		InspectionRequest request = this.requestNewDynamicInspection(name,
				spreadsheet);
		FrontDesk.getInstance().scan();
		DynamicPolicy policy = new DynamicPolicy();

		policy.setSpreadsheetFileName(spreadsheet.getPath());

		// get input cells and write them in the policy
		AbstractElementList<InputCell> inputCells;
		ArrayList<IOCellInfo> inputCellInfos = new ArrayList<IOCellInfo>();
		inputCells = request.getInventory().getListFor(InputCell.class);
		if (inputCells != null) {
			for (Integer i = 0; i < inputCells.getNumberOfElements(); i++) {
				IOCellInfo info = new IOCellInfo();
				InputCell cell = inputCells.getElements().get(i);
				info.setA1Address(createA1Address(cell));
				info.setName(String.format(inputCellName, i.toString()));
				inputCellInfos.add(info);
			}
		}
		policy.setInputCells(inputCellInfos);

		// get output cells and write them in the policy
		AbstractElementList<OutputCell> outputCells;
		ArrayList<IOCellInfo> outputCellInfos = new ArrayList<IOCellInfo>();
		outputCells = request.getInventory().getListFor(OutputCell.class);
		if (outputCells != null) {
			for (Integer i = 0; i < outputCells.getNumberOfElements(); i++) {
				IOCellInfo info = new IOCellInfo();
				OutputCell cell = outputCells.getElements().get(i);
				info.setA1Address(createA1Address(cell));
				info.setName(String.format(outputCellName, i.toString()));
				outputCellInfos.add(info);
			}
		}
		policy.setOutputCells(outputCellInfos);

		return policy;
	}

	private String createA1Address(ICellElement cell) {
		String a1 = "";
		String sheet = cell.getCell().getAbstractAddress().getWorksheet()
				.getName();
		Integer rowIndex = cell.getCell().getRowIndex();
		Integer columnIndex = cell.getCell().getColumnIndex();
		String column = AbstractAddress.getCharacterFor(columnIndex);

		a1 = sheet + "." + column + rowIndex;
		return a1;
	}

	/**
	 * Requests a new inspection, scans the spreadsheet file and runs the
	 * inspection.
	 * 
	 * @param name
	 *            The name of the inspection
	 * @param spreadsheet
	 *            The spreadsheet file which is scanned
	 * @param policy
	 *            The policy against which is checked
	 * @return The inspection request including the findings
	 * @throws Exception
	 */
	public InspectionRequest createAndRunDynamicInspectionRequest(String name,
			File spreadsheet, Policy policy) throws Exception {
		InspectionRequest request = this.requestNewDynamicInspection(name,
				spreadsheet);
		FrontDesk.getInstance().scan();
		FrontDesk.getInstance().register(policy);
		FrontDesk.getInstance().setPolicy(policy);
		FrontDesk.getInstance().run();
		return request;
	}

	/***
	 * Creates an Inspection report for the last created inspection request.
	 * 
	 * @throws Exception
	 * 
	 */
	public String createInspectionReport(ReportFormat format) throws Exception {
		return inspectionManager.createInspectionReport(format);
	}

	/***
	 * Creates an Inspection report for the last created inspection request at
	 * the given path.
	 * 
	 * @param path
	 *            the given path.
	 * @throws Exception
	 * 
	 */
	public void createInspectionReport(String path, ReportFormat format)
			throws Exception {
		inspectionManager.createInspectionReport(path, format);
	}

	/***
	 * Gets all available policies that have been registered with SIF and can be
	 * chosen for an inspection request.
	 * 
	 * @return The list of available policies.
	 */
	public TreeMap<String, Policy> getAvailablePolicies() {
		return policyManager.getAvailablePolicies();

	}

	/***
	 * Gets all available policy rules that have been registered with SIF and
	 * can be added to a new policy.
	 * 
	 * @return The list of available policy rules
	 */
	public ArrayList<AbstractPolicyRule> getAvailablePolicyRules() {
		return policyManager.getAvailablePolicyRules();
	}

	/***
	 * Gets the list of all registered element scanners.
	 * 
	 * @return The list of registered element scanners.
	 */
	public TreeMap<ElementScanner<?>, Integer> getScannerList() {
		return technicalManager.getScannerList();
	}

	/***
	 * Register the given policy with SIF so it can be chosen for inspections.
	 * 
	 * @param policy
	 */
	public void register(Policy policy) {
		this.policyManager.register(policy);
	}

	/**
	 * Register a new {@link AbstractConditionChecker} Class and the
	 * {@link AbstractCondition} class it is responsible for with SIF so it can
	 * be used in {@link DynamicPolicyRule} instances.
	 * 
	 * @param conditionClass
	 * @param checkerClass
	 */
	public void registerCondition(
			Class<? extends AbstractCondition> conditionClass,
			Class<? extends AbstractConditionChecker> checkerClass) {
		this.policyManager.registerCondition(conditionClass, checkerClass);
	}

	// TODO Write Test for register Methods
	/***
	 * Registers the given policy rule class with the {@link PolicyManager} and
	 * associates it with the given test facility class.
	 * 
	 * @param policyRuleClass
	 *            The given policy rule class.
	 * @param testFacilityClass
	 *            The given test facility class.
	 */
	public void registerMonolithicFacility(
			Class<? extends MonolithicPolicyRule> ruleClass,
			Class<? extends MonolithicTestFacility> facilityClass) {
		this.policyManager.register(ruleClass, facilityClass);
	}

	public void registerCompositeFacility(
			Class<? extends CompositePolicyRule> ruleClass) {
		this.policyManager.register(ruleClass);
	}

	/***
	 * Creates a new inspection request with the given request name for the
	 * given spreadsheet file.
	 * 
	 * @param requestName
	 *            The given request name. May not be null or empty.
	 * @param spreadsheetFile
	 *            The given spreadsheet file.
	 * @return The created inspection request.
	 * @throws Exception
	 *             Throws an exception if the given spreadsheet file is invalid.
	 */
	public InspectionRequest requestNewInspection(String requestName,
			File spreadsheetFile) throws Exception {
		return inspectionManager.createNewInspectionRequest(requestName,
				spreadsheetFile);
	}

	/***
	 * Creates a new {@link DynamicInspectionRequest} with the given request
	 * name for the given spreadsheet file.
	 * 
	 * @param requestName
	 *            The given request name. May not be null or empty.
	 * @param spreadsheetFile
	 *            The given spreadsheet file.
	 * @return The created inspection request.
	 * @throws Exception
	 *             Throws an exception if the given spreadsheet file is invalid.
	 */
	public DynamicInspectionRequest<?> requestNewDynamicInspection(
			String requestName, File spreadsheetFile) throws Exception {
		ISpreadsheetIO ssIO = DataFacade.getInstance().createSpreadsheetIO(
				spreadsheetFile.getName());
		DynamicInspectionRequest<?> toReturn = null;
		
		toReturn = inspectionManager.createNewDynamicInspectionRequest(requestName,
					spreadsheetFile, ssIO);


		return toReturn;
	}

	/***
	 * Runs the inspection for the last created inspection request.
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception {
		this.inspectionManager.run();
	}

	/***
	 * Scans the elements the last created inspection request.
	 */
	public void scan() {
		this.inspectionManager.scan();
	}

	/***
	 * Sets the given policy for the last created inspection request. YOU STILL
	 * NEED TO REGISTER THE POLICY AS LONG AS YOU DON'T CREATE THE CONFIGURATION
	 * YOURSELF!
	 * 
	 * @param policy
	 *            The given policy
	 * @throws Exception
	 */
	public void setPolicy(Policy policy) throws Exception {
		this.inspectionManager.setPolicy(policy);
	}

}
