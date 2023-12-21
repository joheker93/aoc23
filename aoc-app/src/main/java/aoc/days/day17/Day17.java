package aoc.days.day17;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import aoc.Day;
import aoc.utils.Tuple;
import aoc.utils.Utils;
import aoc.utils.Utils.Graph;
import aoc.utils.Utils.Point;
import aoc.utils.Utils.Vertice;

public class Day17 implements Day<Graph<Point>, Integer> {

    @Override
    public Integer solveA(Graph<Point> graph) {
        System.out.println(graph);

        Vertice<Point> source = Vertice.of(Point.of(0, 0));
        var sp = sp(graph, Vertice.of(Point.of(0, 0)));
        System.out.println(sp.fst().get(Vertice.of(Point.of(5, 5))));

        LinkedList<Vertice<Point>> seq = new LinkedList<>();
        Vertice<Point> u = Vertice.of(Point.of(5, 5));
        if (sp.snd().get(u) != null || u.equals(source)) {
            while (u != null) {
                seq.addFirst(u);
                u = sp.snd().get(u);
            }
        }

        char[][] grid = new char[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (seq.contains(Vertice.of(Point.of(i, j)))) {
                    grid[i][j] = 'X';
                } else {
                    grid[i][j] = '.';
                }
            }
        }

        Utils.printGrid(grid);

        System.out.println(seq);
        return null;
    }

    // 1 S ← empty sequence
    // 2 u ← target
    // 3 if prev[u] is defined or u = source: // Do something only if the vertex is
    // reachable
    // 4 while u is defined: // Construct the shortest path with a stack S
    // 5 insert u at the beginning of S // Push the vertex onto the stack
    // 6 u ← prev[u]

    // 1 function Dijkstra(Graph, source):
    // 2
    // 3 for each vertex v in Graph.Vertices:
    // 4 dist[v] ← INFINITY
    // 5 prev[v] ← UNDEFINED
    // 6 add v to Q
    // 7 dist[source] ← 0
    // 8
    // 9 while Q is not empty:
    // 10 u ← vertex in Q with min dist[u]
    // 11 remove u from Q
    // 12
    // 13 for each neighbor v of u still in Q:
    // 14 alt ← dist[u] + Graph.Edges(u, v)
    // 15 if alt < dist[v]:
    // 16 dist[v] ← alt
    // 17 prev[v] ← u
    // 18
    // 19 return dist[], prev[]

    private Tuple<Map<Vertice<Point>, Integer>, Map<Vertice<Point>, Vertice<Point>>> sp(Graph<Point> graph,
            Vertice<Point> source) {
        Map<Vertice<Point>, Vertice<Point>> prev = new HashMap<>();
        Map<Vertice<Point>, Integer> dist = new HashMap<>();
        Deque<Vertice<Point>> queue = new ArrayDeque<>();

        for (Vertice<Point> v : graph.vertices()) {
            dist.put(v, Integer.MAX_VALUE);
            queue.add(v);
        }

        dist.put(source, 0);
        while (!queue.isEmpty()) {
            var u = getMinDist(queue, dist);
            queue.remove(u);

            for (var neigh : getNeighs(u, queue, graph)) {
                int alt = dist.get(u) + graph.getEdgeCost(u, neigh);
            

                if (alt < dist.get(neigh)) {
                    dist.put(neigh, alt);
                    prev.put(neigh, u);
                }
            }
        }

        return Tuple.of(dist, prev);
    }

    private Set<Vertice<Point>> getNeighs(Vertice<Point> u, Deque<Vertice<Point>> queue, Graph<Point> graph) {
        Set<Vertice<Point>> neighs = new HashSet<>();
        for (var v : queue) {
            var edge = graph.getEdge(u, v);
            if (edge != null) {
                neighs.add(v);
            }
        }
        return neighs;
    }

    private Vertice<Point> getMinDist(Deque<Vertice<Point>> queue, Map<Vertice<Point>, Integer> dist) {
        Vertice<Point> min = null;
        int minimum = Integer.MAX_VALUE;

        for (var u : queue) {
            var g = dist.get(u);
            if (g < minimum) {
                min = u;
            }
        }
        return min;
    }

    @Override
    public Integer solveB(Graph<Point> parsedInput) {
        return null;

    }

    @Override
    public Graph<Point> parseA(String input) throws IOException {

        Graph<Point> graph = new Graph<>();
        var lines = input.lines().toList();
        int height = lines.size();
        int width = lines.get(0).length();
        int[][] grid = new int[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int val = Character.getNumericValue(lines.get(r).charAt(c));
                grid[r][c] = val;
            }
        }
        Point prev = Point.of(0, 0);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                prev = Point.of(r, c);
                addEdges(r, c, height, width, prev, graph, grid);
            }
        }

        return graph;

    }

    private void addEdges(int r, int c, int height, int width, Point prev, Graph<Point> graph, int[][] grid) {
        var left = Point.of(r, c - 1);
        var right = Point.of(r, c + 1);
        var up = Point.of(r - 1, c);
        var down = Point.of(r + 1, c);

        if (Utils.withinGrid(left, height, width) && left != prev) {
            graph.addEdge(Point.of(r, c), left, grid[left.x()][left.y()]);
        }
        if (Utils.withinGrid(right, height, width) && right != prev) {
            graph.addEdge(Point.of(r, c), right, grid[right.x()][right.y()]);
        }
        if (Utils.withinGrid(up, height, width) && up != prev) {
            graph.addEdge(Point.of(r, c), up, grid[up.x()][up.y()]);
        }
        if (Utils.withinGrid(down, height, width) && down != prev) {
            graph.addEdge(Point.of(r, c), down, grid[down.x()][down.y()]);
        }
    }

    @Override
    public Graph<Point> parseB(String input) throws IOException {

        return null;
    }

}
