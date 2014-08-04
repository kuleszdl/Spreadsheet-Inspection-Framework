package sif.utilities;

import sif.model.elements.IElement;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.elements.basic.tokencontainers.ITokenContainer;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.elements.basic.worksheet.Row;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.elements.containers.AbstractElementList;
import sif.model.inspection.SpreadsheetInventory;
import sif.model.violations.Findings;
import sif.model.violations.ISingleViolation;
import sif.model.violations.IViolation;
import sif.model.violations.IViolationGroup;
import sif.model.violations.lists.ViolationList;

/***
 * PrintUtilities offers some methods to print out elements from the SIF-model
 * to the console.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public final class PrintUtilities {

	/***
	 * Prints the name and number of the given list of elements as well as the
	 * string representation of all contained elements.
	 * 
	 * @param list
	 *            The given element list.
	 */
	public static void print(AbstractElementList<?> list) {
		System.out.print(list.getElementClass().getSimpleName());
		System.out.print(" (");
		System.out.print(list.getNumberOfElements());
		System.out.print(") :");
		System.out.println();
		for (IElement element : list.getElements()) {
			System.out.println(element.getStringRepresentation());
		}

	}

	/***
	 * Prints out all violation lists contained in the given findings
	 * 
	 * @param findings
	 *            The given findings.
	 */
	public static void print(Findings findings) {
		for (ViolationList violationsList : findings.getViolationLists()) {
			System.out.println("PolicyRule: "
					+ violationsList.getPolicyRule().getName());
			print(violationsList);
			System.out.println();
			System.out.println();
		}

	}

	/***
	 * Prints the string representation of the given element.
	 * 
	 * @param element
	 *            The given element.
	 */
	public static void print(IElement element) {
		System.out.print(element.getStringRepresentation());
	}

	/***
	 * Prints the description of the given single violation.
	 * 
	 * @param violation
	 *            The given single violation.
	 */
	public static void print(ISingleViolation violation) {
		System.out.println(violation.getDescription());
	}

	/***
	 * Prints the given violation according to its type as a single or group
	 * violation.
	 * 
	 * @param violation
	 *            The given violation.
	 */
	public static void print(IViolation violation) {
		if (violation instanceof ISingleViolation) {
			System.out.println("Single Violation:");
			print((ISingleViolation) violation);
		} else {
			print((IViolationGroup) violation);
		}
	}

	/***
	 * Prints the given violation group.
	 * 
	 * @param violationGroup
	 *            The given violation group.
	 */
	public static void print(IViolationGroup violationGroup) {
		System.out.println("Violation Group");
		System.out.println(violationGroup.getDescription());
		for (ISingleViolation singleViolation : violationGroup.getMembers()) {
			print(singleViolation);
		}

	}

	/***
	 * Prints all rows for all worksheets in the given spreadsheet.
	 * 
	 * @param spradsheet
	 *            The given {@link Spreadsheet#}
	 */
	public static void print(Spreadsheet spradsheet) {
		for (Worksheet worksheet : spradsheet.getWorksheets()) {
			System.out.println();
			System.out.println();
			System.out.println("(" + worksheet.getIndex() + ")"
					+ worksheet.getName());
			System.out.println();
			for (Row row : worksheet.getRows()) {
				print(row);
				System.out.println();
			}
		}
	}

	/***
	 * Prints all element list from the given inventory.
	 * 
	 * @param inventory
	 *            The given inventory.
	 */
	public static void print(SpreadsheetInventory inventory) {
		for (AbstractElementList<?> list : inventory.getElementLists()) {
			print(list);

		}
	}

	/***
	 * Prints all violations from the given violation list.
	 * 
	 * @param violationList
	 *            The given violation list.
	 */
	public static void print(ViolationList violationList) {
		for (IViolation violation : violationList.getViolations()) {
			print(violation);
		}
	}

	/**
	 * Prints the token and it's subtokens as a simple horizontal tree <br>
	 * token <br> - subtoken <br> -- subsubtoken <br>
	 * and so on
	 * @param depth to start, usually 0
	 * @param form the "root" token to print
	 */
	public static void printTree(int depth, IElement form){
		
		for (int k = 0; k < depth; k++)
			System.out.print("-");
		
		System.out.println(" " + form.getStringRepresentation());
		if (form instanceof ITokenContainer){
			for (ITokenElement ele : ((ITokenContainer) form).getTokens())
				printTree(depth + 1, ele);

		}
	}

}
