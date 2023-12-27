package aoc.days.day18;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import aoc.days.day18.Day18.Dig;
import aoc.utils.Point;
import aoc.Day;

public class Day18 implements Day<List<Dig>, Double> {

    @Override
    public Double solveA(List<Dig> digs) {
        double area = getArea(digs);
        double b = digs.stream().map(x -> (double) x._steps).reduce(0D, Double::sum);
        double i = picksTheorem(area, b);
        return i + b;
    }

    private double picksTheorem(double a, double b) {
        return a - (b / 2) + 1; // A = i + (b/2) - 1
    }

    private double getArea(List<Dig> digs) {
        double area = 0.0;
        Point current = Point.of(0, 0);

        for (var dig : digs) {
            for (int i = 0; i < dig._steps; i++) {
                Point next = Direction.adjust(current, 1, dig._dir);
                area += (current.x * next.y) - (next.x * current.y);
                current = next;
            }

        }
        return Math.abs(area) / 2.0;

    }

    @Override
    public Double solveB(List<Dig> parsedInput) {
        return solveA(parsedInput);
    }

    @Override
    public List<Dig> parseA(String input) throws IOException {
        var lines = input.lines().toList();

        List<Dig> digs = new ArrayList<>();

        for (var line : lines) {
            var split = line.split("\\s");

            String dir = split[0];
            String val = split[1];
            String col = split[2];

            digs.add(new Dig(Direction.of(dir), Integer.parseInt(val), col));
        }

        return digs;
    }

    @Override
    public List<Dig> parseB(String input) throws IOException {
        var digs = parseA(input);
        List<Dig> digsNew = new ArrayList<>();

        for (Dig dig : digs) {
            int firstFive = HexFormat.fromHexDigits(dig._col.subSequence(2, 7));
            char dir = dig._col.charAt(dig._col.length() - 2);
            Direction d = switch (dir) {
                case '0' -> Direction.RIGHT;
                case '1' -> Direction.DOWN;
                case '2' -> Direction.LEFT;
                case '3' -> Direction.UP;
                default -> null;
            };

            digsNew.add(new Dig(d, firstFive, dig._col));
        }
        return digsNew;
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT;

        static Direction of(String s) {
            return Map.of("U", UP, "D", DOWN, "L", LEFT, "R", RIGHT).get(s);
        }

        static Point adjust(Point p, int steps, Direction d) {
            return switch (d) {
                case UP -> Point.of(p.x - steps, p.y);
                case DOWN -> Point.of(p.x + steps, p.y);
                case LEFT -> Point.of(p.x, p.y - steps);
                case RIGHT -> Point.of(p.x, p.y + steps);
                default ->
                    throw new IllegalArgumentException("Unexpected value: " + d);
            };
        }
    }

    protected class Dig {
        Direction _dir;
        int _steps;
        String _col;

        Dig(Direction dir, int steps, String col) {
            _dir = dir;
            _steps = steps;
            _col = col;
        }

        @Override
        public String toString() {
            return _steps + ":" + _dir;
        }

    }

}
