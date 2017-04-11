package sif.io.policy;

import sif.utility.Translator;

public class NoPolicyException extends Exception {
    @Override
    public String getMessage() {
        return Translator.tl("NoPolicyException");
    }
}
