package sif.technicalDepartment.management;

import java.util.TreeMap;

import sif.model.inspection.InspectionRequest;
import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.technicalDepartment.equipment.scanning.ElementScanner;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.IConditionChecker;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * The TechnicalManager manages the scanning of spreadsheets as well as the
 * execution of inspection requests. However it does not perform any work, but
 * simply forwards calls to the {@link ScanningManager} or the
 * {@link TestBayManager}. Implements the Facade and Singleton pattern.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class TechnicalManager {

	private static final TechnicalManager theInstance = new TechnicalManager();

	/**
	 * Gets the instance of the technical manager.
	 * 
	 * @return The instance.
	 */
	public static TechnicalManager getInstance() {
		return theInstance;
	}

	private TestBayManager testBayManager;

	private ScanningManager scanningManager;

	private TechnicalManager() {
		scanningManager = new ScanningManager();
		testBayManager = new TestBayManager();
	}

	/**
	 * Gets a list of all registered element scanners.
	 * 
	 * @return A list of all registered element scanners.
	 */
	public TreeMap<ElementScanner<?>, Integer> getScannerList() {
		return scanningManager.getScannerList();
	}

	public void register(Class<? extends MonolithicPolicyRule> ruleClass,
			Class<? extends MonolithicTestFacility> testFacilityClass) {
		testBayManager.register(ruleClass, testFacilityClass);
	}
	
	public void registerCondition(Class<? extends AbstractCondition> conditionClass,
			Class<? extends IConditionChecker> checkerClass) {
		testBayManager.registerCondition(conditionClass, checkerClass);
	}
	

	public void run(InspectionRequest inspection) throws Exception {
		testBayManager.run(inspection);
	}

	/***
	 * Scans the spreadsheet from the given inspection request for elements.
	 * 
	 * @param inspectionRequest
	 *            The given inspection request.
	 */
	public void scanElementsFor(InspectionRequest inspectionRequest) {
		scanningManager.scanElementsFor(inspectionRequest);
	}
}