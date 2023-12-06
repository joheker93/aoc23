package aoc.days.day3;

import static aoc.utils.EStream.sstream;
import static aoc.utils.EStream.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.Day;
import aoc.utils.Tuple;
import aoc.utils.Utils;

public class Day3 implements Day<Map<List<Tuple<Integer, Integer>>, Integer>, Integer> {

    private int _gridSize;
    private List<String> _grid;

    @Override
    public Map<List<Tuple<Integer, Integer>>, Integer> parseA(String input) throws IOException {
        List<String> grid = input.lines().toList();
        Map<List<Tuple<Integer, Integer>>, Integer> numbersWithIndexes = new HashMap<>();

        _gridSize = grid.size();
        _grid = grid;

        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {
                if (isNum(grid, r, c)) {
                    // found digit
                    String number = consumeNumber(grid, r, c);
                    int offSet = number.length();
                    final int row = r;

                    int num = Utils.stoi(number);
                    numbersWithIndexes.put(new ArrayList<>(
                            Utils.range(c, c + offSet).stream().map(x -> Tuple.of(row, x)).toList()), num);

                    c += offSet - 1;
                }
            }
        }

        return numbersWithIndexes;
    }

    public boolean isNum(List<String> grid, int r, int c) {
        return Utils.isNum(String.valueOf(grid.get(r).charAt(c)));
    }

    public String consumeNumber(List<String> grid, int r, int c) {
        return sstream(grid.get(r)).drop(c).takeWhile(Utils::isNum).foldWith("", (x, y) -> x + y);
    }

    @Override
    public Integer solveA(Map<List<Tuple<Integer, Integer>>, Integer> parsedInput) {
        int res = 0;
        for (var entry : parsedInput.entrySet()) {
            var list = entry.getKey();
            var number = entry.getValue();
            var isPart = list.stream()
                    .map(t -> Utils.neighbours(_gridSize, _gridSize, t)
                            .stream()
                            .anyMatch(neigh -> {
                                char c = _grid.get(neigh.fst()).charAt(neigh.snd());
                                return !Character.isDigit(c) && c != '.';
                            }))
                    .anyMatch(x -> x == true);

            res += isPart ? number : 0;

        }

        return res;
    }

    @Override
    public Map<List<Tuple<Integer, Integer>>, Integer> parseB(String input) throws IOException {
        return parseA(input);
    }

    @Override
    public Integer solveB(Map<List<Tuple<Integer, Integer>>, Integer> parsedInput) {
        int res = 0;

        Map<Tuple<Integer, Integer>, List<Integer>> gearMap = new HashMap<>();
        for (var entry : parsedInput.entrySet()) {
            var list = entry.getKey();
            var number = entry.getValue();

            for (var pos : list) {
                for (var neigh : Utils.neighbours(_gridSize, _gridSize, pos)) {
                    char c = _grid.get(neigh.fst()).charAt(neigh.snd());
                    if (c == '*') {
                        // this number has a gear close
                        var n = gearMap.get(neigh);
                        if (n == null) {
                            gearMap.put(neigh, new ArrayList<>(Arrays.asList(number)));
                        } else {
                            if (!n.contains(number)) {
                                n.add(number);
                            }
                            gearMap.put(neigh, n);
                        }
                    }
                }
            }
        }

        for (var list : gearMap.values()) {
            if (list.size() == 2) {
                res += list.get(0) * list.get(1);
            }
        }

        return res;
    }

}
