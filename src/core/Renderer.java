package core;

import models.Entity;
import models.Position;

public class Renderer
{
    public static final String CLEAR_BYTES = "\033c";
    public void drawMap(WorldMap map)
    {
        consoleClear();
        for(int i = 0; i < map.xMax; i++)
        {
            IO.println();
            for(int j = 0; j < map.yMax; j++)
            {
                Position position = new Position(i,j);
                Entity getEntity = map.tryGetEntity(position);
                if(getEntity == null) IO.print("🔲");
                else IO.print(getEntity.symbol);
            }
        }
        pauseSec();
    }
    public void consoleClear()
    {
        System.out.print(CLEAR_BYTES);
        System.out.flush();
    }
    public void pauseSec()
    {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
