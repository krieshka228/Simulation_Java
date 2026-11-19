package core;
import java.util.*;
import java.util.logging.Handler;

import models.*;

public class Simulation
{
    public interface Handler
    {
        void onCome(WorldMap map , Entity entity);
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
               public void onCome( WorldMap map, Entity entity) {
                   var neibghors = Graph.getNeighbors(map.tryGetPos(entity));
                   for(var neibghor : neibghors)
                   {
                       if(Graph.isValidPos(neibghor, map) && !map.isNull(neibghor))
                       {
                           if(entity.symbol == 'Z')
                           {
                               if(map.tryGetEntity(neibghor).symbol == 'T')
                               {
                                   map.removeEntity(map.tryGetEntity(neibghor));
                                   map.spawnEntity(new Herbivore());
                               }
                           } else if (entity.symbol == 'W')
                           {
                               if(map.tryGetEntity(neibghor).symbol == 'Z')
                               {
                                   map.removeEntity(map.tryGetEntity(neibghor));
                                   map.spawnEntity(new Predator());
                               }
                           }
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
            handler.onCome(gameMap, entity);
            return;
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
