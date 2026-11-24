package models;

import core.Simulation;

public final class Herbivore extends Creature
{
    public Herbivore(int hp)
    {
        super(hp, Simulation.HERBIVORE_SYMBOL);
    }
}
