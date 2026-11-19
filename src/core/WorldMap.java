package core;

import models.*;

import java.util.*;

public class WorldMap
{
    private Map<Position, Entity> entityes = new HashMap<>();
    public int xMax;
    public int yMax;
    public WorldMap(int xMax, int yMax)
    {
        this.xMax = xMax;
        this.yMax = yMax;
    }
    public Entity tryGetEntity(Position position)
    {
        return entityes.get(position);
    }
    public void addEntity(Position position, Entity entity)
    {
        entityes.put(position,entity);
    }
    public void putPos(Entity entity, Position newPos)
    {
        var pos = tryGetPos(entity);
        entityes.remove(pos);
        entityes.put(newPos, entity);
    }
    public Position tryGetPos(Entity entity)
    {
        for(var key : entityes.entrySet())
        {
            var value = key.getValue();
            if(value.equals(entity))
            {
                return key.getKey();
            }
        }
        return null;
    }
    public List<Entity> getEntityes()
    {
        List<Entity> entitiesL = new ArrayList<>();
        for(var set : entityes.entrySet())
        {
            entitiesL.add(set.getValue());
        }
        return entitiesL;
    }
    public void startSpawn()
    {
        spawnEntity(new Herbivore());
        spawnEntity(new Predator());
        spawnEntity(new Grass());
    }
    public void spawnEntity(Entity entity)
    {
        Random random = new Random();
        int x = random.nextInt(xMax);
        int y = random.nextInt(yMax);
        Position position = new Position(x,y);
        addEntity(position,entity);
    }
}
