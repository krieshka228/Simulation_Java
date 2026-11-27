package core;

import models.Grass;
import models.Position;

import java.awt.*;
import java.util.*;
import java.util.List;
public class PathFinder {

    private Deque<List<Position>> deque = new ArrayDeque<>();
    private Set<Position> visited = new HashSet<>();
    private List<Position> startL = new ArrayList<>();
    private List<Position> path;
    private Position node;

    public List<Position> bfs(WorldMap map, Position start, Position end)
    {
        deque.clear();
        visited.clear();
        startL.clear();
        startL.add(start);

        deque.add(startL);
        while(!deque.isEmpty())
        {
            path = deque.removeFirst();
            node = path.get(path.size() - 1);

            if(nodeInVisited())
            {
                continue;
            }
            if(node.equals(end))
            {
                path.removeFirst();
                path.removeLast();
                return path;
            }
            visited.add(node);
            for(var neighb : getNeighbors(node))
            {
                if(isValidPos(neighb, map) && isEmptyCellOrTarget(neighb,map,end))
                {
                    List<Position> newPath = new ArrayList<>();
                    for (int i = 0; i < path.size(); i++)
                    {
                        newPath.add(path.get(i));
                    }
                    newPath.add(neighb);
                    deque.add(newPath);
                }

            }
        }
        return null;
    }
    public boolean isValidSpawn(WorldMap map)
    {
        Random random = new Random();
        var emptyPos = map.getEmptyPos();
        var start  = emptyPos.get(random.nextInt(emptyPos.size()));
        Deque<Position> deque1 = new ArrayDeque<>();
        visited.clear();

        deque1.add(start);
        while(!deque1.isEmpty()) {
            node = deque1.removeFirst();
            if (nodeInVisited()) {
                continue;
            }
            visited.add(node);
            for (var neighb : getNeighbors(node)) {
                if (isValidPos(neighb, map) && isEmptyCellOrGrass(neighb, map))
                {
                    deque1.add(neighb);
                }

            }
        }
        return visited.equals(new HashSet<>(emptyPos));

    }
    public boolean isValidPos(Position pos, WorldMap map)
    {
        return pos.x() < map.xMax && pos.x() >= 0 && pos.y() < map.yMax && pos.y() >= 0;
    }
    public List<Position> getNeighbors(Position node)
    {
        List<Position> neighbors = new ArrayList<>();
        neighbors.add(new Position(node.x() - 1, node.y()));
        neighbors.add(new Position(node.x(), node.y() - 1));
        neighbors.add(new Position(node.x() + 1, node.y()));
        neighbors.add(new Position(node.x(), node.y() + 1));
        return neighbors;
    }
    private boolean isEmptyCellOrTarget(Position currentNeibgh, WorldMap map, Position endPos)
    {
        if(currentNeibgh.equals(endPos))
        {
            return true;
        }
        return map.isPosNull(currentNeibgh);
    }
    private boolean isEmptyCellOrGrass(Position currentNeibgh, WorldMap map)
    {
        return map.isPosNull(currentNeibgh) || Grass.class.isInstance(map.tryGetEntity(currentNeibgh));
    }
    private boolean nodeInVisited()
    {
        return visited.contains(node);
    }
}
