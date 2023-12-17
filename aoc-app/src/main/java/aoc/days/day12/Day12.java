package aoc.days.day12;

import static aoc.utils.EStream.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import aoc.Day;
import aoc.utils.Triplet;
import aoc.utils.Tuple;
import aoc.utils.Utils;

public class Day12 implements Day<List<Tuple<String, List<Integer>>>, Long> {

    @Override
    public Long solveA(List<Tuple<String, List<Integer>>> data) {
        long sum = 0;

        for (var tup : data) {
            HashMap<Triplet<Integer, Integer, Integer>, Long> memo = new HashMap<>();
            long res = solver(tup.fst(), tup.snd(), 0, 0, 0, memo, 0);
            System.out.println(tup.fst() + " " + tup.snd() + " " + res);
            sum += res;
        }

        return sum;
    }

    public long solver(String sequence, List<Integer> group, int posInSeq, int uniq, int currentGroupCtr,
            Map<Triplet<Integer, Integer, Integer>, Long> memo, int memoHash) {

        Triplet<Integer, Integer, Integer> key = Triplet.of(posInSeq, currentGroupCtr, uniq, memoHash);

        var k = memo.get(key);
        if (k != null) {
            return k;
        }

        if (posInSeq == sequence.length()) {
            if (group.isEmpty() && currentGroupCtr == 0) {

                return 1;
            }

            if (group.size() == 1 && group.get(0) == currentGroupCtr) {

                return 1;
            }

            return 0;

        }

        long res = 0;
        List<Character> of = List.of('.', '#');
        char charAt = sequence.charAt(posInSeq);
        for (var c : of) {
            if (charAt == c || charAt == '?') { // Run it twice for ? but only once for # or .

                if (c == '.' && currentGroupCtr == 0) {
                    // Dot appeared but no was not coming from a group
                    // jump forward
                    res += solver(sequence, group, posInSeq + 1, uniq, 0, memo, ++memoHash);
                } else if (c == '.' && currentGroupCtr > 0 && !group.isEmpty()
                        && group.get(0) == currentGroupCtr) {
                    // We found a dot while counting a group, the group matched the requirement
                    // jump forward and go to next group
                    res += solver(sequence, group.subList(1, group.size()), posInSeq + 1, uniq + 1, 0, memo,
                            ++memoHash);
                } else if (c == '#') {
                    // keep moving and increment group ctr
                    res += solver(sequence, group, posInSeq + 1, uniq, currentGroupCtr + 1, memo, ++memoHash);
                }
            }
        }

        memo.put(key, res);
        return res;

    }

    @Override
    public Long solveB(List<Tuple<String, List<Integer>>> parsedInput) {
        return solveA(parsedInput);
    }

    @Override
    public List<Tuple<String, List<Integer>>> parseA(String input) throws IOException {
        List<Tuple<String, List<Integer>>> data = new ArrayList<>();

        String reg = "(.*) ([[\\d+,]||[\\d+]]+)";
        for (var row : input.lines().toList()) {
            Matcher m = Pattern.compile(reg).matcher(row);
            m.find();
            String sequence = m.group(1);
            List<Integer> groups = Arrays.stream(m.group(2).split(",")).map(Utils::stoi).toList();

            data.add(Tuple.of(sequence, groups));
        }
        return data;
    }

    @Override
    public List<Tuple<String, List<Integer>>> parseB(String input) throws IOException {
        var parsed = parseA(input);
        List<Tuple<String, List<Integer>>> data = new ArrayList<>();

        for (var tup : parsed) {
            String sequence = tup.fst();
            List<Integer> group = tup.snd();
            List<String> seq2List = stream("? ? ? ? ?").words().map(q -> sequence + q).get().toList();
            String seq2String = String.join("", seq2List);

            List<Integer> group2 = new ArrayList<>(group.size() * 5);

            for (int i = 0; i < 5; i++) {
                group2.addAll(group);
            }
            data.add(Tuple.of(seq2String.substring(0, seq2String.length() - 1), group2));
        }

        return data;
    }

}
