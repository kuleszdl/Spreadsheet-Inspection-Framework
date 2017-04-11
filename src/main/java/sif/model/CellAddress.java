package sif.model;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.utility.Converter;

import java.util.List;

/***
 * The address of a Cell within a spreadsheet.
 */
public class CellAddress extends Address {

    private final Logger logger = LoggerFactory.getLogger(CellAddress.class);

    private String rowKey = "";
    private Row row = null;
    private String columnKey = "";
    private Column column = null;
    private String cellKey = "";
    private Cell cell = null;

    @AssistedInject
    private CellAddress(Spreadsheet spreadsheet, @Assisted("ca-address") String excelNotation) {
        super(spreadsheet, excelNotation, AddressType.CellAddress);

        List<String> addressParams = Converter.extractExcelNotation(excelNotation);
        if (addressParams != null) {
            columnKey = addressParams.get(1);
            rowKey = addressParams.get(2);
            cellKey = columnKey + rowKey;
            setValidAddress(true);
        } else {
            logger.error("Unable to convert ExcelNotation '{}'! YOU MUST CHECK ALL USER INPUT (POLICY)", excelNotation);
            setValidAddress(false);
        }
    }

    @AssistedInject
    private CellAddress(Spreadsheet spreadsheet, @Assisted("ca-worksheet") Worksheet worksheet, @Assisted("ca-simple-address") String simpleNotation) {
        super(spreadsheet, worksheet, AddressType.CellAddress);

        List<String> addressParams = Converter.extractSimpleNotation(simpleNotation);
        if (addressParams != null) {
            columnKey = addressParams.get(0);
            rowKey = addressParams.get(1);
            cellKey = columnKey + rowKey;
            setValidAddress(true);
        } else {
            logger.error("Unable to convert SimpleNotation '{}'! YOU MUST CHECK ALL USER INPUT (POLICY)", simpleNotation);
            setValidAddress(false);
        }
    }

    public Row getRow() {
        if (row == null) {
            row = getWorksheet().getRow(rowKey);
        }
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public Column getColumn() {
        if (column == null) {
            column = getWorksheet().getColumn(columnKey);
        }
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Cell getCell() {
        if (cell == null) {
            cell = getWorksheet().getCell(cellKey);
        }
        return cell;
    }

    @Override
    public String toString() {
        return "CellAddress [" + getExcelNotation() + "]";
    }

    @Override
    public String getExcelNotation() {
        return getWorksheetKey() + "!" + columnKey + rowKey;
    }

    @Override
    public String getSimpleNotation() {
        return columnKey + rowKey;
    }

    @Override
    public int getLowestColumnIndex() {
        return getColumn().getIndex();
    }

    @Override
    public int getHighestColumnIndex() {
        return getColumn().getIndex();
    }

    @Override
    public int getLowestRowIndex() {
        return getRow().getIndex();
    }

    @Override
    public int getHighestRowIndex() {
        return getRow().getIndex();
    }

    public String getRowKey() {
        return rowKey;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public String getCellKey() {
        return cellKey;
    }

}
