package sif.model.violations;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import sif.model.violations.lists.ViolationList;
import sif.utilities.XML_Constants;

/***
 * Container class to store all violations that have been found during an
 * inspection.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlType(propOrder = { 
		"violationLists"
})
@XmlAccessorType(XmlAccessType.NONE)
public class Findings {

	private TreeMap<String, ViolationList> violationLists;

	/***
	 * Creates a new empty findings object.
	 */
	public Findings() {
		violationLists = new TreeMap<String, ViolationList>();
	}

	/***
	 * Adds the given violation list. If the {@link ViolationList} has a name
	 * this name is used as the key in the {@link TreeMap}. Else the canonical 
	 * classname of the {@link AbstractPolicyRule} is used as key.
	 * 
	 * @param violationList
	 *            The given violation list.
	 */
	public void add(ViolationList violationList) {
		if (violationList.getPolicyRule().getName() != null 
				&& violationList.getPolicyRule().getName() != "") {
			violationLists.put(violationList.getPolicyRule().getName(), violationList);	
		} else {
			String ruleClass = violationList.getClass().getCanonicalName();
			violationLists.put(ruleClass, violationList);
		}
	}

	/***
	 * Gets the number of all top level violations, that is the number of all
	 * violation groups plus the number of all single violations that are not
	 * part of a group.
	 * 
	 * @return The number of top level violations.
	 */
	public Integer getNumberOfTopLevelViolations() {
		Integer number = 0;
		for (ViolationList violationList : violationLists.values()) {
			number = number + violationList.getNumberOfViolations();
		}
		return number;
	}

	/***
	 * Gets the number of all violation groups.
	 * 
	 * @return The number of all violation groups.
	 */
	public Integer getNumberOfViolationGroups() {
		Integer number = 0;
		for (ViolationList violationList : violationLists.values()) {
			number = number + violationList.getNumberOfViolationGroups();
		}
		return number;
	}

	/***
	 * Gets the number of all violations.
	 * 
	 * @return The number of all violations.
	 */
	public Integer getNumberOfViolations() {
		Integer number = 0;
		for (ViolationList violationList : violationLists.values()) {
			number = number + violationList.getNumberOfSingleViolations();
		}
		return number;
	}

	/***
	 * Gets the violation lists.
	 * 
	 * @return The violation lists.
	 */
//	@XmlElementWrapper(name = XML_Constants.NAME_FINDINGS)
//	@XmlElements({ 
		@XmlElement(name = XML_Constants.NAME_FINDINGS_VIOLATIONLIST, type = ViolationList.class) 
//	})
	public ArrayList<ViolationList> getViolationLists() {
		return new ArrayList<ViolationList>(violationLists.values());
	}

}
