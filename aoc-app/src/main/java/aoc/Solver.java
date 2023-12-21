package aoc;

import static aoc.utils.EStream.stream;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import aoc.days.day1.Day1;
import aoc.days.day10.Day10;
import aoc.days.day11.Day11;
import aoc.days.day12.Day12;
import aoc.days.day13.Day13;
import aoc.days.day14.Day14;
import aoc.days.day15.Day15;
import aoc.days.day16.Day16;
import aoc.days.day17.Day17;
import aoc.days.day2.Day2;
import aoc.days.day3.Day3;
import aoc.days.day4.Day4;
import aoc.days.day5.Day5;
import aoc.days.day6.Day6;
import aoc.days.day7.Day7;
import aoc.days.day8.Day8;
import aoc.days.day9.Day9;
import aoc.utils.Tuple;

public class Solver {

    private static final Map<Integer, Day<?, ?>> DAYS = new HashMap<>();

    static {
        DAYS.put(1, new Day1());
        DAYS.put(2, new Day2());
        DAYS.put(3, new Day3());
        DAYS.put(4, new Day4());
        DAYS.put(5, new Day5());
        DAYS.put(6, new Day6());
        DAYS.put(7, new Day7());
        DAYS.put(8, new Day8());
        DAYS.put(9, new Day9());
        DAYS.put(10, new Day10());
        DAYS.put(11, new Day11());
        DAYS.put(12, new Day12());
        DAYS.put(13, new Day13());
        DAYS.put(14, new Day14());
        DAYS.put(15, new Day15());
        DAYS.put(16, new Day16());
        DAYS.put(17, new Day17());


    }

    public static void start(boolean latest) throws IOException, InterruptedException {
        Map<Integer, Day<?, ?>>entries = new HashMap<>();
        if(latest){
            int latestDay = stream(DAYS.keySet()).max();
            entries.put(latestDay, DAYS.get(latestDay));
        } else {
            entries = DAYS;
        }
        for (final Entry<Integer, Day<?, ?>> entry : entries.entrySet()) {

            final Day<?, ?> sol = entry.getValue();
            final int day = entry.getKey();

            final URL path = sol.getClass().getResource("day" + day + ".in");
            final String inputStr = new String(Files.readAllBytes(Paths.get(path.getFile())));

            var t = new Thread(() -> {
                Tuple<?,?> solutions = null;
                try {
                    solutions = sol.solve(inputStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println();
                System.out.println("====== " + sol.getClass().getSimpleName() + " ======");
                System.out.println("Part A: " + solutions.fst());
                System.out.println("Part B: " + solutions.snd());
            });

            t.start();
            t.join(1000);

            if(t.isAlive()){
                System.out.println();
                System.out.println("====== " + sol.getClass().getSimpleName() + " ======");
                System.out.println("Running too slow, continuing in the background!");
            }

        }
    }

}
