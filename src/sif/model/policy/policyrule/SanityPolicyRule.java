package sif.model.policy.policyrule;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import sif.model.policy.policyrule.configuration.ConfigurableParameter;
import sif.utilities.XML_Constants;
@XmlType(name = XML_Constants.NAME_SANITY_POLICY_RULE, propOrder = { 
		XML_Constants.NAME_SANITY_VALUE_WRAPPER, 
		XML_Constants.NAME_SANITY_CONSTRAINT_WRAPPER,
		XML_Constants.NAME_SANITY_EXPLANATION_WRAPPER,  
		XML_Constants.NAME_SANITY_CHECKING_WRAPPER,
		XML_Constants.NAME_SANITY_WARNINGS }
)
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SanityPolicyRule extends MonolithicPolicyRule{


	@ConfigurableParameter(parameterClass = ArrayList.class, displayedName = "Location of the header cells of the values", description = "Defines the value location 'worksheet!ColumnCell'.")
	private ArrayList<String> sanityValueCells = new ArrayList<String>();
	@ConfigurableParameter(parameterClass = ArrayList.class, displayedName = "Location of the header cells of the constraints", description = "Defines the constraint location 'worksheet!ColumnCell'.")
	private ArrayList<String> sanityConstraintCells = new ArrayList<String>();
	@ConfigurableParameter(parameterClass = ArrayList.class, displayedName = "Location of the header cells of the explanations", description = "Defines the explanation location 'worksheet!ColumnCell'.")
	private ArrayList<String> sanityExplanationCells = new ArrayList<String>();
	@ConfigurableParameter(parameterClass = ArrayList.class, displayedName = "Location of the header cells which should be checked", description = "Defines the checking location 'worksheet!ColumnCell'.")
	private ArrayList<String> sanityCheckingCells = new ArrayList<String>();
	
	@ConfigurableParameter(parameterClass = Boolean.class, displayedName = "Warn when a column without constraints is encountered or if an unknown value is used.", description = "Defines whether to warn when a column is neither being checked nor imposes restrictions.")
	private boolean sanityWarnings;
	
	@XmlElementWrapper(name = XML_Constants.NAME_SANITY_VALUE_WRAPPER)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_SANITY_LOCATION_NAME, type = String.class) })
	public ArrayList<String> getSanityValueCells(){
		return sanityValueCells;
	}
	
	@XmlElementWrapper(name = XML_Constants.NAME_SANITY_CONSTRAINT_WRAPPER)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_SANITY_LOCATION_NAME, type = String.class) })
	public ArrayList<String> getSanityConstraintCells() {
		return sanityConstraintCells;
	}


	public void setSanityConstraintCells(ArrayList<String> sanityConstraintCells) {
		this.sanityConstraintCells = sanityConstraintCells;
	}

	@XmlElementWrapper(name = XML_Constants.NAME_SANITY_EXPLANATION_WRAPPER)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_SANITY_LOCATION_NAME, type = String.class) })
	public ArrayList<String> getSanityExplanationCells() {
		return sanityExplanationCells;
	}


	public void setSanityExplanationCells(ArrayList<String> sanityExplanationCells) {
		this.sanityExplanationCells = sanityExplanationCells;
	}

	@XmlElementWrapper(name = XML_Constants.NAME_SANITY_CHECKING_WRAPPER)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_SANITY_LOCATION_NAME, type = String.class) })
	public ArrayList<String> getSanityCheckingCells() {
		return sanityCheckingCells;
	}


	public void setSanityCheckingCells(ArrayList<String> sanityCheckingCells) {
		this.sanityCheckingCells = sanityCheckingCells;
	}

	@XmlElement(name = XML_Constants.NAME_SANITY_WARNINGS, type = Boolean.class)
	public boolean isSanityWarnings() {
		return sanityWarnings;
	}


	public void setSanityWarnings(boolean sanityWarnings) {
		this.sanityWarnings = sanityWarnings;
	}




	public void setSanityValueCells(ArrayList<String> sanityValueCells) {
		this.sanityValueCells = sanityValueCells;
	}



	
	public SanityPolicyRule() {
		super(); 
		setName("Plausibility checks");
		setDescription("Checks whether the to be checked values are conform with the given plausible combinations are.");
		setPossibleSolution("Correcting obvious typing mistakes, consult the explanation or additional research is required.");
	}

	@Override
	public PolicyRuleType getType() {
		return PolicyRuleType.SANITY;
	}
}
