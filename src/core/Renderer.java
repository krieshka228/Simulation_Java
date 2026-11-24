package core;

import models.Entity;
import models.Position;

public class Renderer
{
    private static final String CLEAR_BYTES = "\033c";
    private static final int TIME_PAUSE = 1000;
    private static final String EMPTY_CELL_SYMBOL = "🔲";
    public void drawMap(WorldMap map)
    {
        consoleClear();
        for(int i = 0; i < map.xMax; i++)
        {
            System.out.println();
            for(int j = 0; j < map.yMax; j++)
            {
                Position position = new Position(i,j);
                Entity getEntity = map.tryGetEntity(position);
                if(getEntity == null) System.out.print(EMPTY_CELL_SYMBOL);
                else System.out.print(getEntity.getSymbol());
            }
        }
        startPause();
    }
    private void consoleClear()
    {
        System.out.print(CLEAR_BYTES);
        System.out.flush();
    }
    private void startPause()
    {
        try {
            Thread.sleep(TIME_PAUSE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
