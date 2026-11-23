package models;

public abstract class Creature extends Entity {
    public int hp;
    public Creature(int hp, String symbol)
    {
        super(symbol);
        this.hp = hp;

    }
}
