package models;

import config.SimulationConfig;
import models.creature.Herbivore;
import models.creature.Predator;

public class EntityFactory
{
    public static Entity create(EntityType type)
    {
        return switch (type)
        {
            case HERBIVORE -> new Herbivore(SimulationConfig.HP_HERBIVORE,SimulationConfig.MOVE_SPEED_HERBIVORE);
            case PREDATOR -> new Predator(SimulationConfig.HP_PREDATOR,SimulationConfig.DAMAGE_PREDATOR, SimulationConfig.MOVE_SPEED_HERBIVORE);
            case GRASS -> new Grass();
            case ROCK -> new Rock();
            case TREE -> new Tree();
            default -> throw new IllegalArgumentException("Unknown entity type:" + type);
        };
    }
}
