package models;

import core.Simulation;

public record Grass(String symbol) implements StaticObject
{
    private final static String GRASS_SYMBOL = "🌿";

    public Grass()
    {
        this(GRASS_SYMBOL);
    }
    @Override
    public String getSymbol(){return symbol;}
}
