package sif.model;

import com.google.inject.servlet.RequestScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The representation of a spreadsheet.
 */
@RequestScoped
public class Spreadsheet {

    private final Map<String, Worksheet> worksheets = new HashMap<>();
    private String name;
    private SpreadsheetType spreadsheetType;
    private final List<Cell> cells = new ArrayList<>();
    private final List<Formula> formulas = new ArrayList<>();

    public void add(Worksheet worksheet) {
        worksheets.put(worksheet.getKey(), worksheet);
    }

    public void add(Formula formula) {
        formulas.add(formula);
    }

    public void add(Cell cell) {
        cells.add(cell);
    }

    public List<Worksheet> getWorksheets() {
        return new ArrayList<>(worksheets.values());
    }

    public Worksheet getWorksheet(String worksheetKey) {
        return worksheets.get(worksheetKey);
    }

    public List<Cell> getCells() {
        return cells;
    }

    public List<Formula> getFormulas() {
        return formulas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpreadsheetType getSpreadsheetType() {
        return spreadsheetType;
    }

    public void setSpreadsheetType(SpreadsheetType spreadsheetType) {
        this.spreadsheetType = spreadsheetType;
    }
}