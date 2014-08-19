package sif.model.violations.single;

import sif.model.elements.basic.reference.AbstractReference;

public class MultipleSameRefSingleViolation extends GenericSingleViolation{
	private int count;

	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append("The reference ");
		description.append(getCausingElement().getStringRepresentation());
		description.append(" appears " + count + " times");
		if (getCausingElement() instanceof AbstractReference){
			description.append(" in ");
			description.append(((AbstractReference) getCausingElement()).getContainer().getStringRepresentation());
		}
		description.append(".");
		return description.toString();
	}

	public void setCount(int count) {
		this.count = count;
	}



}
