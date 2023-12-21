package aoc.days.day16;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import aoc.Day;
import aoc.utils.Utils;
import aoc.utils.Utils.Point;

public class Day16 implements Day<char[][], Integer> {

    static final BiFunction<Point, String, Point> NEXT_POS = (p, s) -> switch (s) {
        case "Right" -> Point.of(p.x(), p.y() + 1);
        case "Left" -> Point.of(p.x(), p.y() - 1);
        case "Up" -> Point.of(p.x() - 1, p.y());
        case "Down" -> Point.of(p.x() + 1, p.y());
        default -> throw new IllegalArgumentException("Unexpected value: " + s);
    };

    @Override
    public Integer solveA(char[][] grid) {
        Map<Point, List<String>> energies = createEnergyGrid(grid, new Beam("Right", Point.of(0, -1), 1));
        return energies.values().size() - 1;
    }

    private Map<Point, List<String>> createEnergyGrid(char[][] grid, Beam start) {
        Map<Point, List<String>> energies = new HashMap<>();
        LinkedList<Beam> beams = new LinkedList<>();

        beams.addFirst(start);

        while (true) {
            if (beams.isEmpty()) {
                break;
            }
            Beam b = beams.getFirst();
            move(b, grid, energies, beams);
        }
        return energies;
    }

    @Override
    public Integer solveB(char[][] grid) {
        List<Beam> startingBeams = new ArrayList<>();

        for (int i = 0; i < grid.length; i++) {
            startingBeams.add(new Beam("Right", Point.of(i, -1), 1));
            startingBeams.add(new Beam("Left", Point.of(i, grid.length), 1));
        }

        for (int i = 0; i < grid[0].length; i++) {
            startingBeams.add(new Beam("Down", Point.of(-1, i), 1));
            startingBeams.add(new Beam("Up", Point.of(grid[0].length, i), 1));

        }

        int max = 0;
        for (Beam startingBeam : startingBeams) {
            Map<Point, List<String>> energyGrid = createEnergyGrid(grid, startingBeam);
            if (energyGrid.size() - 1 > max) {
                max = energyGrid.size();
            }
        }
        return max - 1;
    }

    private void move(Beam b, char[][] grid, Map<Point, List<String>> energies, LinkedList<Beam> beams) {

        var energy = energies.get(b.pos);
        if (energy == null) {
            energies.put(b.pos, new ArrayList<>(List.of(b.direction)));
        } else if (!energy.contains(b.direction)) {
            energy.add(b.direction);
        }

        Point nextPos = NEXT_POS.apply(b.pos, b.direction);

        var nextEnergy = energies.get(nextPos);

        if (!Utils.withinGrid(nextPos, grid.length, grid[0].length)) {
            beams.remove(b);
            return;
        }

        if (nextEnergy != null && nextEnergy.contains(b.direction) && grid[nextPos.x()][nextPos.y()] == '.') {
            // already walked here, scram
            beams.remove(b);
            return;
        }

        b.pos = nextPos;

        if (grid[nextPos.x()][nextPos.y()] == '/') {
            if (b.direction == "Right") {
                b.direction = "Up";
            } else if (b.direction == "Left") {
                b.direction = "Down";
            } else if (b.direction == "Up") {
                b.direction = "Right";
            } else if (b.direction == "Down") {
                b.direction = "Left";
            }
        }
        if (grid[nextPos.x()][nextPos.y()] == '\\') {
            if (b.direction == "Right") {
                b.direction = "Down";
            } else if (b.direction == "Left") {
                b.direction = "Up";
            } else if (b.direction == "Up") {
                b.direction = "Left";
            } else if (b.direction == "Down") {
                b.direction = "Right";
            }
        }

        if (grid[nextPos.x()][nextPos.y()] == '-') {
            if (b.direction == "Up" || b.direction == "Down") {
                beams.remove(b);
                beams.addLast(new Beam("Left", nextPos, b.beam + 1));
                beams.addLast(new Beam("Right", nextPos, b.beam + 2));
            }
        }

        if (grid[nextPos.x()][nextPos.y()] == '|') {
            if (b.direction == "Left" || b.direction == "Right") {
                beams.remove(b);
                beams.addLast(new Beam("Up", nextPos, b.beam + 1));
                beams.addLast(new Beam("Down", nextPos, b.beam + 2));
            }
        }

    }


    private void printEnergyGrid(Map<Point, List<String>> energies, int height, int width, char[][] grid) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                var energy = energies.get(Point.of(i, j));

                if (grid[i][j] != '.') {
                    continue;
                }

                if (energy == null) {
                    grid[i][j] = '.';
                } else {
                    grid[i][j] = Character.forDigit(energy.size(), 10);
                }
            }
        }
        Utils.printGrid(grid);
    }

    @Override
    public char[][] parseA(String input) throws IOException {

        var lines = input.lines().toList();
        int height = lines.size();
        int width = lines.get(0).length();
        char[][] grid = new char[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                grid[r][c] = lines.get(r).charAt(c);
            }
        }

        return grid;
    }

    @Override
    public char[][] parseB(String input) throws IOException {
        return parseA(input);
    }

    private class Beam {
        int beam;
        String direction;
        Point pos;

        Beam(String dir, Point p, int b) {
            direction = dir;
            pos = p;
            beam = b;
        }

        @Override
        public String toString() {
            return String.valueOf(this.beam + " @ " + pos + " facing " + direction);
        }
    }

}
