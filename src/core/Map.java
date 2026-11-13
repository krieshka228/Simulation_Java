package core;

import models.Entity;
import models.Position;
import java.util.HashMap;

public class Map
{
    private HashMap<Position, Entity> entityes = new HashMap<>();
    public int xMax;
    public int yMax;
    public Map(int xMax, int yMax)
    {
        this.xMax = xMax;
        this.yMax = yMax;
        for(int i = 0; i < xMax; i++)
        {
            for(int j = 0; j < yMax; j++)
            {
                Position position = new Position(i,j);
                entityes.put(position, null);
            }
        }
    }
    public Entity tryGetEntity(Position position)
    {
        return entityes.get(position);
    }
    public void addEntity(Position position, Entity entity)
    {
        entityes.put(position,entity);
    }
}
