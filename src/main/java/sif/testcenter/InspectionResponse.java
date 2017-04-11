package sif.testcenter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request from a user of the Spreadsheet Inspection Framework to
 * inspect a model.
 */
@SuppressWarnings("unused")
@XmlRootElement
public class InspectionResponse {

    private List<String> errors;
    private List<ValidationReport> validationReports;

    public InspectionResponse() {
        // empty constructor for JAXB
    }

    @XmlElement(name = "error")
    @XmlElementWrapper(name = "errors")
    public List<String> getErrors() { return errors; }
    public void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

    @XmlElement(name = "validationReport")
    @XmlElementWrapper(name = "validationReports")
    public List<ValidationReport> getValidationReports() { return validationReports; }
    public void add(ValidationReport validationReport) {
        if (validationReports == null) {
            validationReports = new ArrayList<>();
        }
        validationReports.add(validationReport);
    }

}