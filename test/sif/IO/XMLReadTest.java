/**
 * 
 */
package sif.IO;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import sif.IO.xml.SifMarshaller;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.PolicyList;
import sif.model.policy.policyrule.AbstractPolicyRule;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.AbstractCondition;
import sif.model.policy.policyrule.dynamicConditions.ETernaryRelation;
import sif.model.policy.policyrule.dynamicConditions.TernaryCondition;

/**
 * Tests if the unmarshalling of XML specifications works properly.
 * 
 * @author Manuel Lemcke
 * 
 */
public class XMLReadTest {

	private static String filePath = "test/sif/testdata/bafoeg2.xml";

	/**
	 * Test method for {@link sif.IO.xml.SifMarshaller#unmarshal(java.io.File)}.
	 * @throws Exception 
	 */
	@Test
	public void testUnmarshal() throws Exception {
		File file = new File(filePath);

		Assert.assertTrue("XML spec. file doesn't exist", file.exists());

		PolicyList polList = SifMarshaller.unmarshal(file);

		assertTrue(polList != null);
		
		DynamicPolicy policy = polList.getCompletePolicy();
		DynamicPolicyRule rule = (DynamicPolicyRule) policy.getRuleByName("between0and10k");
		
		AbstractCondition cond = rule.getPostconditions().get(0);
		
		Assert.assertTrue(cond instanceof TernaryCondition);
		
		cond.getTarget().equals("B34");
		cond.getValue().equals(0);
		((TernaryCondition)cond).getValue2().equals(10000);
		cond.getRelation().equals(ETernaryRelation.closed);
	}

}
