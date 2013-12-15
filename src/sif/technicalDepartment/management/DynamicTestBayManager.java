package sif.technicalDepartment.management;

import sif.model.inspection.InspectionRequest;
import sif.technicalDepartment.equipment.TestBay;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.CheckerCreationException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.NoConditionTargetException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.PropertyAccessException;

/***
 * A TestBayManager which also can handle DynamicPolicy instances.
 * 
 * The TestBayManager is in charge of managing the test equipment and for
 * creating {@link TestBay}s for {@link InspectionRequest}s.
 * 
 * @author Manuel Lemcke
 * 
 */
public class DynamicTestBayManager extends TestBayManager {

	public DynamicTestBayManager() {
		super();
	}

	/**
	 * Creates a new TestBay if needed and runs all {@link TestFacility}
	 * instances registered for rules in the {@link InspectionRequest}.
	 * 
	 * @param inspection
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IncompleteConditionException
	 *             At least one condition does not contain all information
	 *             necessary to check it properly
	 * 
	 * @throws CheckerCreationException
	 *             If a {@link AbstractConditionChecker} could not be created.
	 *             Most likely because it was not via {@link PolicyManager} or
	 *             {@link FrontDesk}
	 * 
	 * @throws PropertyAccessException
	 * @throws NoConditionTargetException
	 * 
	 */
	@Override
	public void run(InspectionRequest inspection) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException,
			NoConditionTargetException, PropertyAccessException,
			CheckerCreationException, IncompleteConditionException {
		prepareTestBayFor(inspection);
		runTestFacilities(inspection);
	}
}
