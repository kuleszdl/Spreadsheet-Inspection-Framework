package sif.model.elements.basic.cell;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import sif.model.elements.BasicAbstractElement;
import sif.model.elements.IAddressableElement;
import sif.model.elements.basic.address.AbstractAddress;
import sif.model.elements.basic.address.CellAddress;
import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.basic.reference.IReferencedElement;
import sif.model.elements.basic.reference.IReferencingElement;
import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.utilities.XML_Constants;

/**
 * Representation of a cell.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlType(name = XML_Constants.NAME_OUTPUT_CELL, propOrder = { 
		XML_Constants.NAME_OUTPUT_CELL_LOCATION,
		XML_Constants.NAME_OUTPUT_CELL_CONTENT
})
@XmlAccessorType(XmlAccessType.NONE)
public class Cell extends BasicAbstractElement implements ICellElement,
		IReferencedElement, IReferencingElement, IAddressableElement {

	private CellAddress address;

	private ArrayList<AbstractReference> incomingReferences;
	private ArrayList<AbstractReference> outgoingReferences;

	private CellContentType cellContentType;
	private Boolean booleanContent;
	private String textContent;
	private Double numericContent;
	private String errorContent;

	private Formula formula;

	/***
	 * Creates a new cell with blank content. @see {@link CellContentType}
	 */
	public Cell() {
		this.cellContentType = CellContentType.BLANK;
		this.address = new CellAddress();
		this.incomingReferences = new ArrayList<AbstractReference>();
		this.outgoingReferences = new ArrayList<AbstractReference>();
	}

	@Override
	public void addIncomingReference(AbstractReference reference) {
		this.incomingReferences.add(reference);
	}

	@Override
	public void addOutgoingReference(AbstractReference reference) {
		this.outgoingReferences.add(reference);

	}

	public Boolean containsFormula() {
		return formula != null;
	}

	@Override
	public AbstractAddress getAbstractAddress() {
		return this.address;
	}

	/***
	 * Gets the boolean content of this cell. Returns the boolean value of this
	 * cell if the content type is BOOLEAN, null otherwise.
	 * 
	 * @return The boolean content of this cell.
	 */
	public Boolean getBooleanContent() {
		Boolean content = null;
		if (cellContentType == CellContentType.BOOLEAN) {
			content = booleanContent;
		}
		return content;
	}

	@Override
	public Cell getCell() {
		return this;
	}

	/***
	 * Gets the address of this cell.
	 * 
	 * @return The address of this cell.
	 */
	public CellAddress getCellAddress() {
		return this.address;
	}

	public CellContentType getCellContentType() {
		return cellContentType;
	}

	/***
	 * Gets the column index of this cell.
	 * 
	 * @return The column index of this cell. (1-based).
	 */
	public Integer getColumnIndex() {
		return this.address.getColumnIndex();
	}

	public String getDescription() {
		return null;
	}

	public String getErrorContent() {
		return errorContent;
	}

	public Formula getFormula() {
		return this.formula;
	}

	@Override
	public ArrayList<AbstractReference> getIncomingReferences() {
		return incomingReferences;
	}

	@XmlAttribute(name = XML_Constants.NAME_OUTPUT_CELL_LOCATION)
	@Override
	public String getLocation() {
		return address.getFullAddress();
	}

	public Double getNumericContent() {
		return numericContent;
	}

	@Override
	public ArrayList<AbstractReference> getOutgoingReferences() {
		return outgoingReferences;
	}

	/***
	 * Gets the row index of this cell.
	 * 
	 * @return The row index of this cell. (1-based)
	 */
	public Integer getRowIndex() {
		return this.address.getRowIndex();
	}

	@Override
	public String getStringRepresentation() {
		StringBuilder builder = new StringBuilder();

		builder.append(getCellContentType().toString().toLowerCase());
		builder.replace(0, 1, Character.toString(builder.charAt(0))
				.toUpperCase());

		builder.append("-Cell ");

		builder.append("[");

		if (containsFormula()) {
			builder.append("Formula]: ");
		} else {
			builder.append("Constant]: ");
		}
		builder.append(getValueAsString());

		return builder.toString();
	}

	public String getTextContent() {
		return textContent;
	}

	@Override
	public String getValueAsString() {

		String cellContent = null;
		switch (cellContentType) {
		case BLANK:
			cellContent = "BLANK";
			break;
		case BOOLEAN:
			cellContent = booleanContent.toString().toUpperCase();
			break;
		case ERROR:
			// TODO: Implement.
			break;
		case NUMERIC:
			if (Math.floor(numericContent) == numericContent) {
				cellContent = ((Integer) numericContent.intValue()).toString();
			} else {
				cellContent = Double.toString(numericContent);
			}
			break;
		case TEXT:
			cellContent = textContent;
		default:
			break;
		}

		return cellContent;
	}

	public Worksheet getWorksheet() {
		return this.address.getWorksheet();
	}

	@Override
	public Boolean isReferenced(IReferencedElement referencedElement) {
		Boolean isReferenced = false;
		for (AbstractReference outgoingReference : outgoingReferences) {
			if (outgoingReference.getReferencedElement().equals(
					referencedElement)) {
				isReferenced = true;
			} else {
				if (outgoingReference.getReferencedElement() instanceof IReferencingElement) {
					isReferenced = ((IReferencingElement) outgoingReference
							.getReferencedElement())
							.isReferenced(referencedElement);
				}
			}

		}
		return isReferenced;
	}

	public void setBooleanContent(Boolean booleanContent) {
		this.booleanContent = booleanContent;
		this.cellContentType = CellContentType.BOOLEAN;
	}

	public void setColumnIndex(Integer columnIndex) {
		this.address.setColumnIndex(columnIndex);
	}

	public void setErrorContent(String errorContent) {
		this.errorContent = errorContent;
		this.cellContentType = CellContentType.ERROR;
	}

	public void setFormula(Formula formula) {
		this.formula = formula;
	}

	public void setNumericContent(Double numericContent) {
		this.numericContent = numericContent;
		this.cellContentType = CellContentType.NUMERIC;
	}

	public void setRowIndex(Integer rowIndex) {
		this.address.setRowIndex(rowIndex);
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
		this.cellContentType = CellContentType.TEXT;
	}

	public void setWorksheet(Worksheet worksheet) {
		this.address.setWorksheet(worksheet);

	}

	@XmlAttribute(name = XML_Constants.NAME_OUTPUT_CELL_CONTENT)
	public String getContent(){
		return getStringRepresentation();
	}
	

}