package models;

public sealed interface StaticObject extends Entity permits Grass, Rock, Tree
{ }
