package sif.model.violations.single;

public class OneAmongOthersSingleViolation extends GenericSingleViolation{


	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append(getCausingElement().getStringRepresentation());
		description.append(" might have a false type");
		return description.toString();
	}



}
