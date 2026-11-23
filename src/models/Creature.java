package models;

import core.WorldMap;

public sealed abstract class Creature implements Entity permits Predator, Herbivore {
    public int hp;
    public String symbol;
    public Creature(int hp, String symbol)
    {
        this.symbol = symbol;
        this.hp = hp;
    }
    public String getSymbol()
    {
        return symbol;
    }

}
