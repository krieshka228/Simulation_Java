package models;

public class Grass extends Entity {
    private final static String GRASS_SYMBOL = "🌿";

    public Grass() {
        super(GRASS_SYMBOL);
    }
    public EntityType getType()
    {
        return EntityType.GRASS;
    }
}
