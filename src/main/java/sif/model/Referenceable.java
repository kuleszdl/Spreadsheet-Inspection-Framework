package sif.model;

import sif.model.tokens.Reference;

import java.util.List;

public interface Referenceable extends Addressable {

    List<Reference> getIncomingReferences();

    void addIncomingReference(Reference reference);

    List<Reference> getOutgoingReferences();

    void addOutgoingReference(Reference reference);
}
