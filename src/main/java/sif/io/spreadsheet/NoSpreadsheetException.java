package sif.io.spreadsheet;

import sif.utility.Translator;

public class NoSpreadsheetException extends Exception {
    @Override
    public String getMessage() {
        return Translator.tl("NoSpreadsheetException");
    }
}
