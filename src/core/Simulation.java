package core;
import java.util.*;
import java.util.logging.Handler;

import models.*;

public class Simulation
{
    public static int hpHerbivore;
    public static int hpPredator;
    public static int damagePredator;
    public Simulation(int hpPredator, int hpHerbivore, int damagePredator)
    {
        this.hpHerbivore = hpHerbivore;
        this.hpPredator = hpPredator;
        this.damagePredator = damagePredator;
    }
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
                   List<Position> neibghors = PathFinder.getNeighbors(pos);
                   for(var neib : neibghors)
                   {
                       if(PathFinder.isValidPos(neib,map))
                       {
                           var entity2 = map.tryGetEntity(neib);
                           if(entity2 != null)
                           {
                               if(isHerbivore(entity1))
                               {
                                   if(isGrass(entity2))
                                   {
                                       var herb = (Herbivore) entity1;
                                       if(herb.hp < hpHerbivore)
                                       {
                                           map.removeEntity(entity2);
                                           map.spawnEntity(new Grass());
                                           herb.hp += damagePredator;
                                       }
                                       else
                                       {
                                           herb.hp = hpHerbivore;
                                           map.removeEntity(entity2);
                                           map.spawnEntity(new Herbivore(hpHerbivore));
                                           map.spawnEntity(new Grass());
                                       }
                                   }
                                   entities = map.getEntityes();
                               }
                               if(isPredator(entity1))
                               {
                                   var pred = (Predator) entity1;
                                   if(isHerbivore(entity2))
                                   {

                                       var herb = (Herbivore) entity2;
                                       if(herb.hp >  pred.damage)
                                       {
                                           pred.hp += pred.damage;
                                           herb.hp -= pred.damage;
                                       }
                                       else
                                       {
                                           pred.hp += herb.hp;

                                           map.spawnEntity(new Predator(hpPredator, damagePredator));
                                           map.removeEntity(entity2);entities = map.getEntityes();
                                       }

                                       entities = map.getEntityes();
                                   }
                                   if(pred.hp > hpPredator)
                                   {
                                       pred.hp = hpPredator;
                                   }
                               }

                           }
                       }
                   }


               }});
               nextTurn(map, ent);

           }
            renderer.drawMap(map);
        }
    }
    public void nextTurn(WorldMap gameMap, Entity entity)
    {
        findPathForWolfs(gameMap);
        findPathForRabbits(gameMap);
        if (isGrass(entity))
        {
            return;
        }
        var path = pathes.get(entity);
        if(path == null)
        {
            return;
        }
        handler.onCome(gameMap, entity);
        if(path.size() == 0)
        {
            return;
        }

        var onePos = path.removeFirst();
        gameMap.putPos(entity, onePos);
        if(isPredator(entity))
        {
            var pred = (Creature) entity;
            if(pred.hp > 2)
            {
                pred.hp -= 2;
            }
            else
            {
                gameMap.removeEntity(entity);
            }

        }

    }
    public boolean isGameOver(WorldMap map)
    {
        boolean herbOver = true;
        boolean predOver = true;
        var entities = map.getEntityes();
        for(var e : entities)
        {
            if(isHerbivore(e))
            {
                herbOver = false;
            } else if (isPredator(e))
            {
                predOver = false;
            }
        }
        return herbOver || predOver;
    }
    public void findPathForWolfs(WorldMap map)
    {
        List<Entity> entities = map.getEntityes();
        for(var ent : entities)
        {
            if(isPredator(ent))
            {
                int min = Integer.MAX_VALUE;
                for (var e : entities)
                {
                    if(isHerbivore(e))
                    {
                        var path = PathFinder.bfs(map, map.tryGetPos(ent), map.tryGetPos(e));
                        if(path != null && path.size() < min)
                        {
                            min = path.size();
                            pathes.put(ent, path);
                        }
                    }
                }
            }
        }
    }
    public void findPathForRabbits(WorldMap map)
    {
        List<Entity> entities = map.getEntityes();

        for(var ent : entities)
        {
            if(isHerbivore(ent))
            {
                int min = Integer.MAX_VALUE;
                for (var e : entities)
                {
                    if(isGrass(e))
                    {
                        var path = PathFinder.bfs(map, map.tryGetPos(ent), map.tryGetPos(e));
                        if(path != null && path.size() < min)
                        {
                            min = path.size();
                            pathes.put(ent, path);
                        }
                    }
                }
            }
        }
    }
    public boolean isCreature(Entity entity)
    {
        return Creature.class.isInstance(entity);
    }
    public boolean isHerbivore(Entity entity)
    {
        if(isCreature(entity))
        {
            var creat = (Creature) entity;
            return creat.getSymbol() == "🐰";
        }
        return false;
    }
    public boolean isPredator(Entity entity)
    {
        if(isCreature(entity))
        {
            var creat = (Creature) entity;
            return creat.getSymbol() == "🐺";
        }
        return false;
    }
    public boolean isGrass(Entity entity)
    {
        if(isStatic(entity))
        {
            var stat = (StaticObject) entity;
            return stat.getSymbol() == "🌿";
        }
        return false;
    }
    public boolean isStatic(Entity entity)
    {
        return StaticObject.class.isInstance(entity);
    }
}
