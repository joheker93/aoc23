package aoc.days.day19;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc.Day;
import aoc.days.day19.Day19.Workflow;
import aoc.utils.Tuple;
import aoc.utils.Utils;

public class Day19 implements Day<Tuple<List<Workflow>, List<Map<String, Integer>>>, Integer> {

    @Override
    public Integer solveA(Tuple<List<Workflow>, List<Map<String, Integer>>> input) {
        var workFlows = input.fst();
        var parts = input.snd();

        Map<String, Workflow> flows = new HashMap<>();
        workFlows.forEach(wf -> flows.put(wf.name, wf));

        int sum = 0;
        for (var part : parts) {

            Workflow current = flows.get("in");
            Deque<Workflow> queue = new ArrayDeque<>();

            queue.add(current);

            while (!queue.isEmpty()) {
                current = queue.pop();
                List<Operation> op = current.operations;
                String next = runOperation(op, part);

                if (next.equals("R")) {
                    continue;
                }

                if (next.equals("A")) {
                    sum += part.values().stream().reduce(0, Integer::sum);
                    continue;
                }

                queue.add(flows.get(next));
            }
        }
        return sum;
    }

    private String runOperation(List<Operation> op, Map<String, Integer> part) {
        for (var operation : op) {
            if (operation.part.isPresent()) {
                int oPart = part.get(operation.part.get());
                if (operation.op.get().equals("<")) {
                    if (oPart < operation.value.get()) {
                        return operation.target;
                    } else {
                        continue;
                    }
                } else if (operation.op.get().equals(">")) {
                    if (oPart > operation.value.get()) {
                        return operation.target;
                    } else {
                        continue;
                    }
                }
            } else {
                return operation.target;
            }
        }

        return null;
    }

    @Override
    public Integer solveB(Tuple<List<Workflow>, List<Map<String, Integer>>> parsedInput) {
        return null;
    }

    @Override
    public Tuple<List<Workflow>, List<Map<String, Integer>>> parseA(String input) throws IOException {
        var split = input.split("\\n\\n");

        String workFlow = "(\\w+)\\{((:?.*,)+.*)+\\}";
        String operation = "(\\w+)(\\W)(\\d+)\\:(\\w+)|(\\w+)";
        List<Workflow> workflows = new ArrayList<>();

        for (var line : split[0].lines().toList()) {
            Matcher wfM = Pattern.compile(workFlow).matcher(line);
            wfM.find();
            String name = wfM.group(1);
            String flow = wfM.group(2);

            var lineSplit = flow.split(",");
            LinkedList<Operation> operations = new LinkedList<>();
            for (var op : lineSplit) {

                Matcher opM = Pattern.compile(operation).matcher(op);
                opM.find();

                Operation oper = new Operation(Optional.ofNullable(opM.group(1)), Optional.ofNullable(opM.group(2)),
                        opM.group(3) == null ? Optional.empty() : Optional.ofNullable(Utils.stoi(opM.group(3))),
                        opM.group(5) == null ? opM.group(4) : opM.group(5));

                operations.add(oper);
            }

            workflows.add(new Workflow(name, operations));

        }

        List<Map<String, Integer>> parts = new ArrayList<>();

        for (var line : split[1].lines().toList()) {
            Map<String, Integer> partMap = new HashMap<>();
            line = line.substring(1, line.length() - 1);
            var ps = line.split(",");
            for (var p : ps) {
                System.out.println("Parsing " + p);
                String pName = String.valueOf(p.charAt(0));
                String val = p.substring(2, p.length());
                partMap.put(pName, Utils.stoi(val));
            }

            parts.add(partMap);

        }
        return Tuple.of(workflows, parts);

    }

    @Override
    public Tuple<List<Workflow>, List<Map<String, Integer>>> parseB(String input) throws IOException {
        return parseA(input);

    }

    protected class Workflow {
        String name;
        LinkedList<Operation> operations;
        int hash;

        public Workflow(String name, LinkedList<Operation> operations) {
            this.name = name;
            this.operations = operations;
            hash = Objects.hash(name, operations);
        }

        @Override
        public String toString() {
            return name + " : " + operations;
        }

    }

    protected class Operation {
        Optional<String> part;
        Optional<String> op;
        Optional<Integer> value;
        String target;
        int hash;

        public Operation(Optional<String> part, Optional<String> op, Optional<Integer> value, String target) {
            this.part = part;
            this.op = op;
            this.value = value;
            this.target = target;
            hash = Objects.hash(part, op, value, target);
        }

        @Override
        public String toString() {
            return part.toString() + " " + op.toString() + " " + value.toString() + " : " + target;
        }

    }
}
