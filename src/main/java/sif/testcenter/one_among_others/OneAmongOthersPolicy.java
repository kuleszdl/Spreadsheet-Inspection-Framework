package sif.testcenter.one_among_others;

import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlRootElement;

/***
 * A policy rule to check if a cell contains something else then it should be in respect to the environment of the cell.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "oneAmongOthersPolicy")
public class OneAmongOthersPolicy extends Policy {

    public static final String HORIZONTAL = "horizontal";
    public static final String VERTICAL = "vertical";
    public static final String BOTH = "both";

    private String environmentStyle;
    private int environmentLength;

    public OneAmongOthersPolicy() {
        super();
        setName(Translator.tl("OneAmongOthersPolicy.Name"));
        setDescription(Translator.tl("OneAmongOthersPolicy.Description"));
        setBackground(Translator.tl("OneAmongOthersPolicy.Background"));
        setSolution(Translator.tl("OneAmongOthersPolicy.Solution"));
        setPolicyType(PolicyType.STATIC);
        this.environmentStyle = BOTH;
        this.environmentLength = 2;
    }

    public OneAmongOthersPolicy(String environmentStyle, int environmentLength) {
        this();
        this.environmentStyle = environmentStyle;
        this.environmentLength = environmentLength;
    }

    public String getEnvironmentStyle() {
        return environmentStyle;
    }

    public void setEnvironmentStyle(String environmentStyle) {
        this.environmentStyle = environmentStyle;
    }

    public int getEnvironmentLength() {
        return environmentLength;
    }

    public void setEnvironmentLength(int environmentLength) {
        this.environmentLength = environmentLength;
    }

}
