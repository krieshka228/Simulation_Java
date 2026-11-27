package models.creature;

import core.WorldMap;
import models.Entity;
import models.Position;

import java.util.List;

public abstract class Creature extends Entity
{
    private int hp;
    private int moveSpeed;
    public Creature(int hp, String symbol, int moveSpeed)
    {
        super(symbol);
        this.hp = hp;
        this.moveSpeed = moveSpeed;
    }
    public int getHp()
    {
        return hp;
    }
    public void setHp(int newHp)
    {
        hp = newHp;
    }
    public int getMoveSpeed()
    {
        return moveSpeed;
    }
    public void setMoveSpeed(int newMoveSpeed)
    {
        moveSpeed = newMoveSpeed;
    }
    public void makeMove(List<Position> path, WorldMap map)
    {
        for(int i = 0; i < moveSpeed; i++)
        {
            var onePos = path.removeFirst();
            map.putPos(this, onePos);
        }
    }
    public abstract void takeDamage();
}
