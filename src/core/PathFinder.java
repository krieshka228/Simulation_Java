package core;

import models.Position;

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

            if(visited.contains(node))
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
                if(isValidPos(neighb, map) && isEmptyCell(neighb,map,end))
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
    public boolean isValidPath(List<Position> path, WorldMap map)
    {
        for(int i = 1; i < path.size() - 1; i++)
        {
            if(!map.isNull(path.get(i)))
            {
                return false;
            }
        }
        return true;
    }
    private boolean isEmptyCell(Position pos, WorldMap map, Position endPos)
    {
        if(pos.equals(endPos))
        {
            return true;
        }
        else
        {
            return map.isNull(pos);
        }
    }
}
