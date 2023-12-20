package aoc.days.day14;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import aoc.Day;
import aoc.utils.Tuple;

public class Day14 implements Day<char[][], Integer> {
    private enum Direction {
        NORTH, EAST, SOUTH, WEST;
    }

    private BiFunction<Direction, Tuple<Integer, Integer>, Tuple<Integer, Integer>> directionF = (d, t) -> switch (d) { //
        case NORTH -> Tuple.of(t.fst() - 1, t.snd());
        case EAST -> Tuple.of(t.fst(), t.snd() + 1);
        case SOUTH -> Tuple.of(t.fst() + 1, t.snd());
        case WEST -> Tuple.of(t.fst(), t.snd() - 1);
    };

    @Override
    public Integer solveA(char[][] grid) {
        int height = grid.length;
        int width = grid[0].length;
        move(grid, Direction.NORTH, 1, height, 0, width);
        return computeWeight(grid);
    }

    private void move(char[][] grid, Direction dir, int startR, int stopR, int startC, int stopC) {
        boolean moved = true;
        while (moved) {
            moved = false;
            for (int i = startR; i < stopR; i++) {
                for (int j = startC; j < stopC; j++) {
                    var dirTup = directionF.apply(dir, Tuple.of(i, j));
                    var r = dirTup.fst();
                    var c = dirTup.snd();
                    if (grid[r][c] == '.' && grid[i][j] == 'O') {
                        grid[r][c] = 'O';
                        grid[i][j] = '.';
                        moved = true;
                    }
                }
            }
        }
    }

    @Override
    public Integer solveB(char[][] grid) {
        int height = grid.length;
        int width = grid[0].length;
        List<Integer> sequence = new ArrayList<>();
        int c = 1;

        while (true) {
            move(grid, Direction.NORTH, 1, height, 0, width);
            move(grid, Direction.WEST, 0, height, 1, width);
            move(grid, Direction.SOUTH, 0, height - 1, 0, width);
            move(grid, Direction.EAST, 0, height, 0, width - 1);
            int computeWeight = computeWeight(grid);
            sequence.add(computeWeight);

            var rep = findFirstRepeatingSequence(sequence);
            if (rep.size() != 0) {
                int step = c - 2 * rep.size();
                int rem = (1000000000 - step) % rep.size();
                return sequence.get(step + rem - 1);
            }
            c++;
        }
    }

    public static List<Integer> findFirstRepeatingSequence(List<Integer> numbers) {
        List<Integer> repeatingSequence = new ArrayList<>();

        for (int chunkSize = 1; chunkSize <= numbers.size() / 2; chunkSize++) {
            int startIndex = 0;
            int endIndex = startIndex + chunkSize;

            while (endIndex <= numbers.size() - chunkSize) {
                List<Integer> pattern = new ArrayList<>(numbers.subList(startIndex, endIndex));
                int nextIndex = numbers.subList(endIndex, numbers.size()).indexOf(pattern.get(0));

                if (nextIndex != -1 && nextIndex + endIndex <= numbers.size()) {

                    List<Integer> potentialMatch = new ArrayList<>(
                            numbers.subList(endIndex, endIndex + pattern.size()));

                    if (pattern.equals(potentialMatch) && pattern.size() > 2) {
                        repeatingSequence.addAll(pattern);
                        return repeatingSequence;
                    }
                }

                startIndex++;
                endIndex++;
            }
        }

        return repeatingSequence;
    }

    private int computeWeight(char[][] grid) {
        int sum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'O') {
                    sum += (grid.length - i);

                }
            }
        }
        return sum;
    }

    @Override
    public char[][] parseA(String input) throws IOException {
        List<String> lines = input.lines().toList();
        int height = lines.size();
        int width = lines.get(0).length();
        char[][] grid = new char[height][width];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = lines.get(i).charAt(j);
            }
        }

        return grid;
    }

    @Override
    public char[][] parseB(String input) throws IOException {
        return parseA(input);
    }

}
