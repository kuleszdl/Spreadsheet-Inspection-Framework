package sif.model.violations.single;

public class RefToNulllSingleViolation extends GenericSingleViolation{



	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append(getCausingElement().getStringRepresentation());
		description.append(" contains a reference to a blanc cell.");
		return description.toString();
	}


}
