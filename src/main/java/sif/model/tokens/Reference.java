package sif.model.tokens;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import sif.model.Cell;
import sif.model.Referenceable;

public class Reference extends TokenContainerToken {

    private final Referenceable targetElement;
    private final Referenceable sourceElement;
    private final ReferenceType referenceType;

    @AssistedInject
    private Reference(@Assisted Cell cell, @Assisted("source") Referenceable source, @Assisted("target") Referenceable target) {
        super(cell, TokenType.REFERENCE);
        this.sourceElement = source;
        this.targetElement = target;
        if (targetElement.isSingleCell()) {
            this.referenceType = ReferenceType.CellReference;
        } else {
            this.referenceType = ReferenceType.CellMatrixReference;
        }
    }

    public boolean onSameWorksheet() {
        return getSourceElement().getWorksheet().equals(getTargetElement().getWorksheet());
    }

    public Referenceable getTargetElement() {
        return targetElement;
    }

    public Referenceable getSourceElement() {
        return sourceElement;
    }

    public ReferenceType getType() {
        return referenceType;
    }

    @Override
    public String getTokenString() {
        return getTargetElement().getSimpleNotation();
    }

    @Override
    public String getStringRepresentation() {
        String s = "[" + getSourceElement().getExcelNotation();
        s += "]-[" + getTargetElement().getExcelNotation() + "]";
        return s;
    }

    @Override
    public String toString() {
        return "Reference: " + sourceElement.getSimpleNotation() + " -> " + targetElement.getSimpleNotation();
    }

    /**
     * CALL THIS METHOD AFTER CREATING A REFERENCE
     * we can not pass this in a constructor
     *
     * @return this reference
     */
    public Reference init() {
        sourceElement.addOutgoingReference(this);
        targetElement.addIncomingReference(this);
        return this;
    }
}
