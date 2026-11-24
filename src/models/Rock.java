package models;

import core.Simulation;

public record Rock(String symbol) implements StaticObject
{
    private final static String ROCK_SYMBOL = "🪨";

    public Rock()
    {
        this(ROCK_SYMBOL);
    }
    public String getSymbol() { return symbol; }
}
