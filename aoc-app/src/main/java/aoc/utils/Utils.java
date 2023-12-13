package aoc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Utils {
    public static enum Colour {
        RED, GREEN, BLUE;

        public static Colour get(String s) {
            if (s.equals("red")) {
                return RED;
            }
            if (s.equals("green")) {
                return GREEN;
            }
            return BLUE;
        }
    }

    private static Map<Integer, String> LEX_MAP = new HashMap<>();

    static {
        LEX_MAP.put(0, "zero");
        LEX_MAP.put(1, "one");
        LEX_MAP.put(2, "two");
        LEX_MAP.put(3, "three");
        LEX_MAP.put(4, "four");
        LEX_MAP.put(5, "five");
        LEX_MAP.put(6, "six");
        LEX_MAP.put(7, "seven");
        LEX_MAP.put(8, "eight");
        LEX_MAP.put(9, "nine");
    }

    public static int stoi(final String str) {
        return Integer.parseInt(str.trim());
    }

    public static Long stol(final String str) {
        return Long.parseLong(str);
    }

    public static boolean stob(final String str) {
        return Boolean.parseBoolean(str);
    }

    public static boolean isNum(String s) {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    // from inclusive to exlusive
    public static List<Integer> range(int start, int stop) {
        List<Integer> l = new ArrayList<>();
        for (int i = start; i < stop; i++) {
            l.add(i);
        }
        return l;
    }

    public static List<Long> range(long start, long stop) {
        List<Long> l = new ArrayList<>();
        for (long i = start; i < stop; i++) {
            l.add(i);
        }
        return l;
    }

    public static String lexValue(int x) {
        return LEX_MAP.get(x);
    }

    public static <T, K> List<Tuple<T, K>> zip(List<T> list1, List<K> list2) {
        var l1 = new ArrayList<>(list1);
        var l2 = new ArrayList<>(list2);
        if (l1.size() == 0) {
            return new ArrayList<>();
        }

        if (l2.size() == 0) {
            return new ArrayList<>();
        }

        var x = l1.get(0);
        var y = l2.get(0);
        var tup = new Tuple<>(x, y);

        l1.remove(0);
        l2.remove(0);

        var zipped = zip(l1, l2);
        zipped.add(0, tup);
        return zipped;

    }

    public static <T, K,R> List<R> zipWith(BiFunction<T,K,R> f, List<T> list1, List<K> list2) {
        var l1 = new ArrayList<>(list1);
        var l2 = new ArrayList<>(list2);
        if (l1.size() == 0) {
            return new ArrayList<>();
        }

        if (l2.size() == 0) {
            return new ArrayList<>();
        }

        var x = l1.get(0);
        var y = l2.get(0);
        var res = f.apply(x, y);

        l1.remove(0);
        l2.remove(0);

        var zipped = zipWith(f,l1, l2);
        zipped.add(0, res);
        return zipped;

    }

    public static <T> List<Tuple<Integer, Integer>> neighbours(int width, int height, Tuple<Integer, Integer> pos) {
        List<Tuple<Integer, Integer>> neighbours = new ArrayList<>();

        for (int r = pos.fst() - 1; r <= pos.fst() + 1; r++) {
            if (!(r >= 0 && r < height)) {
                continue;
            }
            for (int c = pos.snd() - 1; c <= pos.snd() + 1; c++) {
                if (!(c >= 0 && c < width)) {
                    continue;
                }

                if (r == pos.fst() && c == pos.snd()) {
                    continue;
                }

                neighbours.add(new Tuple<>(r, c));

            }
        }
        return neighbours;
    }

    public static void printGrid(char[][] grid) {
        for (var x : grid) {
            String str = "";
            for (char c : x) {
                str += c;
            }
            System.out.println(str);
        }
        System.out.println("\n");
    }
}
