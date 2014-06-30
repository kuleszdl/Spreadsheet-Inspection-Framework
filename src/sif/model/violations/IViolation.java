package sif.model.violations;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sif.model.elements.IElement;
import sif.model.policy.policyrule.AbstractPolicyRule;

/***
 * A violation records where a spreadsheet doesn't fulfill the regulations of a
 * policy rule. It stores the policy rule that was violated, the element that
 * caused the violation, a description of the cause and a severity value.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
@XmlJavaTypeAdapter(IViolation.Adapter.class)
public interface IViolation {

	/***
	 * The constant value for very low severity.
	 */
	public static final double SEVERITY_VERY_LOW = 20.0;
	/***
	 * The constant value for low severity.
	 */
	public static final double SEVERITY_LOW = 40.0;
	/***
	 * The constant value for medium severity.
	 */
	public static final double SEVERITY_MEDIUM = 60.0;
	/***
	 * The constant value for high severity.
	 */
	public static final double SEVERITY_HIGH = 80.0;
	/***
	 * The constant value for very high severity.
	 */
	public static final double SEVERITY_VERY_HIGH = 100.0;

	/***
	 * Gets the spreadsheet element that caused this violation.
	 * 
	 * @return
	 */
	public IElement getCausingElement();

	/***
	 * Gets the description of the violation.
	 * 
	 * @return The description of the violation.
	 */
	public String getDescription();

	/***
	 * Gets the violated policy rule.
	 * 
	 * @return The violated policy rule.
	 */
	public AbstractPolicyRule getPolicyRule();

	/***
	 * Gets the weighted severity value of this violation.
	 * 
	 * @return The weighted severity value.
	 */
	public Double getWeightedSeverityValue();

	/***
	 * Gets the spreadsheet element that caused the violation.
	 * 
	 * @param element
	 *            The causing spreadsheet element.
	 */
	public void setCausingElement(IElement element);

	/***
	 * Sets the violated policy rule.
	 * 
	 * @param policyRule
	 *            The violated policy rule.
	 */
	public void setPolicyRule(AbstractPolicyRule policyRule);

	static class Adapter extends XmlAdapter<Object, Object>{

		@Override
		public Object unmarshal(Object v) throws Exception {
			return v;
		}

		@Override
		public Object marshal(Object v) throws Exception {
			return v;
		}
		
	}
}
