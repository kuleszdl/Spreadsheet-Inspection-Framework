package sif.model.inspection;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import sif.model.elements.AbstractElement;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.elements.containers.AbstractElementList;
import sif.model.elements.containers.SimpleElementList;
import sif.model.elements.custom.InputCell;
import sif.model.elements.custom.IntermediateCell;
import sif.model.elements.custom.OutputCell;
import sif.utilities.XML_Constants;

/***
 * Extended model class to represent spreadsheets.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlType(name = XML_Constants.NAME_INVENTORY, propOrder = { 
		XML_Constants.NAME_INVENTORY_INPUT_WRAPPER,
		XML_Constants.NAME_INVENTORY_INTERMEDIATE_WRAPPER,
		XML_Constants.NAME_INVENTORY_OUTPUT_WRAPPER
})
@XmlAccessorType(XmlAccessType.NONE)
public class SpreadsheetInventory {
	private TreeMap<String, AbstractElementList<?>> elementLists = new TreeMap<String, AbstractElementList<?>>();

	private Spreadsheet spreadsheet;
	private File spreadsheetFile;

	/***
	 * Adds the given list of spreadsheet elements to the inventory. Only one
	 * list per element class can be stored.
	 * 
	 * @param list
	 *            The given list.
	 */
	public void addElementList(AbstractElementList<?> list) {
		this.elementLists.put(list.getElementClass().getCanonicalName(), list);
	}

	/***
	 * Clears all scanned elements from the spreadsheet inventory.
	 */
	public void clearScannedElements() {
		elementLists.clear();
	}

	/***
	 * Gets the list of all element list that have been added to the inventory.
	 * 
	 * @return The list of element lists.
	 */
	public Collection<AbstractElementList<?>> getElementLists() {
		return this.elementLists.values();
	}

	public <Type extends AbstractElement> AbstractElementList<Type> getListFor(
			Class<Type> elementClass) {
		@SuppressWarnings("unchecked")
		AbstractElementList<Type> elementList = (AbstractElementList<Type>) this.elementLists
				.get(elementClass.getCanonicalName());
		if (elementList == null) {
			elementList = new SimpleElementList<Type>(elementClass);
			this.elementLists.put(elementList.getElementClass()
					.getCanonicalName(), (AbstractElementList<?>) elementList);
		}

		return elementList;
	}

	public Spreadsheet getSpreadsheet() {
		return this.spreadsheet;
	}

	public File getSpreadsheetFile() {
		return spreadsheetFile;
	}

	public void setSpreadsheet(Spreadsheet spreadsheet) {
		this.spreadsheet = spreadsheet;
	}

	public void setSpreadsheetFile(File spreadsheetFile) {
		this.spreadsheetFile = spreadsheetFile;
	}
	
	@XmlElementWrapper(name = XML_Constants.NAME_INVENTORY_INPUT_WRAPPER)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_INVENTORY_CELL_NAME, type = Cell.class) })
	public ArrayList<Cell> getInput(){
		ArrayList<InputCell> tmp = getListFor(InputCell.class).getElements();
		ArrayList<Cell> tmp2 = new ArrayList<Cell>(tmp.size());
		for (InputCell c : tmp){
			tmp2.add(c.getCell());
		}
		return tmp2;
	}
	
	@XmlElementWrapper(name = XML_Constants.NAME_INVENTORY_INTERMEDIATE_WRAPPER)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_INVENTORY_CELL_NAME, type = Cell.class) })
	public ArrayList<Cell> getIntermediate(){
		ArrayList<IntermediateCell> tmp = getListFor(IntermediateCell.class).getElements();
		ArrayList<Cell> tmp2 = new ArrayList<Cell>(tmp.size());
		for (IntermediateCell c : tmp){
			tmp2.add(c.getCell());
		}
		return tmp2;
	}
	
	@XmlElementWrapper(name = XML_Constants.NAME_INVENTORY_OUTPUT_WRAPPER)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_INVENTORY_CELL_NAME, type = Cell.class) })
	public ArrayList<Cell> getOutput(){
		ArrayList<OutputCell> tmp = getListFor(OutputCell.class).getElements();
		ArrayList<Cell> tmp2 = new ArrayList<Cell>(tmp.size());
		for (OutputCell c : tmp){
			tmp2.add(c.getCell());
		}
		return tmp2;
	}
}