package aoc.days.day7;

import static aoc.utils.EStream.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import aoc.utils.Utils;

public class Hand {

    private final List<String> _cards;
    private final int _bid;
    private int _type;

    public Hand(String cards, int bid) {
        _cards = new ArrayList<>();
        _bid = bid;

        for (char c : cards.toCharArray()) {
            _cards.add(String.valueOf(c));
        }

        _type = getType();
    }

    public Hand(List<String> cards, int bid) {
        _cards = new ArrayList<>(cards);
        _bid = bid;
        _type = getType();

    }

    public int getType() {
        // hands are typed from Pair(1) .. to Five of a kind(7)
        Set<String> s = Set.copyOf(_cards);
        if (isFiveOfAKind(s)) {
            return 7;
        }

        if (isFourOfAKind(s)) {
            return 6;
        }

        if (isFullHouse(s)) {
            return 5;
        }

        if (isThreeOfAKind(s)) {
            return 4;
        }

        if (isTwoPair(s)) {
            return 3;
        }

        if (isPair(s)) {
            return 2;
        }

        // All cards should be unique here
        if (s.size() != 5) {
            System.out.println(_cards + " seem malformed");
        }

        return 1;
    }

    private boolean isFiveOfAKind(Set<String> set) {
        if (set.size() == 1) {
            return true;
        }

        return false;
    }

    boolean isFourOfAKind(Set<String> set) {

        for (var s : set) {
            if (occurences(s) == 4) {
                return true;
            }
        }

        return false;
    }

    private boolean isFullHouse(Set<String> set) {
        if (set.size() != 2) {
            return false;
        }

        var l = new ArrayList<>(set);
        var e1 = occurences(l.get(0));
        var e2 = occurences(l.get(1));

        if ((e1 == 3 && e2 == 2) || (e1 == 2 && e2 == 3)) {
            return true;
        }

        return false;
    }

    boolean isThreeOfAKind(Set<String> set) {
        for (var s : set) {
            if (occurences(s) == 3) {
                return true;
            }
        }

        return false;
    }

    boolean isTwoPair(Set<String> set) {
        int pairs = 0;

        for (var s : set) {
            if (occurences(s) == 2) {
                pairs++;
            }
        }

        return pairs == 2;
    }

    boolean isPair(Set<String> set) {
        int pairs = 0;

        for (var s : set) {
            if (occurences(s) == 2) {
                pairs++;
            }
        }

        return pairs == 1;
    }


    private int occurences(String card) {
        return Collections.frequency(_cards, card);
    }

    public int getBid() {
        return _bid;
    }

    public List<String> getCards(){
        return _cards;
    }

    public void setType(int type){
        _type = type;
    }

    public int getSavedType(){
        return _type;
    }

    public static int getValue(String card) {
        return Day7.VALUES.get(card);
    }

    public static boolean compare(String c1, String c2) {
        return getValue(c1) > getValue(c2);
    }

    public static int compare(Hand h1, Hand h2) {
        if (h1._type > h2._type) {
            return -1;
        }

        if (h1._type < h2._type) {
            return 1;
        }

        for (var tup : Utils.zip(h1._cards, h2._cards)) {
            var c1 = tup.fst();
            var c2 = tup.snd();
            if (compare(c1, c2)) {
                return -1;
            }

            if (compare(c2, c1)) {
                return 1;
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        return "\nHand=" + _cards + ";Bid=" + _bid + ";Type=" + getType();
    }

}
