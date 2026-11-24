package models;

public record Tree(String symbol) implements StaticObject
{
    private final static String TREE_SYMBOL = "🌳";

    public Tree()
    {
        this(TREE_SYMBOL);
    }
    public String getSymbol()
    {
        return symbol;
    }
}
