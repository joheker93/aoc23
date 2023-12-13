package aoc.days.day9;

import static aoc.utils.EStream.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aoc.Day;
import aoc.utils.Utils;

public class Day9 implements Day<List<List<Integer>>, Integer> {

    @Override
    public Integer solveA(List<List<Integer>> parsedInput) {
        int result = 0;
        for (var sequence : parsedInput) {
            result += extrapolate(sequence);
        }
        return result;
    }

    public int extrapolate(List<Integer> sequence) {
        List<List<Integer>> diffs = new ArrayList<>();
        List<Integer> currentSequence = new ArrayList<>(sequence);

        while (true) {
            List<Integer> diff = new ArrayList<>();

            currentSequence.stream().reduce((x, y) -> {
                diff.add(y - x);
                return y;
            });

            if (diff.stream().allMatch(x -> x == 0)) {
                break;
            }

            diffs.add(new ArrayList<>(diff));

            currentSequence = diff;

        }
        diffs.add(0, sequence);
        return stream(diffs).map(l -> stream(l).last()).foldWith(Integer::sum);
    }

    @Override
    public Integer solveB(List<List<Integer>> parsedInput) {
        int result = 0;

        for (var sequence : parsedInput) {
            Collections.reverse(sequence);
            result += extrapolate(sequence);
        }

        return result;
    }

    @Override
    public List<List<Integer>> parseA(String input) throws IOException {
        return stream(input).lines().map(str -> stream(str).words().map(x -> Utils.stoi(x)).toModList()).toModList();
    }

    @Override
    public List<List<Integer>> parseB(String input) throws IOException {
        return parseA(input);
    }

}
