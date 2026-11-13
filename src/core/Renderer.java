package core;

import models.Entity;
import models.Position;

public class Renderer
{
    public void drawMap(Map gameMap)
    {
        for(int i = 0; i < gameMap.xMax; i++)
        {
            IO.println();
            for(int j = 0; j < gameMap.yMax; j++)
            {
                Position position = new Position(i,j);
                Entity getEntity = gameMap.tryGetEntity(position);
                if(getEntity == null) IO.print('0');
                else IO.print(getEntity.symbol);

            }
        }
    }
}
