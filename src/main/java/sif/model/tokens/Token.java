package sif.model.tokens;

import sif.model.TokenContainer;

public interface Token {

    TokenContainer getContainer();

    void setContainer(TokenContainer container);

    TokenType getTokenType();

    String getTokenString();
}