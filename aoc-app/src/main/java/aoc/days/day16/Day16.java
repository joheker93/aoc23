package aoc.days.day16;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiFunction;

import aoc.Day;
import aoc.utils.Utils.Point;

public class Day16 implements Day<char[][], Integer> {

    static final BiFunction<Point, String, Point> NEXT_POS = (p, s) -> switch (s) {
        case "Right" -> Point.of(p.x(), p.y() + 1);
        case "Left" -> Point.of(p.x(), p.y() - 1);
        case "Up" -> Point.of(p.x() - 1, p.y());
        case "Down" -> Point.of(p.x() + 1, p.y() + 1);
        default -> throw new IllegalArgumentException("Unexpected value: " + s);
    };
    

    @Override
    public Integer solveA(char[][] grid) {
        Map<Point, Integer> energies = new HashMap<>();
        LinkedList<Beam> beams = new LinkedList<>();

        beams.addFirst(new Beam("Right", Point.of(0, 0)));

        while (!beams.isEmpty()) {
            Beam b = beams.pollFirst();
            move(b, grid, energies, beams);
        }

        return null;
    }

    private void move(Beam b, char[][] grid, Map<Point, Integer> energies, LinkedList<Beam> beams) {
        Point nextPos = NEXT_POS.apply(b.pos, b.direction);
        if(!withinGrid(nextPos,grid.length,grid[0].length)){
            // This beam dies here
            beams.remove(b);
            return;
        }

        // apply rules and move
        // update energies
    }

    private boolean withinGrid(Point nextPos, int height, int width) {
        return nextPos.x() >= 0 && nextPos.x() < height && nextPos.y() >= 0 &&  nextPos.y() < width;
	}

	@Override
    public Integer solveB(char[][] parsedInput) {
        return null;
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
        return null;
    }

    private class Beam {
        String direction;
        Point pos;

        public Beam(String dir, Point p) {
            direction = dir;
            pos = p;
        }
    }

}
