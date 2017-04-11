package sif.testcenter.reading_direction;

import sif.testcenter.Policy;
import sif.testcenter.PolicyType;
import sif.utility.Translator;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A policy rule that defines that references must be readable in configurable
 * directions.
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "readingDirectionPolicy")
public class ReadingDirectionPolicy extends Policy {

    private boolean leftToRight;
    private boolean topToBottom;

    public ReadingDirectionPolicy() {
        super();
        setName(Translator.tl("ReadingDirectionPolicy.Name"));
        setDescription(Translator.tl("ReadingDirectionPolicy.Description"));
        setBackground(Translator.tl("ReadingDirectionPolicy.Background"));
        setSolution(Translator.tl("ReadingDirectionPolicy.Solution"));
        setPolicyType(PolicyType.STATIC);
        this.leftToRight = true;
        this.topToBottom = true;
    }

    public ReadingDirectionPolicy(boolean ltr, boolean ttb) {
        this();
        this.leftToRight = ltr;
        this.topToBottom = ttb;
    }

    public boolean getLeftToRight() {
        return leftToRight;
    }

    public void setLeftToRight(boolean leftToRight) {
        this.leftToRight = leftToRight;
    }

    public boolean getTopToBottom() {
        return topToBottom;
    }

    public void setTopToBottom(boolean topToBottom) {
        this.topToBottom = topToBottom;
    }

}
