package models;

public final class Predator extends Creature
{
    public final static String PREDATOR_SYMBOL = "🐺";

    public int damage;
    public Predator(int hp, int damage)
    {
        super(hp, PREDATOR_SYMBOL);
        this.damage = damage;
    }

}
