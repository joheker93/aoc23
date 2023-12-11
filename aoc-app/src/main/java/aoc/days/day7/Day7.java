package aoc.days.day7;

import static aoc.utils.EStream.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aoc.Day;
import aoc.utils.Utils;

public class Day7 implements Day<List<Hand>, Integer> {

    protected static final Map<String, Integer> VALUES = new HashMap<>();
    static {
        // populate values
        List<String> cards = stream("2 3 4 5 6 7 8 9 T J Q K A").words().toModList();
        for (var tup : Utils.zip(Utils.range(2, 15), cards)) {
            VALUES.put(tup.snd(), tup.fst());
        }
    }

    @Override
    public Integer solveA(List<Hand> parsedInput) {
        List<Hand> hands = new ArrayList<>(parsedInput);
        Collections.sort(hands, Hand::compare);

        int rank = hands.size();
        int sum = 0;

        for (var hand : hands) {
            sum = sum + (rank * hand.getBid());
            rank--;

        }

        return sum;
    }

    @Override
    public Integer solveB(List<Hand> parsedInput) {
        VALUES.put("J", 0);
        for (var hand : parsedInput) {
            int jokers = Collections.frequency(hand.getCards(), "J");

            Set<String> handSet = new HashSet<>(Set.copyOf(hand.getCards()));
            handSet.remove("J");
            if (jokers == 5 || jokers == 4) {
                hand.setType(7);
                continue;
            }

            if (jokers == 3) {
                if (hand.isPair(handSet)) {
                    // can get 5 kind
                    hand.setType(7);
                } else {
                    hand.setType(6); // 4 kind
                }
            }

            if (jokers == 2) {
                if (hand.isThreeOfAKind(handSet)) {
                    hand.setType(7); // 5 kind
                } else if (hand.isPair(handSet)) {
                    hand.setType(6); // 4 kind
                } else {
                    hand.setType(4);
                }
            }

            if (jokers == 1) {
                if (hand.isFourOfAKind(handSet)) {
                    hand.setType(7); // 5 kind
                } else if (hand.isThreeOfAKind(handSet)) {
                    hand.setType(6); // 4 kind
                } else if (hand.isTwoPair(handSet)) {
                    hand.setType(5); // full house
                } else if (hand.isPair(handSet)) {
                    hand.setType(4); // 3 kind
                } else {
                    hand.setType(2); // pair
                }
            }
        }

        Collections.sort(parsedInput, Hand::compare);

        int res = 0;
        int rank = parsedInput.size();
        for (var hand : parsedInput) {
            res += rank * hand.getBid();
            rank--;
        }
        return res;
    }

    @Override
    public List<Hand> parseA(String input) throws IOException {
        List<String> lines = stream(input).lines().toList();
        List<Hand> hands = new ArrayList<>();

        for (var line : lines) {
            var split = line.split("\\s");
            var cards = split[0];
            var bid = Utils.stoi(split[1]);

            hands.add(new Hand(cards, bid));
        }

        return hands;
    }

    @Override
    public List<Hand> parseB(String input) throws IOException {
        return parseA(input);
    }

}
