package sif.testcenter;

import sif.model.Cell;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@XmlRootElement
public abstract class Policy {
    private String name;
    private String description;
    private String background;
    private String solution;
    private PolicyType policyType;
    private List<String> ignoredCells;
    private List<String> ignoredWorksheets;
    private Double severityWeight;

    public Policy() {
        ignoredCells = new ArrayList<>();
        ignoredWorksheets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    public Double getSeverityWeight() {
        return severityWeight;
    }

    public void setSeverityWeight(Double severityWeight) {
        this.severityWeight = severityWeight;
    }

    @XmlElementWrapper(name = "ignoredCells")
    @XmlElement(name = "cell")
    public List<String> getIgnoredCells() {
        return ignoredCells;
    }

    public void setIgnoredCells(List<String> ignoredCells) {
        this.ignoredCells = ignoredCells;
    }

    @XmlElementWrapper(name = "ignoredWorksheets")
    @XmlElement(name = "worksheet")
    public List<String> getIgnoredWorksheets() {
        return ignoredCells;
    }

    public void getIgnoredWorksheets(List<String> ignoredCells) {
        this.ignoredCells = ignoredCells;
    }

    public boolean isIgnored(Cell cell) {
        return ignoredCells.contains(cell.getExcelNotation()) || ignoredWorksheets.contains(cell.getWorksheet().getKey());
    }
}