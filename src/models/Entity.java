package models;

public abstract class Entity
{
    private String symbol;
    public Entity(String symbol)
    {
        this.symbol = symbol;
    }
    public String getSymbol()
    {
        return symbol;
    }
    public abstract EntityType getType();


}

