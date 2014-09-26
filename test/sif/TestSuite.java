package sif;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sif.IO.DataFacadeTest;
import sif.IO.POIWriterTest;
import sif.IO.SpreadsheetNumericReadTest;
import sif.IO.XMLReadTest;
import sif.IO.XMLWriteTest;
import sif.evaluation.DynamicScenario;
import sif.inspection.ConditionCheckerTest;
import sif.inspection.InputOutputCellScannerTest;
import sif.inspection.IntermediateCellScannerTest;
import sif.inspection.PolicyTest;
import sif.inspection.PreparatorTest;
import sif.inspection.SpreadsheetCreationTest;
import sif.inspection.TestAllStaticPolicies;
import sif.inspection.TestFacilityTest;
import sif.inspection.TestSanityTestFacility;
import sif.main.TestRunFileMode;
import sif.main.TestRunSocketMode;

@RunWith(Suite.class)
@SuiteClasses({ DynamicScenario.class,
	ConditionCheckerTest.class,
	InputOutputCellScannerTest.class,
	IntermediateCellScannerTest.class,
	PolicyTest.class,
	PreparatorTest.class,
	//	SamplesTest.class,
	SpreadsheetCreationTest.class,
	TestAllStaticPolicies.class,
	TestFacilityTest.class,
	TestRunFileMode.class,
	TestRunSocketMode.class,
	TestSanityTestFacility.class,
	DataFacadeTest.class,
	POIWriterTest.class,
	SpreadsheetNumericReadTest.class,
	XMLReadTest.class,
	XMLWriteTest.class})
public class TestSuite {
	private static Logger logger = Logger.getLogger(TestSuite.class);

	@BeforeClass
	public static void changeSystemOut(){
		File f = new File("nul:");
		try {
			System.setOut(new LogStream(f));
		} catch (FileNotFoundException e) {}
		

	}

	private static class LogStream extends PrintStream{

		public LogStream(File f) throws FileNotFoundException {
			super(f);
		}
		@Override
		public void print(String s) {
			logger.trace(s);
		}
	}
}
