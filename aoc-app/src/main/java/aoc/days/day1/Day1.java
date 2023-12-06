package aoc.days.day1;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.Day;
import aoc.utils.EStream;
import aoc.utils.Tuple;
import aoc.utils.Utils;

import static aoc.utils.EStream.stream;
import static aoc.utils.EStream.sstream;

public class Day1 implements Day<List<String>, Integer> {

    @Override
    public List<String> parseA(String input) throws IOException {

        return stream(input)
                .lines()
                .map(x -> sstream(x)
                        .filter(Utils::isNum)
                        .foldWith("", (a, b) -> a + b))
                .toList();
    }

    @Override
    public Integer solveA(List<String> parsedInput) {
        int sum = 0;

        return stream(parsedInput).foldWith(sum, (acc, x) -> {
            EStream<String> s = sstream(x);
            return x.isEmpty() ? 0 : Utils.stoi(s.head() + s.last()) + acc;
        });
    }

    @Override
    public List<String> parseB(String input) throws IOException {
        List<String> r = stream(input).lines().toList();
        return r;
    }

    @Override
    public Integer solveB(List<String> parsedInput) {
        int res = stream(parsedInput).map(this::getDigits).foldWith(0, (acc, v) -> {
            var first = v.get(stream(v.keySet()).min());
            var last = v.get(stream(v.keySet()).max());
            return Utils.stoi(String.valueOf(first) + String.valueOf(last)) + acc;
        });

        return res;
    }

    public Map<Integer, Integer> getDigits(String str) {
        Map<Integer, Tuple<String, String>> map = new HashMap<>();

        for (int x : Utils.range(1, 10)) {
            map.put(x, Tuple.of(String.valueOf(x), Utils.lexValue(x)));
        }

        Map<Integer, Integer> found = new HashMap<>();
        for (Tuple<String, String> entry : map.values()) {
            String unLex = entry.fst();
            String lex = entry.snd();

            found.put(str.indexOf(unLex), Utils.stoi(unLex));
            found.put(str.indexOf(lex), Utils.stoi(unLex));
            found.put(str.lastIndexOf(unLex), Utils.stoi(unLex));
            found.put(str.lastIndexOf(lex), Utils.stoi(unLex));
            found.remove(-1);

        }
        return found;
    }

}
