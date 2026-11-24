package models;

public final class Herbivore extends Creature
{
    private final static String HERBIVORE_SYMBOL = "🐰";

    public Herbivore(int hp)
    {
        super(hp, HERBIVORE_SYMBOL);
    }
}
