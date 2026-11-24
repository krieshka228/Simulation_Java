package models;

public sealed interface Entity permits StaticObject, Creature
{
    String getSymbol();
}
