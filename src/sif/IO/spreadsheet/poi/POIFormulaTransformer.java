package sif.IO.spreadsheet.poi;

import java.util.Stack;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.ptg.AbstractFunctionPtg;
import org.apache.poi.ss.formula.ptg.AddPtg;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.AreaPtgBase;
import org.apache.poi.ss.formula.ptg.ArrayPtg;
import org.apache.poi.ss.formula.ptg.AttrPtg;
import org.apache.poi.ss.formula.ptg.BoolPtg;
import org.apache.poi.ss.formula.ptg.ConcatPtg;
import org.apache.poi.ss.formula.ptg.ControlPtg;
import org.apache.poi.ss.formula.ptg.DividePtg;
import org.apache.poi.ss.formula.ptg.EqualPtg;
import org.apache.poi.ss.formula.ptg.ErrPtg;
import org.apache.poi.ss.formula.ptg.GreaterEqualPtg;
import org.apache.poi.ss.formula.ptg.GreaterThanPtg;
import org.apache.poi.ss.formula.ptg.IntPtg;
import org.apache.poi.ss.formula.ptg.LessEqualPtg;
import org.apache.poi.ss.formula.ptg.LessThanPtg;
import org.apache.poi.ss.formula.ptg.MissingArgPtg;
import org.apache.poi.ss.formula.ptg.MultiplyPtg;
import org.apache.poi.ss.formula.ptg.NotEqualPtg;
import org.apache.poi.ss.formula.ptg.NumberPtg;
import org.apache.poi.ss.formula.ptg.OperandPtg;
import org.apache.poi.ss.formula.ptg.OperationPtg;
import org.apache.poi.ss.formula.ptg.ParenthesisPtg;
import org.apache.poi.ss.formula.ptg.PercentPtg;
import org.apache.poi.ss.formula.ptg.PowerPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.ptg.Ref3DPxg;
import org.apache.poi.ss.formula.ptg.RefPtg;
import org.apache.poi.ss.formula.ptg.RefPtgBase;
import org.apache.poi.ss.formula.ptg.ScalarConstantPtg;
import org.apache.poi.ss.formula.ptg.StringPtg;
import org.apache.poi.ss.formula.ptg.SubtractPtg;
import org.apache.poi.ss.formula.ptg.UnaryMinusPtg;
import org.apache.poi.ss.formula.ptg.UnaryPlusPtg;
import org.apache.poi.ss.formula.ptg.UnknownPtg;
import org.apache.poi.ss.formula.ptg.ValueOperatorPtg;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sif.model.elements.basic.address.CellAddress;
import sif.model.elements.basic.address.RangeAddress;
import sif.model.elements.basic.cell.Cell;
import sif.model.elements.basic.cell.CellContentType;
import sif.model.elements.basic.operator.Operator;
import sif.model.elements.basic.operator.OperatorEnum;
import sif.model.elements.basic.range.Range;
import sif.model.elements.basic.reference.AbstractReference;
import sif.model.elements.basic.reference.CellReference;
import sif.model.elements.basic.reference.RangeReference;
import sif.model.elements.basic.tokencontainers.Formula;
import sif.model.elements.basic.tokencontainers.Function;
import sif.model.elements.basic.tokencontainers.ITokenContainer;
import sif.model.elements.basic.tokens.ITokenElement;
import sif.model.elements.basic.tokens.ScalarConstant;
import sif.model.elements.basic.tokens.UnsupportedToken;
import sif.model.elements.basic.worksheet.Worksheet;

/***
 * Helper Class to transform the formula content of POI-{@link Cell}s to the
 * internal model.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class POIFormulaTransformer {

	private AbstractPOISpreadsheetIO poiIO;

	/***
	 * Creates a new {@link POIFormulaTransformer} that is linked to the given
	 * {@link AbstractPOISpreadsheetIO}.
	 * 
	 * @param poiIO
	 *            The given AbstractPOISpreadsheetIO.
	 */
	public POIFormulaTransformer(AbstractPOISpreadsheetIO poiIO) {
		this.poiIO = poiIO;
	}

	/***
	 * Gets the formula content of a given cell from the POI-model as a stack of
	 * ptgs.The Ptgs are in reverse polish notation.
	 * 
	 * @param poiCell
	 *            The given cell from the POI-model.
	 * @return The contents of the given cell as a stack of ptgs.
	 */
	private Stack<Ptg> getPtgStackFor(org.apache.poi.ss.usermodel.Cell poiCell) {
		// Parse POI-Formula to ptgs.
		Ptg[] ptgArray = FormulaParser.parse(poiCell.getCellFormula(),
				poiIO.formulaParsingWorkbook, poiCell.getCellType(),
				poiIO.poiWorkbook.getSheetIndex(poiCell.getSheet()));

		// Create stack to keep the reverse polish notation and to ease the
		// transformation.
		Stack<Ptg> ptgs = new Stack<Ptg>();
		for (Ptg ptg : ptgArray) {
			ptgs.push(ptg);
		}

		return ptgs;
	}

	/***
	 * Transforms a given OperandPtg to its correspondent in the internal model.
	 * 
	 * @param ptg
	 *            The given OperandPtg
	 * @return The correspondent of the given OperandPtg in the internal model.
	 */
	private AbstractReference transform(Cell cell, OperandPtg ptg) {
		AbstractReference reference = null;
		// Cell-Reference.
		if (ptg instanceof RefPtgBase) {
			RefPtgBase refPtgBase = (RefPtgBase) ptg;

			// Create new cell reference.
			CellReference cellReference = new CellReference();
			reference = cellReference;

			// Create referenced address.
			CellAddress referencedCellAddress = new CellAddress();
			referencedCellAddress.setColumnIndex(refPtgBase.getColumn() + 1);
			referencedCellAddress.setRowIndex(refPtgBase.getRow() + 1);
			referencedCellAddress.setWorksheet(cell.getWorksheet());

			// Cell reference is within worksheet.
			if (ptg instanceof RefPtg) {
				referencedCellAddress.setWorksheet(cell.getWorksheet());
			}
			// Cell reference is to a different worksheet.
			if (refPtgBase instanceof Ref3DPtg) {
				Ref3DPtg ref3DPtg = (Ref3DPtg) refPtgBase;
				Workbook poiWorkbook = poiIO.poiWorkbook;
				Integer sheetIndex = null;

				// Get the sheet index.
				if (poiWorkbook instanceof HSSFWorkbook) {
					// FIXME: there must be a cleaner way to do this!
					sheetIndex = ((HSSFWorkbook) poiWorkbook)
							.getSheetIndexFromExternSheetIndex(ref3DPtg
									.getExternSheetIndex());
				} else {
					sheetIndex = ref3DPtg.getExternSheetIndex();
				}

				// Transform sheet index to a 1 based index.
				referencedCellAddress.setWorksheet(poiIO.spreadsheet
						.getWorksheetAt(sheetIndex + 1));
			}  else if (refPtgBase instanceof Ref3DPxg) {
				Workbook poiWorkbook = poiIO.poiWorkbook;
				Integer sheetIndex = null;
				
				Ref3DPxg ref3dpxg = (Ref3DPxg) refPtgBase;
				
				// Get the sheet index the XSSF way
				if (poiWorkbook instanceof XSSFWorkbook) {
					sheetIndex = ((XSSFWorkbook) poiWorkbook).getSheetIndex(ref3dpxg.getSheetName());
				}
				// Transform sheet index to a 1 based index.
				referencedCellAddress.setWorksheet(poiIO.spreadsheet
						.getWorksheetAt(sheetIndex+1));
			}

			// Get referenced cell.
			Cell referencedCell = poiIO.spreadsheet
					.getCellFor(referencedCellAddress);

			// Create blank cell if referenced cell does not exist.
			if (referencedCell == null) {
				referencedCell = new Cell();
				referencedCell.setColumnIndex(referencedCellAddress
						.getColumnIndex());
				referencedCell.setRowIndex(referencedCellAddress.getRowIndex());
				Worksheet worksheet = referencedCellAddress.getWorksheet();
				referencedCell.setWorksheet(worksheet);
				worksheet.add(referencedCell);
			}

			// Set up cell reference.
			cellReference.setColumnRelative(refPtgBase.isColRelative());
			cellReference.setRowRelative(refPtgBase.isRowRelative());
			cellReference.setReferencedElement(referencedCell);
			cellReference.setReferencingElement(cell);

		}

		// Range-Reference.
		if (ptg instanceof AreaPtgBase) {
			AreaPtgBase areaPtgBase = (AreaPtgBase) ptg;

			// Create new range reference.
			RangeReference rangeReference = new RangeReference();
			reference = rangeReference;

			// Create address.
			RangeAddress referencedRangeAddress = new RangeAddress();
			referencedRangeAddress.setLeftmostColumnIndex(areaPtgBase
					.getFirstColumn() + 1);
			referencedRangeAddress
					.setTopmostRowIndex(areaPtgBase.getFirstRow() + 1);
			referencedRangeAddress.setRightmostColumnIndex(areaPtgBase
					.getLastColumn() + 1);
			referencedRangeAddress.setBottommostRowIndex(areaPtgBase
					.getLastRow() + 1);
			referencedRangeAddress.setWorksheet(cell.getWorksheet());
			// Reference is within worksheet.
			if (areaPtgBase instanceof AreaPtg) {
				referencedRangeAddress.setWorksheet(cell.getWorksheet());
			}
			// Reference is to a different worksheet.
			if (areaPtgBase instanceof Area3DPtg) {
				Area3DPtg area3DPtg = (Area3DPtg) areaPtgBase;
				referencedRangeAddress.setWorksheet(poiIO.spreadsheet
						.getWorksheetAt(area3DPtg.getExternSheetIndex() + 2)); // TODO: Verify the increment
			}

			// Create blank cells if needed.
			poiIO.spreadsheet
					.createBlankCellsForNullCellsIn(referencedRangeAddress);

			// Get referenced range.
			Range referencedRange = poiIO.spreadsheet
					.getRangeFor(referencedRangeAddress);

			// Set up range reference.
			rangeReference.setReferencedElement(referencedRange);
			rangeReference.setReferencingElement(cell);

			rangeReference.setLeftmostColumnRelative(areaPtgBase
					.isFirstColRelative());
			rangeReference.setTopmostRowRelative(areaPtgBase
					.isFirstRowRelative());
			rangeReference.setRightmostColumnRelative(areaPtgBase
					.isLastColRelative());
			rangeReference.setBottommostRowRelative(areaPtgBase
					.isLastRowRelative());

		}

		return reference;
	}

	/***
	 * Pops the given number of elements from the given stack of ptgs,
	 * transforms them to {@link ITokenElement}s and adds them to the given
	 * token container. Ptgs whose transformation is not supported yet, will be
	 * added as an {@link UnsupportedToken};
	 * 
	 * @param ptgs
	 *            The given stack of Ptgs.
	 * @param tokenContainer
	 *            The given token container to add the tokens to.
	 * @param ptgsToTransform
	 *            The number of Ptgs the method is allowed to transform.
	 */
	private void transform(Cell cell, Stack<Ptg> ptgs,
			ITokenContainer tokenContainer, Integer ptgsToTransform) {
		Integer transformedPtgs = 0;
		// Loop until stack is empty or number of ptgsToTransform is reached.
		while (!ptgs.isEmpty() & transformedPtgs < ptgsToTransform) {

			// Pop the Ptg that is to be tansformed.
			Ptg ptg = ptgs.pop();
			transformedPtgs++;

			ITokenElement transformedToken = new UnsupportedToken();

			// Transform an ArrayPtg.
			if (ptg instanceof ArrayPtg) {
				// TODO: Implement Array Formulas
			}

			// Transform a ControlPtg.
			if (ptg instanceof ControlPtg) {
				if (ptg instanceof AttrPtg) {
					AttrPtg aptg = (AttrPtg) ptg;
					// TODO: AttrPtg seems to be some junk record with different
					// functionality. Not all applications of AttrPtg have been
					// implemented
					// yet.

					if (aptg.isOptimizedChoose() || aptg.isSum()
							|| aptg.isOptimizedIf()) {
						// Create function.
						Function function = new Function();
						function.setContainer(tokenContainer);
						function.setName(aptg.toFormulaString());

						// Set function as transformed token.
						transformedToken = function;

						// Transform the arguments of the current function and
						// add them to the function.
						for (int i = 0; i < aptg.getNumberOfOperands(); i++) {
							// Transform operand tokens.
							transform(cell, ptgs, function, 1);
						}
					}
				} else if (ptg instanceof ParenthesisPtg) {
					// Parenthesis are ignored and thus do not count as a
					// transformed token.
					transformedToken = null;
					transformedPtgs = transformedPtgs - 1;

				}
			}

			// Transform an OperandPtg
			if (ptg instanceof OperandPtg) {
				AbstractReference reference = transform(cell, (OperandPtg) ptg);
				if (reference != null) {
					reference.setContainer(tokenContainer);
					transformedToken = reference;
				}
			}

			// Transform a OperationPtg
			if (ptg instanceof OperationPtg) {
				if (ptg instanceof AbstractFunctionPtg) {
					AbstractFunctionPtg aFuncPtg = (AbstractFunctionPtg) ptg;
					// Create function.
					Function function = new Function();
					function.setContainer(tokenContainer);
					function.setName(aFuncPtg.getName());
					// Set function as transformed token.
					transformedToken = function;

					// Transform the operands of the current function and add
					// them to the function.
					for (int i = 0; i < aFuncPtg.getNumberOfOperands(); i++) {
						// Transform operand tokens.
						transform(cell, ptgs, function, 1);
					}
				}
				if (ptg instanceof ValueOperatorPtg) {
					ValueOperatorPtg voptg = (ValueOperatorPtg) ptg;

					Operator operator = transform(voptg);
					operator.setContainer(tokenContainer);
					// Set function as transformed token.
					transformedToken = operator;

					// Transform the operands of the current operator and add
					// them to the operator.
					for (int i = 0; i < voptg.getNumberOfOperands(); i++) {
						// Transform operand tokens.
						transform(cell, ptgs, operator, 1);
					}

				}
			}

			// Transform a ScalarConstantptg
			if (ptg instanceof ScalarConstantPtg) {
				//
				ScalarConstant constant = transform((ScalarConstantPtg) ptg);
				constant.setContainer(tokenContainer);
				transformedToken = constant;
			}

			// Transform an UnknownPtg
			if (ptg instanceof UnknownPtg) {
				// TODO:Implement
			}

			if (transformedToken != null) {
				// Add transformed token to container.
				tokenContainer.add(transformedToken);
			}
		}
	}

	/***
	 * Transforms a given ScalarConstantPtg to its correspondent in the internal
	 * model.
	 * 
	 * @param scalar
	 *            The given ScalarConstantPtg.
	 * @return The correspondent of the given ScalarConstantPtg in the internal
	 *         model.
	 */
	private ScalarConstant transform(ScalarConstantPtg scalar) {
		ScalarConstant constant = new ScalarConstant();
		// Transform to Integer.
		if (scalar instanceof IntPtg) {
			constant.setIntegerValue(((IntPtg) scalar).getValue());
		}
		// Transform to Double.
		if (scalar instanceof NumberPtg) {
			constant.setNumericValue(((NumberPtg) scalar).getValue());
		}
		// Transform to String.
		if (scalar instanceof StringPtg) {
			constant.setTextValue(((StringPtg) scalar).getValue());
		}
		// Transform to Boolean.
		if (scalar instanceof BoolPtg) {
			constant.setBooleanValue(((BoolPtg) scalar).getValue());
		}
		// Transform to Error.
		if (scalar instanceof ErrPtg) {
			constant.setErrorValue(ErrorEval.getText(((ErrPtg) scalar)
					.getErrorCode()));
		}
		if (scalar instanceof MissingArgPtg) {
			// TODO: Implement.
		}
		return constant;
	}

	/***
	 * Transforms a given ValueOperatorPtg to its correspondent in the internal
	 * model.
	 * 
	 * @param ptg
	 *            The given ValueOperatorPtg
	 * @return The correspondent of the given ValueOperatorPtg in the internal
	 *         model.
	 */
	private Operator transform(ValueOperatorPtg ptg) {
		Operator operator = new Operator();
		if (ptg instanceof AddPtg) {
			operator.setOperatoryType(OperatorEnum.ADD);
		}
		if (ptg instanceof ConcatPtg) {
			operator.setOperatoryType(OperatorEnum.CONCAT);
		}
		if (ptg instanceof DividePtg) {
			operator.setOperatoryType(OperatorEnum.DIVIDE);
		}
		if (ptg instanceof EqualPtg) {
			operator.setOperatoryType(OperatorEnum.EQUAL);
		}
		if (ptg instanceof GreaterEqualPtg) {
			operator.setOperatoryType(OperatorEnum.GREATER_EQUAL);
		}
		if (ptg instanceof GreaterThanPtg) {
			operator.setOperatoryType(OperatorEnum.GREATER_THAN);
		}
		if (ptg instanceof LessEqualPtg) {
			operator.setOperatoryType(OperatorEnum.LESS_EQUAL);
		}
		if (ptg instanceof LessThanPtg) {
			operator.setOperatoryType(OperatorEnum.LESS_THAN);
		}
		if (ptg instanceof MultiplyPtg) {
			operator.setOperatoryType(OperatorEnum.MULTIPLY);
		}
		if (ptg instanceof NotEqualPtg) {
			operator.setOperatoryType(OperatorEnum.NOT_EQUAL);
		}
		if (ptg instanceof PercentPtg) {
			operator.setOperatoryType(OperatorEnum.PERCENT);
		}
		if (ptg instanceof PowerPtg) {
			operator.setOperatoryType(OperatorEnum.POWER);
		}
		if (ptg instanceof SubtractPtg) {
			operator.setOperatoryType(OperatorEnum.SUBTRACT);
		}
		if (ptg instanceof UnaryMinusPtg) {
			operator.setOperatoryType(OperatorEnum.UNARIY_MINUS);
		}
		if (ptg instanceof UnaryPlusPtg) {
			operator.setOperatoryType(OperatorEnum.UNARIY_PLUS);
		}

		return operator;
	}

	/***
	 * Transforms the contents of a given cell from the POI-model to a
	 * {@link Formula} and sets the formula as content of the given cell from
	 * the internal model.
	 * 
	 * @param cell
	 *            The given cell from the internal model.
	 * @param poiCell
	 *            The given cell form the POI-model.
	 */
	protected void transformFormulaContent(Cell cell,
			org.apache.poi.ss.usermodel.Cell poiCell) {

		// Create formula and add to cell.
		Formula formula = new Formula();
		cell.setFormula(formula);
		formula.setCell(cell);
		formula.setFormulaString(poiCell.getCellFormula());

		// Set formula result type.
		switch (poiCell.getCachedFormulaResultType()) {
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
			formula.setResultType(CellContentType.NUMERIC);
			cell.setNumericContent(poiCell.getNumericCellValue());
			break;
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
			formula.setResultType(CellContentType.TEXT);
			cell.setTextContent(poiCell.getStringCellValue());
			break;
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
			formula.setResultType(CellContentType.BOOLEAN);
			cell.setBooleanContent(poiCell.getBooleanCellValue());
			break;
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR:
			formula.setResultType(CellContentType.ERROR);
			cell.setErrorContent(FormulaError.forInt(
					poiCell.getErrorCellValue()).toString());
			break;
		default:

			break;
		}

		// Set formula content.
		Stack<Ptg> ptgs = getPtgStackFor(poiCell);
		transform(cell, ptgs, formula, ptgs.size());
	}

}
