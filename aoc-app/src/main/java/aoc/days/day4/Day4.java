package aoc.days.day4;

import static aoc.utils.EStream.stream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import aoc.Day;
import aoc.utils.Tuple;
import aoc.utils.Utils;

public class Day4 implements Day<Map<Integer, Tuple<List<Integer>, List<Integer>>>, Integer> {

    @Override
    public Map<Integer, Tuple<List<Integer>, List<Integer>>> parseA(String input) throws IOException {
        String card = "Card\\s*(\\d+): ([\\d+\\s]+) \\| ([\\d+\\s]+)";
        Pattern p = Pattern.compile(card);
        Matcher m = p.matcher(input);

        Map<Integer, Tuple<List<Integer>, List<Integer>>> cards = new HashMap<>();
        while (m.find()) {
            int game = Utils.stoi(m.group(1));
            List<Integer> winningNumbers = stream(m.group(2).trim()).words().map(Utils::stoi).toList();
            List<Integer> myNumbers = stream(m.group(3).trim()).words().map(Utils::stoi).toList();
            cards.put(game, Tuple.of(myNumbers, winningNumbers));
        }

        return cards;

    }

    @Override
    public Integer solveA(Map<Integer, Tuple<List<Integer>, List<Integer>>> parsedInput) {
        int res = 0;
        for (var entry : parsedInput.entrySet()) {
            var myCards = entry.getValue().fst();
            var winningCards = entry.getValue().snd();

            res += myCards.stream().reduce(0,
                    (acc, card) -> winningCards.contains(card) ? acc == 0 ? 1 : acc * 2 : acc);
        }
        return res;
    }

    @Override
    public Map<Integer, Tuple<List<Integer>, List<Integer>>> parseB(String input) throws IOException {
        return parseA(input);
    }

    @Override
    public Integer solveB(Map<Integer, Tuple<List<Integer>, List<Integer>>> parsedInput) {
        Map<Integer,Integer> winnings= new HashMap<>();

        for(var entry : parsedInput.entrySet()){
            int game = entry.getKey();
            List<Integer> myNumbers = entry.getValue().fst();
            List<Integer> winningNumbers = entry.getValue().snd();

            int hits = 0;
            for(int num : myNumbers){
                hits += winningNumbers.contains(num) ? 1 : 0;
            }
            winnings.merge(game, hits, Integer::sum);

        }

        Map<Integer,Integer> l = new HashMap<>();
        for(var x : winnings.entrySet()){
            l.merge(x.getKey(), 1, Integer::sum);
            getInstances(x.getKey(), winnings, l);
        }
        return l.values().stream().reduce(0,Integer::sum);
    }

    public void getInstances(Integer card, Map<Integer,Integer> map, Map<Integer,Integer> instances){
        if(map.get(card) == null) {
            return;
        }

        if(map.get(card) == 0){
            return;
        }
        for(var c : Utils.range(card+1,card+map.get(card)+1)){
            instances.merge(c, 1, Integer::sum);
            getInstances(c,map,instances);
        }

    }
}
