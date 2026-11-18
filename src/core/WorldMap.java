package core;

import models.Entity;
import models.Grass;
import models.Herbivore;
import models.Position;
import java.util.HashMap;

public class WorldMap
{
    private HashMap<Position, Entity> entityes = new HashMap<>();
    public int xMax;
    public int yMax;
    public WorldMap(int xMax, int yMax)
    {
        this.xMax = xMax;
        this.yMax = yMax;
        addEntity(new Position(2,2),new Herbivore(new Position(2,2)));
        addEntity(new Position(4,4), new Grass(new Position(4,4)));
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
        entityes.remove(entity.getPos());
        entityes.put(newPos, entity);
    }
}
