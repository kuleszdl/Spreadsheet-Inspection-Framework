package sif.model;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.utility.Converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellMatrixAddress extends Address {

    private final Logger logger = LoggerFactory.getLogger(CellMatrixAddress.class);

    private String cellMatrixKey;
    private CellMatrix cellMatrix = null;
    private List<String> cellKeys;
    private List<String> rowKeys;
    private List<String> columnKeys;
    @Inject
    private ElementFactory elementFactory;

    /**
     * Create a CellMatrix with Cells around the given cell
     *
     * @param cell given cell
     * @param h    horizontal expansion
     * @param v    vertical expansion
     */
    @AssistedInject
    private CellMatrixAddress(
            Spreadsheet spreadsheet,
            @Assisted("cma-cell") Cell cell,
            @Assisted("horizontal") Integer h,
            @Assisted("vertical") Integer v
    ) {
        super(spreadsheet, cell.getWorksheet(), AddressType.CellMatrixAddress);
        Map<String, Integer> borders = new HashMap<>();
        int currentColumnIndex = Converter.columnToInteger(cell.getCellAddress().getColumnKey());
        int currentRowIndex = Integer.parseInt(cell.getCellAddress().getRowKey());

        // compute borders of cell matrix
        borders.put("left", currentColumnIndex - h);
        borders.put("right", currentColumnIndex + h);
        borders.put("top", currentRowIndex - v);
        borders.put("bottom", currentRowIndex + v);

        computeKeys(borders);
    }

    @AssistedInject
    private CellMatrixAddress(Spreadsheet spreadsheet, @Assisted("cma-address") String excelNotation) {
        super(spreadsheet, excelNotation, AddressType.CellMatrixAddress);
        Map<String, Integer> borders = extractExcelMatrixNotation(excelNotation);
        computeKeys(borders);
    }

    @AssistedInject
    private CellMatrixAddress(Spreadsheet spreadsheet, @Assisted("cma-worksheet") Worksheet worksheet, @Assisted("cma-simple-address") String s) {
        super(spreadsheet, worksheet, AddressType.CellMatrixAddress);
        Map<String, Integer> borders = extractSimpleMatrixNotation(s);
        computeKeys(borders);
    }

    private void computeKeys(Map<String, Integer> borders) {
        // check if each index is bigger than 1
        for (Map.Entry<String, Integer> entry : borders.entrySet()) {
            if (entry.getValue() < 1) {
                entry.setValue(1);
            }
        }

        columnKeys = new ArrayList<>();
        for (int i = borders.get("left"); i <= borders.get("right"); i++) {
            columnKeys.add(Converter.columnToString(i));
        }

        rowKeys = new ArrayList<>();
        for (int i = borders.get("top"); i <= borders.get("bottom"); i++) {
            rowKeys.add(Integer.toString(i));
        }

        cellKeys = new ArrayList<>();
        for (String columnKey : columnKeys) {
            for (String rowKey : rowKeys) {
                cellKeys.add(columnKey + rowKey);
            }
        }

        String topLeft = Converter.columnToString(borders.get("left")) + borders.get("top");
        String bottomRight = Converter.columnToString(borders.get("right")) + borders.get("bottom");
        cellMatrixKey = topLeft + ":" + bottomRight;
    }

    private Map<String, Integer> extractSimpleMatrixNotation(String s) {
        Map<String, Integer> borders = new HashMap<>();
        Pattern pattern = Pattern.compile(Converter.SIMPLE_MATRIX_NOTATION_REGEX);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find() && (matcher.groupCount() == 4)) {
            borders.put("left", Converter.columnToInteger(matcher.group(1)));
            borders.put("top", Integer.parseInt(matcher.group(2)));
            borders.put("right", Converter.columnToInteger(matcher.group(3)));
            borders.put("bottom", Integer.parseInt(matcher.group(4)));
            setValidAddress(true);
        } else {
            logger.error("Invalid SimpleMatrixNotation: {}", s);
            setValidAddress(false);
        }
        return borders;
    }

    private Map<String, Integer> extractExcelMatrixNotation(String s) {
        Map<String, Integer> borders = new HashMap<>();
        Pattern pattern = Pattern.compile(Converter.EXCEL_MATRIX_NOTATION_REGEX);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find() && (matcher.groupCount() == 5)) {
            borders.put("left", Converter.columnToInteger(matcher.group(2)));
            borders.put("top", Integer.parseInt(matcher.group(3)));
            borders.put("right", Converter.columnToInteger(matcher.group(4)));
            borders.put("bottom", Integer.parseInt(matcher.group(5)));
            setValidAddress(true);
        } else {
            logger.error("Invalid ExcelMatrixNotation: {}", s);
            setValidAddress(false);
        }
        return borders;
    }

    @Override
    public String getExcelNotation() {
        return getWorksheetKey() + "!" + cellMatrixKey;
    }

    @Override
    public String getSimpleNotation() {
        return cellMatrixKey;
    }

    @Override
    public int getLowestColumnIndex() {
        if (!cellMatrix.getColumns().isEmpty())
            return cellMatrix.getColumns().get(0).getIndex();
        else
            return 0;
    }

    @Override
    public int getHighestColumnIndex() {
        if (!cellMatrix.getColumns().isEmpty())
            return cellMatrix.getColumns().get(cellMatrix.getColumns().size() - 1).getIndex();
        else
            return 0;
    }

    @Override
    public int getLowestRowIndex() {
        if (!cellMatrix.getRows().isEmpty())
            return cellMatrix.getRows().get(0).getIndex();
        else
            return 0;
    }

    @Override
    public int getHighestRowIndex() {
        if (!cellMatrix.getRows().isEmpty())
            return cellMatrix.getRows().get(cellMatrix.getRows().size() - 1).getIndex();
        else
            return 0;
    }

    //region getters & setters
    public CellMatrix getCellMatrix() {
        if (cellMatrix == null) {
            cellMatrix = elementFactory.createCellMatrix(this);
        }
        return cellMatrix;
    }

    public List<String> getCellKeys() {
        return cellKeys;
    }

    public List<String> getRowKeys() {
        return rowKeys;
    }

    public List<String> getColumnKeys() {
        return columnKeys;
    }
    //endregion
}
