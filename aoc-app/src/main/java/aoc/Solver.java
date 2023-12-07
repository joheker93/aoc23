package aoc;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import aoc.days.day1.Day1;
import aoc.days.day2.Day2;
import aoc.days.day3.Day3;
import aoc.days.day4.Day4;
import aoc.days.day5.Day5;
import aoc.days.day6.Day6;
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
    }

    public static void start() throws IOException, InterruptedException {
        for (final Entry<Integer, Day<?, ?>> entry : DAYS.entrySet()) {

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
