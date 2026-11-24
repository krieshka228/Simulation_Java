package core;
import java.util.*;

import models.*;

public class Simulation
{
    private WorldMap map;
    private PathFinder pathFinder = new PathFinder();
    public static int hpHerbivore;
    public static int hpPredator;
    public static int damagePredator;
    public final static String HERBIVORE_SYMBOL = "🐰";
    public final static String PREDATOR_SYMBOL = "🐺";
    public final static String GRASS_SYMBOL = "🌿";

    private List<Entity> entities = new ArrayList<>();
    private Handler handler;
    private Map<Entity,List<Position>> pathes = new HashMap<>();

    public Simulation(int hpPredator, int hpHerbivore, int damagePredator, int xMax, int yMax)
    {
        this.hpHerbivore = hpHerbivore;
        this.hpPredator = hpPredator;
        this.damagePredator = damagePredator;
        map = new WorldMap(xMax,yMax);
    }
    private interface Handler
    {
        void onCome(Entity entity1);
    }


    private void eatEntity(Handler handler)
    {
        this.handler = handler;
    }
    public void startSimulation()
    {
        List<Position> path = new ArrayList<>();
        Renderer renderer = new Renderer();
        map.startSpawn();

        entities = map.getEntityes();
        renderer.drawMap(map);
        while(!isGameOver())
        {
           for(var ent : entities)
           {
               eatEntity(new Handler() {
               @Override
               public void onCome(Entity entity1) {
                   var pos = map.tryGetPos(entity1);
                   if(pos == null)
                   {
                       return;
                   }
                   List<Position> neibghors = pathFinder.getNeighbors(pos);
                   for(var neib : neibghors)
                   {
                       if(pathFinder.isValidPos(neib,map))
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
                                           map.removeEntity(entity2);
                                           map.spawnEntity(new Predator(hpPredator, damagePredator));
                                           entities = map.getEntityes();
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
               nextTurn(ent);

           }
            renderer.drawMap(map);
        }
    }
    private void nextTurn(Entity entity)
    {
        findPathForWolfs();
        findPathForRabbits();
        if (isGrass(entity))
        {
            return;
        }
        var path = pathes.get(entity);
        if(path == null)
        {
            return;
        }
        handler.onCome(entity);
        if(path.size() == 0)
        {
            return;
        }

        var onePos = path.removeFirst();
        map.putPos(entity, onePos);
        if(isPredator(entity))
        {
            var pred = (Creature) entity;
            if(pred.hp > 2)
            {
                pred.hp -= 2;
            }
            else
            {
                map.removeEntity(entity);
            }

        }

    }
    private boolean isGameOver()
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
    private void findPathForWolfs()
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
                        var path = pathFinder.bfs(map, map.tryGetPos(ent), map.tryGetPos(e));
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
    private void findPathForRabbits()
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
                        var path = pathFinder.bfs(map, map.tryGetPos(ent), map.tryGetPos(e));
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
    private boolean isCreature(Entity entity)
    {
        return Creature.class.isInstance(entity);
    }
    private boolean isHerbivore(Entity entity)
    {
        if(isCreature(entity))
        {
            var creat = (Creature) entity;
            return creat.getSymbol() == HERBIVORE_SYMBOL;
        }
        return false;
    }
    private boolean isPredator(Entity entity)
    {
        if(isCreature(entity))
        {
            var creat = (Creature) entity;
            return creat.getSymbol() == PREDATOR_SYMBOL;
        }
        return false;
    }
    private boolean isGrass(Entity entity)
    {
        if(isStatic(entity))
        {
            var stat = (StaticObject) entity;
            return stat.getSymbol() == GRASS_SYMBOL;
        }
        return false;
    }
    private boolean isStatic(Entity entity)
    {
        return StaticObject.class.isInstance(entity);
    }
}
