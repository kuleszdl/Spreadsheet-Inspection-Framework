package sif.model.inspection;

import java.io.File;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import sif.model.policy.Policy;
import sif.model.violations.Findings;
import sif.utilities.XML_Constants;

/**
 * Represents a request from a user of the Spreadsheet Inspection Framework to
 * inspect a spreadsheet.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlRootElement(name = XML_Constants.NAME_INSPECTION_REQUEST)
@XmlType(propOrder = { 
		XML_Constants.NAME_INSPECTION_REQUEST_TITLE,
		XML_Constants.NAME_INSPECTION_REQUEST_FILE,
//		XML_Constants.NAME_OUTPUT_POLICY,
		"inventory", // the inventory as NAME_INVENTORY with input, intermediate and output cells
		XML_Constants.NAME_FINDINGS
})
@XmlAccessorType(XmlAccessType.NONE)
public class InspectionRequest {
	@XmlTransient
	private UUID id;
	@XmlTransient
	private String name;
	@XmlElement(name = XML_Constants.NAME_INVENTORY)
	private SpreadsheetInventory inventory;
//	@XmlElement(name = XML_Constants.NAME_OUTPUT_POLICY)
	@XmlTransient
	private Policy policy;
	@XmlElement
	private Findings findings;
	@XmlTransient
	private File spreadsheetFile;

	public InspectionRequest() {
		this.id = UUID.randomUUID();
	}

	public Findings getFindings() {
		return findings;
	}

	public UUID getId() {
		return this.id;
	}

	public SpreadsheetInventory getInventory() {
		return this.inventory;
	}

	public String getName() {
		return this.name;
	}

	public Policy getPolicy() {
		return this.policy;
	}

	public File getSpreadsheetFile() {
		return spreadsheetFile;
	}

	public void setFindings(Findings findings) {
		this.findings = findings;
	}

	public void setInventory(SpreadsheetInventory inventory) {
		this.inventory = inventory;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public void setSpreadsheetFile(File spreadsheetFile) {
		this.spreadsheetFile = spreadsheetFile;
	}
	
	@XmlAttribute(name = XML_Constants.NAME_INSPECTION_REQUEST_FILE)
	public String getFile(){
		return spreadsheetFile.getAbsolutePath();
	}

	@XmlAttribute(name = XML_Constants.NAME_INSPECTION_REQUEST_TITLE)
	public String getTitle(){
		return spreadsheetFile.getName();
	}
	

}