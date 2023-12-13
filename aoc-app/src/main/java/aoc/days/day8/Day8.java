package aoc.days.day8;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc.Day;
import aoc.utils.Tuple;

public class Day8 implements Day<Map<String, Tuple<String, String>>, Long> {

    @Override
    public Long solveA(Map<String, Tuple<String, String>> parsedInput) {
        Map<String, Tuple<String, String>> network = new HashMap<>(parsedInput);

        System.out.println(parsedInput);
        String goal = "ZZZ";
        String current = "AAA";

        return search(network, current, List.of(goal));
    }

    public long search(Map<String, Tuple<String, String>> network, String start, List<String> goals) {
        String current = start;
        String path = network.get("PATH").fst();
        int pathPtr = 0;

        while (!goals.contains(current)) {
            char instruction = path.charAt((pathPtr++ % path.length()));
            Tuple<String, String> step = network.get(current);

            current = instruction == 'L' ? step.fst() : step.snd();
        }

        return pathPtr;
    }

    @Override
    public Long solveB(Map<String, Tuple<String, String>> parsedInput) {
        Map<String, Tuple<String, String>> network = new HashMap<>(parsedInput);
        List<String> startingNodes = network.keySet().stream().filter(str -> str.charAt(2) == 'A').toList();
        List<String> goals = network.keySet().stream().filter(str -> str.charAt(2) == 'Z').toList();
        List<Long> results = new ArrayList<>();

        for (String start : startingNodes) {
            results.add(search(network, start, goals));
        }

        return results.stream().map(x -> BigInteger.valueOf((long) x)).reduce(Day8::lcm).get().longValue();

    }

    public static BigInteger lcm(BigInteger number1, BigInteger number2) {
        return number1.multiply(number2).abs().divide(number1.gcd(number2));
    }

    @Override
    public Map<String, Tuple<String, String>> parseA(String input) throws IOException {
        var lines = input.lines().toList();
        String path = lines.get(0);
        Map<String, Tuple<String, String>> network = new HashMap<>();
        network.put("PATH", Tuple.of(path, path));

        String reg = "(\\w+) = \\((\\w+), (\\w+)\\)";
        for (String line : lines) {

            Matcher m = Pattern.compile(reg).matcher(line);
            if (!m.find()) {
                continue;
            }

            var from = m.group(1);
            var toLeft = m.group(2);
            var toRight = m.group(3);
            network.put(from, Tuple.of(toLeft, toRight));
        }
        return network;

    }

    @Override
    public Map<String, Tuple<String, String>> parseB(String input) throws IOException {
        return parseA(input);

    }

}
