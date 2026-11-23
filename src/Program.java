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
        Simulation simulation = new Simulation(20,20,10);
        simulation.startSimulation();
    }
}
