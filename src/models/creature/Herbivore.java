package models.creature;

import config.SimulationConfig;
import models.Entity;
import models.EntityType;
import models.Grass;

public final class Herbivore extends Creature
{
    private final static String HERBIVORE_SYMBOL = "🐰";

    public Herbivore(int hp, int moveSpeed)
    {
        super(hp, HERBIVORE_SYMBOL, moveSpeed);
    }


    public EntityType getType()
    {
        return EntityType.HERBIVORE;
    }
    public void takeDamage()
    {
        var newHp = getHp() - SimulationConfig.DAMAGE_PREDATOR;
        setHp(newHp);
    }
}
