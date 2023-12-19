package aoc.days.day13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aoc.Day;
import aoc.utils.Utils;

public class Day13 implements Day<List<char[][]>, Integer> {

    @Override
    public Integer solveA(List<char[][]> grids) {
        return solve(grids, 0);
    }

    private int solve(List<char[][]> grids, int diff) {
        int sum = 0;
        for (var grid : grids) {

            int reflectionLine = findReflectionLine(grid, diff);
            if (reflectionLine == -1) {
                var rotatedGrid = Utils.rotateRight(grid);
                reflectionLine = findReflectionLine(rotatedGrid, diff);

                // vertical line
                reflectionLine++;
                sum += reflectionLine;

            } else {
                // horizontal line
                reflectionLine++;
                sum += 100 * reflectionLine;
            }

        }

        return sum;
    }

    @Override
    public Integer solveB(List<char[][]> grids) {
        return solve(grids, 1);
    }

    private int findReflectionLine(char[][] grid, int diff) {
        for (int i = 0; i < grid.length - 1; i++) {
            var topSub = Arrays.copyOfRange(grid, 0, i + 1);
            var bottomSub = Arrays.copyOfRange(grid, i + 1, grid.length);

            if (bottomSub.length > topSub.length) {
                var reBottomSub = Arrays.copyOfRange(bottomSub, 0, topSub.length);

                if (gridDiff(flip(reBottomSub), topSub) == diff) {
                    return i;
                }

            } else if (bottomSub.length < topSub.length) {

                var reTopSub = Arrays.copyOfRange(topSub, topSub.length - bottomSub.length, topSub.length);

                if (gridDiff(flip(bottomSub), reTopSub) == diff) {
                    return i;
                }

            } else if (gridDiff(flip(bottomSub), topSub) == diff) {
                System.out.println("Found a reflection at " + i);
                return i;
            }

        }

        return -1;
    }

    private int gridDiff(char[][] c1, char[][] c2) {
        int diffs = 0;
        for (int i = 0; i < c1.length; i++) {
            for (int j = 0; j < c1[i].length; j++) {
                if (c1[i][j] != c2[i][j]) {
                    diffs++;
                }
            }
        }

        return diffs;
    }

    public static char[][] flip(char[][] array) {
        int rows = array.length;
        int columns = array[0].length;

        char[][] flippedArray = new char[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                flippedArray[rows - 1 - i][j] = array[i][j];
            }
        }

        return flippedArray;
    }

    @Override
    public List<char[][]> parseA(String input) throws IOException {
        var grids = input.split("\n\n");

        List<char[][]> data = new ArrayList<>();

        for (String grid : grids) {
            List<String> lines = grid.lines().toList();
            int height = lines.size();
            int width = lines.get(0).length();
            char[][] map = new char[height][width];

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    map[i][j] = lines.get(i).charAt(j);
                }
            }

            data.add(map);
        }

        return data;

    }

    @Override
    public List<char[][]> parseB(String input) throws IOException {
        return parseA(input);

    }

}
