/**
 * 
 */
package sif.IO;

import java.io.File;

import org.junit.Test;

import sif.IO.xml.SifMarshaller;
import sif.frontOffice.FrontDesk;
import sif.model.policy.DynamicPolicy;
import sif.model.policy.PolicyList;

/**
 * @author Manuel Lemcke
 *
 */
public class XMLWriteTest {

	private final static File inputFile = new File(
			"test/sif/testdata/bafoeg-rueckzahlung.xls");
	
	/**
	 * Test method for {@link sif.IO.xml.SifMarshaller#marshal(sif.model.policy.DynamicPolicy, java.io.File)}.
	 * @throws Exception 
	 */
	@Test
	public void testMarshal() throws Exception {
		FrontDesk desk = FrontDesk.getInstance();		
		
		DynamicPolicy policy;
		policy = desk.scanAndCreatePolicy("ioTestReq1", inputFile); 
		
		File file = new File(
				"test/sif/testdata/marshalledPolicy.xml");
		PolicyList list = new PolicyList();
		list.setDynamicPolicy(policy);
		SifMarshaller.marshal(list, file, "src/sif/model/schema/SpRuDeL_1_4_Request.xsd");
	}

}
