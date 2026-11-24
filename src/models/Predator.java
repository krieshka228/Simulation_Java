package models;

import core.Simulation;

public final class Predator extends Creature
{
    public int damage;
    public Predator(int hp, int damage)
    {
        super(hp, Simulation.PREDATOR_SYMBOL);
        this.damage = damage;
    }

}
