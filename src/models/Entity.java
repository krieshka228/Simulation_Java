package models;

import core.WorldMap;

public abstract class Entity {
    public final char symbol;
    protected Position pos;
    public Entity(char symbol, Position pos)
    {
        this.symbol = symbol;
        this.pos = pos;
    }
    public Position getPos()
    {
        return pos;
    }
    public void putPos(Position newPos, WorldMap gameMap)
    {
        gameMap.putPos(gameMap.tryGetEntity(pos), newPos);
        pos = newPos;
    }

}
