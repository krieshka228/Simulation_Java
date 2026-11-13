package core;

import models.Entity;
import models.Position;

import java.util.HashMap;

public class Map
{
    private HashMap<Position, Entity> entityes;
    public Entity tryGetEntity(Position position)
    {
        return entityes.get(position);
    }
    public void addEntity(Position position, Entity entity)
    {
        entityes.put(position,entity);
    }
}
