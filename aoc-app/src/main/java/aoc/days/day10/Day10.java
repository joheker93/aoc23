package aoc.days.day10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import aoc.Day;
import aoc.utils.Tuple;

public class Day10 implements Day<Tuple<Tuple<Integer, Integer>, char[][]>, Integer> {

    @Override
    public Integer solveA(Tuple<Tuple<Integer, Integer>, char[][]> parsedInput) {

        List<Tuple<String, Tuple<Integer, Integer>>> path = computeCycle(parsedInput.fst(), parsedInput.snd());
        return path.size() / 2;

    }

    private List<Tuple<String, Tuple<Integer, Integer>>> computeCycle(Tuple<Integer, Integer> current,
            char[][] grid) {
        char direction = getDirection(current, grid);
        List<Tuple<String, Tuple<Integer, Integer>>> path = new ArrayList<>();

        while (true) {

            path.add(Tuple.of(String.valueOf(direction), current));

            int row = current.fst();
            int col = current.snd();

            int stepRow = -1;
            int stepCol = -1;

            if (direction == 'L') {
                stepRow = row;
                stepCol = col - 1;
            } else if (direction == 'R') {
                stepRow = row;
                stepCol = col + 1;
            } else if (direction == 'D') {
                stepRow = row + 1;
                stepCol = col;
            } else if (direction == 'U') {
                stepRow = row - 1;
                stepCol = col;
            }

            char c = grid[stepRow][stepCol];

            if (c == 'S') {
                break;
            }

            if (c == '|') {
                if (direction == 'U') {
                    current = Tuple.of(row - 1, col);
                } else if (direction == 'D') {
                    current = Tuple.of(row + 1, col);
                }
            }

            if (c == '-') {
                if (direction == 'L') {
                    current = Tuple.of(row, col - 1);
                } else if (direction == 'R') {
                    current = Tuple.of(row, col + 1);
                }
            }

            if (c == 'L') {
                if (direction == 'D') {
                    current = Tuple.of(row + 1, col);
                    direction = 'R';
                } else if (direction == 'L') {
                    current = Tuple.of(row, col - 1);
                    direction = 'U';
                }
            }

            if (c == 'J') {
                if (direction == 'R') {
                    current = Tuple.of(row, col + 1);
                    direction = 'U';
                } else if (direction == 'D') {
                    current = Tuple.of(row + 1, col);
                    direction = 'L';
                }
            }

            if (c == '7') {
                if (direction == 'R') {
                    current = Tuple.of(row, col + 1);
                    direction = 'D';
                } else if (direction == 'U') {
                    current = Tuple.of(row - 1, col);
                    direction = 'L';
                }
            }

            if (c == 'F') {
                if (direction == 'U') {
                    current = Tuple.of(row - 1, col);
                    direction = 'R';
                } else if (direction == 'L') {
                    current = Tuple.of(row, col - 1);
                    direction = 'D';
                }
            }

        }

        return path;
    }

    private char getDirection(Tuple<Integer, Integer> current, char[][] grid) {

        Tuple<Integer, Integer> next = getEntry(grid, current);

        char direction = 'X';
        if (next.fst() > current.fst()) {
            direction = 'D';
        }
        if (next.fst() < current.fst()) {
            direction = 'U';
        }
        if (next.snd() > current.snd()) {
            direction = 'R';
        }
        if (next.snd() < current.snd()) {
            direction = 'L';
        }
        return direction;
    }

    private Tuple<Integer, Integer> getEntry(char[][] grid, Tuple<Integer, Integer> start) {
        int row = start.fst();
        int col = start.snd();
        if (grid[row - 1][col] == '|' || grid[row - 1][col] == 'F'
                || grid[row - 1][col] == '7' && row - 1 >= 0) {
            return Tuple.of(row - 1, col);
        }

        if (grid[row + 1][col] == '|' || grid[row + 1][col] == 'J'
                || grid[row + 1][col] == 'L' && row + 1 < grid.length) {
            return Tuple.of(row + 1, col);
        }

        if ((col - 1 >= 0) && grid[row][col - 1] == '-' || grid[row][col - 1] == 'F'
                || grid[row][col - 1] == 'L') {
            return Tuple.of(row, col - 1);
        }

        if (grid[row][col + 1] == '-' || grid[row][col + 1] == 'J'
                || grid[row][col + 1] == '7' && col + 1 < grid[0].length) {
            return Tuple.of(row, col + 1);
        }

        return null;

    }

    @Override
    public Integer solveB(Tuple<Tuple<Integer, Integer>, char[][]> parsedInput) {
        Tuple<Integer, Integer> startPos = parsedInput.fst();
        char[][] grid = parsedInput.snd();
        List<Tuple<String, Tuple<Integer, Integer>>> path = computeCycle(startPos, grid);

        int gridHeight = grid.length;
        int gridWidth = grid[0].length;

        char[][] gridCopy = new char[gridHeight][gridWidth];
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                gridCopy[i][j] = grid[i][j];
            }
        }
        Tuple<String, Tuple<Integer, Integer>> previousPoint = path.get(0);
        for (var point : path) {
            stepAndFill(grid, path, gridHeight, gridWidth, point, previousPoint, "Right");
            stepAndFill(gridCopy, path, gridHeight, gridWidth, point, previousPoint, "Left");
            previousPoint = point;
        }

        int res1 = 0;
        int res2 = 0;
        for (char[] l : grid) {
            for (char c : l) {
                if (c == 'I') {
                    res1++;
                }
            }
        }

        for (char[] l : gridCopy) {
            for (char c : l) {
                if (c == 'I') {
                    res2++;
                }
            }
        }
        // One of the answers are correct, depending on which route you walk
        // along the closed cycle
        System.out.println(Tuple.of(res1, res2));

        return res2;

    }

    private void stepAndFill(char[][] grid, List<Tuple<String, Tuple<Integer, Integer>>> path, int gridHeight,
            int gridWidth, Tuple<String, Tuple<Integer, Integer>> point,
            Tuple<String, Tuple<Integer, Integer>> previousPoint, String side) {
        int row = point.snd().fst();
        int col = point.snd().snd();
        char direction = point.fst().charAt(0);
        var pathNodes = path.stream().map(Tuple::snd).toList();

        int sideInc = side.equals("Left") ? -1 : 1;

        if (direction == 'U') {
            if (col + sideInc < 0 || path.contains(Tuple.of(row, col + sideInc))) {
                return;
            }
            if (side == "Right") {
                floodFill(grid, gridWidth, gridHeight, row, col + 1, pathNodes, 'I');
            } else {
                floodFill(grid, gridWidth, gridHeight, row, col - 1, pathNodes, 'I');
            }
        }

        if (direction == 'D') {
            if (col - sideInc >= gridWidth || path.contains(Tuple.of(row, col - sideInc))) {
                return;
            }
            if (side == "Right") {
                floodFill(grid, gridWidth, gridHeight, row, col - 1, pathNodes, 'I');
            } else {
                floodFill(grid, gridWidth, gridHeight, row, col + 1, pathNodes, 'I');
            }
        }

        if (direction == 'L') {
            if (row - sideInc >= gridHeight || path.contains(Tuple.of(row - sideInc, col))) {
                return;
            }

            if (side == "Right") {
                floodFill(grid, gridWidth, gridHeight, row - 1, col, pathNodes, 'I');
            } else {
                floodFill(grid, gridWidth, gridHeight, row + 1, col, pathNodes, 'I');
                if (previousPoint.fst().equals("D")) {
                    floodFill(grid, gridHeight, gridWidth, row, col + 1, pathNodes, 'I');
                }
            }
        }

        if (direction == 'R') {
            if (row + sideInc < 0 || path.contains(Tuple.of(row + sideInc, col))) {
                return;
            }

            if (side.equals("Right")) {
                floodFill(grid, gridWidth, gridHeight, row + 1, col, pathNodes, 'I');
            } else {
                floodFill(grid, gridWidth, gridHeight, row - 1, col, pathNodes, 'I');
                if (previousPoint.fst().equals("U")) {
                    floodFill(grid, gridWidth, gridHeight, row, col - 1, pathNodes, 'I');
                }
            }
        }

    }

    private boolean canFill(char[][] screen, int height, int width, int row, int col,
            List<Tuple<Integer, Integer>> path,
            char newC) {
        if (row < 0 || row >= height || col < 0 || col >= width || path.contains(Tuple.of(row, col))) {
            return false;
        }

        if (screen[row][col] == newC) {
            return false;
        }

        return true;
    }

    private void floodFill(char[][] grid, int height, int width, int row, int col, List<Tuple<Integer, Integer>> path,
            char replacement) {
        if (!canFill(grid, height, width, row, col, path, replacement)) {
            return;
        }

        grid[row][col] = replacement;

        floodFill(grid, height, width, row + 1, col, path, replacement);
        floodFill(grid, height, width, row - 1, col, path, replacement);
        floodFill(grid, height, width, row, col - 1, path, replacement);
        floodFill(grid, height, width, row, col + 1, path, replacement);
    }

    @Override
    public Tuple<Tuple<Integer, Integer>, char[][]> parseA(String input) throws IOException {
        Tuple<Integer, Integer> start = null;

        var inputLines = input.lines().toList();
        int height = inputLines.size();
        int width = inputLines.get(0).length();

        char[][] grid = new char[height][width];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = inputLines.get(i).charAt(j);
            }
        }
        var resized = padGrid(grid, Math.max(height, width) + 3);

        for (int i = 0; i < resized.length; i++) {
            for (int j = 0; j < resized[i].length; j++) {
                if (resized[i][j] == 'S') {
                    start = Tuple.of(i, j);
                }
            }
        }
        return Tuple.of(start, resized);

    }

    // pad and put in center, just for visuals
    public static char[][] padGrid(char[][] original, int newSize) {
        int rows = original.length;
        int cols = original[0].length;

        char[][] paddedArray = new char[newSize][newSize];

        for (char[] row : paddedArray) {
            for (int i = 0; i < newSize; i++) {
                row[i] = '.';
            }
        }

        int startRow = (newSize - rows) / 2;
        int startCol = (newSize - cols) / 2;

        for (int i = 0; i < rows; i++) {
            System.arraycopy(original[i], 0, paddedArray[startRow + i], startCol, cols);
        }

        return paddedArray;
    }

    @Override
    public Tuple<Tuple<Integer, Integer>, char[][]> parseB(String input) throws IOException {
        return parseA(input);
    }

}
