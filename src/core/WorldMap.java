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
        spawnEntity(new Herbivore(Simulation.hpHerbivore));
        spawnEntity(new Predator(Simulation.hpPredator,Simulation.damagePredator));
        spawnEntity(new Grass());
    }
    public void spawnEntity(Entity entity)
    {
        var freePos = getFreePos();
        if(freePos.size() > 0)
        {
            Random random = new Random();
            var pos = freePos.get(random.nextInt(freePos.size()));
            addEntity(pos,entity);
        }

    }
    public void removeEntity(Entity entity)
    {
        entityes.remove(tryGetPos(entity));
    }
    public boolean isNull(Position pos)
    {
        return entityes.get(pos) == null;
    }
    public List<Position> getFreePos()
    {
        List<Position> freePos = new ArrayList<>();
        for(int x = 0; x < xMax; x++)
        {
            for(int y = 0; y < yMax; y++)
            {
                Position pos = new Position(x,y);
                if(isNull(pos))
                {
                    freePos.add(pos);
                }
            }
        }
        return freePos;
    }
    public List<Creature> getCreatures()
    {
        List<Entity> entityList = getEntityes();
        List<Creature> creaturesList = new ArrayList<>();
        for(var ent : entityList)
        {
            if(Creature.class.isInstance(ent))
            {
                creaturesList.add((Creature) ent);
            }
        }
        return creaturesList;
    }
}
