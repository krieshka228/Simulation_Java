package core;
import java.util.*;
import java.util.logging.Handler;

import models.*;

public class Simulation
{
    public interface Handler
    {
        void onCome(WorldMap map , Entity entity1);
    }
    private List<Entity> entities = new ArrayList<>();
    private Handler handler;
    public Map<Entity,List<Position>> pathes = new HashMap<>();

    public void eatEntity(Handler handler)
    {
        this.handler = handler;
    }
    public void startSimulation()
    {

        List<Position> path = new ArrayList<>();
        WorldMap map = new WorldMap(10,10);
        Renderer renderer = new Renderer();
        map.startSpawn();

        entities = map.getEntityes();
        renderer.drawMap(map);
        while(!isGameOver(map))
        {
            addPathForRabbit(map);
            addPathForWolf(map);

           for(var ent : entities)
           {
               eatEntity(new Handler() {
               @Override
               public void onCome( WorldMap map, Entity entity1) {
                   var pos = map.tryGetPos(entity1);
                   if(pos == null)
                   {
                       return;
                   }
                   List<Position> neibghors = Graph.getNeighbors(pos);
                   for(var neib : neibghors)
                   {
                       if(Graph.isValidPos(neib,map))
                       {
                           var entity2 = map.tryGetEntity(neib);
                           if(entity2 != null)
                           {
                               if(entity1.symbol == "🐰")
                               {
                                   if(entity2.symbol == "🌿")
                                   {
                                       map.removeEntity(entity2);
                                       map.spawnEntity(new Herbivore());
                                       map.spawnEntity(new Grass());
                                       entities = map.getEntityes();
                                   }
                               } else if (entity1.symbol == "🐺")
                               {
                                   if(entity2.symbol == "🐰")
                                   {
                                       map.removeEntity(entity2);
                                       map.spawnEntity(new Predator());
                                       entities = map.getEntityes();
                                   }
                               }
                           }
                       }
                   }


               }});
               playOneStep(map, ent);

           }
           renderer.drawMap(map);

        }
    }
    public void playOneStep(WorldMap gameMap, Entity entity)
    {
        if (entity.symbol == "🌿")
        {
            return;
        }
        var path = pathes.get(entity);
        if(path == null)
        {
            return;
        }
        if(path.size() == 0)
        {
            handler.onCome(gameMap, entity);
            return;
        }
        handler.onCome(gameMap, entity);
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
            if(e.symbol == "🐰")
            {
                herbOver = false;
            } else if (e.symbol == "🐺")
            {
                predOver = false;
            }
        }
        return herbOver || predOver;
    }
    public void addPathForWolf(WorldMap map)
    {
        List<List<Position>> pathesNeibghors = new ArrayList<>();
        List<Entity> entities = map.getEntityes();
        for(var ent : entities)
        {
            if(ent.symbol == "🐺")
            {
                for (var e : entities)
                {
                    if(e.symbol == "🐰")
                    {
                         var path = Graph.bfs(map, map.tryGetPos(ent), map.tryGetPos(e));
                            for(var p : path)
                            {
                                Entity enti = map.tryGetEntity(p);
                                if(enti != null && (enti.symbol == "🌿" || enti.symbol == "🐺"))
                                {
                                    List<Position> neibghors = Graph.getNeighbors(map.tryGetPos(enti));
                                    for(var neib : neibghors)
                                    {
                                        if(Graph.isValidPos(neib,map))
                                        {
                                            path = Graph.bfs(map, map.tryGetPos(ent), map.tryGetPos(enti));
                                            pathesNeibghors.add(path);
                                        }
                                    }
                                }
                            }
                        int min = Integer.MAX_VALUE;
                        for(var p : pathesNeibghors)
                        {
                            if(p.size() < min)
                            {
                                min = p.size();
                                if(path.size() != 0)
                                {
                                    path = p;
                                }

                            }
                        }
                         pathes.put(ent, path);
                    }
                }
            }
        }
    }
    public void addPathForRabbit(WorldMap map)
    {
        List<List<Position>> pathesNeibghors = new ArrayList<>();
        List<Entity> entities = map.getEntityes();
        for(var ent : entities)
        {
            if(ent.symbol == "🐰")
            {
                for (var e : entities)
                {
                    if(e.symbol == "🌿")
                    {
                        var path = Graph.bfs(map, map.tryGetPos(ent), map.tryGetPos(e));
                        for(var p : path)
                        {
                            Entity enti = map.tryGetEntity(p);
                            if(enti != null && (enti.symbol == "🐺" || enti.symbol == "🐰"))
                            {
                                List<Position> neibghors = Graph.getNeighbors(map.tryGetPos(enti));
                                for(var neib : neibghors)
                                {
                                    if(Graph.isValidPos(neib,map))
                                    {
                                        path = Graph.bfs(map, map.tryGetPos(ent), map.tryGetPos(enti));
                                        pathesNeibghors.add(path);
                                    }
                                }
                            }
                        }
                        int min = Integer.MAX_VALUE;
                        for(var p : pathesNeibghors)
                        {
                            if(p.size() < min)
                            {
                                min = p.size();
                                if(path.size() != 0)
                                {
                                    path = p;
                                }
                            }
                        }
                        pathes.put(ent, path);
                    }
                }
            }
        }
    }
}
