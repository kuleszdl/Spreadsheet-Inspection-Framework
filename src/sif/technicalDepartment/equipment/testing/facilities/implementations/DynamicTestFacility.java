package sif.technicalDepartment.equipment.testing.facilities.implementations;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import sif.IO.spreadsheet.poi.ISpreadsheetWriter;
import sif.model.elements.AbstractElement;
import sif.model.elements.IElement;
import sif.model.elements.basic.address.AbstractAddress;
import sif.model.elements.basic.address.CellAddress;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.elements.containers.AbstractElementList;
import sif.model.inspection.DynamicInspectionRequest;
import sif.model.inspection.InspectionRequest;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.policy.policyrule.dynamicConditions.TestInput;
import sif.model.violations.ISingleViolation;
import sif.model.violations.groupors.SameCausingCellGroupor;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.ConditionSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.AbstractConditionChecker;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.IConditionChecker;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.TargetlessConditionChecker;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.CheckerCreationException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.NoConditionTargetException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.PropertyAccessException;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;
import sif.technicalDepartment.equipment.testing.tools.IDynamicSpreadsheetRunner;
import sif.technicalDepartment.equipment.testing.tools.POISpreadsheetPreparator;

/**
 * A TestFacility which can realize the {@link TestInput}s of a
 * {@link DynamicPolicyRule} and check for it's {@link AbstractCondition}
 * instances (preconditions, invariants, postconditions) as long as they're
 * registered at the {@link TestBayManager}.
 * 
 * @author Manuel Lemcke
 */
public class DynamicTestFacility extends MonolithicTestFacility {

	private boolean invariantCheckLazy = true;

	// As i discovered it is necessary that the TestFacility has the same
	// fields as the tested PolicyRule because else the creation of the
	// configuration fails, so here they are...:
	@SuppressWarnings("unused")
	private ArrayList<AbstractCondition> invariants;
	@SuppressWarnings("unused")
	private ArrayList<TestInput> testInputs;
	@SuppressWarnings("unused")
	private ArrayList<AbstractCondition> postconditions;

	protected IDynamicSpreadsheetRunner spreadsheetRunner;
	protected ISpreadsheetWriter poiWriter;
	protected DynamicPolicyRule dynamicRule;
	// protected Spreadsheet preparedSpreadsheet = null;
	private TreeMap<String, Class<? extends IConditionChecker>> checkers;

	/**
	 * Hide standard constructor so only the parameterized constructor is
	 * available
	 */
	@SuppressWarnings("unused")
	private DynamicTestFacility() {

	}

	public DynamicTestFacility(
			TreeMap<String, Class<? extends IConditionChecker>> conditionCheckers) {
		this.checkers = conditionCheckers;
	}

	/**
	 * Checks all {@link IConditionChecker} implementations for which a
	 * {@link DynamicPolicyRule} object exists.
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
	 * @throws IncompleteConditionException
	 *             Thrown, if the one of the conditions contained in one of the
	 *             policy rules does not contain all information necessary to
	 *             check it properly.
	 * 
	 */
	public ViolationList run() throws NoConditionTargetException,
			PropertyAccessException, CheckerCreationException,
			IncompleteConditionException {
		// local Variables which are filled from AbstractTestFacility's
		// properties
		AbstractPolicyRule rule = this.getTestedPolicyRule();
		ViolationList result = new ViolationList(rule, new SameCausingCellGroupor());

		InspectionRequest request = this.getTestBay().getInspection();

		if (request instanceof DynamicInspectionRequest
				&& this.getTestedPolicyRule() instanceof DynamicPolicyRule) {
			// local Variables which are filled only if the request is a
			// DynamicInspectionRequest
			DynamicInspectionRequest<?> dynRequest = (DynamicInspectionRequest<?>) request;
			this.dynamicRule = (DynamicPolicyRule) this.getTestedPolicyRule();

			Spreadsheet spreadsheet = request.getInventory().getSpreadsheet();
			
			/*
			 * Check invariants if not lazy
			 */
			if (!invariantCheckLazy && this.dynamicRule.getInvariants().size() > 0) {
				checkConditions(this.dynamicRule.getInvariants(), result,
						spreadsheet);
			}

			IDynamicSpreadsheetRunner preparator = new POISpreadsheetPreparator(
					dynRequest);			
			Spreadsheet preparedSpreadsheet = null;
			// realize test inputs
			try {
				preparedSpreadsheet = preparator.prepare(this.dynamicRule,
						dynRequest.getExternalSpreadsheet());
			} catch (Exception e) {
				// If an exception occurred propagate it, so it will be displayed for debugging
				throw new PropertyAccessException(e);
			}
			
			/*
			 * Check invariants if not lazy
			 */
			if (!invariantCheckLazy && this.dynamicRule.getInvariants().size() > 0) {
				checkConditions(this.dynamicRule.getInvariants(), result,
						preparedSpreadsheet);
			}
			
			preparedSpreadsheet = preparator.evaluate();

			/*
			 * Check postconditions and invariants
			 */
			if (this.dynamicRule.getPostconditions().size() > 0) {
				// run check
				checkConditions(this.dynamicRule.getPostconditions(), result,
						preparedSpreadsheet);
			}

			if (this.dynamicRule.getInvariants().size() > 0) {
				checkConditions(this.dynamicRule.getInvariants(), result,
						preparedSpreadsheet);
			}
		}

		return result;
	}

	/**
	 * Checks if the Spreadsheet meets a set of {@link AbstractCondition}
	 * instances and adds found violations to the resultList
	 * 
	 * @param conditions
	 * @param result
	 * @return The {@link ViolationList} containing new violations
	 * @throws NoConditionTargetException
	 *             Thrown, if a condition had no target and no elementType info
	 * @throws PropertyAccessException
	 * @throws CheckerCreationException
	 * @throws IncompleteConditionException
	 */
	protected void checkConditions(List<AbstractCondition> conditions,
			ViolationList result, Spreadsheet preparedSpreadsheet)
			throws NoConditionTargetException, PropertyAccessException,
			CheckerCreationException, IncompleteConditionException {

		for (AbstractCondition condition : conditions) {
			ISingleViolation violation = null;

			// get the corresponding checker
			String conditionClassName = condition.getClass().getCanonicalName();
			Class<? extends IConditionChecker> checkerClass = this.checkers
					.get(conditionClassName);

			IConditionChecker checker;
			try {
				checker = checkerClass.getConstructor(AbstractPolicyRule.class)
						.newInstance(this.dynamicRule);
			} catch (IllegalArgumentException e1) {
				throw new CheckerCreationException(e1);
			} catch (InvocationTargetException e1) {
				throw new CheckerCreationException(e1);
			} catch (NoSuchMethodException e1) {
				throw new CheckerCreationException(e1);
			} catch (SecurityException e1) {
				throw new CheckerCreationException(e1);
			} catch (InstantiationException e) {
				throw new CheckerCreationException(e);
			} catch (IllegalAccessException e) {
				throw new CheckerCreationException(e);
			}

			if (condition.getElementType() == null
					&& condition.getTarget() != null) {

				checkTargetedCondition(condition, result,
						(AbstractConditionChecker) checker, preparedSpreadsheet);

			} else if (condition.getElementType() != null
					&& condition.getElementType() != "") {
				if (checker instanceof AbstractConditionChecker) {
					// If a type is set check all elements of that type
					for (AbstractElementList<?> list : this.inventory
							.getElementLists()) {
						String currentClassName = list.getElementClass()
								.getCanonicalName();
						String expectedClassName = condition.getElementType();
						if (currentClassName.endsWith(expectedClassName)) {
							for (int e = 0; e < list.getElements().size(); e++) {
								AbstractElement element;
								element = (AbstractElement) list.getElements()
										.get(e);
								if (element != null) {
									AbstractConditionChecker abstractChecker;
									abstractChecker = ((AbstractConditionChecker) checker);
									abstractChecker.setElement(element);
									// If no type is set but a target is set
									// check only the target
									violation = abstractChecker
											.check(condition);
								} else {
									ConditionSingleViolation csViolation;
									csViolation = new ConditionSingleViolation();
									csViolation.setCausingElement(null);
									csViolation.setPolicyRule(this.dynamicRule);
									csViolation
											.setBaseSeverityValue(this.dynamicRule
													.getSeverityWeight());
									violation = csViolation;
								}
							}
						}
					}
				} else if (checker instanceof TargetlessConditionChecker) {
					TargetlessConditionChecker tlChecker = ((TargetlessConditionChecker) checker);
					tlChecker.setInventory(inventory);
					violation = tlChecker.check(condition);
				}

				if (violation != null) {
					// Add violation to the result list
					result.add(violation);
				}
			} else {
				// TODO create specific Excepion class
				// ElementType == null && Target == null
				throw new NoConditionTargetException(
						"The Target and elementType of condition are null!");
			}
		}
	}

	private void checkTargetedCondition(AbstractCondition condition,
			ViolationList result, AbstractConditionChecker checker,
			Spreadsheet preparedSpreadsheet) throws PropertyAccessException,
			IncompleteConditionException {
		ISingleViolation violation = null;
		IElement element = null;

		/*
		 * Prepare / instrumentalize spreadsheet
		 */
		try {
			element = retrieveTarget(condition.getTarget(), preparedSpreadsheet);
		} catch (ParseException pe) {
			violation = new ConditionSingleViolation();
			((ConditionSingleViolation) violation).setCausingElement(null);
			violation.setPolicyRule(this.dynamicRule);
			((ConditionSingleViolation) violation)
					.setBaseSeverityValue(this.dynamicRule.getSeverityWeight());
			((ConditionSingleViolation) violation)
					.appendToDescription("Unresolvable target: "
							+ condition.getTarget());

		}

		if (element != null && checker instanceof AbstractConditionChecker) {
			// If no type is set but a target is set check only the
			// target
			checker.setElement(element);
			violation = checker.check(condition);
		}

		if (violation != null) {
			// Add violation to the result list
			result.add(violation);
		}

	}

	/**
	 * Gets the {@link IDynamicSpreadsheetRunner} which is used to evaluate the
	 * formulae in the spreadsheet
	 * 
	 * @return the spreadsheetRunner
	 */
	public IDynamicSpreadsheetRunner getSpreadsheetRunner() {
		return spreadsheetRunner;
	}

	/**
	 * @param spreadsheetRunner
	 *            the spreadsheetRunner to set
	 */
	public void setSpreadsheetRunner(IDynamicSpreadsheetRunner spreadsheetRunner) {
		this.spreadsheetRunner = spreadsheetRunner;
	}

	/**
	 * @return the invariantCheckLazy
	 */
	public boolean isInvariantCheckLazy() {
		return invariantCheckLazy;
	}

	/**
	 * @param invariantCheckLazy
	 *            the invariantCheckLazy to set
	 */
	public void setInvariantCheckLazy(boolean invariantCheckLazy) {
		this.invariantCheckLazy = invariantCheckLazy;
	}

	/**
	 * Gets the {@link IElement} instance which is described by the target
	 * string.
	 * 
	 * If the target string doesn't specify a worksheet the first one is taken.
	 * 
	 * @param target
	 * @param spreadsheet
	 * @return
	 * @throws ParseException
	 */
	public static IElement retrieveTarget(String target, Spreadsheet spreadsheet)
			throws ParseException {
		IElement element = null;
		String[] targetParts = target.split("\\.");
		AbstractAddress address = null;
		Worksheet sheet = null;

		// TODO resolve Range address
		switch (targetParts.length) {
		case 1:
			address = createCellAddress(targetParts[0]);
			if (spreadsheet == null){
				throw new RuntimeException("Spreadsheet is null.");
			}
			if (spreadsheet.getWorksheets() == null){
				throw new RuntimeException("Spreadsheet " + spreadsheet.getName() +  " has no worksheets.");
			}
			if (spreadsheet.getWorksheets().size() > 0) {
				address.setWorksheet(spreadsheet.getWorksheetAt(1));
			} else {
				throw new RuntimeException("Spreadsheet has no worksheets.");
			}
			break;
		case 2:
			address = createCellAddress(targetParts[1]);
			sheet = getWorksheet(targetParts[0], spreadsheet);
			address.setWorksheet(sheet);
			break;
		case 3:
			throw new ParseException(
					"References to other Spreadsheets are not supported", 0);
			// address = createCellAddress(targetParts[2]);
			// sheet = getWorksheet(targetParts[1], spreadsheet);
			// address.setWorksheet(sheet);
		default:
			break;
		}

		element = spreadsheet.getCellFor((CellAddress) address);

		return element;
	}

	/**
	 * Gets the {@link Worksheet} object from a {@link Spreadsheet} specified by
	 * a String in A1 Notion.
	 * 
	 * @param a1NotionPart
	 * @param spreadsheet
	 * @return
	 */
	private static Worksheet getWorksheet(String a1NotionPart,
			Spreadsheet spreadsheet) {
		Worksheet sheet = null;

		// If the first letter ist a letter, treat part as worksheet name, else
		// as index
		if (Character.isLetter(a1NotionPart.charAt(0))) {
			sheet = spreadsheet.getWorksheetFor(a1NotionPart);
		} else if (Character.isDigit(a1NotionPart.charAt(0))) {
			int sheetIndex = 1;
			try {
				sheetIndex = Integer.parseInt(a1NotionPart);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
			sheet = spreadsheet.getWorksheetAt(sheetIndex);
		}
		return sheet;
	}

	/**
	 * Creates the {@link AbstractAddress} from a {@link Spreadsheet} specified
	 * by a String in A1 Notion.
	 * 
	 * @param a1Notion
	 * @return
	 */
	private static AbstractAddress createCellAddress(String a1Notion) {
		CellAddress address = null;

		if (Character.isLetter(a1Notion.charAt(0))
				&& Character.isDigit(a1Notion.charAt(a1Notion.length() - 1))) {
			Integer horizontalIndex = 0;
			Integer verticalIndex = 0;

			// First convert the letters to the column index
			int i = 0;
			char currentChar = a1Notion.charAt(i);

			while (i < a1Notion.length() && Character.isLetter(currentChar)) {
				horizontalIndex += characterToColumnIndex(currentChar);
				i++;
				currentChar = a1Notion.charAt(i);
			}

			// Then convert the number part to the row index
			if (Character.isDigit(currentChar)) {
				verticalIndex = Integer.parseInt(a1Notion.substring(i));
			}

			address = new CellAddress();
			address.setColumnIndex(horizontalIndex);
			address.setRowIndex(verticalIndex);
		}

		return address;
	}

	/**
	 * Calculates the columnindex from a A1 notion string
	 * 
	 * @param character
	 * @return
	 */
	private static int characterToColumnIndex(char character) {
		return Character.getNumericValue(character)
				- Character.getNumericValue('A') + 1;
	}

}
