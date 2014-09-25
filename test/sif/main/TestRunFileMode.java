package sif.main;

import org.junit.Test;

public class TestRunFileMode {

	private static final String filePath = "test/sif/testdata/TestSanity.xls",
			policyPath = "test/sif/testdata/TestSanityPolicy.xml";

	@Test
	public void testRunFileMode(){
		Application.main(new String[] { "file", "xml", policyPath, filePath });
	}


}
