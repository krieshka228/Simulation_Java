package models;

public class Predator extends Creature
{
    public int damage;
    public Predator(int hp, int damage)
    {
        super(hp,"🐺");
        this.damage = damage;

    }

}
