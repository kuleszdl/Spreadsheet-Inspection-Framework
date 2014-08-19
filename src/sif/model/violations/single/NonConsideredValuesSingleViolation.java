package sif.model.violations.single;

public class NonConsideredValuesSingleViolation extends GenericSingleViolation{


	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append("The constant ");
		description.append(getCausingElement().getStringRepresentation());
		description.append(" is not referenced");
		return description.toString();
	}


}
