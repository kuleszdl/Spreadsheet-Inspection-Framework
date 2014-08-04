package sif.model.violations.groupors;


import sif.model.elements.IElement;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokencontainers.Function;
import sif.model.elements.basic.tokencontainers.ITokenContainer;
import sif.model.elements.basic.tokens.ScalarConstant;
import sif.model.violations.ISingleViolation;
import sif.model.violations.IViolation;
import sif.model.violations.IViolationGroup;
import sif.model.violations.ViolationGroupor;
import sif.model.violations.groups.GenericViolationGroup;

/**
 * A groupor for Violations that have the same Cell as cause.
 *
 */
public class SameCausingCellGroupor extends ViolationGroupor{

	@Override
	public IViolationGroup createGroupFor(
			ISingleViolation firstSingleViolation,
			ISingleViolation secondSingleViolation) {
		IViolationGroup newViolationGroup = new GenericViolationGroup(
				secondSingleViolation.getPolicyRule());
		newViolationGroup.add(secondSingleViolation);
		newViolationGroup.add(firstSingleViolation);
		return newViolationGroup;

	}

	@Override
	public Boolean mustBeGrouped(ISingleViolation firstSingleViolation,
			ISingleViolation secondSingleViolation) {
		return firstSingleViolation.getCausingElement().getLocation().equals(
				secondSingleViolation.getCausingElement().getLocation());

	}

	@Override
	public Boolean mustBeGrouped(ISingleViolation singleViolation,
			IViolationGroup violationGroup) {
		return violationGroup.getMembers().get(0).getCausingElement().getLocation().equals(
				singleViolation.getCausingElement().getLocation());
	}


	@Override
	public void setGroupElementFor(IViolationGroup violationGroup) {
		for (IViolation vio : violationGroup.getMembers()){
			IElement cause = vio.getCausingElement();
			if (cause instanceof ScalarConstant){
				violationGroup.setCausingElement(((ScalarConstant) cause).getContainer().getCell().getFormula());
			}
			if (cause instanceof Formula){
				violationGroup.setCausingElement(cause);
				break;
			}
			if (cause instanceof Function){
				violationGroup.setCausingElement(((Function) cause).getCell().getFormula());
				break;
			}
			if (cause instanceof Cell){
				violationGroup.setCausingElement(cause);
				break;
			}
			if (cause instanceof ITokenContainer){
				violationGroup.setCausingElement(((ITokenContainer) cause).getCell());
			}
		}
	}

}
