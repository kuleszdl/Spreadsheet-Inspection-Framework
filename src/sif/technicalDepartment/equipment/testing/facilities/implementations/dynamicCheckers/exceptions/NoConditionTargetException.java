package sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions;


/**
 * This exception is thrown, if a condition has no target and no elementType.
 * 
 * @author Manuel Lemcke
 *
 */
public class NoConditionTargetException extends IncompleteConditionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9125585820488872069L;

	public NoConditionTargetException() {
	}

	public NoConditionTargetException(String message) {
		super(message);
	}

	public NoConditionTargetException(String message, Throwable cause) {
		super(message, cause);
	}

}
