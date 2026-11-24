package models;

import core.Simulation;

public record Grass(String symbol) implements StaticObject
{
    public Grass()
    {
        this(Simulation.GRASS_SYMBOL);
    }
    @Override
    public String getSymbol(){return symbol;}
}
