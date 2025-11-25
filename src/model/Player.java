package model;

public enum Player {
    X("X"), O("O");

    private final String symbol;

    Player(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public Player getOpponent() {
        return this == X ? O : X;
    }
}
