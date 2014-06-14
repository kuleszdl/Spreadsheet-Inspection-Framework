package sif.model.policy.policyrule;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import sif.model.policy.policyrule.configuration.ConfigurableParameter;
@XmlType(name = "sanityRules", propOrder = { "sanityValueCells", "sanityConstraintCells",
		"sanityExplanationCells", "sanityCheckingCells", "sanityWarnings" })
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
	
	@XmlElementWrapper(name = "sanityValueCells")
	@XmlElements({ @XmlElement(name = "location", type = String.class) })
	public ArrayList<String> getSanityValueCells(){
		return sanityValueCells;
	}
	
	@XmlElementWrapper(name = "sanityConstraintCells")
	@XmlElements({ @XmlElement(name = "location", type = String.class) })
	public ArrayList<String> getSanityConstraintCells() {
		return sanityConstraintCells;
	}


	public void setSanityConstraintCells(ArrayList<String> sanityConstraintCells) {
		this.sanityConstraintCells = sanityConstraintCells;
	}

	@XmlElementWrapper(name = "sanityExplanationCells")
	@XmlElements({ @XmlElement(name = "location", type = String.class) })
	public ArrayList<String> getSanityExplanationCells() {
		return sanityExplanationCells;
	}


	public void setSanityExplanationCells(ArrayList<String> sanityExplanationCells) {
		this.sanityExplanationCells = sanityExplanationCells;
	}

	@XmlElementWrapper(name = "sanityCheckingCells")
	@XmlElements({ @XmlElement(name = "location", type = String.class) })
	public ArrayList<String> getSanityCheckingCells() {
		return sanityCheckingCells;
	}


	public void setSanityCheckingCells(ArrayList<String> sanityCheckingCells) {
		this.sanityCheckingCells = sanityCheckingCells;
	}

	@XmlElement(name = "sanityWarnings", type = Boolean.class)
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
