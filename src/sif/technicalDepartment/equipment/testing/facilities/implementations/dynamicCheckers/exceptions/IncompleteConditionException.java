/**
 * 
 */
package sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions;

/**
 * Indicates that a condition does not contain all properties to be checked
 * properly.
 * 
 * @author Manuel Lemcke
 * 
 */
public class IncompleteConditionException extends Exception {

	private static final long serialVersionUID = -1673418932679417538L;

	/**
	 * Constructs a new exception with null as its detail message. The cause is
	 * not initialized, and may subsequently be initialized by a call to {@link
	 * Throwable.initCause(java.lang.Throwable)}.
	 */
	public IncompleteConditionException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to
	 * Throwable.initCause(java.lang.Throwable).
	 * 
	 * @param message
	 *            the detail message.
	 */
	public IncompleteConditionException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * Note that the detail message associated with cause is not automatically
	 * incorporated in this exception's detail message.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause (A null value is permitted, and indicates that the
	 *            cause is nonexistent or unknown.)
	 */
	public IncompleteConditionException(String message, Throwable cause) {
		super(message, cause);
	}

}
