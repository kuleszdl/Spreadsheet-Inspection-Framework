package sif;

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
	TestSanityTestFacility.class,
	DataFacadeTest.class,
	POIWriterTest.class,
	SpreadsheetNumericReadTest.class,
	XMLReadTest.class,
	XMLWriteTest.class})
public class TestSuite {

}
