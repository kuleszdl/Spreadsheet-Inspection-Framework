package sif.technicalDepartment.equipment.testing.facilities.implementations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.cell.CellContentType;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.elements.basic.worksheet.Worksheet;
import sif.model.policy.policyrule.sanityModel.SanityConstraint;
import sif.model.policy.policyrule.sanityModel.SanityTuple;

/**
 * A facility to provide SanityConstraints
 * TODO: Get it through the frontend
 * @author Wolfgang Kraus
 *
 */
public class SanityConstraintFacility {
	
	/**
	 * The Spreadsheet which contains the constraints
	 */
	private Spreadsheet sheet;
	/**
	 * Our map for the known constraints, worksheet-name as key
	 */
	private HashMap<String, Vector<SanityConstraint>> constraintMap;
	/**
	 * Our map for known restricted columns
	 */
	private HashMap<String, String> targetMap;
		
	/**
	 * Creates a new instance. The instance must be initialized before it can be used, see {@link #init(String[][])}
	 * @param sheet Containing the restrictions
	 */
	public SanityConstraintFacility(Spreadsheet sheet){
		this.sheet = sheet;
		constraintMap = new HashMap<String, Vector<SanityConstraint>>();
		targetMap = new HashMap<String, String>();
	}
	
	/**
	 * Searches and returns the associated sanity constraints
	 * @param target The worksheet / table which is targeted
	 * @param value which is referenced
	 * @return the constraint or <code>null</code> if none is defined
	 */
	public Vector<SanityConstraint> getSanityInfo(String target, String value){
		Vector<SanityConstraint> cons = constraintMap.get(target);
		Vector<SanityConstraint> toReturn = new Vector<SanityConstraint>(1, 1);
		if (cons != null){
			for (SanityConstraint con : cons){
				if (con.value.equalsIgnoreCase(value)){
					toReturn.add(con);
				}
			}
		}
		
		return toReturn.size() > 0 ? toReturn : null;
	}
	
	/**
	 * Extracts the worksheet name 
	 * @param headerCell 
	 * @return the name
	 */
	private String extractWorksheetName(Cell headerCell){
		return extractWorksheetName(headerCell.getTextContent());
	}
	
	/**
	 * Extracts the worksheet name 
	 * @param headerString
	 * @return the name
	 */
	private String extractWorksheetName(String headerString){
		int cutPos = headerString.indexOf("!");
		if (cutPos == -1){
			return headerString;
		}
		String sheetName = headerString.substring(0, cutPos);
		return sheetName;
	}
	
	/**
	 * Extracts the column index
	 * @param headerString
	 * @return the 1-based index for the letter or -1 if the string is not worksheet!Letter
	 */
	private int extractColumnIndex(String headerString){
		int cutPos = headerString.indexOf("!");
		if (cutPos < 0)
			return cutPos;
		int index = headerString.charAt(cutPos + 1) + 1 - 'A';
		return index;
	}
	
	/**
	 * Creates a new SanityConstraint
	 * @param headerCell containing the worksheet / value to restrict
	 * @param constraintCell containing the restraining values
	 * @param value triggering value
	 * @return the new {@link SanityConstraint} or <code>null</code> if either of the cells is null 
	 */
	private SanityConstraint createConstraint(Cell headerCell, Cell constraintCell, String value){
		SanityConstraint constraint = new SanityConstraint();
		if (headerCell != null && constraintCell != null){
			
			String[] vals = constraintCell.getValueAsString().split(";");
			Vector<String> values = new Vector<String>(vals.length);
			// restoring escaped semicolons 
			for (int i = 0; i < vals.length; i++){
				if (vals[i].endsWith("\\")){
					vals[i] += (i == vals.length - 1) ? "" : ";" + vals[i + 1];
					values.add(vals[i]);
					i++;
				} else {
					values.add(vals[i]);
				}
			}
			constraint.allowedValues = values.toArray(new String[values.size()]);
			constraint.definedFor = extractWorksheetName(headerCell);
			constraint.value = value;
			constraint.definedFrom = headerCell.getWorksheet().getName();
			return constraint;
		}
		return null;
	}

	/**
	 * Checks whether the worksheet is known to be restricted or to contain restrictions
	 * @param worksheetName
	 * @return true if it is targeted or contains restrictions or both
	 */
	public boolean existConstraints(String worksheetName){
		return (constraintMap.get(worksheetName) != null
				|| targetMap.get(worksheetName) != null
				);
	}

	/**
	 * Searches and returns the associated sanity constraints
	 * @param tup the SanityTuple to check
	 * @return the constraint or <code>null</code> if none is defined
	 */
	public Vector<SanityConstraint> getSanityInfo(SanityTuple tup) {
		return getSanityInfo(tup.referencing, tup.value);
	}


	/**
	 * Initializes the facility 
	 * @param constraintLocations An array containing worksheet!ColumnOfValue and 
	 * 			worksheet!ColumnOfRestriction [worksheet!ColumnOfExplanation]
	 */
	public void addConstraint(String[] pair) {
		// we need at least 2 columns, the value and constraint, the optional third column 
		// is the explanation
		if (pair.length < 2){
			System.err.println("init failed, extraction of worksheet names was erroneus. \n"
					+ "Values: " + Arrays.toString(pair));
			return;
		}
		// try and get the worksheets, if the names are wrong we'll get a null back
		String worksheetName1 = extractWorksheetName(pair[0]);
		String worksheetName2 = extractWorksheetName(pair[1]);
		if (worksheetName1 == null || worksheetName2 == null){
			System.err.println("init failed, extraction of worksheet names was erroneus. \n"
					+ "Values: " + Arrays.toString(pair));
			return;
		}
		Worksheet worksheet1 = sheet.getWorksheetFor(worksheetName1);
		Worksheet worksheet2 = sheet.getWorksheetFor(worksheetName2);
		Worksheet worksheet3 = null;
		if (worksheet1 == null || worksheet2 == null){
			System.err.println("init failed, extraction of worksheet names was erroneus. \n"
					+ "Values: " + Arrays.toString(pair) + ", worksheets: " + worksheet1 
					+ " " + worksheet2);
			return;
		}
		 
		int column1 = extractColumnIndex(pair[0]);
		int column2 = extractColumnIndex(pair[1]);
		int column3 = -1;
		Cell headerCell = null;
		if (pair[0].equals(pair[1])){
			headerCell = new Cell();
			headerCell.setWorksheet(worksheet1);
			headerCell.setTextContent(worksheetName1);
		} else {
			headerCell = worksheet1.getCellAt(column2, 1);
		}
		
		// if we have 3 columns we need to get the explanation
		if (pair.length > 2){
			String worksheetName3 = extractWorksheetName(pair[2]);
			if (worksheetName3 != null){
				worksheet3 = sheet.getWorksheetFor(worksheetName3);
				column3 = extractColumnIndex(pair[2]);
			}
		}

		// go through the rows and create the SanityConstraint for each
		Cell value;
		for (int row = 2; (value = worksheet1.getCellAt(column1, row)) != null; row++){
			Cell restraint = worksheet2.getCellAt(column2, row);
			String val = value.getValueAsString();
			SanityConstraint newConstraint;
			if (worksheet3 != null){
				Cell information = worksheet3.getCellAt(column3, row);
				newConstraint = createConstraint(headerCell, restraint, val, information);
			} else {
				newConstraint = createConstraint(headerCell, restraint, val);
			}
			if (newConstraint == null){
				continue;					
			}

			Vector<SanityConstraint> cons = constraintMap.get(worksheetName1);
			if (cons == null){
				cons = new Vector<SanityConstraint>(5, 1);
				constraintMap.put(worksheetName1, cons);
			}
			targetMap.put(newConstraint.definedFor, "");
			cons.add(newConstraint);
		}


		
	}

	private SanityConstraint createConstraint(Cell headerCell, Cell constraintCell,
			String value, Cell information) {
		SanityConstraint constraint = createConstraint(headerCell, constraintCell, value);
		if (information != null && information.getCellContentType() != CellContentType.BLANK){
			String info = information.getValueAsString();
			if (!info.isEmpty()){
				constraint.explanation = info;
			}
		}

		return constraint;
	}

}
