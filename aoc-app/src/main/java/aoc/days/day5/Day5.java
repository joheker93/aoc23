package aoc.days.day5;

import static aoc.utils.EStream.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc.Day;
import aoc.utils.Triplet;
import aoc.utils.Tuple;
import aoc.utils.Utils;

public class Day5 implements Day<Triplet<List<Long>, Map<Category, Category>, Map<String, Category>>, Long> {

    @Override
    public Long solveA(Triplet<List<Long>, Map<Category, Category>, Map<String, Category>> parsedInput) {
        var seeds = parsedInput.fst();
        var cats = parsedInput.snd();
        var lookup = parsedInput.trd();

        long min = Long.MAX_VALUE;
        for (long l : seeds) {
            long loc = getLocation(l, "START", cats, lookup);
            min = loc < min ? loc : min;
        }
        return min;
    }
    
    @Override
    public Long solveB(Triplet<List<Long>, Map<Category, Category>, Map<String, Category>> parsedInput) {
        var seeds = getSeedsFromRanges(parsedInput.fst());
        var cats = parsedInput.snd();
        var lookup = parsedInput.trd();

        long min = Long.MAX_VALUE;
        for (var tup : seeds) {
            long start = tup.fst();
            long count = tup.snd();
            for (long i = start; i < start + count; i++) {
                var loc = getLocation(i, "START", cats, lookup);
                min = loc < min ? loc : min;
            }
        }
        return min;
    }

    public long getLocation(long seed, String loc, Map<Category, Category> cats, Map<String, Category> lookup) {

        if (loc.equals("location")) {
            return seed;
        }

        Category c = lookup.get(loc);
        long target = c.getTarget(seed);
        String targetLoc = c.getTargetName();

        return getLocation(target, targetLoc, cats, lookup);
    }

    public List<Tuple<Long, Long>> getSeedsFromRanges(List<Long> list) {
        List<Tuple<Long, Long>> l = new ArrayList<>();
        var it = list.iterator();

        while (it.hasNext()) {
            if (it.hasNext()) {
                var start = it.next();
                var count = it.next();
                l.add(Tuple.of(start, count));
            }
        }

        return l;
    }

    @Override
    public Triplet<List<Long>, Map<Category, Category>, Map<String, Category>> parseA(String input) throws IOException {
        List<Category> categories = new ArrayList<>();
        Map<String, Category> cats = new HashMap<>();

        String[] split = input.split("\n\n");
        var seeds = stream(split[0].split(":")[1].trim()).words().map(Utils::stol).toList();
        for (String str : split) {
            Category cat = parseItem(str);

            categories.add(cat);
            cats.put(cat.getName(), cat);

        }

        Map<Category, Category> map = new HashMap<>();
        for (Category cat : categories) {

            Category destCat = cats.get(cat.getTargetName());
            map.put(cat, destCat);
        }

        return Triplet.of(seeds, map, cats);
    }

    @Override
    public Triplet<List<Long>, Map<Category, Category>, Map<String, Category>> parseB(String input) throws IOException {
        return parseA(input);
    }

    public Category parseItem(String str) {
        String reg = "(.*) map:\\n([\\d+\\s]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        Category c = null;

        while (m.find()) {
            String[] split = m.group(1).split("-");
            String source = split[0];
            String dest = split[2];
            c = new Category(source, dest);
            var ranges = stream(m.group(2)).lines().map(a -> stream(a).words().map(Utils::stol).toList()).toList();

            for (var rangeItem : ranges) {
                long destinationRangeStart = rangeItem.get(0);
                long sourceRangeStart = rangeItem.get(1);
                long rangeLength = rangeItem.get(2);

                c.addRange(sourceRangeStart, destinationRangeStart, rangeLength);
            }
        }

        if (c == null) {
            c = new Category("START", "seed");
        }
        return c;
    }
}
