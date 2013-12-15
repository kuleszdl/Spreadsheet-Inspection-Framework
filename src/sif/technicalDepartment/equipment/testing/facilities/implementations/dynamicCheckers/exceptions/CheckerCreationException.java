package sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions;

/**
 * An Exception that nests an exception thrown during the creation of a checker
 * via reflection.
 * 
 * @author Manuel Lemcke
 * 
 */
public class CheckerCreationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5302614125219544008L;

	public CheckerCreationException() {
		// TODO Auto-generated constructor stub
	}

	public CheckerCreationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CheckerCreationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public CheckerCreationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
