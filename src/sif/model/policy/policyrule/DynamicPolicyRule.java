package sif.model.policy.policyrule;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import sif.model.policy.policyrule.configuration.ConfigurableParameter;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.policy.policyrule.dynamicConditions.BinaryCondition;
import sif.model.policy.policyrule.dynamicConditions.ElementCountCondition;
import sif.model.policy.policyrule.dynamicConditions.TernaryCondition;
import sif.model.policy.policyrule.dynamicConditions.TestInput;
import sif.utilities.Translator;

/**
 * A rule intended for dynamic spreadsheet testing
 * 
 * @author Manuel Lemcke
 * 
 */
@XmlSeeAlso(AbstractCondition.class)
@XmlType(name = "rule", propOrder = { "invariants", "testInputs",
		"postconditions" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DynamicPolicyRule extends MonolithicPolicyRule {
	@ConfigurableParameter(parameterClass = ArrayList.class, displayedName = "test inputs", description = "Configures the test setup.")
	private ArrayList<TestInput> testInputs = new ArrayList<TestInput>();

	@ConfigurableParameter(parameterClass = ArrayList.class, displayedName = "post conditions", description = "Configures conditions that have to be met after the test inputs have been inserted.")
	private ArrayList<AbstractCondition> postconditions = new ArrayList<AbstractCondition>();

	@ConfigurableParameter(parameterClass = ArrayList.class, displayedName = "Invariants", description = "Configures conditions, that have to be met before, during and at the end of the inspection.")
	private ArrayList<AbstractCondition> invariants = new ArrayList<AbstractCondition>();
	
	public DynamicPolicyRule() {
		super();
		// setAuthor("Manuel Lemcke");
		setName(Translator.instance.tl("PolicyScenarios.0001", "Policy Rule: Dynamic Conditions"));
		setDescription(Translator.instance.tl("PolicyScenarios.0002", "\"Executes\" the spreadsheet and checks for several "
				+ "possible conditions."));
		setBackground(Translator.instance.tl("PolicyScenarios.0003", "A testscenario which is built by userdefined conditions. "
				+ "While the process of checking the spreadsheet is executed, "
				+ "that is formulae of the spreadsheet are evaluated."));
		setPossibleSolution(Translator.instance.tl("PolicyScenarios.0004","Check the formulae in the causing cell or region."));
		setType(PolicyRuleType.DYNAMIC);
	}

	// JAXB Annotations that tell JAXB which element type is serialized to which
	// class
	@XmlElementWrapper(name = "invariants")
	@XmlElements({ @XmlElement(name = "compare", type = BinaryCondition.class),
			@XmlElement(name = "interval", type = TernaryCondition.class),
			@XmlElement(name = "count", type = ElementCountCondition.class) })
	/**
	 * @return the invariants
	 */
	public ArrayList<AbstractCondition> getInvariants() {
		return invariants;
	}

	/**
	 * @param invariants
	 *            the invariants to set
	 */
	public void setInvariants(ArrayList<AbstractCondition> invariants) {
		this.invariants = invariants;
	}

	@XmlElementWrapper(name = "testInputs")
	@XmlElements({ @XmlElement(name = "testInput", type = TestInput.class) })
	/**
	 * @return the testInputs
	 */
	public ArrayList<TestInput> getTestInputs() {
		return testInputs;
	}

	/**
	 * @param testInputs
	 *            the testInputs to set
	 */
	public void setTestInputs(ArrayList<TestInput> testInputs) {
		this.testInputs = testInputs;
	}

	@XmlElementWrapper(name = "postconditions")
	@XmlElements({ @XmlElement(name = "compare", type = BinaryCondition.class),
			@XmlElement(name = "interval", type = TernaryCondition.class),
			@XmlElement(name = "count", type = ElementCountCondition.class) })
	public ArrayList<AbstractCondition> getPostconditions() {
		return postconditions;
	}

	public void setPostconditions(ArrayList<AbstractCondition> pPostconditions) {
		this.postconditions = pPostconditions;
	}




}
