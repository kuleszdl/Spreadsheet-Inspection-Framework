package sif.model.tokens;

import com.google.inject.assistedinject.Assisted;
import sif.model.Cell;
import sif.model.Referenceable;

public interface TokenFactory {

    UnspecifiedToken createUnspecifiedToken();

    ScalarConstant createScalarConstant();

    Operator createOperator(@Assisted Cell cell);

    Function createFunction(@Assisted Cell cell, @Assisted String functionString);

    Reference createReference(@Assisted Cell cell, @Assisted("source") Referenceable source, @Assisted("target") Referenceable target);

}
