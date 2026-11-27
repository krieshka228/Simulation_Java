package models;

public class Rock extends Entity {
    private final static String ROCK_SYMBOL = "🪨";

    public Rock()
    {
        super(ROCK_SYMBOL);
    }
    public EntityType getType()
    {
        return EntityType.ROCK;
    }
}
