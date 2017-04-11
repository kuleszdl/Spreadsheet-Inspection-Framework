package sif.io.spreadsheet;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaParsingWorkbook;
import org.apache.poi.ss.formula.ptg.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.model.*;
import sif.model.tokens.*;
import sif.model.values.ValueFactory;

import java.util.ArrayDeque;
import java.util.Deque;

/***
 * Helper Class to create the formula content of POI-Cells to the internal model.
 */
@RequestScoped
public class OoxmlFormulaTransformer {

    private final Logger logger = LoggerFactory.getLogger(OoxmlFormulaTransformer.class);
    private final Spreadsheet spreadsheet;
    private Workbook workbook;
    private FormulaParsingWorkbook formulaParsingWorkbook;
    private Deque<Token> tokenStack;
    private Cell cell;

    @Inject
    private ElementFactory formulaFactory;
    @Inject
    private AddressFactory addressFactory;
    @Inject
    private TokenFactory tokenFactory;
    @Inject
    private ValueFactory valueFactory;

    @Inject
    OoxmlFormulaTransformer(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
    }

    public void initializeFormulaTransformer(Workbook workbook) throws InvalidSpreadsheetException {
        // set workbook and initialize the formulaParsingWorkbook
        this.workbook = workbook;
        if (workbook instanceof XSSFWorkbook) {
            spreadsheet.setSpreadsheetType(SpreadsheetType.XLSX);
            logger.debug("detected XLSX spreadsheet: Creating XSSFEvaluationWorkbook");
            formulaParsingWorkbook = XSSFEvaluationWorkbook.create((XSSFWorkbook) workbook);

        } else if (workbook instanceof HSSFWorkbook) {
            spreadsheet.setSpreadsheetType(SpreadsheetType.XLS);
            logger.debug("detected XLS spreadsheet: Creating HSSFEvaluationWorkbook");
            formulaParsingWorkbook = HSSFEvaluationWorkbook.create((HSSFWorkbook) workbook);
        } else {
            throw new InvalidSpreadsheetException();
        }
    }

    /***
     * Transforms the contents of a given cell from the POI-model to a
     * {@link Formula} and sets the formula as content of the given cell from
     * the internal model.
     *
     * @param cell The given cell from the internal model.
     * @param poiCell The given cell form the POI-model.
     */
    void transformFormulaContent(Cell cell, org.apache.poi.ss.usermodel.Cell poiCell) {
        // Create formula and addCell to cell.
        Formula formula = formulaFactory.createFormula(cell);
        cell.setFormula(formula);
        formula.setFormulaString(poiCell.getCellFormula());
        spreadsheet.add(formula);

        if (poiCell.getCachedFormulaResultType() != org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR) {

            // ptgArray will give us the formula in reverse polish notation (as excel stores it)
            Ptg[] ptgArray = FormulaParser.parse(
                    poiCell.getCellFormula(),
                    formulaParsingWorkbook,
                    poiCell.getCellType(),
                    workbook.getSheetIndex(poiCell.getSheet())
            );

            this.cell = cell;
            this.tokenStack = new ArrayDeque<>();

            // Transform all the ptgs to their internal model counterparts
            for (Ptg ptg : ptgArray) {
                transformFormulaPtg(ptg);
            }

            // everything left on the stack will
            while (!tokenStack.isEmpty()) {
                Token token = tokenStack.pop();
                token.setContainer(formula);
                formula.add(token);
            }
        }

        logger.debug("finished formula transformation for formula '{}'", formula.getFormulaString());
    }

    /***
     * Pops the given number of spreadsheet from the given stack of ptgs,
     * transforms them to Tokens and adds them to the given
     * token container. Ptgs whose transformation is not supported yet, will be
     * added as an UnspecifiedToken};
     */
    private void transformFormulaPtg(Ptg ptg) {

        if (ptg instanceof ArrayPtg) {
            // Transform an ArrayPtg.
            // @TODO:later - Implement Array Formulas
            logger.error("Can not transformValueOperatorPtg ArrayPtg: not implemented");
        } else if (ptg instanceof ControlPtg) {
            // Transform a ControlPtg.
            transformControlPtg((ControlPtg) ptg);
        } else if (ptg instanceof OperandPtg) {
            // Transform an OperandPtg
            transformOperandPtg((OperandPtg) ptg);
        } else if (ptg instanceof OperationPtg) {
            // Transform a OperationPtg
            transformOperationPtg((OperationPtg) ptg);
        } else if (ptg instanceof ScalarConstantPtg) {
            // Transform a ScalarConstantPtg
            transformScalarConstantPtg((ScalarConstantPtg) ptg);
        } else {
            logger.error("Not implemented Ptg with Type '{}' found!", ptg);
        }
    }

    private void transformOperationPtg(OperationPtg ptg) {

        if (ptg instanceof AbstractFunctionPtg) {
            transformAbstractFunctionPtg((AbstractFunctionPtg) ptg);
        } else if (ptg instanceof ValueOperatorPtg) {
            transformValueOperatorPtg((ValueOperatorPtg) ptg);
        } else {
            logger.error("Not implemented OperationPtg with Type '{}' found!", ptg);
        }
    }

    private void transformAbstractFunctionPtg(AbstractFunctionPtg aFuncPtg) {
        // Create function.
        Function function = tokenFactory.createFunction(cell, aFuncPtg.getName());
        pushContainer(function, aFuncPtg);
    }

    /***
     * Transforms a given OperandPtg to its correspondent in the internal model.
     */
    private void transformOperandPtg(OperandPtg ptg) {

        if (ptg instanceof RefPtgBase) {
            // Cell-Reference
            transformRefPtgBase((RefPtgBase) ptg);
        } else if (ptg instanceof AreaPtgBase) {
            // Area Reference
            transformAreaPtgBase((AreaPtgBase) ptg);
        } else {
            logger.error("Not implemented OperandPtg with Type '{}' found!", ptg);
        }
    }

    private void transformAreaPtgBase(AreaPtgBase areaPtgBase) {

        CellMatrixAddress cma = null;

        // Reference is within worksheet.
        if (areaPtgBase instanceof AreaPtg) {
            cma = addressFactory.createCMA(cell.getWorksheet(), areaPtgBase.toFormulaString());
            // Reference to other worksheet
        } else if (areaPtgBase instanceof Area3DPtg) {
            Area3DPtg area3DPtg = (Area3DPtg) areaPtgBase;
            cma = addressFactory.createCMA(area3DPtg.toFormulaString());
        } else if (areaPtgBase instanceof Area3DPxg) {
            Area3DPxg area3DPxg = (Area3DPxg) areaPtgBase;
            cma = addressFactory.createCMA(area3DPxg.toFormulaString());
        } else {
            logger.error("Not implemented AreaPtgBase with Type '{}' found!", areaPtgBase);
        }

        if (cma != null) {
            // caveat! you must call init() of newly created reference
            // this will push incoming and outgoing references to source and target
            tokenStack.push(tokenFactory.createReference(cell, cell, cma.getCellMatrix()).init());
        }

    }

    private void transformRefPtgBase(RefPtgBase refPtgBase) {

        CellAddress cellAddress = null;

        // Cell reference is within worksheet.
        if (refPtgBase instanceof RefPtg) {
            cellAddress = addressFactory.createCellAddress(cell.getWorksheet(), refPtgBase.toFormulaString());
        } else if (refPtgBase instanceof Ref3DPtg) {
            // Cell reference is to a different worksheet.
            cellAddress = addressFactory.createCellAddress(refPtgBase.toFormulaString());
        } else if (refPtgBase instanceof Ref3DPxg) {
            cellAddress = addressFactory.createCellAddress(refPtgBase.toFormulaString());
        }

        if (cellAddress != null) {
            // caveat! you must call init() of newly created reference
            // this will push incoming and outgoing references to source and target
            tokenStack.push(tokenFactory.createReference(cell, cell, cellAddress.getCell()).init());
        }
    }

    private void transformScalarConstantPtg(ScalarConstantPtg ptg) {

        ScalarConstant constant = tokenFactory.createScalarConstant();
        // Transform to Double.
        if (ptg instanceof NumberPtg) {
            constant.setValue(valueFactory.createNumericValue(((NumberPtg) ptg).getValue()));
        } else if (ptg instanceof IntPtg) {
            constant.setValue(valueFactory.createNumericValue(((IntPtg) ptg).getValue()));
        } else if (ptg instanceof StringPtg) {
            constant.setValue(valueFactory.createStringValue(((StringPtg) ptg).getValue()));
        } else if (ptg instanceof BoolPtg) {
            constant.setValue(valueFactory.createBooleanValue(((BoolPtg) ptg).getValue()));
        } else {
            logger.error("Not implemented ScalarConstantPtg with Type '{}' found!", ptg);
        }

        tokenStack.push(constant);
    }

    private void transformControlPtg(ControlPtg ptg) {

        if (ptg instanceof AttrPtg) {
            AttrPtg aptg = (AttrPtg) ptg;
            /*
            * "Special Attributes"
            * This seems to be a Misc Stuff and Junk record.  One function it serves is
            * in SUM functions (i.e. SUM(A1:A3) causes an area PTG then an ATTR with the SUM option set)
            */
            if (aptg.isOptimizedChoose() || aptg.isSum() || aptg.isOptimizedIf()) {
                // Create function.
                Function function = tokenFactory.createFunction(cell, aptg.toFormulaString());
                // aptg only has one operand, so no need to call pushContainer()
                Token token = tokenStack.pop();
                token.setContainer(function);
                function.add(token);
                tokenStack.push(function);
            }
        } else if (ptg instanceof ParenthesisPtg) {
            // Parenthesis
            logger.trace("Found a parenthesis...");
        } else {
            logger.error("Not implemented ControlPtg with Type '{}' found!", ptg);
        }
    }

    /***
     * Transforms a given ValueOperatorPtg to its correspondent in the internal model.
     */
    private void transformValueOperatorPtg(ValueOperatorPtg ptg) {
        Operator operator = tokenFactory.createOperator(cell);

        if (ptg instanceof AddPtg) {
            operator.setOperatorType(OperatorType.ADD);
        } else if (ptg instanceof ConcatPtg) {
            operator.setOperatorType(OperatorType.CONCAT);
        } else if (ptg instanceof DividePtg) {
            operator.setOperatorType(OperatorType.DIVIDE);
        } else if (ptg instanceof EqualPtg) {
            operator.setOperatorType(OperatorType.EQUAL);
        } else if (ptg instanceof GreaterEqualPtg) {
            operator.setOperatorType(OperatorType.GREATER_EQUAL);
        } else if (ptg instanceof GreaterThanPtg) {
            operator.setOperatorType(OperatorType.GREATER_THAN);
        } else if (ptg instanceof LessEqualPtg) {
            operator.setOperatorType(OperatorType.LESS_EQUAL);
        } else if (ptg instanceof LessThanPtg) {
            operator.setOperatorType(OperatorType.LESS_THAN);
        } else if (ptg instanceof MultiplyPtg) {
            operator.setOperatorType(OperatorType.MULTIPLY);
        } else if (ptg instanceof NotEqualPtg) {
            operator.setOperatorType(OperatorType.NOT_EQUAL);
        } else if (ptg instanceof PercentPtg) {
            operator.setOperatorType(OperatorType.PERCENT);
        } else if (ptg instanceof PowerPtg) {
            operator.setOperatorType(OperatorType.POWER);
        } else if (ptg instanceof SubtractPtg) {
            operator.setOperatorType(OperatorType.SUBTRACT);
        } else if (ptg instanceof UnaryMinusPtg) {
            operator.setOperatorType(OperatorType.UNARY_MINUS);
        } else if (ptg instanceof UnaryPlusPtg) {
            operator.setOperatorType(OperatorType.UNARY_PLUS);
        } else {
            logger.error("Not implemented ValueOperatorPtg with Type '{}' found!", ptg);
        }

        pushContainer(operator, ptg);
    }

    private void pushContainer(TokenContainerToken containerToken, OperationPtg ptg) {
        for (int i = 0; i < ptg.getNumberOfOperands(); i++) {
            Token token = tokenStack.pop();
            token.setContainer(containerToken);
            containerToken.add(token);
        }
        tokenStack.push(containerToken);
    }

}
