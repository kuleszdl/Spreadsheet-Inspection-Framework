package sif.technicalDepartment.equipment.testing.instruments.types;

import sif.model.elements.IElement;
import sif.model.violations.ISingleViolation;

public abstract class AbstractTestInstrument<ElementType extends IElement> {
	private DeterminantEnum determinant;

	@SuppressWarnings("unchecked")
	protected ElementType castToElementTyp(IElement element) {
		return (ElementType) element;
	}

	protected Boolean evaulateWithDeterminant(Boolean violated) {
		Boolean isViolated = violated;
		switch (determinant) {
		case MUST_NOT:
			isViolated = true ^ violated;
		case MUST:
			break;
		default:
			break;
		}
		return isViolated;
	}

	public abstract ISingleViolation getCachedViolation();

	public abstract Class<ElementType> getElementClass();

	public void setDeterminant(DeterminantEnum determinant) {
		this.determinant = determinant;
	}

	protected abstract Boolean test(ElementType elementType);

	public Boolean testIElement(IElement element) {
		return test(castToElementTyp(element));
	}
}