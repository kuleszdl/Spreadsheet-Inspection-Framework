package sif.io.spreadsheet;

import sif.utility.Translator;

class InvalidSpreadsheetException extends Exception {
    @Override
    public String getMessage() {
        return Translator.tl("InvalidSpreadsheetException");
    }
}
