package sif.io.policy;

import sif.utility.Translator;

class InvalidPolicyException extends Exception {
    @Override
    public String getMessage() {
        return Translator.tl("InvalidPolicyException");
    }
}
