package Actions;

import core.*;
import models.EntityType;

public class initActions implements Action
{
    private WorldMap map;
    private EntitySpawner entitySpawner;
    private PathFinder pathFinder;

    public initActions(WorldMap map, EntitySpawner entitySpawner, PathFinder pathFinder)
    {
        this.entitySpawner = entitySpawner;
        this.map = map;
        this.pathFinder = pathFinder;
    }

    public void execute()
    {
        initMap();
    }
    private void initMap()
    {
        do {
            map.clearMap();
            entitySpawner.spawnStaticObjects();
            entitySpawner.spawnHerbivore();
            entitySpawner.spawnPredator();
        }while (!pathFinder.isValidSpawn(map));
    }



}
