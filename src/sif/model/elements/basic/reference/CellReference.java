package sif.model.elements.basic.reference;

import sif.model.elements.basic.address.AbstractAddress;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.tokens.ITokenElement;

public class CellReference extends AbstractReference {

	private IReferencingElement referencingElement;
	private Cell referencedCell;
	private Boolean isColumnRelative = true;
	private Boolean isRowRelative = true;

	@Override
	public String getLocation() {
		return referencingElement.getLocation();
	}

	public Cell getReferencedCell() {
		return this.referencedCell;
	}

	@Override
	public IReferencedElement getReferencedElement() {
		return referencedCell;
	}

	@Override
	public IReferencingElement getReferencingElement() {
		return referencingElement;
	}

	@Override
	public String getStringRepresentation() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cellreference [" + getValueAsString() + "]");
		return builder.toString();
	}

	@Override
	public String getValueAsString() {
		StringBuilder value = new StringBuilder("=");
		if (isReferenceToExternWorksheet()) {
			value.append(referencedCell.getWorksheet().getName() + "!");
		}
		if (!isColumnRelative) {
			value.append('$');
		}
		value.append(AbstractAddress.getCharacterFor(referencedCell
				.getColumnIndex()));
		if (!isRowRelative) {
			value.append('$');
		}
		value.append(referencedCell.getRowIndex());

		return value.toString();
	}

	public Boolean isColumnRelative() {
		return isColumnRelative;
	}

	@Override
	public Boolean isReferenceToExternWorksheet() {
		return !referencedCell.getWorksheet().equals(
				referencingElement.getAbstractAddress().getWorksheet());
	}

	public Boolean isRowRelative() {
		return isRowRelative;
	}

	@Override
	public Boolean isSameAs(ITokenElement token) {
		Boolean same = true;
		if (token instanceof CellReference) {
			CellReference otherReference = (CellReference) token;
			
			if (otherReference.isColumnRelative != isColumnRelative){
				same = false;
			}
			if (otherReference.isRowRelative() != isRowRelative){
				same = false;
			}
			Cell ownRefCell = getReferencedCell();
			Cell otherRefCell = otherReference.getReferencedCell();
			if (otherRefCell.getWorksheet() != ownRefCell.getWorksheet()){
				same = false;
			}
			if (otherRefCell.getAbstractAddress().getWorksheet() != 
					ownRefCell.getAbstractAddress().getWorksheet()) {
				same = false;
			}
			if (!otherRefCell.getAbstractAddress().isWithin(ownRefCell.getAbstractAddress()) ||
					!ownRefCell.getAbstractAddress().isWithin(otherRefCell.getAbstractAddress())){
				same = false;
			}
		} else {
			same = false;
		}
		return same;
	}

	public void setColumnRelative(Boolean relative) {
		isColumnRelative = relative;
	}

	@Override
	public void setReferencedElement(IReferencedElement referencedElement) {
		if (referencedElement instanceof Cell) {
			this.referencedCell = (Cell) referencedElement;
			referencedElement.addIncomingReference(this);
		}

	}

	@Override
	public void setReferencingElement(IReferencingElement referencingElement) {
		this.referencingElement = referencingElement;
		referencingElement.addOutgoingReference(this);
	}

	public void setRowRelative(Boolean relative) {
		isRowRelative = relative;
	}

}
