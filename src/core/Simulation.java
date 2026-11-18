package core;
import java.util.List;
import java.util.Random;

import models.Graph;
import models.Position;
import models.Entity;

public class Simulation
{
    public void spawnEntity(WorldMap gameMap, Entity entity)
    {
        Random random = new Random();
        int x = random.nextInt(gameMap.xMax);
        int y = random.nextInt(gameMap.yMax);
        Position position = new Position(x,y);
        gameMap.addEntity(position,entity);
    }
    public void roadToDream(Renderer renderer, WorldMap gameMap)
    {

        Entity entity1 = gameMap.tryGetEntity(new Position(2,2));
        Entity entity2 = gameMap.tryGetEntity(new Position(4,4));
        List<Position> path = Graph.bfs(gameMap, entity1.getPos(), entity2.getPos());
        while(!entity1.getPos().equals(entity2.getPos()))
        {
            entity1.putPos(path.removeFirst(), gameMap);
            renderer.drawMap(gameMap);
            System.out.println("\n------");
        }



    }

}
