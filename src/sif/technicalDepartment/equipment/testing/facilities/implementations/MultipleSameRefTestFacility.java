package sif.technicalDepartment.equipment.testing.facilities.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.operator.Operator;
import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokencontainers.Function;
import sif.model.elements.basic.tokencontainers.ITokenContainer;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.elements.containers.AbstractElementList;
import sif.model.violations.groupors.SameCausingCellGroupor;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.MultipleSameRefSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * the multiple same references pattern.
 *
 * @author Sebastian Beck
 *
 */

public class MultipleSameRefTestFacility extends MonolithicTestFacility{

	private String[] ignoredCells = null;
	private ViolationList violations;
	private static Comparator<AbstractReference> comp;

	private boolean isIgnored(AbstractReference reference) {
		boolean isIgnored = false;
		if (reference.getReferencingElement() instanceof Cell) {
			Cell cell = (Cell) reference.getReferencingElement();
			// getting the location as worksheet!address
			String location = cell.getCellAddress().getSpreadsheetAddress();
			for (String ignoredCell : ignoredCells) {
				// discarding existent $ and = chars from SIFEI
				if (location.equals(ignoredCell.replaceAll("[$=]", ""))) {
					isIgnored = true;
					break;
				}
			}
		}
		return isIgnored;
	}

	/**
	 * Adds all references from the container to the list
	 * @param container with the elements
	 * @param list for the references
	 */
	private void addReferences(ITokenContainer container, ArrayList<AbstractReference> list){
		for (ITokenElement element : container.getTokens()){
			if (element instanceof AbstractReference){
				list.add((AbstractReference) element);
			} else if (element instanceof Function || element instanceof Operator){
				checkContainer(container);
			} else if (element instanceof ITokenContainer){
				addReferences((ITokenContainer) element, list);
			}
		}
	}

	/**
	 * Checks the references of the container whether they exist multiple times
	 * @param container to check
	 */
	private void checkContainer(ITokenContainer container){
		ArrayList<AbstractReference> references = new ArrayList<AbstractReference>();
		// recursively checking each container on its own
		for (ITokenElement inner : container.getTokens()){
			if (inner instanceof AbstractReference){
				references.add((AbstractReference) inner);
			}  else if (inner instanceof Function){
				checkContainer((ITokenContainer) inner);
			} else if (inner instanceof ITokenContainer){
				addReferences((ITokenContainer) inner, references);
			}
		}

		checkReferences(references);
	}

	/**
	 * Checks the given list of references whether any is contained multiple times.
	 * If any violation exists it will be added to the {@link #violations}
	 * @param references
	 */
	private void checkReferences(ArrayList<AbstractReference> references){
		Collections.sort(references, getComparator());
		int count = 0;
		Iterator<AbstractReference> it = references.iterator();
		AbstractReference current = null, next = null;
		while (it.hasNext()){
			next = it.next();
			if (current != null){
				if (next.isSameAs(current)){
					count++;
				} else {
					if (count > 0){
						createViolation(next, count);
						count = 0;
					}
				}
			} 
			current = next;
		}
		if (count > 0){
			createViolation(next, count);
		}
	}

	/**
	 * Creates the Violation and adds it to the {@link #violations}
	 * @param cause the reference which is used multiple  times
	 * @param count how often it was used
	 */
	private void createViolation(AbstractReference cause, int count){
		if (!isIgnored(cause)){
			MultipleSameRefSingleViolation violation = new MultipleSameRefSingleViolation();
			violation.setCausingElement(cause);
			violation.setCount(count);
			violations.add(violation);
		}
	}

	@Override
	public ViolationList run() {
		violations = new ViolationList(getTestedPolicyRule(), new SameCausingCellGroupor());

		AbstractElementList<Formula> formulas = this.inventory.getListFor(Formula.class);
		for (Formula form : formulas.getElements()){
			checkContainer(form);
		}

		return violations;
	}

	/**
	 * Creates the comparator for AbstractReference's, uses the {@link String#compareTo(String)} of the location
	 * @return
	 */
	private Comparator<AbstractReference> getComparator(){
		if (comp == null){
			comp = new Comparator<AbstractReference>() {

				@Override
				public int compare(AbstractReference o1, AbstractReference o2) {
					return o1.getLocation().compareTo(o2.getLocation());
				}
			};
		}
		return comp;
	}
}
