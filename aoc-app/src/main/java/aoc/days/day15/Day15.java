package aoc.days.day15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aoc.Day;
import aoc.days.day15.Day15.Lens;
import aoc.utils.Utils;

public class Day15 implements Day<List<Lens>, Integer> {

    @Override
    public Integer solveA(List<Lens> lenses) {
        int sum = 0;

        for (var lens : lenses) {
            sum += computeHash(lens.stringify());
        }

        return sum;
    }

    @Override
    public Integer solveB(List<Lens> lenses) {
        Map<String, Lens> labels = new HashMap<>();
        Map<Integer, LinkedList<String>> boxes = new HashMap<>();
        Map<String, Integer> lensBox = new HashMap<>();

        for (Lens lens : lenses) {
            labels.put(lens.label(), lens);

            int box = computeHash(lens.label());
            char op = lens.operation();
            LinkedList<String> boxLenses = boxes.get(box);

            if (op == '-') {
                if (boxLenses == null || !boxLenses.contains(lens.label())) {
                    continue;
                } else if (boxLenses != null && boxLenses.contains(lens.label())) {
                    boxLenses.remove(lens.label());
                }
            } else if (op == '=') {
                if (boxLenses == null) {
                    boxes.put(box, new LinkedList<>(List.of(lens.label())));
                    lensBox.put(lens.label(), box);
                } else if (!boxLenses.contains(lens.label())) {
                    boxLenses.addLast(lens.label());
                    lensBox.put(lens.label(), box);
                }
            } else {
                System.out.println("Problem");
                return -1;
            }
        }

        return computeResult(labels, boxes, lensBox);

    }

    private int computeResult(Map<String, Lens> labels, Map<Integer, LinkedList<String>> boxes,
            Map<String, Integer> lensBox) {
        int sum = 0;

        for (var labelEntry : labels.entrySet()) {
            String label = labelEntry.getKey();

            if (!lensBox.containsKey(label)) {
                continue;
            }

            Lens lens = labelEntry.getValue();
            int box = lensBox.get(label);
            LinkedList<String> boxL = boxes.get(box);

            int prod = (1 + box) * (boxL.indexOf(label) + 1) * lens.value();
            sum += prod;
        }

        return sum;
    }

    private int computeHash(String str) {
        int current = 0;
        for (char c : str.toCharArray()) {
            int val = (int) c;
            current += val;
            current *= 17;
            current %= 256;
        }

        return current;
    }

    @Override
    public List<Lens> parseA(String input) throws IOException {
        return parseB(input);
    }

    @Override
    public List<Lens> parseB(String input) throws IOException {
        String[] strs = input.split(",");
        List<Lens> lenses = new ArrayList<>();

        for (String str : strs) {
            boolean op = false;
            String label = "";
            char operation = '?';
            String val = "";

            for (char c : str.toCharArray()) {
                if (op) {
                    val += c;
                }
                if (c == '-' || c == '=') {
                    operation = c;
                    op = true;
                }
                if (!op) {
                    label += c;
                    continue;
                }
            }

            lenses.add(new Lens(label, operation, val == "" ? 0 : Utils.stoi(val)));
        }

        return lenses;

    }

    record Lens(String label, char operation, int value) {
        String stringify() {
            return label + operation + (value == 0 ? "" : value);
        }
    };
}
