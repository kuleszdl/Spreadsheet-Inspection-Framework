package sif.model.policy.policyrule;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import sif.model.policy.classification.ILeafCategory;
import sif.model.policy.policyrule.configuration.PolicyRuleConfiguration;
import sif.technicalDepartment.equipment.testing.facilities.types.CompositeTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/***
 * A policy rule defines regulations that must be met by a spreadsheet. New
 * policy rules can be created by extending {@link CompositePolicyRule} or
 * {@link MonolithicPolicyRule}. The conformity of a spreadsheet with a policy
 * rule is checked by an {@link MonolithicTestFacility} or a
 * {@link CompositeTestFacility}.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractPolicyRule {
	@XmlAttribute(required=true)
	private String name = "N.A.";
	
	@XmlTransient
	private String description = "N.A.";
	
	@XmlTransient
	private String background = "N.A.";
	
	@XmlAttribute
	private String author = "N.A.";
	
	@XmlTransient
	private String possibleSolution = "N.A.";
	
	@XmlTransient
	private ILeafCategory category;
	
	@XmlAttribute
	private Double severityWeight = 1.0;
	
	@XmlTransient
	private PolicyRuleConfiguration configuration = null;

	/**
	 * Gets the author that created the policy
	 * 
	 * @return The author of the policy.
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Gets the description of the background of the policy.
	 * 
	 * @return The background description.
	 */
	public String getBackground() {
		return this.background;
	}

	/***
	 * Gets the category of the policy rule.
	 * 
	 * @return
	 */
	public ILeafCategory getCategory() {
		return category;
	}

	/**
	 * Gets the configuration of the policy rule.
	 */
	public PolicyRuleConfiguration getConfiguration() {
		return configuration;
	}

	/***
	 * Gets the description of the policy rule.
	 * 
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the name of the policy rule.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the description to a possible solution to solve violations of the
	 * policy rule.
	 * 
	 * @return The description of a possible solution.
	 */
	public String getPossibleSolution() {
		return possibleSolution;
	}

	/**
	 * Gets the severity weight for the policy rule.
	 * 
	 * @return the severity weight set for the policy rule.
	 */
	public double getSeverityWeight() {
		return severityWeight;
	}

	/**
	 * Sets the author that created the policy rule.
	 * 
	 * @param author
	 *            The author of the policy rule.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public void setCategory(ILeafCategory category) {
		this.category = category;
	}

	public void setConfiguration(PolicyRuleConfiguration configuration) {
		this.configuration = configuration;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPossibleSolution(String possibleSolution) {
		this.possibleSolution = possibleSolution;
	}

	/**
	 * Sets the given value as the severity weight for the policy rule. Negative
	 * values will be ignored.
	 * 
	 * @param severityWeight
	 */
	public void setSeverityWeight(Double severityWeight) {
		if (severityWeight >= 0.0) {
			this.severityWeight = severityWeight;
		}

	}
	
	/**
	 * Gets the type of the rule
	 * 
	 * @return the type
	 */
	public abstract PolicyRuleType getType();

}