package sif.technicalDepartment.equipment.testing.facilities.implementations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.cell.CellContentType;
import sif.model.elements.basic.worksheet.Column;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.policy.policyrule.sanityModel.SanityConstraint;
import sif.model.policy.policyrule.sanityModel.SanityTuple;
import sif.model.violations.IViolation;
import sif.model.violations.groupors.SameCausingCellGroupor;
import sif.model.violations.lists.ViolationList;
import sif.model.violations.single.GenericSingleViolation;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.CheckerCreationException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.IncompleteConditionException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.NoConditionTargetException;
import sif.technicalDepartment.equipment.testing.facilities.implementations.dynamicCheckers.exceptions.PropertyAccessException;
import sif.technicalDepartment.equipment.testing.facilities.types.MonolithicTestFacility;

/**
 * A Facility to check the sanity of the Functions and InputCells
 * @author Wolfgang Kraus
 *
 */
public class SanityTestFacility extends MonolithicTestFacility {
	private SanityConstraintFacility constraintFacility;
	private boolean sanityWarnings;
	private ViolationList violations;
	private boolean warnPatternException = true;
	
	private ArrayList<String> sanityValueCells = new ArrayList<String>();
	private ArrayList<String> sanityConstraintCells = new ArrayList<String>();
	private ArrayList<String> sanityExplanationCells = new ArrayList<String>();
	private ArrayList<String> sanityCheckingCells = new ArrayList<String>();


	/**
	 * Checks the tuples whether each tuple contains a sane value
	 * @param tuples to check
	 */
	private void checkTuples(SanityTuple[] tuples){
		Vector<SanityConstraint> constraints = new Vector<SanityConstraint>(tuples.length,1);
		int emptyTuples = 0;
		// fetching the constraints
		Vector<GenericSingleViolation> warnings = new Vector<GenericSingleViolation>(2, 1);
		for (SanityTuple tup : tuples){
			if (tup.value.isEmpty()){
				emptyTuples++;
			} else {
				Vector<SanityConstraint> con = constraintFacility.getSanityInfo(tup);
				if (con != null){
					constraints.addAll(con);
				} else if (constraintFacility.existConstraints(tup.referencing) && sanityWarnings) {
					warnings.add(generateViolation(tup));
				}
			}
		}
		// if it's an empty row we don't generate warnings.
		if (emptyTuples == tuples.length){
			return;
		}
		for (GenericSingleViolation vio : warnings){
			violations.add(vio);
		}
		// Check the tuples against all constraints
		for (SanityConstraint con : constraints){
			for (SanityTuple tup : tuples){
				if (hasSameWorksheet(con, tup)){
					if (!isInConstraint(con, tup)){
						generateViolation(con, tup);
					}
				}
			}
		}

	}
	
	/**
	 * Checks whether the tuple conforms to the constraint
	 * @param con The constraint to check against
	 * @param tup The tuple to be checked
	 * @return true if the tuples value is contained in the constraint
	 */
	private boolean isInConstraint(SanityConstraint con, SanityTuple tup){
		for (String sane : con.allowedValues){
			// try to match it as a pattern, if it contains special characters
			if (sane.matches(".*[\\{\\[\\(\\\\].*")){
				try {
					if (tup.value.matches(sane)){
						return true;
					}
					// parseable string didn't match so we don't need to compare it again
					continue;
				} catch (PatternSyntaxException e){
					if (warnPatternException ){
						generateViolation(tup, e);
					}
					e.printStackTrace();
				}
			}
			if (tup.value.equalsIgnoreCase(sane)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the constraint and tuple refer to the same column
	 * @param con 
	 * @param tup
	 * @return true if the tupels referecing = con defined for
	 */
	private boolean hasSameWorksheet(SanityConstraint con, SanityTuple tup){
		return (con.definedFor.equals(tup.referencing));
	}
	
	/**
	 * Creates and adds the Violation for a missing Constraint
	 * @param tup Tuple without constraint
	 * @return the violation
	 */
	private GenericSingleViolation generateViolation(SanityTuple tup){
		// value is unknown
		GenericSingleViolation violation = new GenericSingleViolation();
		violation.setCausingElement(tup.owner);
		violation.setPolicyRule(getTestedPolicyRule());
		violation.appendToDescription("The value \"" +
				tup.value + 
				"\" for \"" + 
				tup.referencing + 
				"\" is unknown. "
				//+ " (" + tup.owner.getStringRepresentation() + ")"
				);
		return violation;
	}
	
	/**
	 * Creates and adds the Violation if an regular expression could not be parsed
	 * @param tup Tuple which would be checked
	 * @param e corresponding exception
	 */
	private void generateViolation(SanityTuple tup, PatternSyntaxException e){
		GenericSingleViolation violation = new GenericSingleViolation();
		violation.setCausingElement(tup.owner);
		violation.setPolicyRule(getTestedPolicyRule());
		violation.appendToDescription("The restraint contained and invalid regular expression,"
				+ " please correct the expression. Causing expression:" 
				+ e.getPattern() + " : " + e.getDescription());
		
		violations.add(violation);
	}

	/**
	 * Creates a Violation for a Tuple failing to adhere to the constraint
	 * @param constraint unfulfilled constraint
	 * @param tup violating tuple
	 * @return the violation
	 */
	private void generateViolation(SanityConstraint constraint, SanityTuple tup){
		// value is not a sane one
		GenericSingleViolation violation = new GenericSingleViolation();
		violation.setCausingElement(tup.owner);
		violation.setPolicyRule(getTestedPolicyRule());
		violation.setBaseSeverityValue(IViolation.SEVERITY_MEDIUM);
		if (constraint.explanation == null){
			violation.appendToDescription("Due to the value \"" +
					constraint.value + 
					"\" as \"" +
					constraint.definedFrom +
					"\" should \"" +
					tup.referencing +
					"\" be instead of \"" +
					tup.value +
					"\" one of the following: " + 
					Arrays.toString(constraint.allowedValues).replaceAll("[\\]\\[]", "")
					);
		} else {
			violation.appendToDescription("Due to the value \"" +
					constraint.value + 
					"\" as \"" +
					constraint.definedFrom +
					"\" should \"" +
					tup.referencing +
					"\" abide by: " +
					constraint.explanation +
					", or be one of the following: " +
					Arrays.toString(constraint.allowedValues).replaceAll("[\\]\\[]", "")
					);
		}
		violations.add(violation);
	}
	
	/**
	 * Gathers all constrained columns from the worksheet which are to checked
	 */
	private void gatherColumns(){
		HashMap<String, Vector<Column>> columns = new HashMap<String, Vector<Column>>();
		for (String worksheet : sanityCheckingCells){
			String[] pair = worksheet.split("!");
			String sheetName = pair[0].substring(1);
			Worksheet sheet = inventory.getSpreadsheet().getWorksheetFor(sheetName);
			if (sheet == null){
				System.err.println("No worksheet for " + sheetName);
			}
			int column = pair[1].replaceAll("[^a-z,A-Z]", "").charAt(0) - 'A' + 1;
			Column col = sheet.getColumnAt(column);
			if (col == null){
				GenericSingleViolation vio = new GenericSingleViolation();
				vio.appendToDescription("No entry for checking column " + worksheet);
				Cell cause = sheet.getCellAt(column, 1);
				vio.setCausingElement(cause);
				violations.add(vio);
				continue;
			}
			Vector<Column> vec = columns.get(pair[0]);
			if (vec == null){
				vec = new Vector<Column>(5, 1);
				columns.put(pair[0], vec);
			}
			if (constraintFacility.existConstraints(col.getCellAt(1).getTextContent())){
				vec.add(col);
			} else {
				GenericSingleViolation vio = new GenericSingleViolation();
				vio.appendToDescription("No constraint for checking column " + worksheet);
				Cell cause = sheet.getCellAt(column, 1);
				vio.setCausingElement(cause);
				violations.add(vio);
			}
		}
		
		
		checkColumns(columns);
	}
	
	/**
	 * Checks all constrained columns row for row, each worksheet separately
	 * @param gathered Vector for each worksheet containing a Vector for each column
	 */
	private void checkColumns(HashMap<String, Vector<Column>> gathered){
		for (Vector<Column> vec : gathered.values()){
			int max = 0;
			for (Column c : vec)
				max = Math.max(max, c.getHighestRowIndex());
			
			for (int i = 2; i <= max; i++){
				SanityTuple[] tuples = new SanityTuple[vec.size()];
				for (int k = 0; k < vec.size(); k++){
					Column col = vec.get(k);
					Cell value = col.getCellAt(i);
					if (value == null){
						value = new Cell();
						value.setRowIndex(i);
						value.setColumnIndex(col.getIndex());
						value.setWorksheet(col.getWorksheet());
					} 
					String val = "";
					if (value.getCellContentType() != CellContentType.BLANK){
						val = value.getValueAsString();
					}
					
					String target = col.getCellAt(1).getValueAsString();
					SanityTuple tup = new SanityTuple();
					tup.owner = value;
					tup.value = val;
					tup.referencing = target;
					tuples[k] = tup;
				}
				checkTuples(tuples);
			}
		}
	}

	@Override
	public ViolationList run() throws NoConditionTargetException,
			PropertyAccessException, CheckerCreationException,
			IncompleteConditionException {
		
		// We use hashmaps as we only take one column per type per worksheet except for the
		// constraints
		HashMap<String, String> valueMap = new HashMap<String, String>();
		HashMap<String, ArrayList<String>> constraintMap = new HashMap<String, ArrayList<String>>();
		HashMap<String, String> explanationMap = new HashMap<String, String>();
		
		// we get the cell references in the absolute format =$WORKSHEET!$CELL$ROW
		/* check for the needed amount of values and put them into the maps
		 * we discard the row number, as we take the entire column
		 */
		for (String str : sanityValueCells){
			String[] parts = str.split("!");
			if (parts.length != 2)
				continue;
			valueMap.put(parts[0].substring(1), parts[1].replaceAll("[^a-z,A-Z]", ""));
		}
		// the list is needed to support multiple constraints for one value / worksheet
		for (String str : sanityConstraintCells){
			String[] parts = str.split("!");
			if (parts.length != 2)
				continue;
			ArrayList<String> list = constraintMap.get(parts[0].substring(1));
			if (list == null){
				list = new ArrayList<String>();
				constraintMap.put(parts[0].substring(1), list);
			}
			if (!list.contains(parts[1])){
				list.add(parts[1].replaceAll("[^a-z,A-Z]", ""));
			}
		}
		for (String str : sanityExplanationCells){
			String[] parts = str.split("!");
			if (parts.length != 2)
				continue;
			explanationMap.put(parts[0].substring(1), parts[1].replaceAll("[^a-z,A-Z]", ""));
		}

		// setting up the constraint facility
		violations = new ViolationList(getTestedPolicyRule(), new SameCausingCellGroupor());
		constraintFacility = new SanityConstraintFacility(inventory.getSpreadsheet());

		for (String worksheet : valueMap.keySet()){
			if (constraintMap.containsKey(worksheet) && explanationMap.containsKey(worksheet)){
				for (String constraint : constraintMap.get(worksheet)){
					constraintFacility.addConstraint(new String[]{
							worksheet + "!" + valueMap.get(worksheet), 
							worksheet + "!" + constraint,
							worksheet + "!" + explanationMap.get(worksheet)});
				}
			} else if (constraintMap.containsKey(worksheet)){
				for (String constraint : constraintMap.get(worksheet)){
					constraintFacility.addConstraint(new String[]{
							worksheet + "!" + valueMap.get(worksheet), 
							worksheet + "!" + constraint});
				}
			} else {
				constraintFacility.addConstraint(new String[]{
						worksheet + "!" + valueMap.get(worksheet), 
						worksheet + "!" + valueMap.get(worksheet)});
			}
		}
		sanityWarnings = true;
		gatherColumns();
		
		return violations;
	}

}
