package models;

import core.WorldMap;

public sealed interface Entity permits StaticObject, Creature
{
    public String getSymbol();
}
