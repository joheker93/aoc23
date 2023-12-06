package aoc.days.day2;

import static aoc.utils.EStream.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc.Day;
import aoc.utils.Tuple;
import aoc.utils.Utils;
import aoc.utils.Utils.Colour;

public class Day2 implements Day<Map<Tuple<Integer, Integer>, Map<Colour, Integer>>, Integer> {

    @Override
    public Map<Tuple<Integer, Integer>, Map<Colour, Integer>> parseA(String input) throws IOException {

        String gp = "Game (\\d+):(.*)";
        Pattern p = Pattern.compile(gp);
        Matcher m = p.matcher(input);
        Map<Tuple<Integer, Integer>, Map<Colour, Integer>> games = new HashMap<>();

        while (m.find()) {
            int game = Utils.stoi(m.group(1));
            String[] sets = m.group(2).split(";");

            int setc = 1;
            for (String set : sets) {
                Map<Colour, Integer> gameMap = new HashMap<>();
                String[] items = set.split(",");
                for (String item : items) {
                    item = item.trim();
                    String[] data = item.split("\\s");
                    int value = Utils.stoi(data[0]);
                    String col = data[1];

                    gameMap.merge(Colour.get(col), value, Integer::sum);
                }
                games.put(new Tuple<Integer, Integer>(game, setc++), gameMap);
            }
        }
        return games;
    }

    @Override
    public Integer solveA(Map<Tuple<Integer, Integer>, Map<Colour, Integer>> parsedInput) {
        int redLimit = 12;
        int greenLimit = 13;
        int blueLimit = 14;

        Set<Integer> notPossibleGames = new HashSet<>();
        Set<Integer> games = new HashSet<>();

        for (var entry : parsedInput.entrySet()) {
            games.add(entry.getKey().fst());

            if (!possible(entry, Colour.RED, redLimit) || !possible(entry, Colour.BLUE, blueLimit)
                    || !possible(entry, Colour.GREEN, greenLimit)) {
                notPossibleGames.add(entry.getKey().fst());
            }
        }

        return stream(games).foldWith(Integer::sum) - stream(notPossibleGames).foldWith(Integer::sum);
    }

    public boolean possible(Entry<Tuple<Integer, Integer>, Map<Colour, Integer>> entry, Colour c, int limit) {
        return entry.getValue().getOrDefault(c, 0) <= limit;
    }

    @Override
    public Map<Tuple<Integer, Integer>, Map<Colour, Integer>> parseB(String input) throws IOException {
        return parseA(input);
    }

    @Override
    public Integer solveB(Map<Tuple<Integer, Integer>, Map<Colour, Integer>> parsedInput) {
        Map<Integer, List<Map<Colour, Integer>>> lowest = new HashMap<>();

        parsedInput.entrySet().stream().forEach(entry -> lowest.merge(entry.getKey().fst(),
                new ArrayList<>(Arrays.asList(entry.getValue())), (x, y) -> {
                    x.addAll(y);
                    return x;
                }));

        int res = 0;
        for (var mapList : lowest.values()) {
            res += getMax(Colour.RED, mapList) * getMax(Colour.GREEN, mapList) * getMax(Colour.BLUE, mapList);
        }

        return res;
    }

    public int getMax(Colour col, List<Map<Colour, Integer>> map) {
        return stream(map).map(m -> m.getOrDefault(col, 0)).max();
    }

}
