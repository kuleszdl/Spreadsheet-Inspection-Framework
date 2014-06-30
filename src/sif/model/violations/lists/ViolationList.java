package sif.model.violations.lists;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.violations.ISingleViolation;
import sif.model.violations.IViolation;
import sif.model.violations.IViolationGroup;
import sif.model.violations.ViolationGroupor;
import sif.utilities.XML_Constants;

/***
 * A violation list stores all violations of a specific policy rule. The
 * violations can be grouped by providing a {@link ViolationGroupor}.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlType(name = XML_Constants.NAME_FINDINGS_VIOLATIONLIST, propOrder = { 
		"violations",
		"policyRule",
})
@XmlAccessorType(XmlAccessType.NONE)
public class ViolationList {
	@XmlTransient
	private ViolationGroupor violationGroupor;
	@XmlElement
	private ArrayList<IViolation> violations;
	@XmlElement(name = XML_Constants.NAME_VIOLATIONLIST_POLICY)
	private AbstractPolicyRule policyRule;

	/**
	 * Only for jaxb
	 */
	public ViolationList(){
		
	}
	/***
	 * Creates an empty violation list that stores violations of the given
	 * policy rule.
	 * 
	 * @param policyRule
	 *            The given policy rule.
	 */
	public ViolationList(AbstractPolicyRule policyRule) {
		violations = new ArrayList<IViolation>();
		this.policyRule = policyRule;
	}

	/***
	 * Creates an empty violation list for violations of the given policy rule
	 * that will be grouped by the given violation groupor.
	 * 
	 * @param groupor
	 *            The given violation groupor used for grouping the violations
	 *            of this list. If null is passed as a groupor, violations of
	 *            this list will not be grouped.
	 * @param policyRule
	 *            The given policy rule.
	 */
	public ViolationList(AbstractPolicyRule policyRule, ViolationGroupor groupor) {
		this(policyRule);
		this.violationGroupor = groupor;

	}

	/***
	 * Adds the given single violation.
	 * 
	 * @param newViolation
	 *            The given single violation.
	 */
	public void add(ISingleViolation newViolation) {
		Boolean added = false;
		// Violations must be grouped.
		if (violationGroupor != null) {
			// Iterate over all top level violations.
			for (int i = 0; i < violations.size() & !added; i++) {
				IViolation existingViolation = violations.get(i);

				if (violationGroupor.mustBeGrouped(newViolation,
						existingViolation)) {
					violations.remove(existingViolation);
					violations.add(violationGroupor.createGroupFor(
							newViolation, existingViolation));
					added = true;
				}
			}
		}

		// Add violation if it has not been added to a group.
		if (added == false) {
			this.violations.add(newViolation);
		}
	}

	/***
	 * Gets the number of all single violation.
	 * 
	 * @return The number of all violations.
	 */
	public Integer getNumberOfSingleViolations() {
		Integer numberOfAbstractViolations = 0;
		for (IViolation violation : violations) {
			if (violation instanceof IViolationGroup) {
				IViolationGroup group = (IViolationGroup) violation;
				numberOfAbstractViolations = numberOfAbstractViolations
						+ group.getMembers().size();

			} else {
				numberOfAbstractViolations++;
			}
		}
		return numberOfAbstractViolations;
	}

	/***
	 * Gets the number of all violations, that is the number of all violation
	 * groups plus the number of all single violations that are not part of a
	 * group.
	 * 
	 * @return The number of top level violations.
	 */
	public Integer getNumberOfViolations() {
		return violations.size();
	}

	/***
	 * Gets the number of all violation groups.
	 * 
	 * @return The number of all violation groups.
	 */
	public Integer getNumberOfViolationGroups() {
		Integer numberOfGroups = 0;
		for (IViolation violation : violations) {
			if (violation instanceof IViolationGroup) {
				numberOfGroups++;
			}

		}
		return numberOfGroups;
	}

	/***
	 * Gets the violated policy rule.
	 * 
	 * @return The violated policy rule.
	 */
	public AbstractPolicyRule getPolicyRule() {
		return policyRule;
	}

	/**
	 * Gets the list of violations.
	 * 
	 * @return the list of violations.
	 */
	public ArrayList<IViolation> getViolations() {
		return violations;
	}
	

}