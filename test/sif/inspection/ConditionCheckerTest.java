package sif.inspection;

import static org.junit.Assert.assertTrue;

import java.beans.IntrospectionException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sif.IO.DataFacade;
import sif.model.elements.IElement;
import sif.model.elements.basic.address.CellAddress;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.cell.CellContentType;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.policy.policyrule.DynamicPolicyRule;
import sif.model.policy.policyrule.dynamicConditions.BinaryCondition;
import sif.model.policy.policyrule.dynamicConditions.EBinaryRelation;
import sif.model.violations.IViolation;
import sif.technicalDepartment.equipment.testing.facilities.implementations.DynamicTestFacility;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.AbstractConditionChecker;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.BinaryConditionChecker;

public class ConditionCheckerTest {

	private static Spreadsheet spreadsheet;
	private static BinaryConditionChecker binChecker;
	private static CellAddress expectedAddress;
	private final static String target1 = "1.A1";
	private final static String target2 = "Sheet1.A1";
	private final static File inputFile = new File(
			"test/sif/testdata/input03_s1.xls");

	@Before
	public void setUp() throws Exception {
		DataFacade df = DataFacade.getInstance();

		spreadsheet = df.createSpreadsheet(inputFile);
		binChecker = new BinaryConditionChecker(new DynamicPolicyRule(), null);

		expectedAddress = new CellAddress();
	}

	@Test
	public void testRetrieveTargetBySheetNo() throws ParseException {
		expectedAddress.setColumnIndex(1);
		expectedAddress.setRowIndex(1);
		Worksheet worksheet = spreadsheet.getWorksheetAt(1);
		expectedAddress.setWorksheet(worksheet);

		Assert.assertTrue(worksheet != null);

		IElement expectedElement = spreadsheet.getCellFor(expectedAddress);
		IElement actualElement = DynamicTestFacility.retrieveTarget(target1,
				spreadsheet);

		Assert.assertSame(expectedElement, actualElement);
	}

	@Test
	public void testRetrieveTargetBySheetName() throws ParseException {
		expectedAddress.setColumnIndex(1);
		expectedAddress.setRowIndex(1);
		Worksheet worksheet = spreadsheet.getWorksheetAt(1);
		expectedAddress.setWorksheet(worksheet);

		Assert.assertTrue(worksheet != null);

		IElement expectedElement = spreadsheet.getCellFor(expectedAddress);
		IElement actualElement = DynamicTestFacility.retrieveTarget(target2,
				spreadsheet);

		Assert.assertTrue(expectedElement != null);
		Assert.assertSame(expectedElement, actualElement);
	}

	@Test
	public void testCheckTrueBinaryTextCondition() throws Exception {
		final String expectedResult = "Welches Auto soll ich kaufen?";

		BinaryCondition binCond = new BinaryCondition();
		binCond.setRelation(EBinaryRelation.equal);
		binCond.setValue(expectedResult);
		binCond.setTarget("A1");

		IElement target = DynamicTestFacility.retrieveTarget(
				binCond.getTarget(), spreadsheet);
		IViolation result = null;
		result = binChecker.check(binCond, target);
		assertTrue(result == null);

	}

	@Test
	public void testCheckFalseBinaryTextCondition() throws Exception {
		final String expectedResult = "13";

		BinaryCondition binCond = new BinaryCondition();
		binCond.setRelation(EBinaryRelation.equal);
		binCond.setValue(expectedResult);
		binCond.setTarget("A1");

		IElement target = DynamicTestFacility.retrieveTarget(
				binCond.getTarget(), spreadsheet);
		IViolation result;
		result = binChecker.check(binCond, target);

		assertTrue(result != null);
	}

	@Test
	public void testPropertyRetrieval() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			IntrospectionException {
		Cell element = spreadsheet.getWorksheetAt(1).getCellAt(1, 1);
		System.out.println(element.getTextContent());
		Object property = AbstractConditionChecker.retrieveProperty("cellContentType", element);
		System.out.println(element.getCellContentType());
		System.out.println(property);
		assertTrue(property.equals(CellContentType.TEXT));
	}

	// @Test
	// public void testDate() {
	// expectedAddress.setColumnIndex(2);
	// expectedAddress.setRowIndex(1);
	// Worksheet worksheet = spreadsheet.getWorksheetAt(1);
	// expectedAddress.setWorksheet(worksheet);
	//
	// Cell cell = spreadsheet.getCellFor(expectedAddress);
	// try {
	// @SuppressWarnings("unused")
	// Double d = cell.getNumericContent();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// try {
	// @SuppressWarnings("unused")
	// String s = cell.getValueAsString();
	// @SuppressWarnings("unused")
	// String sr = cell.getStringRepresentation();
	// @SuppressWarnings("unused")
	// String t = cell.getTextContent();
	// Assert.assertTrue(false);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// fail("not implemented");
	// }
}
