package aoc.days.day17;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import aoc.Day;

public class Day17 implements Day<int[][], Integer> {

    static int minHeatLoss = Integer.MAX_VALUE;

    private class State implements Comparable<State> {
        int heat, x, y, dx, dy;

        public State(int heat, int x, int y, int dx, int dy) {
            this.heat = heat;
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.heat, other.heat);
        }
    }
  
    @Override
    public Integer solveA(int[][] city) {
        return minHeatLoss(city,1,3);
    }

    private int minHeatLoss(int[][] grid, int minStep, int maxStep) {
        PriorityQueue<State> queue = new PriorityQueue<>();
        Set<List<Integer>> seen = new HashSet<>();

        int[][] directions = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

        queue.offer(new State(0, 0, 0, 0, 0));

        while (!queue.isEmpty()) {
            State cur = queue.poll();
            int heat = cur.heat;
            int x = cur.x;
            int y = cur.y;
            int dx = cur.dx;
            int dy = cur.dy;

            if (x == grid.length - 1 && y == grid[0].length - 1) {
                return heat;
            }

            if (seen.contains(Arrays.asList(x, y, dx, dy))) {
                continue;
            }

            seen.add(Arrays.asList(x, y, dx, dy));

            for (int[] dir : directions) {
                int dx2 = dir[0];
                int dy2 = dir[1];
                if (dx2 == dx && dy2 == dy || dx2 == -dx && dy2 == -dy) {
                    continue;
                }

                int x2 = x;
                int y2 = y;
                int h2 = heat;

                for (int i = 1; i <= maxStep; i++) {
                    x2 += dx2;
                    y2 += dy2;
                    if (x2 >= 0 && x2 < grid.length && y2 >= 0 && y2 < grid[0].length) {
                        h2 += grid[x2][y2];
                        if (i >= minStep) {
                            queue.offer(new State(h2, x2, y2, dx2, dy2));
                        }
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public Integer solveB(int[][] grid) {
        return minHeatLoss(grid, 4,10);

    }

    @Override
    public int[][] parseA(String input) throws IOException {

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

        return grid;

    }

    @Override
    public int[][] parseB(String input) throws IOException {

        return parseA(input);
    }

}
