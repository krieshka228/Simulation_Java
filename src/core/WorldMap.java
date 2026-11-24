package core;

import models.*;

import java.util.*;

public class WorldMap {
    private Map<Position, Entity> entityesByPosition = new HashMap<>();
    public final int xMax;
    public final int yMax;

    public WorldMap(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public Entity tryGetEntity(Position position) {
        return entityesByPosition.get(position);
    }

    public void addEntity(Position position, Entity entity) {
        entityesByPosition.put(position, entity);
    }

    public void putPos(Entity entity, Position newPos) {
        var pos = tryGetPosEntity(entity);
        entityesByPosition.remove(pos);
        entityesByPosition.put(newPos, entity);
    }

    public Position tryGetPosEntity(Entity entity) {
        for (var key : entityesByPosition.entrySet()) {
            var value = key.getValue();
            if (value.equals(entity)) {
                return key.getKey();
            }
        }
        return null;
    }

    public List<Entity> getEntityes() {
        List<Entity> entities = new ArrayList<>();
        for (var set : entityesByPosition.entrySet()) {
            entities.add(set.getValue());
        }
        return entities;
    }

    public void removeEntity(Entity entity) {
        entityesByPosition.remove(tryGetPosEntity(entity));
    }

    public boolean isPosNull(Position pos) {
        return entityesByPosition.get(pos) == null;
    }

    public List<Position> getEmptyPos() {
        List<Position> emptyPos = new ArrayList<>();
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                Position currentPos = new Position(x, y);
                if (isPosNull(currentPos)) {
                    emptyPos.add(currentPos);
                }
            }
        }
        return emptyPos;
    }

    public boolean isEntityNull(Entity currentEntity) {
        return currentEntity == null;
    }
}

