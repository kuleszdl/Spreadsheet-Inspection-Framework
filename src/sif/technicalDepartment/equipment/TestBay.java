package sif.technicalDepartment.equipment;

import java.util.ArrayList;

import sif.model.inspection.InspectionRequest;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.violations.Findings;
import sif.model.violations.lists.ViolationList;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.CheckerCreationException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.NoConditionTargetException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.PropertyAccessException;
import sif.technicalDepartment.equipment.testing.facilities.types.AbstractTestFacility;

public class TestBay {
	private Findings findings;
	private InspectionRequest inspection;
	private ArrayList<AbstractTestFacility> testFacilities;

	public TestBay(InspectionRequest inspection) {
		this.testFacilities = new ArrayList<AbstractTestFacility>();
		this.inspection = inspection;
		this.findings = new Findings();
	}

	public void add(AbstractTestFacility testFacility) {
		this.testFacilities.add(testFacility);
	}

	public InspectionRequest getInspection() {
		return this.inspection;
	}

	/**
	 * Runs all {@link TestFacility} instances which were added to the test bay
	 * 
	 * 
	 * @throws CheckerCreationException
	 *             Thrown, if a checker for a {@link AbstractCondition} subclass
	 *             couldn't be created. This might happen, if a
	 *             {@link AbstractCondition} has not been registered.
	 * 
	 * @throws PropertyAccessException
	 *             Thrown, if a property of a SIF Element could not be access.
	 *             Most likely because the element doesn't have that property.
	 * 
	 * @throws NoConditionTargetException
	 *             Thrown, if a {@link AbstractCondition} object has no target
	 *             but is supposed to.
	 * 
	 * @throws IncompleteConditionException
	 *             Thrown, if the one of the conditions contained in one of the
	 *             policy rules does not contain all information necessary to
	 *             check it properly.
	 */
	public void runTestFacilities() throws NoConditionTargetException,
											PropertyAccessException, 
											CheckerCreationException,
											IncompleteConditionException {
		for (AbstractTestFacility testFacility : testFacilities) {
			findings.add(testFacility.run());
		}
		this.inspection.setFindings(findings);
	}

	public void setInspection(InspectionRequest currentSession) {
		this.inspection = currentSession;
	}

}