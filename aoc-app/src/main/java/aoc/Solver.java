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
import aoc.utils.Tuple;

public class Solver {

    private static final Map<Integer, Day<?, ?>> DAYS = new HashMap<>();

    static {
        DAYS.put(1, new Day1());
        DAYS.put(2, new Day2());

    }

    public static void start() throws IOException {
        for (final Entry<Integer, Day<?, ?>> entry : DAYS.entrySet()) {

            final Day<?, ?> sol = entry.getValue();
            final int day = entry.getKey();

            final URL path = sol.getClass().getResource("day" + day + ".in");
            final String inputStr = new String(Files.readAllBytes(Paths.get(path.getFile())));

            Tuple<?, ?> solutions = sol.solve(inputStr);
            System.out.println();
            System.out.println("====== " + sol.getClass().getSimpleName() + " ======");
            System.out.println("Part A: " + solutions.fst());
            System.out.println("Part B: " + solutions.snd());

        }
    }

}
