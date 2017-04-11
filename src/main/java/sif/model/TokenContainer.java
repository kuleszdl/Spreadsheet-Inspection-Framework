package sif.model;

import sif.model.tokens.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class TokenContainer extends Element {

    private final List<Token> tokens = new ArrayList<>();
    private final Cell cell;

    protected TokenContainer(Cell cell) {
        this.cell = cell;
    }

    public void add(Token formulaToken) {
        tokens.add(formulaToken);
    }

    public void add(List<Token> tokenList) {
        for (Token token : tokenList) {
            tokens.add(token);
        }
    }

    public int getDepth() {
        int maxDepth = 0;
        for (Token token : getTokens()) {
            if (token instanceof TokenContainer) {
                TokenContainer container = (TokenContainer) token;
                int depth = container.getDepth();
                if (depth > maxDepth)
                    maxDepth = depth;
            }
        }
        return maxDepth + 1;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<Token> getAllTokens() {
        ArrayList<Token> allTokens = new ArrayList<>();

        for (Token token : tokens) {
            allTokens.add(token);
            if (token instanceof TokenContainer) {
                allTokens.addAll(((TokenContainer) token).getAllTokens());
            }

        }
        return allTokens;
    }

    public Cell getCell() {
        return cell;
    }
}
