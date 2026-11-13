package core;
import java.util.Random;
import models.Position;
import models.Entity;

public class Simulation
{
    public void spawnEntity(Map gameMap, Entity entity)
    {
        Random random = new Random();
        int x = random.nextInt(gameMap.xMax);
        int y = random.nextInt(gameMap.yMax);
        Position position = new Position(x,y);
        gameMap.addEntity(position,entity);
    }

}
