import core.WorldMap;
import core.Renderer;
import core.Simulation;
import core.WorldMap;
import models.Entity;
import models.Graph;
import models.Herbivore;
import models.Position;

import java.util.List;

public class Program
{
    public static void main(String args[])
    {
        WorldMap map = new WorldMap(5,5);
        Graph graph = new Graph();
        Simulation simulation = new Simulation();
        Renderer renderer = new Renderer();
        Entity entity = map.tryGetEntity(new Position(2,2));
        Entity entity1 = map.tryGetEntity(new Position(4,4));
        List<Position> path = Graph.bfs(map, entity.getPos(), entity1.getPos());
        simulation.roadToDream(renderer, map);
    }
}
