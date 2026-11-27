package core;

import models.*;

import java.util.Random;

public class EntitySpawner
{
    private WorldMap map;
    private PathFinder pathFinder;
    public EntitySpawner(WorldMap map, PathFinder pathFinder)
    {
        this.map = map;
        this.pathFinder = pathFinder;
    }

    public void spawnStaticObject(EntityType type)
    {
        Random random = new Random();
        for(int i = 1; i < random.nextInt(map.xMax * map.yMax); i++)
        {
            spawnEntity(type);
        }
    }
    public void spawnStaticObjects()
    {
        spawnStaticObject(EntityType.ROCK);
        spawnStaticObject(EntityType.GRASS);
        spawnStaticObject(EntityType.TREE);
    }
    public void spawnHerbivore()
    {
        spawnEntity(EntityType.HERBIVORE);
    }
    public void spawnPredator()
    {
        spawnEntity(EntityType.PREDATOR);
    }
    public void spawnEntity(EntityType type)
    {
        var emptyPos = map.getEmptyPos();
        if(emptyPos.size() > 0)
        {
            Random random = new Random();
            var pos = emptyPos.get(random.nextInt(emptyPos.size()));
            map.addEntity(pos, type);
        }

    }
}
