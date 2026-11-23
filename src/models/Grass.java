package models;

public record Grass(String symbol) implements StaticObject
{
    public Grass()
    {
        this("🌿");
    }
    @Override
    public String getSymbol(){return symbol;}
}
