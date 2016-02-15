package sif.model.violations.single;

import sif.model.elements.basic.reference.AbstractReference;
import sif.utilities.Translator;

public class MultipleSameRefSingleViolation extends GenericSingleViolation{
	private int count;

	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();
		
		description.append(Translator.instance.tl("PolicyMultipleSameRef.0010", "The formula in this cells cell refers"));
		description.append(" ");
		description.append(count);
		description.append(" ");
		description.append(Translator.instance.tl("PolicyMultipleSameRef.0011","times in a row to the cell"));
		description.append(" ");
		description.append(getCausingElement().getStringRepresentation());
		description.append(". ");
		description.append(".");
		
		if (getCausingElement() instanceof AbstractReference){
			description.append(" ");
			description.append(Translator.instance.tl("PolicyMultipleSameRef.0012","The references are separated by"));
			description.append(": ");
			description.append(((AbstractReference) getCausingElement()).getContainer().getStringRepresentation());
		}
		
		return description.toString();
	}

	public void setCount(int count) {
		this.count = count;
	}



}
