package sif.io.spreadsheet;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import sif.model.Spreadsheet;

@RequestScoped
abstract class BaseSpreadsheetIO implements SpreadsheetIO {

    private final Spreadsheet spreadsheet;

    @Inject
    BaseSpreadsheetIO(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
    }

    Spreadsheet getSpreadsheet() {
        return spreadsheet;
    }
}
