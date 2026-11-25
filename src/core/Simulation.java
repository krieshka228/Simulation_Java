package core;
import java.util.*;

import models.*;

public class Simulation
{
    private WorldMap map;
    private Renderer renderer;
    private PathFinder pathFinder = new PathFinder();
    public static int hpHerbivore;
    public static int hpPredator;
    public static int damagePredator;

    private List<Entity> entities = new ArrayList<>();
    private Handler handlerArrival;
    private Map<Entity,List<Position>> pathesByEntity = new HashMap<>();

    public Simulation(int hpPredator, int hpHerbivore, int damagePredator, int xMax, int yMax)
    {
        this.hpHerbivore = hpHerbivore;
        this.hpPredator = hpPredator;
        this.damagePredator = damagePredator;
        map = new WorldMap(xMax,yMax);
        renderer = new Renderer();
    }
    private interface Handler
    {
        void onArrivle(Entity entity1);
    }

    private void eatEntity(Handler handlerArrival)
    {
        this.handlerArrival = handlerArrival;
    }

    public void startSimulation()
    {
        startSpawn();

        entities = map.getEntityes();
        renderer.drawMap(map);
        while(!isGameOver())
        {
           for(var currentEntity : entities)
           {
               eatEntity(new Handler() {
               @Override
               public void onArrivle(Entity currentEntity) {
                   var posCurrentEntity = map.tryGetPosEntity(currentEntity);
                   if(posCurrentEntity == null) { return; }

                   List<Position> neibghors = pathFinder.getNeighbors(posCurrentEntity);

                   for(var currentNeibghor : neibghors)
                   {
                       if(pathFinder.isValidPos(currentNeibghor, map))
                       {
                           var entityNeibghor = map.tryGetEntity(currentNeibghor);
                           if(!map.isEntityNull(entityNeibghor))
                           {
                                entityEatTarget(currentEntity, entityNeibghor);
                           }
                       }
                   }
               }});
               findPathForEntities();
               nextTurn(currentEntity);
           }
            renderer.drawMap(map);
        }
    }
    private void nextTurn(Entity curentEntity)
    {
        if (isGrass(curentEntity))
        {
            return;
        }
        var path = pathesByEntity.get(curentEntity);
        handlerArrival.onArrivle(curentEntity);
        if(isPathNullOrEmpty(path))
        {
            return;
        }

        var onePos = path.removeFirst();
        map.putPos(curentEntity, onePos);
        dropHpPredator(curentEntity);

    }
    private boolean isGameOver()
    {
        boolean herbIsOver = true;
        boolean predIsOver = true;
        var entities = map.getEntityes();
        for(var e : entities)
        {
            if(isHerbivore(e))
            {
                herbIsOver = false;
            } else if (isPredator(e))
            {
                predIsOver = false;
            }
        }
        return herbIsOver || predIsOver;
    }
    private void findPathForPred()
    {
        List<Entity> entities = map.getEntityes();
        addPathForPred(entities);
    }
    private void findPathForHerb()
    {
        List<Entity> entities = map.getEntityes();

        addPathForHerb(entities);
    }
    private boolean isHerbivore(Entity entity)
    {
        return Herbivore.class.isInstance(entity);
    }
    private boolean isPredator(Entity entity)
    {
        return Predator.class.isInstance(entity);
    }
    private boolean isGrass(Entity entity)
    {
        return Grass.class.isInstance(entity);
    }
    private int addPath(Entity currentEntity, Entity targetEntity, int min)
    {
        var start = map.tryGetPosEntity(currentEntity);
        var end = map.tryGetPosEntity(targetEntity);
        var path = pathFinder.bfs(map, start, end);
        if(!isPathNull(path) && path.size() < min)
        {
            min = path.size();
            pathesByEntity.put(currentEntity, path);
        }
        return min;
    }
    private void addPathForHerb(List<Entity> entities)
    {
        for(var currentEntity : entities)
        {
            if(isHerbivore(currentEntity))
            {
                int min = Integer.MAX_VALUE;
                for (var targetEntity : entities)
                {
                    if(isGrass(targetEntity))
                    {
                        min = addPath(currentEntity, targetEntity, min);
                    }
                }
            }
        }
    }
    private void addPathForPred(List<Entity> entities)
    {
        for(var currentEntity : entities)
        {
            if(isPredator(currentEntity))
            {
                int min = Integer.MAX_VALUE;
                for (var targetEntity : entities)
                {
                    if(isHerbivore(targetEntity))
                    {
                        addPath(currentEntity, targetEntity, min);
                    }
                }
            }
        }
    }
    private void herbivoreEatGrass(Herbivore herb, Grass currentGrass)
    {
        if(herb.hp < hpHerbivore)
        {
            map.removeEntity(currentGrass);
            spawnEntity(new Grass());
            herb.hp += damagePredator;
        }
        else
        {
            herb.hp = hpHerbivore;
            var posGrass = map.tryGetPosEntity(currentGrass);
            map.removeEntity(currentGrass);
            map.removeEntity(herb);
            map.putPos(herb, posGrass);
            spawnEntity(new Herbivore(hpHerbivore));
            spawnEntity(new Grass());
        }
    }
    private void predatorEatHerb(Predator currentPred, Herbivore currentHerb)
    {
        if(currentHerb.hp >  currentPred.damage)
        {
            currentPred.hp += currentPred.damage;
            currentHerb.hp -= currentPred.damage;
        }
        else
        {
            currentPred.hp += currentHerb.hp;
            map.removeEntity(currentHerb);
            spawnEntity(new Predator(hpPredator, damagePredator));
            entities = map.getEntityes();
        }
    }
    private void entityEatTarget(Entity currentEntity, Entity entityNeibghor)
    {
        if(isHerbivore(currentEntity))
        {
            if(isGrass(entityNeibghor))
            {
                var currentGrass = (Grass) entityNeibghor;
                var currentHerb = (Herbivore) currentEntity;
                herbivoreEatGrass(currentHerb, currentGrass);
            }
            entities = map.getEntityes();
        }
        if(isPredator(currentEntity))
        {
            var CurrentPred = (Predator) currentEntity;
            if (isHerbivore(entityNeibghor)) {
                var herb = (Herbivore) entityNeibghor;
                predatorEatHerb(CurrentPred, herb);
                entities = map.getEntityes();
            }
            if (CurrentPred.hp > hpPredator) {
                CurrentPred.hp = hpPredator;
            }
        }
    }
    private void findPathForEntities()
    {
        findPathForHerb();
        findPathForPred();
    }
    private boolean isPathNull(List<Position> path)
    {
        return path == null;
    }
    private boolean isPathNullOrEmpty(List<Position> path)
    {
        return isPathNull(path) || path.isEmpty();
    }
    private void dropHpPredator(Entity currentEntity)
    {
        if(isPredator(currentEntity))
        {
            var pred = (Creature) currentEntity;
            if(pred.hp > 1)
            {
                pred.hp -= 1;
            }
            else
            {
                map.removeEntity(pred);
            }

        }
    }
    public void startSpawn()
    {
        do {
            clearMap();
            spawnStaticObjects();
            spawnHerbivore();
            spawnPredator();
        }while (!pathFinder.isValidSpawn(map));
    }
    public void spawnStaticObject(StaticObject currentStaticObject)
    {
        Random random = new Random();
        for(int i = 1; i < random.nextInt(map.xMax * map.yMax); i++)
        {
            spawnEntity(currentStaticObject);
        }
    }
    public void spawnStaticObjects()
    {
        spawnStaticObject(new Rock());
        spawnStaticObject(new Tree());
        spawnStaticObject(new Grass());
    }
    public void spawnHerbivore()
    {
        spawnEntity(new Herbivore(Simulation.hpHerbivore));
    }
    public void spawnPredator()
    {
        spawnEntity(new Predator(Simulation.hpPredator,Simulation.damagePredator));
    }
    public void spawnEntity(Entity currentEntity)
    {
        var emptyPos = map.getEmptyPos();
        if(emptyPos.size() > 0)
        {
            Random random = new Random();
            var pos = emptyPos.get(random.nextInt(emptyPos.size()));
            map.addEntity(pos,currentEntity);
        }

    }
    public void clearMap()
    {
        entities = map.getEntityes();
        if(entities.size() > 0)
        {
            for(var currentEntity : entities)
            {
                map.removeEntity(currentEntity);
            }
        }

    }
}
