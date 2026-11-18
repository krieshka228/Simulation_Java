package core;

import models.Entity;
import models.Position;

public class Renderer
{
    public void drawMap(WorldMap map)
    {
        for(int i = 0; i < map.xMax; i++)
        {
            IO.println();
            for(int j = 0; j < map.yMax; j++)
            {
                Position position = new Position(i,j);
                Entity getEntity = map.tryGetEntity(position);
                if(getEntity == null) IO.print('0');
                else IO.print(getEntity.symbol);
            }
        }
    }
}
