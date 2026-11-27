package models;

public class Tree extends Entity {
    private final static String TREE_SYMBOL = "🌳";

    public Tree()
    {
        super(TREE_SYMBOL);
    }
    public EntityType getType()
    {
        return EntityType.TREE;
    }
}
