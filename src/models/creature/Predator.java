package models.creature;

import config.SimulationConfig;
import models.EntityType;

public final class Predator extends Creature
{
    private final static String PREDATOR_SYMBOL = "🐺";

    private int damage;
    public Predator(int hp, int damage,int moveSpeed)
    {
        super(hp, PREDATOR_SYMBOL, moveSpeed);
        this.damage = damage;
    }
    public int getDamage()
    {
        return damage;
    }
    public void setDamage(int newDamage)
    {
        damage = newDamage;
    }

    public EntityType getType()
    {
        return EntityType.PREDATOR;
    }
    public void takeDamage()
    {
        var newHp = getHp() - SimulationConfig.DAMAGE_FOR_PREDATOR;
        setHp(newHp);
    }

}
