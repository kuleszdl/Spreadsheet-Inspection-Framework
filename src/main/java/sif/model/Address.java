package sif.model;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.utility.Converter;

import java.util.List;

public abstract class Address {

    @SuppressWarnings("FieldCanBeLocal")
    private final Logger logger = LoggerFactory.getLogger(Address.class);
    private final Spreadsheet spreadsheet;
    private final Worksheet worksheet;
    private final String worksheetKey;
    private final AddressType addressType;
    private boolean isValidAddress = false;

    @AssistedInject
    Address(Spreadsheet spreadsheet, @Assisted String excelNotation, @Assisted AddressType addressType) {
        this.addressType = addressType;
        this.spreadsheet = spreadsheet;
        List<String> addressParams = Converter.extractExcelNotation(excelNotation);
        if (addressParams != null) {
            this.worksheetKey = addressParams.get(0);
            this.isValidAddress = true;
        } else {
            worksheetKey = "INVALID";
            logger.error("Unable to convert ExcelNotation '{}' interpreting as 'INVALID' YOU MUST CHECK ALL USER INPUT (POLICY)", excelNotation);
            this.isValidAddress = false;
        }
        this.worksheet = spreadsheet.getWorksheet(worksheetKey);
    }

    @AssistedInject
    Address(Spreadsheet spreadsheet, @Assisted Worksheet worksheet, @Assisted AddressType addressType) {
        this.addressType = addressType;
        this.spreadsheet = spreadsheet;
        this.worksheetKey = worksheet.getKey();
        this.worksheet = worksheet;
        this.isValidAddress = true;
    }

    public boolean onSameWorksheetAs(Address address) {
        return getWorksheet().equals(address.getWorksheet());
    }

    public String getExcelNotation() {
        return worksheetKey;
    }

    public String getSimpleNotation() {
        return worksheetKey;
    }

    public Worksheet getWorksheet() {
        return worksheet;
    }

    public String getWorksheetKey() {
        return worksheetKey;
    }

    public Spreadsheet getSpreadsheet() {
        return spreadsheet;
    }

    public AddressType getType() {
        return addressType;
    }

    public boolean isRightOf(Address address) {
        if (onSameWorksheetAs(address)) {
            if (getLowestColumnIndex() > address.getHighestColumnIndex()) {
                return true;
            }
        }
        return false;
    }

    public boolean isLeftOf(Address address) {
        if (onSameWorksheetAs(address)) {
            if (getHighestColumnIndex() > address.getLowestColumnIndex()) {
                return true;
            }
        }
        return false;
    }

    public boolean isBelow(Address address) {
        if (onSameWorksheetAs(address)) {
            if (getLowestRowIndex() > address.getHighestRowIndex()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAbove(Address address) {
        if (onSameWorksheetAs(address)) {
            if (getHighestRowIndex() < address.getLowestRowIndex()) {
                return true;
            }
        }
        return false;
    }

    public abstract int getLowestColumnIndex();

    public abstract int getHighestColumnIndex();

    public abstract int getLowestRowIndex();

    public abstract int getHighestRowIndex();

    public boolean isValidAddress() {
        return isValidAddress;
    }

    void setValidAddress(boolean flag) {
        isValidAddress = flag;
    }
}
