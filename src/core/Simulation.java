package core;
import java.util.*;
import java.util.logging.Handler;

import models.*;

public class Simulation
{
    public interface Handler
    {
        void onCome(WorldMap map , Entity entity1, Entity entity2);
    }
    private Handler handler;
    public Map<Entity,List<Position>> pathes = new HashMap<>();

    public void eatEntity(Handler handler)
    {
        this.handler = handler;
    }
    public void startSimulation()
    {

        List<Position> path = new ArrayList<>();
        WorldMap map = new WorldMap(5,5);
        Renderer renderer = new Renderer();
        map.startSpawn();
        addPathForRabbit(map);
        List<Entity> entities = map.getEntityes();
        renderer.drawMap(map);
        System.out.println("\n------");
        while(!isGameOver(map))
        {
            addPathForWolf(map);

           for(var ent : entities)
           {
               playOneStep(map, ent);
               eatEntity(new Handler() {
               @Override
               public void onCome( WorldMap map, Entity entity1, Entity entity2 ) {
                           if(entity1.symbol == 'Z')
                           {
                               if(entity2.symbol == 'T')
                               {
                                   map.removeEntity(entity2);
                                   map.spawnEntity(new Herbivore());
                                   map.spawnEntity(new Grass());
                               }
                           } else if (entity1.symbol == 'W')
                           {
                               if(entity2.symbol == 'Z')
                               {
                                   map.removeEntity(entity2);
                                   map.spawnEntity(new Predator());
                               }
                           }

               }});
           }
           renderer.drawMap(map);
           System.out.println("\n------");

        }
    }
    public void playOneStep(WorldMap gameMap, Entity entity)
    {
        if (entity.symbol == 'T')
        {
            return;
        }
        var path = pathes.get(entity);
        if(path.size() == 0)
        {
            return;
        }w
        if(path.size() == 1)
        {
            var ent = gameMap.tryGetEntity(path.get(0));
            handler.onCome(gameMap, entity, ent);
        }
        var onePos = path.removeFirst();
        gameMap.putPos(entity, onePos);

    }
    public boolean isGameOver(WorldMap map)
    {
        boolean herbOver = true;
        boolean predOver = true;
        var entities = map.getEntityes();
        for(var e : entities)
        {
            if(e.symbol == 'Z')
            {
                herbOver = false;
            } else if (e.symbol == 'W')
            {
                predOver = false;
            }
        }
        return herbOver || predOver;
    }
    public void addPathForWolf(WorldMap map)
    {
        List<Entity> entities = map.getEntityes();
        for(var ent : entities)
        {
            if(ent.symbol == 'W')
            {
                for (var e : entities)
                {
                    if(e.symbol == 'Z')
                    {
                         var path = Graph.bfs(map, map.tryGetPos(ent), map.tryGetPos(e));
                         pathes.put(ent, path);
                    }
                }
            }
        }
    }
    public void addPathForRabbit(WorldMap map)
    {
        List<Entity> entities = map.getEntityes();
        for(var ent : entities)
        {
            if(ent.symbol == 'Z')
            {
                for (var e : entities)
                {
                    if(e.symbol == 'T')
                    {
                        var path = Graph.bfs(map, map.tryGetPos(ent), map.tryGetPos(e));
                        pathes.put(ent, path);
                    }
                }
            }
        }
    }
}
