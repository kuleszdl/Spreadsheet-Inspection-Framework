package sif.model.violations.single;

import sif.model.elements.IElement;

public class StringDistanceSingleViolation extends GenericSingleViolation{

	private IElement causingRef;
	private IElement nearestMatch;

	@Override
	public String getDescription() {
		StringBuilder description = new StringBuilder();

		description.append(causingRef.getValueAsString());
		description.append(" appears only once, nearest match would be: "
				+ getNearestMatch().getValueAsString());
		return description.toString();
	}

	@Override
	public IElement getCausingElement() {
		return causingRef;
	}

	@Override
	public void setCausingElement(IElement element) {
		this.causingRef = element;

	}

	public IElement getNearestMatch() {
		return nearestMatch;
	}

	public void setNearestMatch(IElement nearestMatch) {
		this.nearestMatch = nearestMatch;
	}

}
