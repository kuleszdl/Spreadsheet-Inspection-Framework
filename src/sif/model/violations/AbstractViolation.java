package sif.model.violations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import sif.model.elements.IElement;
import sif.model.violations.groups.GenericViolationGroup;
import sif.model.violations.single.GenericSingleViolation;
import sif.utilities.XML_Constants;

@XmlType(propOrder = {
		"content",
		"location",
		"description",
		"weightedSeverityValue"
})
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ // include known top level implementations for the xsd
	GenericSingleViolation.class,
	GenericViolationGroup.class})
public abstract class AbstractViolation implements IViolation{


	/***
	 * Gets the description of the violation.
	 * 
	 * @return The description of the violation.
	 */
	@XmlAttribute(name = XML_Constants.NAME_SINGLE_VIOLATION_DESCRIPTION)
	public abstract String getDescription();


	/***
	 * Gets the weighted severity value of this violation.
	 * 
	 * @return The weighted severity value.
	 */
	@XmlAttribute(name = XML_Constants.NAME_SINGLE_VIOLATION_SEVERITY)
	public abstract Double getWeightedSeverityValue();

	@XmlAttribute(name = XML_Constants.NAME_SINGLE_VIOLATION_CAUSING)
	public String getContent(){
		IElement cause = getCausingElement();
		String location = cause.getLocation();
		String causingStr = "Cell " 
				+ location.substring(location.indexOf(']') + 1) 
				+ ", "
				+ cause.getStringRepresentation();

		
		return causingStr;
	}
	
	@XmlAttribute(name = XML_Constants.NAME_SINGLE_VIOLATION_LOCATION)
	public String getLocation(){
		return getCausingElement().getLocation();
	}


}
