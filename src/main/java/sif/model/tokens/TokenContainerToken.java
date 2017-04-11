package sif.model.tokens;

import sif.model.Cell;
import sif.model.TokenContainer;

abstract public class TokenContainerToken extends TokenContainer implements Token {

    private TokenContainer container = null;
    private TokenType tokenType = TokenType.UNSPECIFIED;

    TokenContainerToken(Cell cell, TokenType tokenType) {
        super(cell);
        this.tokenType = tokenType;
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
