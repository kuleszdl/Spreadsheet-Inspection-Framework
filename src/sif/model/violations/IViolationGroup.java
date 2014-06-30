package sif.model.violations;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sif.model.violations.groups.GenericViolationGroup;
import sif.utilities.XML_Constants;

/***
 * A violation group is a set of single violations that share specific criteria
 * and thus should be presented as a group. If a custom violation group is
 * needed, it can be realized by implementing this interface. Otherwise the
 * {@link GenericViolationGroup} can be used.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlJavaTypeAdapter(IViolation.Adapter.class)
@XmlType(name = XML_Constants.NAME_GROUP_VIOLATION)
public interface IViolationGroup extends IViolation {

	/***
	 * Adds the given single violation to the group.
	 * 
	 * @param violation
	 *            The given single violation.
	 */
	public void add(ISingleViolation violation);

	/***
	 * Gets all single violations that have been added to the group.
	 * 
	 * @return The list of single violations belonging to this group.
	 */
	public ArrayList<ISingleViolation> getMembers();

}
