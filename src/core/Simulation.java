package core;
import java.util.*;

import Actions.initActions;
import models.*;
import models.creature.Creature;
import models.creature.Herbivore;
import models.creature.Predator;

public class Simulation
{
    private WorldMap map;
    private Renderer renderer;
    private PathFinder pathFinder = new PathFinder();
    private initActions initActions;


    private List<Entity> entities = new ArrayList<>();
    private Handler handlerArrival;
    private Map<Entity,List<Position>> pathesByEntity = new HashMap<>();

    public Simulation(int hpPredator, int hpHerbivore, int damagePredator, int moveSpeedHerbivore, int moveSpeedPredator, int xMax, int yMax)
    {
        map = new WorldMap(xMax,yMax);
        renderer = new Renderer();
        EntitySpawner entitySpawner = new EntitySpawner(map);
        initActions = new initActions(map, entitySpawner, pathFinder);
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
        initActions.execute();
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
               if(isCreature(currentEntity))
               {
                   var currentCreature = (Creature) currentEntity;
                   for (int i = 0; i < currentCreature.getMoveSpeed(); i++)
                   {
                       findPathForEntities();
                       nextTurn(currentCreature);
                   }
               }


           }
            renderer.drawMap(map);
        }
    }
    private void nextTurn(Creature currentCreature)
    {
        var path = pathesByEntity.get(currentCreature);
        handlerArrival.onArrivle(currentCreature);
        if(isPathNullOrEmpty(path))
        {
            return;
        }
        currentCreature.makeMove(path, map);
        dropHpPredator(currentCreature);

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
        var currentHerbHp = herb.getHp();
        if(currentHerbHp < hpHerbivore)
        {
            map.removeEntity(currentGrass);
            spawnEntity(new Grass());
            currentHerbHp += damagePredator;
            herb.setHp(currentHerbHp);
        }
        else
        {
            currentHerbHp = hpHerbivore;
            var posGrass = map.tryGetPosEntity(currentGrass);
            map.removeEntity(currentGrass);
            map.removeEntity(herb);
            map.putPos(herb, posGrass);
            spawnEntity(new Herbivore(hpHerbivore, moveSpeedHerbivore));
            spawnEntity(new Grass());
            herb.setHp(currentHerbHp);
        }
    }
    private void predatorEatHerb(Predator currentPred, Herbivore currentHerb)
    {
        var currentPredHp = currentPred.getHp();
        var currentPredDamage = currentPred.getDamage();
        var currentHerbHp = currentHerb.getHp();
        if(currentHerbHp >  currentPredDamage)
        {
            currentPredHp += currentPredDamage;
            currentHerbHp -= currentPredDamage;
            currentHerb.setHp(currentHerbHp);
            currentPred.setHp(currentPredHp);
        }
        else
        {
            currentPredHp += currentHerbHp;
            currentPred.setHp(currentPredHp);
            map.removeEntity(currentHerb);
            spawnEntity(new Predator(hpPredator, damagePredator, moveSpeedPredator));
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
            var currentPred = (Predator) currentEntity;
            var currentPredHp = currentPred.getHp();
            if (isHerbivore(entityNeibghor)) {
                var herb = (Herbivore) entityNeibghor;
                predatorEatHerb(currentPred, herb);
                entities = map.getEntityes();
            }
            if (currentPredHp > hpPredator) {
                currentPredHp = hpPredator;
                currentPred.setHp(currentPredHp);
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
            var predHp = pred.getHp();
            if(predHp > 1)
            {
                predHp -= 1;
                pred.setHp(predHp);
            }
            else
            {
                map.removeEntity(pred);
            }

        }
    }
    public boolean isCreature(Entity entity)
    {
        return Creature.class.isInstance(entity);
    }
}
