package sif.io.spreadsheet;

import sif.model.Spreadsheet;

import java.io.InputStream;

public interface SpreadsheetIO {
    Spreadsheet createSpreadsheet(InputStream inputStream) throws InvalidSpreadsheetException;

    void updateSpreadsheetValues();

    void updateModelValues();

    void calculateFormulaValues();
}
