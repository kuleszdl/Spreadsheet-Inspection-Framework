package sif.model.tokens;

import com.google.inject.assistedinject.AssistedInject;
import sif.model.Element;
import sif.model.TokenContainer;

public class UnspecifiedToken extends Element implements Token {

    private final TokenType tokenType;
    private TokenContainer container = null;

    @AssistedInject
    private UnspecifiedToken() {
        this.tokenType = TokenType.UNSPECIFIED;
    }

    UnspecifiedToken(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenString() {
        return "UNSPECIFIED";
    }

    public String getStringRepresentation() {
        return getClass().getSimpleName();
    }

    public TokenContainer getContainer() {
        return container;
    }

    public void setContainer(TokenContainer container) {
        this.container = container;
    }

    public TokenType getTokenType() {
        return tokenType;
    }
}
