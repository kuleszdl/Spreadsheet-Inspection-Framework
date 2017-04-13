package sif.testcenter;

import sif.model.Cell;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/***
 * A generic single violation to record the violation of any policy rule.
 */
@SuppressWarnings("unused")
@XmlRootElement
public abstract class Violation {

    public static final double SEVERITY_VERY_LOW = 20.0;
    public static final double SEVERITY_LOW = 40.0;
    public static final double SEVERITY_MEDIUM = 60.0;
    public static final double SEVERITY_HIGH = 80.0;
    public static final double SEVERITY_VERY_HIGH = 100.0;

    private Cell causingCell;

    /**
     * Constructor for a violation without a causingCell
     * If you use it, you should override getLocation() in your custom violation
     */
    public Violation() {
    }

    /**
     * Default constructor for a violation.
     *
     * @param cell provide the cell which caused the violation.
     */
    public Violation(Cell cell) {
        this.causingCell = cell;
    }

    @XmlElement(name = "location")
    public String getLocation() {
        if (causingCell != null)
            return causingCell.getCellAddress().getExcelNotation();
        else
            return null;
    }

    /**
     * Returns the cell which causes the violations. Most violations will use this, except for some global violations.
     *
     * @return the cell which caused the violation
     */
    @XmlTransient
    public Cell getCausingCell() {
        return causingCell;
    }

    /**
     * Returns a unique id (String) which represents the violation.
     * If you implement this, make sure that every violation will have a unique id that can be reproducable.
     * Look at other violation implementations for examples.
     *
     * @return the unique identifier as String
     */
    @XmlElement(name = "uid")
    public abstract String getUid();

    /**
     * Returns the description of the violation.
     * If you implement this, make sure that every violation will have proper (and translated) description.
     * Look at other violation implementations for examples.
     *
     * @return the description as String
     */
    @XmlElement(name = "description")
    public abstract String getDescription();

    /**
     * Returns the severity of the violation.
     * Currently this will return 0.0 in all implementations. Will maybe used in future versions.
     *
     * @return the description as String
     */
    @XmlElement(name = "severity")
    public abstract Double getSeverity();
}
