package aoc.days.day6;

import static aoc.utils.EStream.stream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import aoc.Day;
import aoc.utils.Tuple;
import aoc.utils.Utils;

public class Day6 implements Day<List<Tuple<Long, Long>>, Integer> {

    @Override
    public Integer solveA(List<Tuple<Long, Long>> parsedInput) {
        int res = 1;
        for (var tuple : parsedInput) {
            res *= waysToBeat(tuple);
        }
        return res;
    }

    @Override
    public Integer solveB(List<Tuple<Long, Long>> parsedInput) {
        return solveA(parsedInput);
    }

    public long waysToBeat(Tuple<Long, Long> race) {
        long res = 0;
        long time = race.fst();
        for (long i = 0; i <= time + 1; i++) {
            long charge = i;
            if (charge * (time - i) > race.snd()) {
                res++;
            }
        }
        return res;
    }

    @Override
    public List<Tuple<Long, Long>> parseA(String input) throws IOException {
        String reg = "Time:\\s*([\\d+\\s+]+)\\nDistance:\\s*([\\d+\\s+]+)";
        var m = Pattern.compile(reg).matcher(input);
        m.find();

        List<Long> times = stream(m.group(1).trim()).words().map(Utils::stol).toModList();
        List<Long> distances = stream(m.group(2).trim()).words().map(Utils::stol).toModList();
        return Utils.zip(times, distances);
    }

    @Override
    public List<Tuple<Long, Long>> parseB(String input) throws IOException {
        String reg = "Time:\\s*([\\d+\\s+]+)\\nDistance:\\s*([\\d+\\s+]+)";
        var m = Pattern.compile(reg).matcher(input);
        m.find();
        long time = Utils.stol(m.group(1).trim().replace(" ", ""));
        long distance = Utils.stol(m.group(2).trim().replace(" ", ""));

        return new ArrayList<>(Arrays.asList(Tuple.of(time, distance)));
    }

}
