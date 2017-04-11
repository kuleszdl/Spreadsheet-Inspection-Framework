package sif.testcenter;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import sif.io.spreadsheet.SpreadsheetIO;
import sif.model.Element;
import sif.model.Spreadsheet;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/***
 * Extended model class to represent spreadsheets.
 */
@RequestScoped
public class SpreadsheetInventory {

    private final TreeMap<String, List<? extends Element>> elementLists = new TreeMap<>();
    private final InspectionResponse inspectionResponse;
    private Spreadsheet spreadsheet;
    private InspectionRequest inspectionRequest;
    private SpreadsheetIO spreadsheetIO = null;

    @Inject
    SpreadsheetInventory(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
        this.inspectionRequest = new InspectionRequest();
        this.inspectionResponse = new InspectionResponse();
    }

    public Spreadsheet getSpreadsheet() {
        return spreadsheet;
    }

    public void setSpreadsheet(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
    }

    public InspectionRequest getInspectionRequest() {
        return inspectionRequest;
    }

    public void setInspectionRequest(InspectionRequest inspectionRequest) {
        this.inspectionRequest = inspectionRequest;
    }

    public InspectionResponse getInspectionResponse() {
        return inspectionResponse;
    }

    /***
     * Adds the given list of model model to the inventory. Only one
     * list per element class can be stored.
     *
     * @param key custom key for this list
     * @param list The given list.
     */
    public void addElementList(String key, List<? extends Element> list) {
        elementLists.put(key, list);
    }

    public List<? extends Element> getListFor(String key) {
        return elementLists.computeIfAbsent(key, k -> new ArrayList<>());
    }

    public SpreadsheetIO getSpreadsheetIO() {
        return spreadsheetIO;
    }

    public void setSpreadsheetIO(SpreadsheetIO spreadsheetIO) {
        this.spreadsheetIO = spreadsheetIO;
    }
}