package sif.model.policy.policyrule.implementations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import sif.model.policy.policyrule.MonolithicPolicyRule;
import sif.model.policy.policyrule.PolicyRuleType;
import sif.model.policy.policyrule.configuration.ConfigurableParameter;
import sif.utilities.Translator;
import sif.utilities.XML_Constants;

/**
 * A policy rule that defines that references must be readable in configurable
 * directions.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlType(name = XML_Constants.NAME_READING_DIRECTION_POLICY_RULE, propOrder = { 
		"mustBeLeftToRightReadable",
		"mustBeTopToBottomReadable",
		"ignoredCells"
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ReadingDirectionPolicyRule extends MonolithicPolicyRule {

	@ConfigurableParameter(parameterClass = Boolean.class, displayedName = "Readable from left to right.", description = "Defines if the spreadsheet must be readable from left to right.")
	private Boolean mustBeLeftToRightReadable = true;

	@ConfigurableParameter(parameterClass = Boolean.class, displayedName = "Readable from top to bottom.", description = "Defines if the spreadsheet must be readable from top to bottom.")
	private Boolean mustBeTopToBottomReadable = true;

	@ConfigurableParameter(parameterClass = String[].class, displayedName = "Ignored Cells.", description = "Defines the cells that are allowed to reference against reading direction.")
	private String[] ignoredCells = {};

	public ReadingDirectionPolicyRule() {
		super();
		setAuthor("Sebastian Zitzelsberger");
		setName(Translator.instance.tl("PolicyReadingDirection.0001", "Reading direction"));
		setDescription(Translator.instance.tl("PolicyReadingDirection.0002", "Checks whether the spreadsheet can be read in configurable directions."));
		setType(PolicyRuleType.STATIC);
		
		setBackground(Translator.instance.tl("PolicyReadingDirection.0003",
				"Formula cells should only reference cells to the left or above as this is the normal western reading direction."));
		setPossibleSolution(Translator.instance.tl("PolicyReadingDirection.0004", "Move the cell with the formula or the cells referenced by it."));
		
	}


	@XmlElement(name = XML_Constants.NAME_READING_DIRECTION_LEFT_TO_RIGHT, type = Boolean.class)
	public Boolean getMustBeLeftToRightReadable() {
		return mustBeLeftToRightReadable;
	}

	public void setMustBeLeftToRightReadable(Boolean mustBeLeftToRightReadable) {
		this.mustBeLeftToRightReadable = mustBeLeftToRightReadable;
	}

	@XmlElement(name = XML_Constants.NAME_READING_DIRECTION_TOP_TO_BOTTOM, type = Boolean.class, required = false)
	public Boolean getMustBeTopToBottomReadable() {
		return mustBeTopToBottomReadable;
	}

	public void setMustBeTopToBottomReadable(Boolean mustBeTopToBottomReadable) {
		this.mustBeTopToBottomReadable = mustBeTopToBottomReadable;
	}

	@XmlElementWrapper(name = XML_Constants.NAME_IGNORED_CELLS_WRAPPER, required = false)
	@XmlElements({ @XmlElement(name = XML_Constants.NAME_IGNORED_CELLS_VALUE, type = String.class) })
	public String[] getIgnoredCells() {
		return ignoredCells;
	}

	public void setIgnoredCells(String[] ignoredCells) {
		this.ignoredCells = ignoredCells;
	}

}
